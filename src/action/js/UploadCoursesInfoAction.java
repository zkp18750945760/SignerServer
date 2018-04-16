package action.js;

import com.opensymphony.xwork2.ActionSupport;
import db.DBHelper;
import action.js.bean.CourseInfo;
import action.js.bean.CourseUtil;
import utils.FileUtils;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 网站Action
 */
public class UploadCoursesInfoAction extends ActionSupport {

    private File uploadFile; //上传的文件
    private String uploadFileContentType; //文件类型
    private String uploadFileFileName; //文件名字
    private String gradeCode; //年级代码
    private String majorCode; //专业代码
    private String classCode; //班级代码
    private String termCode; //学期代码

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileContentType() {
        return uploadFileContentType;
    }

    public void setUploadFileContentType(String uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }

    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getTermCode() {
        return termCode;
    }

    public void setTermCode(String termCode) {
        this.termCode = termCode;
    }

    /**
     * 将课程信息表录入数据库中
     *
     * @param inputStream InputStream
     */
    private void insertDataToDB(InputStream inputStream) {
        String[][] values = FileUtils.readXlsFiles(inputStream, 2, 0);
        setGradeCode("2014");
        setMajorCode("102");
        setClassCode("1");
        setTermCode("8");
        String tableName = gradeCode + majorCode + classCode + "a";
        //查询当前班级的课程表在不在数据库中
        String sql = "select table_name from information_schema.tables where table_name = '" + tableName + "'";
        DBHelper dbHelper = new DBHelper(sql);
        try {
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }
            dbHelper.close();
            resultSet.close();

            if (tabCount == 0) {
                //当前班级的课程表还不存在，创建当前班级的课程表
                sql = "create table `" + tableName + "`(" +
                        "courseName varchar(40) NOT NULL," +
                        "courseLocation varchar(7) DEFAULT NULL," +
                        "courseWeek varchar(2) NOT NULL," +
                        "courseTime varchar(6) DEFAULT NULL," +
                        "courseLong varchar(3) DEFAULT NULL," +
                        "courseWeeks varchar(7) DEFAULT NULL," +
                        "primary key (courseName,courseWeek))";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            if (values != null) {
                String[] split;
                List<CourseInfo> courseInfos = new ArrayList<>();

                for (int i = 2; i < values.length; i++) {
                    if ((i + 1) % 4 == 0) {
                        //课程区域 4  8  12  16  20
                        for (int j = 4; j < 10; j++) {
                            //1、2节  3、4节  5、6节  7、8节  9、10节  11、12、13节
                            split = values[i][j].split("\n|\r\n|\r");

                            int length = split.length;
                            CourseInfo courseInfo = new CourseInfo();

                            if (length == 3) {
                                //只有课程名、上课地点、上课周数
                                courseInfo.setCourseName(split[0]);
                                courseInfo.setCourseLocation(split[1]);
                                courseInfo.setCourseWeek(CourseUtil.countWeek(i));
                                courseInfo.setCourseTime(CourseUtil.countTime(j, false));
                                courseInfo.setCourseLong("2节");
                                courseInfo.setCourseWeeks(split[2]);
                                if (split[1].contains("(3节)")) {
                                    courseInfo.setCourseLong("3节");
                                    courseInfo.setCourseTime(CourseUtil.countTime(j, true));
                                    courseInfo.setCourseLocation(split[1].replace("(3节)", ""));
                                }
                                courseInfos.add(courseInfo);
                            } else if (length == 4) {
                                //只有课程名、上课地点、上课周数  实验课
                                courseInfo.setCourseName(split[0] + split[1]);
                                courseInfo.setCourseLocation(split[2]);
                                courseInfo.setCourseWeek(CourseUtil.countWeek(i));
                                courseInfo.setCourseTime(CourseUtil.countTime(j, false));
                                courseInfo.setCourseLong("2节");
                                courseInfo.setCourseWeeks(split[3]);
                                if (split[2].contains("(3节)")) {
                                    courseInfo.setCourseLong("3节");
                                    if (split[2].contains("(3节)")) {
                                        courseInfo.setCourseLong("3节");
                                        courseInfo.setCourseTime(CourseUtil.countTime(j, true));
                                        courseInfo.setCourseLocation(split[2].replace("(3节)", ""));
                                    }
                                }
                                courseInfos.add(courseInfo);
                            } else if (length == 5) {
                                courseInfo.setCourseName(split[0] + split[1] + split[2]);
                                courseInfo.setCourseLocation(split[3]);
                                courseInfo.setCourseWeek(CourseUtil.countWeek(i));
                                courseInfo.setCourseTime(CourseUtil.countTime(j, false));
                                courseInfo.setCourseLong("2节");
                                courseInfo.setCourseWeeks(split[4]);
                                if (split[3].contains("(3节)")) {
                                    if (split[2].contains("(3节)")) {
                                        courseInfo.setCourseLong("3节");
                                        courseInfo.setCourseTime(CourseUtil.countTime(j, true));
                                        courseInfo.setCourseLocation(split[3].replace("(3节)", ""));
                                    }
                                }
                                courseInfos.add(courseInfo);
                            }
                        }
                    }
                }
                System.out.println(courseInfos.size());
                for (CourseInfo courseInfo : courseInfos) {
                    sql = "select * from `" + tableName + "` where (courseName,courseWeek) in" +
                            " (('" + courseInfo.getCourseName() + "','" + courseInfo.getCourseWeek() + "'))";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    tabCount = 0;
                    while (resultSet.next()) {
                        tabCount++;
                    }
                    resultSet.close();
                    dbHelper.close();

                    if (tabCount == 0) {
                        //当前条目不在表格中，直接插入
                        sql = "insert into `" + tableName + "`(courseName,courseLocation,courseWeek,courseTime,courseLong,courseWeeks) values('"
                                + courseInfo.getCourseName() + "','"
                                + courseInfo.getCourseLocation() + "','"
                                + courseInfo.getCourseWeek() + "','"
                                + courseInfo.getCourseTime() + "','"
                                + courseInfo.getCourseLong() + "','"
                                + courseInfo.getCourseWeeks() + "')";
                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();
                    } else {
                        //该条目已在表格中，修改数据
                        sql = "update `" + tableName + "` set courseTime = '" + courseInfo.getCourseTime()
                                + "',courseLong = '" + courseInfo.getCourseLong()
                                + "',courseWeeks = '" + courseInfo.getCourseWeeks()
                                + "',courseLocation = '" + courseInfo.getCourseLocation()
                                + "' where (courseName,courseWeek) in ('" + courseInfo.getCourseName() + "','"
                                + courseInfo.getCourseWeek() + "')";
                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String execute() throws Exception {
        String uploadPath = FileUtils.uploadFile(uploadFile, uploadFileFileName, "/files");
        uploadPath = new File(uploadPath, uploadFileFileName).getAbsolutePath();
        insertDataToDB(new FileInputStream(uploadPath));
        return SUCCESS;
    }
}
