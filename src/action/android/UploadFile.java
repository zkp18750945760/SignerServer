package action.android;

import action.android.bean.ASCQBean;
import com.alibaba.fastjson.JSON;
import db.DBHelper;
import org.apache.struts2.ServletActionContext;
import utils.FileUtils;
import utils.SchoolYearUtils;
import utils.StringUtils;
import utils.TimeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhoukp
 * @time 2018/3/27 23:57
 * @email 275557625@qq.com
 * @function
 */
public class UploadFile {

    /**
     * 上传第二台账
     *
     * @param month 月份
     * @param file  文件
     * @return 200->成功 101->表格为空 102->文件IO错误 103->数据库IO错误
     */
    public static String uploadLedger(int month, File file) {
        HashMap<String, Object> hashMap = new HashMap<>();

        //首先将文件存入对应的文件夹中
        try {
            String path = FileUtils.uploadFile(file, file.getName(), "/files");
            path = new File(path, file.getName()).getAbsolutePath();
            FileInputStream inputStream = new FileInputStream(path);

            String[][] values = FileUtils.readXlsFiles(inputStream, 3, 1);

            String sql = "select table_name from information_schema.tables where table_name = 'secondLedger'";
            DBHelper dbHelper = new DBHelper(sql);
            int tabCount = 0;
            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //第二台账表格不存在，创建对应的表格
                sql = "create table secondLedger(" +
                        "stuId char(11)," +
                        "year int," +
                        "month int," +
                        "count float," +
                        "foreign key(stuId) references students(stuId))";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            if (values != null) {
                for (int i = 3; i < values.length - 1; i++) {
                    tabCount = 0;
                    sql = "select * from secondLedger where stuId = '"
                            + values[i][1] + "' and year = '" + TimeUtils.getCurrentYear() + "' " +
                            "and month = '" + month + "'";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    while (resultSet.next()) {
                        tabCount++;
                    }
                    resultSet.close();
                    dbHelper.close();

                    if (tabCount == 0) {
                        //这一列数据不存在，可以直接插入数据库中
                        sql = "insert into secondLedger(stuId,year,month,count) values ('" + values[i][1] + "'," +
                                "'" + TimeUtils.getCurrentYear() + "','" + month + "','" + values[i][20] + "')";

                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();


                    } else {
                        //数据已经存在，更新数据
                        sql = "update secondLedger set count = '" + values[i][20]
                                + "' where stuId = '" + values[i][1] + "' " +
                                "and year = '" + TimeUtils.getCurrentYear() + "' and month = '" + month + "'";

                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();
                    }
                }
                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("status", 200);
                return JSON.toJSONString(hashMap);
            }

            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 101);
            return JSON.toJSONString(hashMap);

        } catch (IOException e) {
            e.printStackTrace();
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 102);
            return JSON.toJSONString(hashMap);
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 103);
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 获取某个学生某个学期的所有台账
     *
     * @param userId 用户ID
     * @param year   年份
     * @return 200->成功 101->班长还未上传数据 102->数据库IO错误
     */
    public static String getSecondLedger(String userId, int year) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            String sql = "select table_name from information_schema.tables where table_name = 'secondLedger'";
            DBHelper dbHelper = new DBHelper(sql);
            int tabCount = 0;
            ResultSet resultSet;
            resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //第二台账表格还不存在，还没有班长上传本班的第二台账
                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("status", 101);
                return JSON.toJSONString(hashMap);
            } else {
                //表格已经存在，查找对应的数据返回给用户
                sql = "select * from secondLedger where stuId = '" + userId + "' and year = '" + year + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                while (resultSet.next()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("month", resultSet.getString("month"));
                    map.put("count", resultSet.getFloat("count"));
                    jsonObjects.add(map);
                }

                resultSet.close();
                dbHelper.close();

                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("status", 200);
                hashMap.put("data", jsonObjects);
                return JSON.toJSONString(hashMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 102);
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 获取版本更新信息
     *
     * @return 200->成功 101->数据库IO错误
     */
    public static String getUpdateInfo() {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            String sql = "select * from updateVersion order by versionCode desc limit 1 ";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("appName", resultSet.getString("appName"));
                map.put("versionName", resultSet.getString("versionName"));
                map.put("versionCode", resultSet.getInt("versionCode"));
                map.put("description", resultSet.getString("description"));
                map.put("downloadUrl", resultSet.getString("downloadUrl"));
                map.put("updateTime", resultSet.getString("updateTime"));
                String realPath = ServletActionContext.getServletContext().getRealPath("/files");
                File file = new File(realPath, resultSet.getString("appName"));
                map.put("apkSize", file.length());
                jsonObjects.add(map);
            }

            resultSet.close();
            dbHelper.close();

            hashMap.put("data", jsonObjects);
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 200);

            return JSON.toJSONString(hashMap);
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 101);
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 上传错误日志到服务器
     *
     * @param logcat 错误日志TXT文件
     * @return 200->成功 101->数据库IO错误 102->文件为空 103->IO错误
     */
    public static String uploadCrashLogcat(File logcat) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select table_name from information_schema.tables where table_name = 'logcat'";
            DBHelper dbHelper = new DBHelper(sql);
            int tabCount = 0;
            ResultSet resultSet;
            resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //错误日志表还不存在，创建错误日志表
                sql = "create table logcat(" +
                        "uploadTime char(19)," +
                        "fileUrl varchar(70))";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }
            //表格已经存在，直接将文件放入对应的位置并插入数据到数据库中
            String path = FileUtils.uploadFile(logcat, logcat.getName(), "/crash");
            path = new File(path, logcat.getName()).getAbsolutePath();
            FileInputStream inputStream = new FileInputStream(path);

            sql = "insert into logcat (uploadTime,fileUrl) values ('"
                    + TimeUtils.getCurrentTime() + "','" + ("/crash/" + logcat.getName()) + "')";
            dbHelper = new DBHelper(sql);
            dbHelper.pst.executeUpdate();
            dbHelper.close();

            hashMap.put("status", 200);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);

        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 101);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
            hashMap.put("status", 103);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }


    /**
     * 获取某个班级的课程表
     *
     * @param userId 用户ID
     * @param grade  年级
     * @param major  专业
     * @param clazz  班级
     * @return 200->成功 101->管理员还没有上传课表 102->数据库IO错误
     */
    public static String getCourse(String userId, String grade, String major, String clazz) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        String tabName = SchoolYearUtils.getGradeCode(grade)
                + SchoolYearUtils.getMajorCode(major)
                + SchoolYearUtils.getClassCode(clazz)
                + SchoolYearUtils.getTermCodeByMonth(userId,
                Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear())
                + "a";

        try {
            String sql = "select table_name from information_schema.tables where table_name = '" + tabName + "'";
            DBHelper dbHelper = new DBHelper(sql);
            int tabCount = 0;
            ResultSet resultSet;
            resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //对应的课程表还不存在，管理员还没有上传该学期的课表
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //对应的课表已经存在，返回相应的数据给用户
                sql = "select * from `" + tabName + "`";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                while (resultSet.next()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", resultSet.getString("courseName"));
                    map.put("location", resultSet.getString("courseLocation"));
                    map.put("day", Integer.parseInt(resultSet.getString("courseWeek")));
                    map.put("start", Integer.parseInt(StringUtils.getSubString(resultSet.getString("courseTime"), "(.*?)-")));
                    map.put("end", Integer.parseInt(StringUtils.getSubString(resultSet.getString("courseTime"), "-(.*?)节")));
                    map.put("weeks", resultSet.getString("courseWeeks"));
                    jsonObjects.add(map);
                }

                resultSet.close();
                dbHelper.close();

                hashMap.put("data", jsonObjects);
                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 获取签到记录
     *
     * @param userId 用户ID
     * @param grade  年级
     * @param major  专业
     * @param clazz  班级
     * @return 200->成功 101->数据库IO错误
     */
    public static String getSignRecord(String userId, String grade, String major, String clazz) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();


        String termCode = SchoolYearUtils.getGradeCode(grade)
                + SchoolYearUtils.getMajorCode(major)
                + SchoolYearUtils.getClassCode(clazz)
                + SchoolYearUtils.getTermCodeByMonth(userId,
                Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

        try {
            //先查看课表存不存在
            String sql = "select table_name from information_schema.tables where table_name = '" + (termCode + "a") + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 1) {
                //课程表存在
                sql = "select * from `" + (termCode + "a") + "`";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                DBHelper helper;
                ResultSet set;

                while (resultSet.next()) {
                    sql = "select table_name from information_schema.tables where table_name = '"
                            + (resultSet.getString("courseName") + termCode) + "'";
                    tabCount = 0;
                    helper = new DBHelper(sql);
                    set = helper.pst.executeQuery();

                    while (set.next()) {
                        tabCount++;
                    }
                    set.close();
                    helper.close();

                    if (tabCount == 1) {
                        //对应的课程已经存在签到表
                        sql = "select * from `" + (resultSet.getString("courseName") + termCode) + "` " +
                                "where stuId = '" + userId + "'";

                        helper = new DBHelper(sql);
                        set = helper.pst.executeQuery();

                        while (set.next()) {
                            if (set.getMetaData().getColumnCount() > 2) {
                                for (int i = 3; i <= set.getMetaData().getColumnCount(); i++) {
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("time", set.getMetaData().getColumnName(i));
                                    map.put("content", resultSet.getString("courseName"));
                                    if (set.getString(i) == null || set.getString(i).equals("")) {
                                        map.put("status", "3");
                                    } else {
                                        map.put("status", set.getString(i));
                                    }
                                    if (!jsonObjects.contains(map)) {
                                        jsonObjects.add(map);
                                    }
                                }
                            }
                        }

                        set.close();
                        helper.close();
                    }
                }
            }

            tabCount = 0;
            //查看会议表存不存在
            sql = "select table_name from information_schema.tables where table_name = 'meetings'";
            dbHelper = new DBHelper(sql);

            resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 1) {
                sql = "select * from meetings";
                dbHelper = new DBHelper(sql);

                resultSet = dbHelper.pst.executeQuery();

                DBHelper helper;
                ResultSet set;

                while (resultSet.next()) {
                    tabCount = 0;
                    sql = "select table_name from information_schema.tables where table_name = '"
                            + (resultSet.getString("metName") + resultSet.getString("metTime")) + "'";
                    helper = new DBHelper(sql);
                    set = helper.pst.executeQuery();

                    while (set.next()) {
                        tabCount++;
                    }

                    set.close();
                    helper.close();

                    if (tabCount == 1) {
                        sql = "select * from `" + (resultSet.getString("metName") + resultSet.getString("metTime")) + "` " +
                                "where userId = '" + userId + "'";
                        helper = new DBHelper(sql);
                        set = helper.pst.executeQuery();

                        while (set.next()) {
                            if (set.getMetaData().getColumnCount() > 2) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("time", set.getString("signTime"));
                                map.put("content", resultSet.getString("metName"));
                                if (set.getString("signStatus") == null || set.getString("signStatus").equals("")) {
                                    map.put("status", "3");
                                } else {
                                    map.put("status", set.getString("signStatus"));
                                }
                                jsonObjects.add(map);
                            }
                        }
                        set.close();
                        helper.close();
                    }
                }
            }

            hashMap.put("data", jsonObjects);
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 200);
            return JSON.toJSONString(hashMap);
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 101);
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 获取第一台账
     *
     * @param userId 用户ID
     * @param grade  年级
     * @param major  专业
     * @param clazz  班级
     * @return 200->成功 101->还没上传课表 102->数据库IO错误
     */
    public static String getFirstLedger(String userId, String grade, String major, String clazz) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        String termCode = SchoolYearUtils.getGradeCode(grade)
                + SchoolYearUtils.getMajorCode(major)
                + SchoolYearUtils.getClassCode(clazz)
                + SchoolYearUtils.getTermCodeByMonth(userId,
                Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

        try {
            //先查看课表存不存在
            String sql = "select table_name from information_schema.tables where table_name = '" + (termCode + "a") + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //对应的课程表还不存在
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //对应的课程表存在，查询记录返回给用户
                sql = "select * from `" + (termCode + "a") + "`";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                DBHelper helper;
                ResultSet set;
                String date;

                while (resultSet.next()) {
                    sql = "select table_name from information_schema.tables where table_name = '"
                            + (resultSet.getString("courseName") + termCode) + "'";
                    tabCount = 0;
                    helper = new DBHelper(sql);
                    set = helper.pst.executeQuery();

                    while (set.next()) {
                        tabCount++;
                    }
                    set.close();
                    helper.close();

                    if (tabCount == 1) {
                        //对应的课程已经存在签到表
                        sql = "select * from `" + (resultSet.getString("courseName") + termCode) + "` " +
                                "where stuId = '" + userId + "'";

                        helper = new DBHelper(sql);
                        set = helper.pst.executeQuery();

                        while (set.next()) {
                            if (set.getMetaData().getColumnCount() > 2) {
                                for (int i = 3; i <= set.getMetaData().getColumnCount(); i++) {
                                    HashMap<String, Object> map = new HashMap<>();
                                    date = StringUtils.getSubString(set.getMetaData().getColumnName(i), "(.*?) ");
                                    map.put("time", set.getMetaData().getColumnName(i));
                                    map.put("date", date + " " + StringUtils.dateToWeek(date));
                                    map.put("content", resultSet.getString("courseName"));
                                    map.put("contentLong", StringUtils.getSubString(resultSet.getString("courseLong"), "(.*?)节") + "学时");
                                    if (set.getString(i) == null || set.getString(i).equals("")) {
                                        map.put("status", "3");
                                    } else {
                                        map.put("status", set.getString(i));
                                    }
                                    if (!jsonObjects.contains(map)) {
                                        jsonObjects.add(map);
                                    }
                                }
                            }
                        }

                        set.close();
                        helper.close();
                    }
                }
                hashMap.put("data", jsonObjects);
                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("status", 200);
                return JSON.toJSONString(hashMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 102);
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 获取支书会议列表
     *
     * @param userId 用户ID
     * @return 200->成功 101->还没有支书会议记录 102->数据库IO错误
     */
    public static String getDiscussion(String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            String sql = "select table_name from information_schema.tables where table_name = 'Discussion'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //支书会议表格还不存在
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //支书会议表格存在
                sql = "select * from Discussion";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                DBHelper helper;
                ResultSet set;

                while (resultSet.next()) {
                    //查询相应的数据返回给用户
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("theme", resultSet.getString("disContent"));
                    map.put("date", resultSet.getString("disDate"));
                    map.put("abstract", resultSet.getString("disAbstract"));
                    map.put("browse", resultSet.getString("disRead"));
                    map.put("pdfUrl", resultSet.getString("disFileUrl"));

                    sql = "select * from AlreadyRead where stuId = '" + userId + "' and (content,contentDate) " +
                            "in (('" + resultSet.getString("disContent") + "','"
                            + resultSet.getString("disDate") + "'))";

                    helper = new DBHelper(sql);
                    set = helper.pst.executeQuery();
                    tabCount = 0;

                    while (set.next()) {
                        tabCount++;
                    }
                    set.close();
                    helper.close();

                    if (tabCount == 1) {
                        //该用户已读
                        map.put("read", true);
                    } else {
                        map.put("read", false);
                    }

                    jsonObjects.add(map);
                }

                resultSet.close();
                dbHelper.close();

                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("data", jsonObjects);
                return JSON.toJSONString(hashMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 标记支书会议内容已读
     *
     * @param userId 用户ID
     * @param theme  content
     * @param date   date
     * @return 200->成功 101->还没有支书会议记录 102->已经阅读多该会议记录 103->数据库IO错误
     */
    public static String updateDiscussionRead(String userId, String theme, String date) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String sql = "select table_name from information_schema.tables where table_name = 'Discussion'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //支书会议表格还不存在
                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("status", 101);
                return JSON.toJSONString(hashMap);
            } else {
                //支书会议表格存在，进行相应的更新操作
                sql = "select * from AlreadyRead where stuId = '" + userId + "' and (content,contentDate) in (('" + theme + "','" + date + "'))";
                dbHelper = new DBHelper(sql);
                tabCount = 0;
                resultSet = dbHelper.pst.executeQuery();

                while (resultSet.next()) {
                    tabCount++;
                }
                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //还没有用户已读记录
                    sql = "select * from Discussion where (disContent,disDate) in (('" + theme + "','" + date + "'))";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    int disRead = 0;
                    while (resultSet.next()) {
                        disRead = resultSet.getInt("disRead");
                    }
                    resultSet.close();
                    dbHelper.close();

                    sql = "update Discussion set disRead = '" + (++disRead) + "' where (disContent,disDate) in " +
                            "(('" + theme + "','" + date + "'))";

                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

                    sql = "insert into AlreadyRead(content,contentDate,stuId) values ('"
                            + theme + "','" + date + "','" + userId + "')";

                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

                    sql = "select * from Discussion where (disContent,disDate) in (('" + theme + "','" + date + "'))";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    while (resultSet.next()) {
                        hashMap.put("browse", resultSet.getString("disRead"));
                    }
                    resultSet.close();
                    dbHelper.close();

                    hashMap.put("time", TimeUtils.getCurrentTime());
                    hashMap.put("status", 200);
                    return JSON.toJSONString(hashMap);
                } else {
                    //已经有用户已读记录
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    hashMap.put("status", 102);
                    return JSON.toJSONString(hashMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 103);
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 上传综测成绩
     *
     * @param ASCQBean  ASCQBean
     * @param userId    ID
     * @param userGrade 年级
     * @param userMajor 专业
     * @param userClazz 班级
     * @return 200->成功 101->数据库IO错误
     */
    public static String uploadASQC(ASCQBean ASCQBean, String userId, String userGrade, String userMajor, String userClazz) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'ASQC'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //综测表还不存在，创建综测表
                sql = "create table ASQC(" +
                        "stuId char(11) not null," +
                        "schoolYear char(9) not null," +
                        "time char(19) not null," +
                        "moral1 float not null," +
                        "moral2 float not null," +
                        "moral3 float not null," +
                        "moral4 float not null," +
                        "moral5 float not null," +
                        "moral6 float not null," +
                        "moralMax float not null default '10.0'," +
                        "moralSelf float not null," +
                        "moralMutual float not null," +
                        "GPA float not null," +
                        "witMax float not null default '60.0'," +
                        "witSelf float not null," +
                        "witMutual float not null," +
                        "level int not null," +
                        "sportsMax float not null default '5.0'," +
                        "sportsSelf float not null," +
                        "sportsMutual float not null," +
                        "practiceBasic1 varchar(5) not null," +
                        "practiceBasic2 varchar(5) not null," +
                        "practiceBasic3 varchar(5) not null," +
                        "practice1 float not null," +
                        "practice2 float not null," +
                        "practice3 float not null," +
                        "practice4 float not null," +
                        "practice5 float not null," +
                        "practice6 float not null," +
                        "practice7 float not null," +
                        "practiceMax float not null default '12.0'," +
                        "practiceSelf float not null," +
                        "practiceMutual float not null," +
                        "GenresBasic1 varchar(5) not null," +
                        "GenresBasic2 varchar(5) not null," +
                        "GenresBasic3 varchar(5) not null," +
                        "Genres1 float not null," +
                        "Genres2 float not null," +
                        "Genres3 float not null," +
                        "Genres4 float not null," +
                        "Genres5 float not null," +
                        "GenresMax float not null default '7.0'," +
                        "GenresSelf float not null," +
                        "GenresMutual float not null," +
                        "teamBasic1 varchar(5) not null," +
                        "teamBasic2 varchar(5) not null," +
                        "teamBasic3 varchar(5) not null," +
                        "team1 float not null," +
                        "team2 float not null," +
                        "team3 float not null," +
                        "team4 float not null," +
                        "team5 float not null," +
                        "team6 float not null," +
                        "team7 float not null," +
                        "teamMax float not null default '6.0'," +
                        "teamSelf float not null," +
                        "teamMutual float not null," +
                        "extraMax float not null default '5.0'," +
                        "extraSelf float not null," +
                        "extraMutual float not null," +
                        "isMutual char(5) not null default 'false'," +
                        "mutualId char(11)," +
                        "mutualCode char(10)," +
                        "foreign key(stuId) references students(stuId)," +
                        "foreign key(mutualId) references students(stuId)," +
                        "primary key (stuId,schoolYear))";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            //不存在这条数据，插入数据
            sql = "replace into ASQC(stuId,schoolYear,time,moral1,moral2,moral3,moral4,moral5,moral6," +
                    "moralSelf,moralMutual,GPA,witSelf,witMutual,level,sportsSelf,sportsMutual," +
                    "practiceBasic1,practiceBasic2,practiceBasic3,practice1,practice2,practice3,practice4,practice5," +
                    "practice6,practice7,practiceSelf,practiceMutual,GenresBasic1,GenresBasic2,GenresBasic3," +
                    "Genres1,Genres2,Genres3,Genres4,Genres5,GenresSelf,GenresMutual,teamBasic1,teamBasic2," +
                    "teamBasic3,team1,team2,team3,team4,team5,team6,team7,teamSelf,teamMutual,extraSelf,extraMutual,mutualCode)" +
                    " values ('" + ASCQBean.getUserId() + "'," +
                    "'" + ASCQBean.getSchoolYear() + "'," +
                    "'" + ASCQBean.getTime() + "'," +
                    "'" + ASCQBean.getMoral().getMoral1() + "'," +
                    "'" + ASCQBean.getMoral().getMoral2() + "'," +
                    "'" + ASCQBean.getMoral().getMoral3() + "'," +
                    "'" + ASCQBean.getMoral().getMoral4() + "'," +
                    "'" + ASCQBean.getMoral().getMoral5() + "'," +
                    "'" + ASCQBean.getMoral().getMoral6() + "'," +
                    "'" + ASCQBean.getMoral().getMoralSelf() + "'," +
                    "'" + ASCQBean.getMoral().getMoralMutual() + "'," +
                    "'" + ASCQBean.getWit().getGPA() + "'," +
                    "'" + ASCQBean.getWit().getWitSelf() + "'," +
                    "'" + ASCQBean.getWit().getWitMutual() + "'," +
                    "'" + ASCQBean.getSports().getLevel() + "'," +
                    "'" + ASCQBean.getSports().getSportsSelf() + "'," +
                    "'" + ASCQBean.getSports().getSportsMutual() + "'," +
                    "'" + ASCQBean.getPractice().getPracticeBasic().isPracticeBasic1() + "'," +
                    "'" + ASCQBean.getPractice().getPracticeBasic().isPracticeBasic2() + "'," +
                    "'" + ASCQBean.getPractice().getPracticeBasic().isPracticeBasic3() + "'," +
                    "'" + ASCQBean.getPractice().getPractice1() + "'," +
                    "'" + ASCQBean.getPractice().getPractice2() + "'," +
                    "'" + ASCQBean.getPractice().getPractice3() + "'," +
                    "'" + ASCQBean.getPractice().getPractice4() + "'," +
                    "'" + ASCQBean.getPractice().getPractice5() + "'," +
                    "'" + ASCQBean.getPractice().getPractice6() + "'," +
                    "'" + ASCQBean.getPractice().getPractice7() + "'," +
                    "'" + ASCQBean.getPractice().getPracticeSelf() + "'," +
                    "'" + ASCQBean.getPractice().getPracticeMutual() + "'," +
                    "'" + ASCQBean.getGenres().getGenresBasic().isGenresBasic1() + "'," +
                    "'" + ASCQBean.getGenres().getGenresBasic().isGenresBasic2() + "'," +
                    "'" + ASCQBean.getGenres().getGenresBasic().isGenresBasic3() + "'," +
                    "'" + ASCQBean.getGenres().getGenres1() + "'," +
                    "'" + ASCQBean.getGenres().getGenres2() + "'," +
                    "'" + ASCQBean.getGenres().getGenres3() + "'," +
                    "'" + ASCQBean.getGenres().getGenres4() + "'," +
                    "'" + ASCQBean.getGenres().getGenres5() + "'," +
                    "'" + ASCQBean.getGenres().getGenresSelf() + "'," +
                    "'" + ASCQBean.getGenres().getGenresMutual() + "'," +
                    "'" + ASCQBean.getTeam().getTeamBasic().isTeamBasic1() + "'," +
                    "'" + ASCQBean.getTeam().getTeamBasic().isTeamBasic2() + "'," +
                    "'" + ASCQBean.getTeam().getTeamBasic().isTeamBasic3() + "'," +
                    "'" + ASCQBean.getTeam().getTeam1() + "'," +
                    "'" + ASCQBean.getTeam().getTeam2() + "'," +
                    "'" + ASCQBean.getTeam().getTeam3() + "'," +
                    "'" + ASCQBean.getTeam().getTeam4() + "'," +
                    "'" + ASCQBean.getTeam().getTeam5() + "'," +
                    "'" + ASCQBean.getTeam().getTeam6() + "'," +
                    "'" + ASCQBean.getTeam().getTeam7() + "'," +
                    "'" + ASCQBean.getTeam().getTeamSelf() + "'," +
                    "'" + ASCQBean.getTeam().getTeamMutual() + "'," +
                    "'" + ASCQBean.getExtra().getExtraSelf() + "'," +
                    "'" + ASCQBean.getExtra().getExtraMutual() + "'," +
                    "'" + mutualCode + "')";

            dbHelper = new DBHelper(sql);
            dbHelper.pst.executeUpdate();
            dbHelper.close();

            hashMap.put("status", 200);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 101);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }
}