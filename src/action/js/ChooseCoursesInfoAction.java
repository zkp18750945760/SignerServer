package action.js;

import com.opensymphony.xwork2.ActionSupport;
import db.DBHelper;
import utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 网站Action
 */
public class ChooseCoursesInfoAction extends ActionSupport {

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
        String[][] values = FileUtils.readXlsFiles(inputStream, 1, 2);
        setGradeCode("2014");
        setMajorCode("102");
        setClassCode("1");
        setTermCode("8");
        String tableName = gradeCode + majorCode + classCode + "b";
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
                //当前班级的选课表还不存在，创建当前班级的选课表
                sql = "create table `" + tableName + "`(stuId varchar(11) not null," +
                        "stuName varchar(20) not null," +
                        "foreign key(stuId) references students(stuId))";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            //动态添加列
            if (values != null) {
                for (int i = 3; i < values[1].length - 1; i++) {

                    sql = "select column_name, data_type, is_nullable, column_default" +
                            " from information_schema.columns where table_schema = 'Signer' and " +
                            "table_name = '" + tableName + "' and column_name = '" + values[1][i] + "'";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    tabCount = 0;
                    while (resultSet.next()) {
                        tabCount++;
                    }
                    resultSet.close();
                    dbHelper.close();

                    if (tabCount == 0) {
                        sql = "alter table `" + tableName + "` add `"
                                + values[1][i] + "` varchar(40) not null default '×'";
                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();
                    }
                }

                for (int i = 2; i < values.length - 2; i++) {

                    sql = "select * from `" + tableName + "` where stuId = '" + values[i][1] + "'";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    tabCount = 0;
                    while (resultSet.next()) {
                        tabCount++;
                    }
                    resultSet.close();
                    dbHelper.close();

                    if (tabCount == 0) {
                        sql = "insert into `" + tableName + "`(stuId,stuName) values('" + values[i][1] + "','" + values[i][2] + "')";
                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();

                        for (int j = 3; j < values[1].length - 1; j++) {
                            if (values[i][j] != null && !values[i][j].equals("")) {
                                sql = "update `" + tableName + "` set `"
                                        + values[1][j] + "` = '" + values[i][j] + "' where stuId = '" + values[i][1] + "'";
                                dbHelper = new DBHelper(sql);
                                dbHelper.pst.executeUpdate();
                                dbHelper.close();
                            }
                        }
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
//        insertDataToDB(new FileInputStream(uploadPath));
        return SUCCESS;
    }
}
