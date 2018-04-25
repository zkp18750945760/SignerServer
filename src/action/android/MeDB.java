package action.android;

import action.android.bean.ActivityBean;
import action.android.bean.UserBean;
import action.android.bean.VolunteerBean;
import com.alibaba.fastjson.JSON;
import db.DBHelper;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import utils.MapUtils;
import utils.SchoolYearUtils;
import utils.StringUtils;
import utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeDB {

    /**
     * 登录
     *
     * @param userId       用户ID
     * @param userPassword 用户密码
     * @param stuSerialNo  设备序列号
     * @return 200->登录成功 101->服务器无响应 102->数据库IO错误 103->密码错误
     */
    public static String login(String userId, String userPassword, String stuSerialNo) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select * from students where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("stuPassword").equals(userPassword)) {
                    System.out.println(userId + " " + TimeUtils.getCurrentTimeSecond() + "登陆成功");

                    UserBean user = new UserBean(
                            resultSet.getString("stuId"),
                            resultSet.getString("stuName"),
                            resultSet.getString("stuDuty"),
                            resultSet.getString("stuGrade"),
                            resultSet.getString("stuMajor"),
                            resultSet.getString("stuClass"),
                            resultSet.getString("stuPassword"));

                    resultSet.close();
                    dbHelper.close();

                    //第一次登陆的时候绑定设备序列号
                    if (resultSet.getString("stuSerialNo") == null ||
                            resultSet.getString("stuSerialNo").equals("")) {
                        sql = "update students set stuSerialNo = '" + stuSerialNo + "' where stuId = '" + userId + "'";
                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();
                    }

                    hashMap.put("status", 200);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    hashMap.put("user", user.getUser());
                    String jsonString = JSON.toJSONString(hashMap);
                    System.out.println(jsonString);
                    return jsonString;
                } else {
                    //密码错误的情况
                    hashMap.put("status", 103);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
        hashMap.put("status", 101);
        hashMap.put("time", TimeUtils.getCurrentTime());
        return JSON.toJSONString(hashMap);
    }

    /**
     * 上传或修改头像
     *
     * @param userId       用户ID
     * @param headIcon     头像文件
     * @param headIconName 文件名
     * @return 101->文件为空 102->文件IO错误 103->数据库IO错误 200->上传/修改成功
     */
    public static String uploadHeadIcon(String userId, File headIcon, String headIconName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (headIcon == null) {
            hashMap.put("status", 101);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
        try {
            utils.FileUtils.uploadFile(headIcon, headIconName, "/imgs");
            String sql = "select table_name from information_schema.tables where table_name = 'headIcons'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();
            if (tabCount == 0) {
                //还没有头像表，建立表格
                sql = "create table headIcons(stuId varchar(11) not null," +
                        "headIconUrl varchar(50) not null," +
                        "foreign key (stuId) references students(stuId))";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            String realPath = ServletActionContext.getServletContext().getRealPath("imgs");
            File file = new File(realPath, headIconName);
            FileUtils.copyFile(headIcon, file);

            sql = "select * from headIcons where stuId = '" + userId + "'";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //直接插入数据库中
                sql = "insert into headIcons(stuId,headIconUrl) values('" + userId + "','" + ("/imgs/" + headIconName) + "')";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            } else {
                sql = "update headIcons set headIconUrl = '" + ("/imgs/" + headIconName) + "' where stuId = '" + userId + "'";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            hashMap.put("status", 200);
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("headIconUrl", ("/imgs/" + headIconName));
            return JSON.toJSONString(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 103);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 获取用户头像
     *
     * @param userId 用户ID
     * @return 200->成功 101->还没有上传头像 102->数据库IO错误
     */
    public static String getHeadIcon(String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select table_name from information_schema.tables where table_name = 'headIcons'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                sql = "select * from headIcons where stuId = '" + userId + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                while (resultSet.next()) {
                    hashMap.put("headIconUrl", resultSet.getString("headIconUrl"));
                    break;
                }
                resultSet.close();
                dbHelper.close();

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
     * 解绑设备(一个月内只能解绑一次)
     *
     * @param userId 用户ID
     * @return 200->解绑成功 101->距离上一次解绑不到30天 102->数据库IO错误 103->服务器无响应
     */
    public static String unBindDevice(String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select * from students where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                //获取上一次解绑时间
                String stuUnBindTime = resultSet.getString("stuUnBindTime");
                long currentTime = TimeUtils.getCurrentTimeSecond();
                if (!stuUnBindTime.equals("")) {
                    long unBindTime = Long.parseLong(stuUnBindTime);
                    //获取当前系统时间
                    if (currentTime - unBindTime > 2592000) {
                        //30天可以解绑一次设备
                        resultSet.close();
                        dbHelper.close();

                        sql = "update students set stuUnBindTime = '" + currentTime + "'," +
                                "stuSerialNo = '" + "" + "' where stuId = '" + userId + "'";

                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();

                        //解绑成功
                        hashMap.put("status", 200);
                        hashMap.put("time", currentTime);
                        return JSON.toJSONString(hashMap);
                    } else {
                        //距离上一次解绑时间不到30天
                        hashMap.put("status", 101);
                        hashMap.put("time", unBindTime);
                        return JSON.toJSONString(hashMap);
                    }
                } else {
                    //从未解绑过，可以解绑
                    sql = "update students set stuUnBindTime = '" + currentTime + "'," +
                            "stuSerialNo = '" + "" + "' where stuId = '" + userId + "'";

                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

                    //解绑成功
                    hashMap.put("status", 200);
                    hashMap.put("time", currentTime);
                    return JSON.toJSONString(hashMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
        hashMap.put("status", 103);
        hashMap.put("time", TimeUtils.getCurrentTime());
        return JSON.toJSONString(hashMap);
    }

    /**
     * 绑定序列号
     *
     * @param userId 用户ID
     * @param uuId   序列号
     * @return 200->绑定成功 101->未解绑，请先解绑 102->数据库IO错误 103->服务器无响应
     */
    public static String bindDevice(String userId, String uuId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select * from students where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                //获取该用户的序列号
                String stuSerialNo = resultSet.getString("stuSerialNo");
                resultSet.close();
                dbHelper.close();

                if (stuSerialNo.equals("")) {
                    //序列号为空，可以绑定
                    sql = "update students set stuSerialNo = '" + uuId + "' where stuId = '" + userId + "'";
                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

                    hashMap.put("status", 200);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    //序列号不为空，请先解绑
                    hashMap.put("status", 101);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
        hashMap.put("status", 103);
        hashMap.put("time", TimeUtils.getCurrentTime());
        return JSON.toJSONString(hashMap);
    }

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param stuPassword 旧密码
     * @param newPassword 新密码
     * @return 200->修改成功 101->旧密码错误 102->数据库IO错误 103->服务器无响应
     */
    public static String modifyPassword(String userId, String stuPassword, String newPassword) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select * from students where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                if (stuPassword.equals(resultSet.getString("stuPassword"))) {
                    //旧密码正确，可以修改密码
                    UserBean user = new UserBean(
                            resultSet.getString("stuId"),
                            resultSet.getString("stuName"),
                            resultSet.getString("stuDuty"),
                            resultSet.getString("stuGrade"),
                            resultSet.getString("stuMajor"),
                            resultSet.getString("stuClass"),
                            newPassword);

                    resultSet.close();
                    dbHelper.close();

                    sql = "update students set stuPassword = '" + newPassword + "' where stuId = '" + userId + "'";
                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();
                    hashMap.put("status", 200);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    hashMap.put("user", user.getUser());
                    return JSON.toJSONString(hashMap);
                } else {
                    //旧密码不正确，不可以修改密码
                    hashMap.put("status", 101);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
        hashMap.put("status", 103);
        hashMap.put("time", TimeUtils.getCurrentTime());
        return JSON.toJSONString(hashMap);
    }

    /**
     * 获取活动信息
     *
     * @param userId 用户ID
     * @return 200->成功 101->数据库IO错误
     */
    public static String getActivities(String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();
        try {
            String sql = "select * from activities";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            String timeStart, timeEnd;
            String secondMonth;

            while (resultSet.next()) {
                HashMap<String, Object> map = new HashMap<>();
                String actName = resultSet.getString("actName");
                String actTime = resultSet.getString("actTime");
                String actYear = resultSet.getString("actYear") + "-";
                String actLocation = resultSet.getString("actLocation");

                timeStart = actYear + StringUtils.getSubString(actTime, "(.*?)~") + " " +
                        actTime.substring(actTime.length() - 5);

                secondMonth = StringUtils.getSubString(actTime, "~(.*?) ");

                if (secondMonth.length() < 3) {
                    //活动不跨月
                    timeEnd = actYear + actTime.substring(0, 3) +
                            secondMonth + " " + actTime.substring(actTime.length() - 5);
                } else {
                    //活动跨月
                    timeEnd = actYear + secondMonth + " " + actTime.substring(actTime.length() - 5);
                }

                if (TimeUtils.calculateTimeLagAct(timeEnd, TimeUtils.getCurrentTime()) >= 0) {
                    map.put("actName", actName);
                    map.put("actTime", actTime);
                    map.put("actLocation", actLocation);

                    sql = "select * from `" + (actName + actTime) + "` where stuId = '" + userId + "'";
                    dbHelper = new DBHelper(sql);
                    ResultSet enrollInfo = dbHelper.pst.executeQuery();
                    tabCount = 0;
                    while (enrollInfo.next()) {
                        tabCount++;
                    }
                    enrollInfo.close();
                    dbHelper.close();
                    if (tabCount == 0) {
                        map.put("enroll", false);
                    } else {
                        map.put("enroll", true);
                    }
                    jsonObjects.add(map);
                }
            }
            resultSet.close();
            hashMap.put("status", 200);
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("activities", jsonObjects);
            return JSON.toJSONString(hashMap);
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 101);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 报名文体活动
     *
     * @param userId  用户ID
     * @param actName 活动名称
     * @param actTime 活动时间
     * @return 200->成功 101->已报名该活动 102->数据库IO错误
     */
    public static String applyActivities(String userId, String actName, String actTime) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select * from `" + (actName + actTime) + "` where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //该用户还没有报名对应的活动
                sql = "insert into `" + (actName + actTime) + "` " +
                        "(stuId,enrollTime) values ('" + userId + "','" + TimeUtils.getCurrentTime() + "')";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();

                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                ActivityBean activityBean = JSON.parseObject(getActivities(userId), ActivityBean.class);
                hashMap.put("activities", activityBean.getActivities());
                return JSON.toJSONString(hashMap);
            } else {
                //该用户已报名对应的活动
                hashMap.put("status", 101);
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
     * 取消文体活动的报名
     *
     * @param userId  用户ID
     * @param actName 活动名称
     * @param actTime 活动时间
     * @return 200->成功 101->未报名 102->数据库IO错误
     */
    public static String cancelApplyActivities(String userId, String actName, String actTime) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select * from `" + (actName + actTime) + "` where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //未报名
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                sql = "delete from `" + (actName + actTime) + "` where stuId = '" + userId + "'";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();

                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                ActivityBean activityBean = JSON.parseObject(getActivities(userId), ActivityBean.class);
                hashMap.put("activities", activityBean.getActivities());
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
     * 获取志愿活动信息
     *
     * @param userId 用户ID
     * @return 200->成功 101->数据库IO错误
     */
    public static String getVolunteers(String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();
        try {
            String sql = "select * from volunteers";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            String timeStart, timeEnd;
            String secondMonth;

            while (resultSet.next()) {
                HashMap<String, Object> map = new HashMap<>();
                String volName = resultSet.getString("volName");
                String volTime = resultSet.getString("volTime");
                String volYear = resultSet.getString("volYear") + "-";

                timeStart = volYear + StringUtils.getSubString(volTime, "(.*?)~") + " 00:00";

                secondMonth = volTime.substring(6);

                if (secondMonth.length() < 3) {
                    //活动不跨月
                    timeEnd = volYear + volTime.substring(0, 4) +
                            secondMonth + " 00:00";
                } else {
                    //活动跨月
                    timeEnd = volYear + secondMonth + " 00:00";
                }

                if (TimeUtils.calculateTimeLagAct(timeEnd, TimeUtils.getCurrentTime()) >= 0) {

                    map.put("volName", volName);
                    map.put("volTime", volTime);

                    sql = "select * from `" + (volName + volTime) + "` where stuId = '" + userId + "'";
                    dbHelper = new DBHelper(sql);
                    ResultSet enrollInfo = dbHelper.pst.executeQuery();
                    tabCount = 0;
                    while (enrollInfo.next()) {
                        tabCount++;
                    }
                    enrollInfo.close();
                    dbHelper.close();
                    if (tabCount == 0) {
                        map.put("enroll", false);
                    } else {
                        map.put("enroll", true);
                    }
                    jsonObjects.add(map);
                }
            }
            resultSet.close();
            hashMap.put("status", 200);
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("volunteers", jsonObjects);
            return JSON.toJSONString(hashMap);
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 101);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 报名志愿活动
     *
     * @param userId  用户ID
     * @param volName 活动名称
     * @param volTime 活动时间
     * @return 200->成功 101->已报名该活动 102->数据库IO错误
     */
    public static String applyVolunteers(String userId, String volName, String volTime) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select * from `" + (volName + volTime) + "` where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //该用户还没有报名对应的活动
                sql = "insert into `" + (volName + volTime) + "` " +
                        "(stuId,enrollTime) values ('" + userId + "','" + TimeUtils.getCurrentTime() + "')";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();

                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                VolunteerBean volunteerBean = JSON.parseObject(getVolunteers(userId), VolunteerBean.class);
                hashMap.put("volunteers", volunteerBean.getVolunteers());
                return JSON.toJSONString(hashMap);
            } else {
                //该用户已报名对应的活动
                hashMap.put("status", 101);
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
     * 取消志愿活动的报名
     *
     * @param userId  用户ID
     * @param volName 活动名称
     * @param volTime 活动时间
     * @return 200->成功 101->未报名 102->数据库IO错误
     */
    public static String cancelApplyVolunteers(String userId, String volName, String volTime) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String sql = "select * from `" + (volName + volTime) + "` where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //未报名
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                sql = "delete from `" + (volName + volTime) + "` where stuId = '" + userId + "'";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();

                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                VolunteerBean volunteerBean = JSON.parseObject(getVolunteers(userId), VolunteerBean.class);
                hashMap.put("volunteers", volunteerBean.getVolunteers());
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
     * 查询某个用户今天所有的日程
     *
     * @param userId 用户ID
     * @return 200->成功 101->数据库IO错误
     */
    public static String getAllSchedule(String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            //1.查询今天所有已选的课程
            String sql = "select * from students where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            String tabName = "";
            while (resultSet.next()) {
                tabName = SchoolYearUtils.getGradeCode(resultSet.getString("stuGrade")) +
                        SchoolYearUtils.getMajorCode(resultSet.getString("stuMajor")) +
                        SchoolYearUtils.getClassCode(resultSet.getString("stuClass")) +
                        SchoolYearUtils.getTermCodeByMonth(userId,
                                Integer.parseInt(TimeUtils.getCurrentYear()),
                                TimeUtils.getMonthOfYear());
            }
            resultSet.close();
            dbHelper.close();

            StringBuffer selectedCourse = new StringBuffer();
            sql = "select * from `" + (tabName + "b") + "` where stuId = '" + userId + "'";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getMetaData().getColumnCount());
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    if (resultSet.getString(i).equals("√")) {
                        selectedCourse.append(resultSet.getMetaData().getColumnName(i)).append(",");
                    }
                }
            }

            resultSet.close();
            dbHelper.close();

            sql = "select * from `" + (tabName + "a") + "` where courseWeek = '" + (TimeUtils.getDayOfWeek() + "") + "' " +
                    "group by courseTime";

            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                if (selectedCourse.toString().contains(resultSet.getString("courseName"))) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("week", TimeUtils.getDayOfWeekString());
                    map.put("date", TimeUtils.getDayOfMonth());
                    map.put("time", resultSet.getString("courseTime"));
                    map.put("content", resultSet.getString("courseName"));
                    map.put("address", resultSet.getString("courseLocation"));
                    map.put("type", 1);

                    jsonObjects.add(map);
                }
            }
            resultSet.close();
            dbHelper.close();

            //2.查询今天所有的会议
            sql = "select * from meetings where metTime like '" + (TimeUtils.getCurrentDate() + "%") + "'";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("week", TimeUtils.getDayOfWeekString());
                map.put("date", TimeUtils.getDayOfMonth());
                map.put("time", resultSet.getString("metTime"));
                map.put("content", resultSet.getString("metName"));
                map.put("address", resultSet.getString("metLocation"));
                map.put("type", 2);

                jsonObjects.add(map);
            }
            resultSet.close();
            dbHelper.close();

            //3.查询今天所有已报名的文体活动
            sql = "select * from activities";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            int tabCount;

            String timeEnd;
            String secondMonth;

            while (resultSet.next()) {
                String actName = resultSet.getString("actName");
                String actYear = resultSet.getString("actYear") + "-";
                String actTime = resultSet.getString("actTime");

                sql = "select * from `" + (actName + actTime) + "` where stuId = '" + userId + "'";
                DBHelper helper = new DBHelper(sql);
                ResultSet set = helper.pst.executeQuery();
                tabCount = 0;
                while (set.next()) {
                    tabCount++;
                }
                set.close();
                helper.close();

                if (tabCount > 0) {
                    secondMonth = StringUtils.getSubString(actTime, "~(.*?) ");

                    if (secondMonth.length() < 3) {
                        //活动不跨月
                        timeEnd = actYear + actTime.substring(0, 3) +
                                secondMonth + " " + actTime.substring(actTime.length() - 5);
                    } else {
                        //活动跨月
                        timeEnd = actYear + secondMonth + " " + actTime.substring(actTime.length() - 5);
                    }

                    if (TimeUtils.calculateTimeLagAct(timeEnd, TimeUtils.getCurrentTime()) >= 0) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("week", TimeUtils.getDayOfWeekString());
                        map.put("date", TimeUtils.getDayOfMonth());
                        map.put("time", actTime);
                        map.put("content", actName);
                        map.put("address", resultSet.getString("actLocation"));
                        map.put("type", 3);

                        jsonObjects.add(map);
                    }
                }
            }
            resultSet.close();
            dbHelper.close();

            //4.查询今天所有已报名的志愿活动
            sql = "select * from volunteers";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();


            while (resultSet.next()) {
                String volName = resultSet.getString("volName");
                String volTime = resultSet.getString("volTime");
                String volYear = resultSet.getString("volYear") + "-";

                sql = "select * from `" + (volName + volTime) + "` where stuId = '" + userId + "'";
                DBHelper helper = new DBHelper(sql);
                ResultSet set = helper.pst.executeQuery();
                tabCount = 0;
                while (set.next()) {
                    tabCount++;
                }

                set.close();
                helper.close();

                if (tabCount > 0) {

                    secondMonth = volTime.substring(6);
                    if (secondMonth.length() < 3) {
                        //活动不跨月
                        timeEnd = volYear + volTime.substring(0, 4) +
                                secondMonth + " 00:00";
                    } else {
                        //活动跨月
                        timeEnd = volYear + secondMonth + " 00:00";
                    }

                    if (TimeUtils.calculateTimeLagAct(timeEnd, TimeUtils.getCurrentTime()) >= 0) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("week", TimeUtils.getDayOfWeekString());
                        map.put("date", TimeUtils.getDayOfMonth());
                        map.put("time", volTime);
                        map.put("content", volName);
                        map.put("address", "");
                        hashMap.put("type", 4);

                        jsonObjects.add(map);
                    }
                }
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
     * 获取所有banner图片信息
     *
     * @return 200->成功 101->数据库IO错误
     */
    public static String getBanners() {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();
        try {
            String sql = "select * from banner";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("uploadTime", resultSet.getString("uploadTime"));
                map.put("bannerUrl", resultSet.getString("bannerUrl"));
                map.put("bannerIndex", resultSet.getInt("bannerIndex"));
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
     * 获取当前用户需要签到的事项(课堂和会议)
     *
     * @param userId 用户ID
     * @return 200->成功 101->数据库IO错误
     */
    public static String getSignEvents(String userId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            //1.查询今天所有已选的课程(上课前10分钟到上课后5分钟)
            String sql = "select * from students where stuId = '" + userId + "'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet;
            resultSet = dbHelper.pst.executeQuery();

            String tabName = "";
            while (resultSet.next()) {
                tabName = SchoolYearUtils.getGradeCode(resultSet.getString("stuGrade")) +
                        SchoolYearUtils.getMajorCode(resultSet.getString("stuMajor")) +
                        SchoolYearUtils.getClassCode(resultSet.getString("stuClass")) +
                        SchoolYearUtils.getTermCodeByMonth(userId,
                                Integer.parseInt(TimeUtils.getCurrentYear()),
                                TimeUtils.getMonthOfYear());
            }
            resultSet.close();
            dbHelper.close();

            StringBuffer selectedCourse = new StringBuffer("");
            sql = "select * from `" + (tabName + "b") + "` where stuId = '" + userId + "'";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getMetaData().getColumnCount());
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    if (resultSet.getString(i).equals("√")) {
                        selectedCourse.append(resultSet.getMetaData().getColumnName(i)).append(",");
                    }
                }
            }

            resultSet.close();
            dbHelper.close();

            sql = "select * from `" + (tabName + "a") + "` where courseWeek = '" + (TimeUtils.getDayOfWeek() + "") + "' " +
                    "group by courseTime";

            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                long timeLag = TimeUtils.calculateTimeLag(
                        SchoolYearUtils.getClassBeginTime(resultSet.getString("courseTime")),
                        TimeUtils.getTimeSupreme());
                if (selectedCourse.toString().contains(resultSet.getString("courseName")) &&
                        (timeLag <= 600000 && timeLag >= -300000)) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("timeLag", timeLag);
                    map.put("week", TimeUtils.getDayOfWeekString());
                    map.put("date", TimeUtils.getDayOfMonth());
                    map.put("time", resultSet.getString("courseTime"));
                    map.put("content", resultSet.getString("courseName"));
                    map.put("address", resultSet.getString("courseLocation"));
                    map.put("type", 1);

                    jsonObjects.add(map);
                }
            }
            resultSet.close();
            dbHelper.close();

            //2.查询今天所有的会议(开会前15分钟到上课后10分钟)
            sql = "select * from meetings where metTime like '" + (TimeUtils.getCurrentDate() + "%") + "'";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                long timeLagMeet = TimeUtils.calculateTimeLagMeet(
                        resultSet.getString("metTime"),
                        TimeUtils.getCurrentTime());
                if (timeLagMeet <= 900000 && timeLagMeet >= -600000) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("timeLag", timeLagMeet);
                    map.put("week", TimeUtils.getDayOfWeekString());
                    map.put("date", TimeUtils.getDayOfMonth());
                    map.put("time", resultSet.getString("metTime"));
                    map.put("content", resultSet.getString("metName"));
                    map.put("address", resultSet.getString("metLocation"));
                    map.put("type", 2);

                    jsonObjects.add(map);
                }
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
     * 获取事项的发起签到状态
     *
     * @param content 主题
     * @return 201->还未发起签到 202->已经发起签到 101->数据库IO错误
     */
    public static String getEventsSponsorSignStatus(String content) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {

            String sql = "select table_name from information_schema.tables where table_name = 'signs'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //签到表还不存在
                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("status", 201);
                return JSON.toJSONString(hashMap);
            } else {
                sql = "select * from signs where signContent = '" + content + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                tabCount = 0;

                while (resultSet.next()) {
                    tabCount++;
                }
                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //还未发起签到
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    hashMap.put("status", 201);
                    return JSON.toJSONString(hashMap);
                } else {
                    //已经发起签到
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    hashMap.put("status", 202);
                    return JSON.toJSONString(hashMap);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 101);
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 发起签到
     *
     * @param userId    用户ID
     * @param grade     年级
     * @param major     专业
     * @param clazz     班级
     * @param type      类型(1---课程  2---会议)
     * @param time      时间
     * @param content   主题
     * @param longitude 经度
     * @param latitude  纬度
     * @param radius    定位精度
     * @return 200->成功 101->数据库IO错误 102->服务器无响应 103->已经发起签到了
     */
    public static String sponsorSign(String userId, String grade, String major, String clazz, int type, String time,
                                     String content, double longitude, double latitude, float radius) {

        HashMap<String, Object> hashMap = new HashMap<>();

        String tableName = null;
        String theme = null;
        int tabCount = 0;
        String sql = "select table_name from information_schema.tables where table_name = 'signs'";
        DBHelper dbHelper = new DBHelper(sql);
        ResultSet resultSet;

        if (type == 1) {
            tableName = content + SchoolYearUtils.getGradeCode(grade)
                    + SchoolYearUtils.getMajorCode(major)
                    + SchoolYearUtils.getClassCode(clazz)
                    + SchoolYearUtils.getTermCodeByMonth(userId,
                    Integer.parseInt(TimeUtils.getCurrentYear()),
                    TimeUtils.getMonthOfYear());

            theme = tableName + TimeUtils.getCurrentDate() + " " + SchoolYearUtils.getClassBeginTime(time);

        } else if (type == 2) {
            tableName = content + time;
            theme = tableName;
        }

        try {
            resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //签到信息表不存在，创建签到信息表
                sql = "create table signs(signContent varchar(50) primary key," +
                        "sponsorTime char(19)," +
                        "sponsorID char(11)," +
                        "longitude varchar(20)," +
                        "latitude varchar(20)," +
                        "radius varchar(20)," +
                        "type char(1))";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            sql = "select * from signs where signContent = '" + theme + "'";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();

            tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount > 0) {
                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("status", 103);
                return JSON.toJSONString(hashMap);
            } else {
                sql = "insert into signs(signContent,sponsorTime,sponsorID,longitude,latitude,radius,type) " +
                        "values('" + theme + "','"
                        + TimeUtils.getCurrentTime() + "','"
                        + userId + "','"
                        + longitude + "','"
                        + latitude + "','"
                        + radius + "','"
                        + type + "')";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();

            hashMap.put("time", TimeUtils.getCurrentTime());
            hashMap.put("status", 101);
            return JSON.toJSONString(hashMap);
        }

        if (type == 1) {
            //课堂签到
            try {
                sql = "select table_name from information_schema.tables where table_name = '" + tableName + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                tabCount = 0;
                while (resultSet.next()) {
                    tabCount++;
                }

                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //对应的签到表还不存在，需要创建该签到表
                    sql = "create table `" + tableName + "`(" +
                            "stuId varchar(11) not null," +
                            "stuName varchar(20) not null," +
                            "foreign key(stuId) references students(stuId))";
                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

                    //把选课的学生学号和姓名插入表中
                    sql = "select * from `" + (SchoolYearUtils.getGradeCode(grade)
                            + SchoolYearUtils.getMajorCode(major)
                            + SchoolYearUtils.getClassCode(clazz)
                            + SchoolYearUtils.getTermCodeByMonth(userId,
                            Integer.parseInt(TimeUtils.getCurrentYear()),
                            TimeUtils.getMonthOfYear())
                            + "b") + "`";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    while (resultSet.next()) {
                        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                            if (resultSet.getMetaData().getColumnName(i).equals(content) &&
                                    resultSet.getString(content).equals("√")) {
                                sql = "insert into `" + tableName + "`(stuId,stuName) " +
                                        "values('" + resultSet.getString("stuId") + "'," +
                                        "'" + resultSet.getString("stuName") + "')";

                                DBHelper helper = new DBHelper(sql);
                                helper.pst.executeUpdate();
                                helper.close();
                            }
                        }
                    }
                    resultSet.close();
                    dbHelper.close();
                }

                sql = "alter table `" + tableName + "` add `" + TimeUtils.getCurrentDate() + " " + SchoolYearUtils.getClassBeginTime(time) + "` varchar(2) default '3'";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();

                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("status", 200);
                return JSON.toJSONString(hashMap);
            } catch (SQLException e) {
                e.printStackTrace();

                hashMap.put("time", TimeUtils.getCurrentTime());
                hashMap.put("status", 101);
                return JSON.toJSONString(hashMap);
            }
        } else if (type == 2) {
            //会议签到
            try {
                sql = "alter table `" + tableName + "` add(signStatus varchar(2),signTime char(19))";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();

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

        hashMap.put("time", TimeUtils.getCurrentTime());
        hashMap.put("status", 102);
        return JSON.toJSONString(hashMap);
    }

    /**
     * 签到
     *
     * @param userId      用户ID
     * @param grade       年级
     * @param major       专业
     * @param clazz       班级
     * @param type        类型(1---课程  2---会议)
     * @param time        时间
     * @param content     主题
     * @param longitude   经度
     * @param latitude    纬度
     * @param radius      定位精度
     * @param stuSerialNo 设备序列号
     * @return 200->成功 101->还未发起签到 102->不在教室范围内 103->已经签到过了 104->数据库IO错误 105->不是本机签到 106->服务器无响应
     */
    public static String sign(String userId, String grade, String major, String clazz, int type, String time,
                              String content, double longitude, double latitude, float radius, String stuSerialNo) {

        HashMap<String, Object> hashMap = new HashMap<>();

        String sql;
        String tableName;
        int tabCount = 0;
        DBHelper dbHelper;
        ResultSet resultSet;
        if (type == 1) {
            //课堂签到
            try {
                tableName = content + SchoolYearUtils.getGradeCode(grade)
                        + SchoolYearUtils.getMajorCode(major)
                        + SchoolYearUtils.getClassCode(clazz)
                        + SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()),
                        TimeUtils.getMonthOfYear());

                sql = "select * from signs where signContent = '" + (tableName + TimeUtils.getCurrentDate() + " " + SchoolYearUtils.getClassBeginTime(time)) + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                tabCount = 0;
                while (resultSet.next()) {
                    tabCount++;
                }
                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //还未发起签到
                    hashMap.put("status", 101);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    //已经发起签到
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    while (resultSet.next()) {
                        //获取数据库中对应item的发起签到人的经纬度坐标，计算是否在相应的半径范围内，若不在，则不能进行签到
                        double longitudeDB = Double.parseDouble(resultSet.getString("longitude"));
                        double latitudeDB = Double.parseDouble(resultSet.getString("latitude"));
                        float radiusDB = Float.parseFloat(resultSet.getString("radius"));
                        if (radiusDB < 100.0f) {
                            radiusDB = 100.0f;
                        }
                        double distance = MapUtils.distanceOfTwoPoints(latitudeDB, longitudeDB, latitude, longitude);
                        if (distance > (radiusDB + radius)) {
                            //不在规定范围内，不给签到
                            hashMap.put("status", 102);
                            hashMap.put("time", TimeUtils.getCurrentTime());
                            return JSON.toJSONString(hashMap);
                        } else {
                            //在规定范围内，可以签到

                            //1.查看设备序列号是不是一致
                            sql = "select stuSerialNo from students where stuId = '" + userId + "'";
                            dbHelper = new DBHelper(sql);
                            resultSet = dbHelper.pst.executeQuery();
                            String stuSerialNoDB = "";
                            while (resultSet.next()) {
                                stuSerialNoDB = resultSet.getString("stuSerialNo");
                            }
                            resultSet.close();
                            dbHelper.close();

                            if (stuSerialNo.equals(stuSerialNoDB)) {
                                //查看用户是否已经签到
                                sql = "select * from `" + tableName + "` where stuId = '" + userId + "'";
                                DBHelper helper = new DBHelper(sql);
                                ResultSet query = helper.pst.executeQuery();

                                tabCount = 0;
                                while (query.next()) {
                                    String signStatus =
                                            query.getString(TimeUtils.getCurrentDate() + " " + SchoolYearUtils.getClassBeginTime(time));
                                    if (signStatus != null && !signStatus.equals("")) {
                                        tabCount++;
                                    }
                                }
                                query.close();
                                helper.close();

                                if (tabCount > 0) {
                                    //已经签到过了哦
                                    hashMap.put("status", 103);
                                    hashMap.put("time", TimeUtils.getCurrentTime());
                                    return JSON.toJSONString(hashMap);
                                } else {
                                    //还未签到，可以签到
                                    //时间判断，准时/迟到
                                    long timeLag = TimeUtils.calculateTimeLag(SchoolYearUtils.getClassBeginTime(time), TimeUtils.getTimeSupreme());
                                    if (timeLag >= 0 && timeLag <= 600000) {
                                        //准时
                                        sql = "update `" + tableName + "` set `"
                                                + TimeUtils.getCurrentDate() + " " + SchoolYearUtils.getClassBeginTime(time) + "` = 1 " +
                                                "where stuId = '" + userId + "'";

                                    } else if (timeLag < 0 && timeLag >= -300000) {
                                        //迟到
                                        sql = "update `" + tableName + "` set `"
                                                + TimeUtils.getCurrentDate() + " " + SchoolYearUtils.getClassBeginTime(time) + "` = 2 " +
                                                "where stuId = '" + userId + "'";
                                    } else if (timeLag < -300000) {
                                        //旷课
                                        sql = "update `" + tableName + "` set `"
                                                + TimeUtils.getCurrentDate() + " " + SchoolYearUtils.getClassBeginTime(time) + "` = 3 " +
                                                "where stuId = '" + userId + "'";
                                    }
                                    helper = new DBHelper(sql);
                                    helper.pst.executeUpdate();
                                    helper.close();

                                    hashMap.put("status", 200);
                                    hashMap.put("time", TimeUtils.getCurrentTime());
                                    return JSON.toJSONString(hashMap);
                                }
                            } else {
                                hashMap.put("status", 105);
                                hashMap.put("time", TimeUtils.getCurrentTime());
                                return JSON.toJSONString(hashMap);
                            }
                        }
                    }
                    resultSet.close();
                    dbHelper.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                hashMap.put("status", 104);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            }
        } else if (type == 2) {
            //会议签到
            try {
                tableName = content + time;

                System.out.println(tableName);

                sql = "select * from signs where signContent = '" + tableName + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                tabCount = 0;
                while (resultSet.next()) {
                    tabCount++;
                }
                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //还未发起签到
                    hashMap.put("status", 101);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    //已经发起签到
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    while (resultSet.next()) {
                        //获取数据库中对应item的发起签到人的经纬度坐标，计算是否在相应的半径范围内，若不在，则不能进行签到
                        double longitudeDB = Double.parseDouble(resultSet.getString("longitude"));
                        double latitudeDB = Double.parseDouble(resultSet.getString("latitude"));
                        float radiusDB = Float.parseFloat(resultSet.getString("radius"));
                        if (radiusDB < 100.0f) {
                            radiusDB = 100.0f;
                        }
                        double distance = MapUtils.distanceOfTwoPoints(latitudeDB, longitudeDB, latitude, longitude);
                        if (distance > (radiusDB + radius)) {
                            //不在规定的范围内，不可以进行签到
                            hashMap.put("status", 102);
                            hashMap.put("time", TimeUtils.getCurrentTime());
                            return JSON.toJSONString(hashMap);
                        } else {
                            //在规定的距离范围内，可以签到

                            //1.查看设备序列号是不是一致
                            sql = "select stuSerialNo from students where stuId = '" + userId + "'";
                            dbHelper = new DBHelper(sql);
                            resultSet = dbHelper.pst.executeQuery();
                            String stuSerialNoDB = "";
                            while (resultSet.next()) {
                                stuSerialNoDB = resultSet.getString("stuSerialNo");
                            }
                            resultSet.close();
                            dbHelper.close();

                            if (stuSerialNo.equals(stuSerialNoDB)) {
                                //判断用户是否签到过了
                                sql = "select * from `" + tableName + "` where userId = '" + userId + "'";
                                DBHelper helper = new DBHelper(sql);
                                ResultSet query = helper.pst.executeQuery();

                                tabCount = 0;
                                while (query.next()) {
                                    String signStatus = query.getString("signStatus");
                                    if (signStatus != null && !signStatus.equals("")) {
                                        tabCount++;
                                    }
                                }
                                query.close();
                                helper.close();

                                if (tabCount > 0) {
                                    //已经签到过了
                                    hashMap.put("status", 103);
                                    hashMap.put("time", TimeUtils.getCurrentTime());
                                    return JSON.toJSONString(hashMap);
                                } else {
                                    //还未签到，可以签到
                                    //时间判断，准时/迟到
                                    long timeLagMeet = TimeUtils.calculateTimeLagMeet(time,
                                            TimeUtils.getCurrentTime());
                                    if (timeLagMeet >= 0 && timeLagMeet <= 900000) {
                                        //准时
                                        sql = "update `" + tableName + "` set signStatus = '1',signTime = '"
                                                + TimeUtils.getCurrentTime() + "' where userId = '" + userId + "'";

                                    } else if (timeLagMeet < 0 && timeLagMeet >= -600000) {
                                        //迟到
                                        sql = "update `" + tableName + "` set signStatus = '2',signTime = '"
                                                + TimeUtils.getCurrentTime() + "' where userId = '" + userId + "'";

                                    } else if (timeLagMeet < -600000) {
                                        //旷课
                                        sql = "update `" + tableName + "` set signStatus = '3',signTime = '"
                                                + TimeUtils.getCurrentTime() + "' where userId = '" + userId + "'";
                                    }
                                    helper = new DBHelper(sql);
                                    helper.pst.executeUpdate();
                                    helper.close();

                                    hashMap.put("status", 200);
                                    hashMap.put("time", TimeUtils.getCurrentTime());
                                    return JSON.toJSONString(hashMap);
                                }
                            } else {
                                hashMap.put("status", 105);
                                hashMap.put("time", TimeUtils.getCurrentTime());
                                return JSON.toJSONString(hashMap);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                hashMap.put("status", 104);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            }
        }
        hashMap.put("status", 106);
        hashMap.put("time", TimeUtils.getCurrentTime());
        return JSON.toJSONString(hashMap);
    }

    /**
     * 获取某个签到事项已签到的人的头像连接
     *
     * @param userId  用户ID
     * @param grade   用户年级
     * @param major   用户专业
     * @param clazz   用户班级
     * @param type    事项类型
     * @param time    事项开始时间
     * @param content 事项主题
     * @return 200->成功 101->未发起签到 102->数据库IO错误 103->服务器无响应
     */
    public static String getSignedHeadIcons(String userId, String grade, String major, String clazz,
                                            int type, String time, String content) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        String sql, tableName;
        DBHelper dbHelper;
        ResultSet resultSet;
        int tabCount = 0;

        if (type == 1) {
            //课堂签到
            try {
                tableName = content + SchoolYearUtils.getGradeCode(grade) +
                        SchoolYearUtils.getMajorCode(major) +
                        SchoolYearUtils.getClassCode(clazz) +
                        SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

                sql = "select table_name from information_schema.tables where table_name = '" + tableName + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                tabCount = 0;
                while (resultSet.next()) {
                    tabCount++;
                }

                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //还未发起签到
                    hashMap.put("status", 101);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    sql = "select * from signs where signContent = '"
                            + (tableName + TimeUtils.getCurrentDate() + " " + SchoolYearUtils.getClassBeginTime(time)) + "'";

                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    tabCount = 0;
                    while (resultSet.next()) {
                        tabCount++;
                    }

                    resultSet.close();
                    dbHelper.close();

                    if (tabCount == 0) {
                        //班长为发起签到
                        hashMap.put("status", 101);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);
                    } else {
                        //已经发起签到
                        sql = "select * from `" + tableName + "`";
                        dbHelper = new DBHelper(sql);
                        resultSet = dbHelper.pst.executeQuery();
                        File file;

                        while (resultSet.next()) {
                            String signed = resultSet.getString(TimeUtils.getCurrentDate() + " " + SchoolYearUtils.getClassBeginTime(time));
                            if (signed != null && !signed.equals("")) {
                                String realPath = ServletActionContext.getServletContext().getRealPath("/imgs");
                                file = new File(realPath, userId + ".jpg");
                                HashMap<String, Object> map = new HashMap<>();
                                if (file.exists()) {
                                    map.put("headIconUrl", "/imgs/" + userId + ".jpg");
                                } else {
                                    map.put("headIconUrl", "/imgs/default.png");
                                }
                                map.put("userId", resultSet.getString("stuId"));
                                map.put("userName", resultSet.getString("stuName"));
                                jsonObjects.add(map);
                            }
                        }

                        resultSet.close();
                        dbHelper.close();

                        hashMap.put("data", jsonObjects);
                        hashMap.put("status", 200);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                hashMap.put("status", 102);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            }

        } else if (type == 2) {
            //会议签到
            try {
                tableName = content + time;

                sql = "select table_name from information_schema.tables where table_name = '" + tableName + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                tabCount = 0;
                while (resultSet.next()) {
                    tabCount++;
                }

                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //还未签到
                    hashMap.put("status", 101);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    sql = "select * from signs where signContent = '" + tableName + "'";

                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    tabCount = 0;
                    while (resultSet.next()) {
                        tabCount++;
                    }

                    resultSet.close();
                    dbHelper.close();

                    if (tabCount == 0) {
                        //班长还未发起签到
                        hashMap.put("status", 101);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);
                    } else {
                        //已经发起签到
                        sql = "select * from `" + tableName + "`";
                        dbHelper = new DBHelper(sql);
                        resultSet = dbHelper.pst.executeQuery();
                        File file;

                        while (resultSet.next()) {
                            String signStatus = resultSet.getString("signStatus");
                            if (signStatus != null && !signStatus.equals("")) {
                                String realPath = ServletActionContext.getServletContext().getRealPath("/imgs");
                                file = new File(realPath, userId + ".jpg");
                                HashMap<String, Object> map = new HashMap<>();
                                if (file.exists()) {
                                    map.put("headIconUrl", "/imgs/" + userId + ".jpg");
                                } else {
                                    map.put("headIconUrl", "/imgs/default.png");
                                }
                                map.put("userId", resultSet.getString("stuId"));
                                map.put("userName", resultSet.getString("stuName"));
                                jsonObjects.add(map);
                            }
                        }

                        resultSet.close();
                        dbHelper.close();

                        hashMap.put("data", jsonObjects);
                        hashMap.put("status", 200);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                hashMap.put("status", 102);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            }
        }

        hashMap.put("status", 103);
        hashMap.put("time", TimeUtils.getCurrentTime());
        return JSON.toJSONString(hashMap);
    }
}
