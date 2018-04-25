package action.android;

import action.android.bean.ASCQBean;
import action.android.bean.UpdateASCQBean;
import com.alibaba.fastjson.JSON;
import db.DBHelper;
import utils.SchoolYearUtils;
import utils.TimeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhoukp
 * @time 2018/4/12 15:20
 * @email 275557625@qq.com
 * @function 综测审核处理类
 */
public class ASCQ {

    /**
     * 获取某个年级的某个专业的某个班级的所有学生信息
     *
     * @param userId 用户ID
     * @param grade  年级
     * @param major  专业
     * @param clazz  班级
     * @return 200->成功 101->学生表不存在 102->数据库IO错误
     */
    public static String getClazzStudents(String userId, String grade, String major, String clazz) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {

            String mutualCode = SchoolYearUtils.getGradeCode(grade) + SchoolYearUtils.getMajorCode(major) +
                    SchoolYearUtils.getClassCode(clazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'students'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //学生信息表还不存在
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //学生信息表已经存在，查询相应的数据返回给用户
                sql = "select * from students where stuGrade = '"
                        + grade + "' and stuMajor = '"
                        + major + "' and stuClass = '"
                        + clazz + "'";

                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                DBHelper helper;
                ResultSet set;

                while (resultSet.next()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("stuName", resultSet.getString("stuName"));
                    map.put("stuId", resultSet.getString("stuId"));

                    if (resultSet.getString("stuId").equals(userId)) {
                        map.put("isInvite", true);
                    } else {
                        sql = "select table_name from information_schema.tables where table_name = 'Mutual'";
                        tabCount = 0;
                        helper = new DBHelper(sql);
                        set = helper.pst.executeQuery();

                        while (set.next()) {
                            tabCount++;
                        }

                        set.close();
                        helper.close();

                        if (tabCount == 0) {
                            map.put("isInvite", false);
                        } else {
                            sql = "select * from Mutual where mutualCode = '" + mutualCode
                                    + "' and mutualId = '" + resultSet.getString("stuId") + "'";
                            helper = new DBHelper(sql);
                            set = helper.pst.executeQuery();
                            tabCount = 0;

                            while (set.next()) {
                                tabCount++;
                            }

                            set.close();
                            helper.close();

                            if (tabCount == 1) {
                                map.put("isInvite", true);
                            }
                        }
                    }
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

            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());

            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 邀请某个学生进入互评小组
     *
     * @param userId    邀请人ID
     * @param userGrade 邀请人年级
     * @param userMajor 邀请人专业
     * @param userClazz 邀请人班级
     * @param mutualId  被邀请人ID
     * @return 200->成功 101->已经邀请过该同学 102->数据库IO错误 103->互评小组人数超过上限6人
     */
    public static String applyMutual(String userId, String userGrade, String userMajor, String userClazz, String mutualId) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'Mutual'";
            int tabCount = 0;
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //互评表还不存在，代码创建互评表
                sql = "create table Mutual(" +
                        "mutualCode varchar(10)," +
                        "mutualId char(11)," +
                        "userId char(11)," +
                        "time char(19)," +
                        "foreign key(mutualId) references students(stuId)," +
                        "foreign key(userId) references students(stuId)," +
                        "primary key (mutualCode,mutualId))";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            sql = "select * from Mutual where mutualCode = '" + mutualCode + "' and mutualId = '" + userId + "'";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //申请人还没有加入互评小组
                //把申请人插入表格中
                sql = "insert into Mutual (mutualCode,mutualId,userId,time) values ('" + mutualCode + "'," +
                        "'" + userId + "'," +
                        "'" + userId + "'," +
                        "'" + TimeUtils.getCurrentTime() + "')";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            sql = "select * from Mutual where mutualCode = '" + mutualCode + "'";
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount < 6) {
                //互评表已经存在，进行相应的读写操作
                sql = "select * from Mutual where mutualCode = '" + mutualCode + "' and mutualId = '" + mutualId + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                tabCount = 0;

                while (resultSet.next()) {
                    tabCount++;
                }

                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //该条数据不存在,直接插入数据库中
                    sql = "insert into Mutual (mutualCode,mutualId,userId,time) values ('" + mutualCode + "'," +
                            "'" + mutualId + "'," +
                            "'" + userId + "'," +
                            "'" + TimeUtils.getCurrentTime() + "')";
                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

                    hashMap.put("status", 200);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);

                } else {
                    hashMap.put("status", 101);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                }
            } else {
                hashMap.put("status", 103);
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
     * 取消邀请某个学生进入互评小组
     *
     * @param userId    邀请人ID
     * @param userGrade 邀请人年级
     * @param userMajor 邀请人专业
     * @param userClazz 邀请人班级
     * @param mutualId  被邀请人ID
     * @return 200->成功 101->互评表不存在 102->数据库IO错误 103->班长必须是互评小组成员
     */
    public static String cancelMutual(String userId, String userGrade, String userMajor, String userClazz, String mutualId) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'Mutual'";
            int tabCount = 0;
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();

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
                //如果这个人是班长自己就不能取消邀请
                sql = "select stuDuty from students where stuId = '" + mutualId + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                String stuDuty = "";
                while (resultSet.next()) {
                    stuDuty = resultSet.getString("stuDuty");
                }
                resultSet.close();
                dbHelper.close();

                if (stuDuty.equals("班长")) {
                    hashMap.put("status", 103);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    sql = "delete from Mutual where mutualCode = '" + mutualCode + "' and mutualId = '" + mutualId + "'";
                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

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

    /**
     * 获取该班级测评小组人数
     *
     * @param userId    邀请人ID
     * @param userGrade 邀请人年级
     * @param userMajor 邀请人专业
     * @param userClazz 邀请人班级
     * @return 200->成功 101->互评表不存在 102->数据库IO错误
     */
    public static String getMutualNum(String userId, String userGrade, String userMajor, String userClazz) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'Mutual'";

            int tabCount = 0;
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //互评表还不存在
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //互评表已经存在,查询该班级互评小组人数
                ArrayList<String> mutualIds = new ArrayList<>();
                sql = "select mutualId from Mutual where mutualCode = '" + mutualCode + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                while (resultSet.next()) {
                    mutualIds.add(resultSet.getString("mutualId"));
                }

                resultSet.close();
                dbHelper.close();

                hashMap.put("mutualIds", mutualIds.size());
                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            //互评表还不存在
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 随机分配综测表给互评小组
     *
     * @param userId    邀请人ID
     * @param userGrade 邀请人年级
     * @param userMajor 邀请人专业
     * @param userClazz 邀请人班级
     * @return 200->成功 101->互评表不存在 102->数据库IO错误 103->已经分配了
     */
    public static String groupMutual(String userId, String userGrade, String userMajor, String userClazz) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'Mutual'";

            int tabCount = 0;
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //互评表还不存在
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //互评表已经存在，进行相应的分组操作
                //1.判断该班级的数据有没有分配
                tabCount = 0;
                sql = "select mutualId,stuId from ASQC where mutualCode = '" + mutualCode + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                //该班级所有学生学号的集合
                ArrayList<String> studentIds = new ArrayList<>();

                while (resultSet.next()) {
                    if (!(resultSet.getString("mutualId") == null
                            || resultSet.getString("mutualId").equals(""))) {
                        tabCount++;
                    }
                    studentIds.add(resultSet.getString("stuId"));
                }
                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //该班级的综测成绩还没有分配
                    //2.获取该班级测评小组人数
                    ArrayList<String> mutualIds = new ArrayList<>();
                    sql = "select mutualId from Mutual where mutualCode = '" + mutualCode + "'";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    while (resultSet.next()) {
                        mutualIds.add(resultSet.getString("mutualId"));
                    }

                    resultSet.close();
                    dbHelper.close();

                    //3.开始比较随机地分配
                    ArrayList<ArrayList<String>> groups = new ArrayList<>();

                    for (int i = 0; i < mutualIds.size(); i++) {
                        groups.add(new ArrayList<>());
                    }

                    //取到的随机数
                    int randomNum;
                    //第几次去随机数
                    int count = 0;

                    while (studentIds.size() > 0) {
                        randomNum = (int) (Math.random() * studentIds.size());
                        while (mutualIds.get(count % mutualIds.size()).equals(studentIds.get(randomNum))) {
                            randomNum = (int) (Math.random() * studentIds.size());
                        }
                        groups.get(count % mutualIds.size()).add(studentIds.get(randomNum));
                        studentIds.remove(randomNum);
                        count++;
                    }//随机数结束

                    //根据分组信息更新数据库
                    for (int i = 0; i < groups.size(); i++) {
                        for (int j = 0; j < groups.get(i).size(); j++) {
                            sql = "update ASQC set mutualId = '" + mutualIds.get(i) + "' " +
                                    "where stuId = '" + groups.get(i).get(j) + "'";
                            dbHelper = new DBHelper(sql);
                            dbHelper.pst.executeUpdate();
                            dbHelper.close();
                        }
                    }//分组结束

                    sql = "select table_name from information_schema.tables where table_name = 'groupMutual'";
                    tabCount = 0;
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    while (resultSet.next()) {
                        tabCount++;
                    }

                    resultSet.close();
                    dbHelper.close();

                    if (tabCount == 0) {
                        //分组记录表还不存在，创建该表
                        sql = "create table groupMutual(" +
                                "mutualCode char(11) primary key," +
                                "isGroup varchar(5) not null default 'false')";
                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();
                    }

                    //分组记录表已经存在，打分组标记
                    sql = "update groupMutual set isGroup = 'true' where mutualCode = '" + mutualCode + "'";
                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

                    hashMap.put("status", 200);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);

                } else {
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
    }


    /**
     * 获取某个班级学生上报综测成绩是否已经分配给互评小组
     *
     * @param userId    用户ID
     * @param userGrade 年级
     * @param userMajor 专业
     * @param userClazz 班级
     * @return 200->成功 102->数据库IO错误
     */
    public static String getGroupMutualStatus(String userId, String userGrade, String userMajor, String userClazz) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'groupMutual'";
            int tabCount = 0;
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                hashMap.put("isGroup", "false");
                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                sql = "select * from groupMutual where mutualCode = '" + mutualCode + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                while (resultSet.next()) {
                    hashMap.put("isGroup", resultSet.getString("isGroup"));
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
     * 判断某个学生是不是互评小组成员
     *
     * @param userId    用户ID
     * @param userGrade 年级
     * @param userMajor 专业
     * @param userClazz 班级
     * @return 200->成功 101->数据库IO错误
     */
    public static String isMutualMembers(String userId, String userGrade, String userMajor, String userClazz) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'Mutual'";
            int tabCount = 0;
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //互评表还不存在，肯定不是互评小组成员
                hashMap.put("isMember", false);
                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //互评表存在的情况
                sql = "select * from Mutual where mutualCode = '" + mutualCode + "' and mutualId = '" + userId + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                tabCount = 0;

                while (resultSet.next()) {
                    tabCount++;
                }

                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    hashMap.put("isMember", false);
                } else {
                    hashMap.put("isMember", true);
                }

                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 101);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 判断是不是分配的任务都审核完了
     *
     * @param userId    用户ID
     * @param userGrade 年级
     * @param userMajor 专业
     * @param userClazz 班级
     * @return 200->成功 101->数据库IO错误 102->没有任务
     */
    public static String isMutualFinish(String userId, String userGrade, String userMajor, String userClazz) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'ASQC'";
            int tabCount = 0;
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //待审核的综测表还不存在
                hashMap.put("hasTask", false);
                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //待审核的综测表存在
                sql = "select isMutual from ASQC where mutualCode = '" + mutualCode + "' and mutualId = '" + userId + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                hashMap.put("hasTask", true);

                boolean isFinish = true;

                while (resultSet.next()) {
                    if (resultSet.getString("isMutual").equals("false")) {
                        isFinish = false;
                        break;
                    }
                }

                resultSet.close();
                dbHelper.close();

                hashMap.put("isFinish", isFinish);
                hashMap.put("status", 200);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 101);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }

    }

    /**
     * 获取当前用户还没有完成的任务列表
     *
     * @param userId    用户ID
     * @param userGrade 年级
     * @param userMajor 专业
     * @param userClazz 班级
     * @return 200->成功 101->数据库IO错误 102->待审核的综测表还不存在
     */
    public static String getMutualTask(String userId, String userGrade, String userMajor, String userClazz) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'ASQC'";
            int tabCount = 0;
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //待审核的综测表还不存在
                hashMap.put("status", 102);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //待审核的综测表存在，查找当前用户的任务
                sql = "select * from ASQC where mutualCode = '" + mutualCode + "' and mutualId = '" + userId + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                while (resultSet.next()) {
                    //查找未完成的任务
                    if (resultSet.getString("isMutual").equals("false")) {
                        HashMap<String, Object> itemMap = new HashMap<>();
                        HashMap<String, Object> map = new HashMap<>();
                        HashMap<String, Object> base = new HashMap<>();

                        map.put("moral1", resultSet.getFloat("moral1"));
                        map.put("moral2", resultSet.getFloat("moral2"));
                        map.put("moral3", resultSet.getFloat("moral3"));
                        map.put("moral4", resultSet.getFloat("moral4"));
                        map.put("moral5", resultSet.getFloat("moral5"));
                        map.put("moral6", resultSet.getFloat("moral6"));
                        map.put("moralMax", resultSet.getFloat("moralMax"));
                        map.put("moralSelf", resultSet.getFloat("moralSelf"));
                        map.put("moralMutual", resultSet.getFloat("moralMutual"));
                        itemMap.put("moral", map);

                        map = new HashMap<>();
                        map.put("GPA", resultSet.getFloat("GPA"));
                        map.put("witMax", resultSet.getFloat("witMax"));
                        map.put("witSelf", resultSet.getFloat("witSelf"));
                        map.put("witMutual", resultSet.getFloat("witMutual"));
                        itemMap.put("wit", map);

                        map = new HashMap<>();
                        map.put("level", resultSet.getInt("level"));
                        map.put("sportsMax", resultSet.getInt("sportsMax"));
                        map.put("sportsSelf", resultSet.getInt("sportsSelf"));
                        map.put("sportsMutual", resultSet.getInt("sportsMutual"));
                        itemMap.put("sports", map);

                        map = new HashMap<>();
                        base.put("practiceBasic1", resultSet.getString("practiceBasic1"));
                        base.put("practiceBasic2", resultSet.getString("practiceBasic2"));
                        base.put("practiceBasic3", resultSet.getString("practiceBasic3"));
                        map.put("practiceBasic", base);
                        map.put("practice1", resultSet.getFloat("practice1"));
                        map.put("practice2", resultSet.getFloat("practice2"));
                        map.put("practice3", resultSet.getFloat("practice3"));
                        map.put("practice4", resultSet.getFloat("practice4"));
                        map.put("practice5", resultSet.getFloat("practice5"));
                        map.put("practice6", resultSet.getFloat("practice6"));
                        map.put("practice7", resultSet.getFloat("practice7"));
                        map.put("practiceMax", resultSet.getFloat("practiceMax"));
                        map.put("practiceSelf", resultSet.getFloat("practiceSelf"));
                        map.put("practiceMutual", resultSet.getFloat("practiceMutual"));
                        itemMap.put("practice", map);

                        map = new HashMap<>();
                        base = new HashMap<>();
                        base.put("GenresBasic1", resultSet.getString("GenresBasic1"));
                        base.put("GenresBasic2", resultSet.getString("GenresBasic2"));
                        base.put("GenresBasic3", resultSet.getString("GenresBasic3"));
                        map.put("GenresBasic", base);
                        map.put("Genres1", resultSet.getFloat("Genres1"));
                        map.put("Genres2", resultSet.getFloat("Genres2"));
                        map.put("Genres3", resultSet.getFloat("Genres3"));
                        map.put("Genres4", resultSet.getFloat("Genres4"));
                        map.put("Genres5", resultSet.getFloat("Genres5"));
                        map.put("GenresMax", resultSet.getFloat("GenresMax"));
                        map.put("GenresSelf", resultSet.getFloat("GenresSelf"));
                        map.put("GenresMutual", resultSet.getFloat("GenresMutual"));
                        itemMap.put("Genres", map);

                        map = new HashMap<>();
                        base = new HashMap<>();
                        base.put("teamBasic1", resultSet.getString("teamBasic1"));
                        base.put("teamBasic2", resultSet.getString("teamBasic2"));
                        base.put("teamBasic3", resultSet.getString("teamBasic3"));
                        map.put("teamBasic", base);
                        map.put("team1", resultSet.getFloat("team1"));
                        map.put("team2", resultSet.getFloat("team2"));
                        map.put("team3", resultSet.getFloat("team3"));
                        map.put("team4", resultSet.getFloat("team4"));
                        map.put("team5", resultSet.getFloat("team5"));
                        map.put("team6", resultSet.getFloat("team6"));
                        map.put("team7", resultSet.getFloat("team7"));
                        map.put("teamMax", resultSet.getFloat("teamMax"));
                        map.put("teamSelf", resultSet.getFloat("teamSelf"));
                        map.put("teamMutual", resultSet.getFloat("teamMutual"));
                        itemMap.put("team", map);

                        map = new HashMap<>();
                        map.put("extraMax", resultSet.getFloat("extraMax"));
                        map.put("extraSelf", resultSet.getFloat("extraSelf"));
                        map.put("extraMutual", resultSet.getFloat("extraMutual"));
                        itemMap.put("extra", map);

                        itemMap.put("schoolYear", resultSet.getString("schoolYear"));
                        itemMap.put("stuId", resultSet.getString("stuId"));
                        itemMap.put("mutualId", resultSet.getString("mutualId"));
                        itemMap.put("mutualCode", resultSet.getString("mutualCode"));
                        itemMap.put("time", resultSet.getString("time"));
                        jsonObjects.add(itemMap);
                    }
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
            hashMap.put("status", 101);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }

    }

    /**
     * 上传互评成绩
     *
     * @param ascqBean  ASCQBean
     * @param userId    用户ID
     * @param userGrade 年级
     * @param userMajor 专业
     * @param userClazz 班级
     * @return 200->成功 101->该条数据已经被审核过了 102->数据库IO错误
     */
    public static String uploadMutual(ASCQBean ascqBean, String userId, String userGrade, String userMajor, String userClazz) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'CheckedASCQ'";
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();

            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //审核后的综测表还不存在，代码创建审核后的综测表
                //审核通过表还不存在，代码创建该表格
                sql = "create table CheckedASCQ(" +
                        "checkedStuId char(11) not null," +
                        "stuId char(11) not null," +
                        "schoolYear char(9) not null," +
                        "time char(19) not null," +
                        "checkedTime char(19) not null," +
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
                        "mutualCode varchar(10) not null," +
                        "isConfirm varchar(5) not null default 'false'," +
                        "isModify varchar(5) not null default 'false'," +
                        "foreign key(stuId) references students(stuId)," +
                        "foreign key(checkedStuId) references students(stuId)," +
                        "primary key (stuId,schoolYear))";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            //审核后的综测表已经存在，将学生数据写入表中
            //1.判断该条数据是不是已经存在
            sql = "select stuId from CheckedASCQ where stuId = '" + ascqBean.getUserId() + "' " +
                    "and schoolYear = '" + ascqBean.getSchoolYear() + "'";
            dbHelper = new DBHelper(sql);
            tabCount = 0;
            resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 1) {
                //该条学生的数据已经存在
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //获取该学年第二台账的前10%，判断当前用户的互评得分是不是要加上0.5分
                sql = "select ceil(count(stuId) / 5) as totalStudent from students where (stuGrade,stuMajor,stuClass) " +
                        "in ((select stuGrade,stuMajor,stuClass from students where stuId = '" + userId + "'))";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                int totalStudent = 5;
                while (resultSet.next()) {
                    totalStudent = resultSet.getInt("totalStudent");
                }
                resultSet.close();
                dbHelper.close();

                System.out.println("totalStudent==" + totalStudent);

                sql = "select @rownum:=@rownum+1 as rownum,if(@total=total,@rank,@rank:=@rownum) as rank,@total:=total,stuId,A.* " +
                        "from(select stuId," +
                        "sum(count) as total " +
                        "from secondledger " +
                        "where (stuId) in " +
                        "((select stuId from students " +
                        "where (stuGrade,stuMajor,stuClass) in ((select stuGrade,stuMajor,stuClass from students where stuId = '" + userId + "')))) " +
                        "group by stuId order by total desc)A,(select @rank:=0,@rownum:=0,@total:=null)B;";

                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();

                int rank = 0;
                int rankAscq = 1;

                while (resultSet.next()) {
                    if (resultSet.getInt("rownum") == totalStudent) {
                        rank = resultSet.getInt("rank");
                    }

                    if (resultSet.getString("stuId").equals(ascqBean.getUserId())) {
                        rankAscq = resultSet.getInt("rank");
                    }
                }
                resultSet.close();
                dbHelper.close();

                System.out.println("rankAscq==" + rankAscq + ",rank==" + rank);

                float genresMutual = ascqBean.getGenres().getGenresMutual();

                if (rankAscq <= rank) {
                    genresMutual += 0.5f;
                }

                //该条学生的数据还不存在，直接插入数据库中
                sql = "insert into CheckedASCQ(checkedStuId,stuId,schoolYear,time,checkedTime,moral1,moral2,moral3,moral4,moral5,moral6," +
                        "moralSelf,moralMutual,GPA,witSelf,witMutual,level,sportsSelf,sportsMutual," +
                        "practiceBasic1,practiceBasic2,practiceBasic3,practice1,practice2,practice3,practice4,practice5," +
                        "practice6,practice7,practiceSelf,practiceMutual,GenresBasic1,GenresBasic2,GenresBasic3," +
                        "Genres1,Genres2,Genres3,Genres4,Genres5,GenresSelf,GenresMutual,teamBasic1,teamBasic2," +
                        "teamBasic3,team1,team2,team3,team4,team5,team6,team7,teamSelf,teamMutual,extraSelf,extraMutual,mutualCode)" +
                        " values ('" + userId + "'," +
                        "'" + ascqBean.getUserId() + "'," +
                        "'" + ascqBean.getSchoolYear() + "'," +
                        "'" + ascqBean.getTime() + "'," +
                        "'" + TimeUtils.getCurrentTime() + "'," +
                        "'" + ascqBean.getMoral().getMoral1() + "'," +
                        "'" + ascqBean.getMoral().getMoral2() + "'," +
                        "'" + ascqBean.getMoral().getMoral3() + "'," +
                        "'" + ascqBean.getMoral().getMoral4() + "'," +
                        "'" + ascqBean.getMoral().getMoral5() + "'," +
                        "'" + ascqBean.getMoral().getMoral6() + "'," +
                        "'" + ascqBean.getMoral().getMoralSelf() + "'," +
                        "'" + ascqBean.getMoral().getMoralMutual() + "'," +
                        "'" + ascqBean.getWit().getGPA() + "'," +
                        "'" + ascqBean.getWit().getWitSelf() + "'," +
                        "'" + ascqBean.getWit().getWitMutual() + "'," +
                        "'" + ascqBean.getSports().getLevel() + "'," +
                        "'" + ascqBean.getSports().getSportsSelf() + "'," +
                        "'" + ascqBean.getSports().getSportsMutual() + "'," +
                        "'" + ascqBean.getPractice().getPracticeBasic().isPracticeBasic1() + "'," +
                        "'" + ascqBean.getPractice().getPracticeBasic().isPracticeBasic2() + "'," +
                        "'" + ascqBean.getPractice().getPracticeBasic().isPracticeBasic3() + "'," +
                        "'" + ascqBean.getPractice().getPractice1() + "'," +
                        "'" + ascqBean.getPractice().getPractice2() + "'," +
                        "'" + ascqBean.getPractice().getPractice3() + "'," +
                        "'" + ascqBean.getPractice().getPractice4() + "'," +
                        "'" + ascqBean.getPractice().getPractice5() + "'," +
                        "'" + ascqBean.getPractice().getPractice6() + "'," +
                        "'" + ascqBean.getPractice().getPractice7() + "'," +
                        "'" + ascqBean.getPractice().getPracticeSelf() + "'," +
                        "'" + ascqBean.getPractice().getPracticeMutual() + "'," +
                        "'" + ascqBean.getGenres().getGenresBasic().isGenresBasic1() + "'," +
                        "'" + ascqBean.getGenres().getGenresBasic().isGenresBasic2() + "'," +
                        "'" + ascqBean.getGenres().getGenresBasic().isGenresBasic3() + "'," +
                        "'" + ascqBean.getGenres().getGenres1() + "'," +
                        "'" + ascqBean.getGenres().getGenres2() + "'," +
                        "'" + ascqBean.getGenres().getGenres3() + "'," +
                        "'" + ascqBean.getGenres().getGenres4() + "'," +
                        "'" + ascqBean.getGenres().getGenres5() + "'," +
                        "'" + ascqBean.getGenres().getGenresSelf() + "'," +
                        "'" + genresMutual + "'," +
                        "'" + ascqBean.getTeam().getTeamBasic().isTeamBasic1() + "'," +
                        "'" + ascqBean.getTeam().getTeamBasic().isTeamBasic2() + "'," +
                        "'" + ascqBean.getTeam().getTeamBasic().isTeamBasic3() + "'," +
                        "'" + ascqBean.getTeam().getTeam1() + "'," +
                        "'" + ascqBean.getTeam().getTeam2() + "'," +
                        "'" + ascqBean.getTeam().getTeam3() + "'," +
                        "'" + ascqBean.getTeam().getTeam4() + "'," +
                        "'" + ascqBean.getTeam().getTeam5() + "'," +
                        "'" + ascqBean.getTeam().getTeam6() + "'," +
                        "'" + ascqBean.getTeam().getTeam7() + "'," +
                        "'" + ascqBean.getTeam().getTeamSelf() + "'," +
                        "'" + ascqBean.getTeam().getTeamMutual() + "'," +
                        "'" + ascqBean.getExtra().getExtraSelf() + "'," +
                        "'" + ascqBean.getExtra().getExtraMutual() + "'," +
                        "'" + mutualCode + "')";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();

                //将待审核综测表对应的数据打上已审核标记
                sql = "update ASQC set isMutual = 'true' where stuId = '" + ascqBean.getUserId() + "' " +
                        "and schoolYear = '" + ascqBean.getSchoolYear() + "'";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
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
     * 获取综测成绩
     *
     * @param userId     用户ID
     * @param schoolYear 学年
     * @param userGrade  年级
     * @param userMajor  专业
     * @param userClazz  班级
     * @return 200->成功(已审核成绩) 101->未审核成绩 102->待审核综测表还不存在 103->该用户还没有上传对应学年的综测成绩 104->数据库IO错误
     */
    public static String getASCQ(String userId, String schoolYear, String userGrade, String userMajor, String userClazz) {
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, Object> itemMap = new HashMap<>();

        try {
            String mutualCode = SchoolYearUtils.getGradeCode(userGrade) + SchoolYearUtils.getMajorCode(userMajor) +
                    SchoolYearUtils.getClassCode(userClazz) +
                    SchoolYearUtils.getTermCodeByMonth(userId, Integer.parseInt(TimeUtils.getCurrentYear()), TimeUtils.getMonthOfYear());

            String sql = "select table_name from information_schema.tables where table_name = 'ASQC'";
            DBHelper dbHelper = new DBHelper(sql);
            int tabCount = 0;

            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //待审核综测表还不存在
                hashMap.put("status", 102);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //带审核综测表已经存在
                //1.查看还用户的成绩存不存在
                sql = "select isMutual from ASQC where stuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                dbHelper = new DBHelper(sql);
                tabCount = 0;
                resultSet = dbHelper.pst.executeQuery();

                while (resultSet.next()) {
                    tabCount++;
                }

                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //该用户还没有上传对应学年的综测成绩
                    hashMap.put("status", 103);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    //该用户还已经上传对应学年的综测成绩，查看其是否被互评小组审核

                    //获取该用户班级总人数
                    tabCount = 0;
                    sql = "select count(stuId) as amount from ASQC where mutualCode = '" + mutualCode + "'";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    while (resultSet.next()) {
                        tabCount = resultSet.getInt("amount");
                    }

                    resultSet.close();
                    dbHelper.close();

                    sql = "select isMutual from ASQC where stuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    String isMutual = "false";

                    while (resultSet.next()) {
                        isMutual = resultSet.getString("isMutual");
                    }

                    resultSet.close();
                    dbHelper.close();

                    if (isMutual.equals("false")) {
                        //还没有被审核，返回该用户自己上传的成绩
                        //获取该用户在班上的排名
                        sql = "select @rownum:=@rownum+1 as rownum,if(@total=total,@rank,@rank:=@rownum) as rank,@total:=total,A.* " +
                                "from(select stuId," +
                                "schoolYear," +
                                "sum(moralSelf+witSelf+sportsSelf+practiceSelf+GenresSelf+teamSelf+extraSelf) as total " +
                                "from ASQC where stuId = '" + userId + "')A,(select @rank:=0,@rownum:=0,@total:=null)B";

                        dbHelper = new DBHelper(sql);
                        resultSet = dbHelper.pst.executeQuery();

                        int rank = tabCount;
                        float total = 0f;

                        while (resultSet.next()) {
                            rank = resultSet.getInt("rank");
                            total = resultSet.getFloat("total");
                        }

                        resultSet.close();
                        dbHelper.close();

                        itemMap.put("rank", rank);
                        itemMap.put("total", tabCount);
                        itemMap.put("totalScore", total);

                        sql = "select * from ASQC where stuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                        dbHelper = new DBHelper(sql);
                        resultSet = dbHelper.pst.executeQuery();

                        while (resultSet.next()) {
                            HashMap<String, Object> map = new HashMap<>();
                            HashMap<String, Object> base = new HashMap<>();

                            map.put("moral1", resultSet.getFloat("moral1"));
                            map.put("moral2", resultSet.getFloat("moral2"));
                            map.put("moral3", resultSet.getFloat("moral3"));
                            map.put("moral4", resultSet.getFloat("moral4"));
                            map.put("moral5", resultSet.getFloat("moral5"));
                            map.put("moral6", resultSet.getFloat("moral6"));
                            map.put("moralMax", resultSet.getFloat("moralMax"));
                            map.put("moralSelf", resultSet.getFloat("moralSelf"));
                            map.put("moralMutual", resultSet.getFloat("moralMutual"));
                            itemMap.put("moral", map);

                            map = new HashMap<>();
                            map.put("GPA", resultSet.getFloat("GPA"));
                            map.put("witMax", resultSet.getFloat("witMax"));
                            map.put("witSelf", resultSet.getFloat("witSelf"));
                            map.put("witMutual", resultSet.getFloat("witMutual"));
                            itemMap.put("wit", map);

                            map = new HashMap<>();
                            map.put("level", resultSet.getInt("level"));
                            map.put("sportsMax", resultSet.getInt("sportsMax"));
                            map.put("sportsSelf", resultSet.getInt("sportsSelf"));
                            map.put("sportsMutual", resultSet.getInt("sportsMutual"));
                            itemMap.put("sports", map);

                            map = new HashMap<>();
                            base.put("practiceBasic1", resultSet.getString("practiceBasic1"));
                            base.put("practiceBasic2", resultSet.getString("practiceBasic2"));
                            base.put("practiceBasic3", resultSet.getString("practiceBasic3"));
                            map.put("practiceBasic", base);
                            map.put("practice1", resultSet.getFloat("practice1"));
                            map.put("practice2", resultSet.getFloat("practice2"));
                            map.put("practice3", resultSet.getFloat("practice3"));
                            map.put("practice4", resultSet.getFloat("practice4"));
                            map.put("practice5", resultSet.getFloat("practice5"));
                            map.put("practice6", resultSet.getFloat("practice6"));
                            map.put("practice7", resultSet.getFloat("practice7"));
                            map.put("practiceMax", resultSet.getFloat("practiceMax"));
                            map.put("practiceSelf", resultSet.getFloat("practiceSelf"));
                            map.put("practiceMutual", resultSet.getFloat("practiceMutual"));
                            itemMap.put("practice", map);

                            map = new HashMap<>();
                            base = new HashMap<>();
                            base.put("GenresBasic1", resultSet.getString("GenresBasic1"));
                            base.put("GenresBasic2", resultSet.getString("GenresBasic2"));
                            base.put("GenresBasic3", resultSet.getString("GenresBasic3"));
                            map.put("GenresBasic", base);
                            map.put("Genres1", resultSet.getFloat("Genres1"));
                            map.put("Genres2", resultSet.getFloat("Genres2"));
                            map.put("Genres3", resultSet.getFloat("Genres3"));
                            map.put("Genres4", resultSet.getFloat("Genres4"));
                            map.put("Genres5", resultSet.getFloat("Genres5"));
                            map.put("GenresMax", resultSet.getFloat("GenresMax"));
                            map.put("GenresSelf", resultSet.getFloat("GenresSelf"));
                            map.put("GenresMutual", resultSet.getFloat("GenresMutual"));
                            itemMap.put("Genres", map);

                            map = new HashMap<>();
                            base = new HashMap<>();
                            base.put("teamBasic1", resultSet.getString("teamBasic1"));
                            base.put("teamBasic2", resultSet.getString("teamBasic2"));
                            base.put("teamBasic3", resultSet.getString("teamBasic3"));
                            map.put("teamBasic", base);
                            map.put("team1", resultSet.getFloat("team1"));
                            map.put("team2", resultSet.getFloat("team2"));
                            map.put("team3", resultSet.getFloat("team3"));
                            map.put("team4", resultSet.getFloat("team4"));
                            map.put("team5", resultSet.getFloat("team5"));
                            map.put("team6", resultSet.getFloat("team6"));
                            map.put("team7", resultSet.getFloat("team7"));
                            map.put("teamMax", resultSet.getFloat("teamMax"));
                            map.put("teamSelf", resultSet.getFloat("teamSelf"));
                            map.put("teamMutual", resultSet.getFloat("teamMutual"));
                            itemMap.put("team", map);

                            map = new HashMap<>();
                            map.put("extraMax", resultSet.getFloat("extraMax"));
                            map.put("extraSelf", resultSet.getFloat("extraSelf"));
                            map.put("extraMutual", resultSet.getFloat("extraMutual"));
                            itemMap.put("extra", map);

                            itemMap.put("schoolYear", resultSet.getString("schoolYear"));
                            itemMap.put("stuId", resultSet.getString("stuId"));
                            itemMap.put("time", resultSet.getString("time"));
                        }

                        resultSet.close();
                        dbHelper.close();

                        hashMap.put("data", itemMap);
                        hashMap.put("status", 101);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);

                    } else if (isMutual.equals("true")) {
                        //成绩已经被审核，返回审核后的成绩

                        sql = "select @rownum:=@rownum+1 as rownum,if(@total=total,@rank,@rank:=@rownum) as rank,@total:=total,A.* " +
                                "from(select stuId," +
                                "schoolYear," +
                                "sum(moralMutual+witMutual+sportsMutual+practiceMutual+GenresMutual+teamMutual+extraMutual) as total " +
                                "from CheckedASCQ where stuId = '" + userId + "')A,(select @rank:=0,@rownum:=0,@total:=null)B";

                        dbHelper = new DBHelper(sql);
                        resultSet = dbHelper.pst.executeQuery();

                        int rank = tabCount;
                        float total = 0f;

                        while (resultSet.next()) {
                            rank = resultSet.getInt("rank");
                            total = resultSet.getFloat("total");
                        }

                        resultSet.close();
                        dbHelper.close();

                        itemMap.put("rank", rank);
                        itemMap.put("total", tabCount);
                        itemMap.put("totalScore", total);

                        sql = "select * from CheckedASCQ where stuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                        dbHelper = new DBHelper(sql);
                        resultSet = dbHelper.pst.executeQuery();

                        while (resultSet.next()) {
                            HashMap<String, Object> map = new HashMap<>();
                            HashMap<String, Object> base = new HashMap<>();

                            map.put("moral1", resultSet.getFloat("moral1"));
                            map.put("moral2", resultSet.getFloat("moral2"));
                            map.put("moral3", resultSet.getFloat("moral3"));
                            map.put("moral4", resultSet.getFloat("moral4"));
                            map.put("moral5", resultSet.getFloat("moral5"));
                            map.put("moral6", resultSet.getFloat("moral6"));
                            map.put("moralMax", resultSet.getFloat("moralMax"));
                            map.put("moralSelf", resultSet.getFloat("moralSelf"));
                            map.put("moralMutual", resultSet.getFloat("moralMutual"));
                            itemMap.put("moral", map);

                            map = new HashMap<>();
                            map.put("GPA", resultSet.getFloat("GPA"));
                            map.put("witMax", resultSet.getFloat("witMax"));
                            map.put("witSelf", resultSet.getFloat("witSelf"));
                            map.put("witMutual", resultSet.getFloat("witMutual"));
                            itemMap.put("wit", map);

                            map = new HashMap<>();
                            map.put("level", resultSet.getInt("level"));
                            map.put("sportsMax", resultSet.getInt("sportsMax"));
                            map.put("sportsSelf", resultSet.getInt("sportsSelf"));
                            map.put("sportsMutual", resultSet.getInt("sportsMutual"));
                            itemMap.put("sports", map);

                            map = new HashMap<>();
                            base.put("practiceBasic1", resultSet.getString("practiceBasic1"));
                            base.put("practiceBasic2", resultSet.getString("practiceBasic2"));
                            base.put("practiceBasic3", resultSet.getString("practiceBasic3"));
                            map.put("practiceBasic", base);
                            map.put("practice1", resultSet.getFloat("practice1"));
                            map.put("practice2", resultSet.getFloat("practice2"));
                            map.put("practice3", resultSet.getFloat("practice3"));
                            map.put("practice4", resultSet.getFloat("practice4"));
                            map.put("practice5", resultSet.getFloat("practice5"));
                            map.put("practice6", resultSet.getFloat("practice6"));
                            map.put("practice7", resultSet.getFloat("practice7"));
                            map.put("practiceMax", resultSet.getFloat("practiceMax"));
                            map.put("practiceSelf", resultSet.getFloat("practiceSelf"));
                            map.put("practiceMutual", resultSet.getFloat("practiceMutual"));
                            itemMap.put("practice", map);

                            map = new HashMap<>();
                            base = new HashMap<>();
                            base.put("GenresBasic1", resultSet.getString("GenresBasic1"));
                            base.put("GenresBasic2", resultSet.getString("GenresBasic2"));
                            base.put("GenresBasic3", resultSet.getString("GenresBasic3"));
                            map.put("GenresBasic", base);
                            map.put("Genres1", resultSet.getFloat("Genres1"));
                            map.put("Genres2", resultSet.getFloat("Genres2"));
                            map.put("Genres3", resultSet.getFloat("Genres3"));
                            map.put("Genres4", resultSet.getFloat("Genres4"));
                            map.put("Genres5", resultSet.getFloat("Genres5"));
                            map.put("GenresMax", resultSet.getFloat("GenresMax"));
                            map.put("GenresSelf", resultSet.getFloat("GenresSelf"));
                            map.put("GenresMutual", resultSet.getFloat("GenresMutual"));
                            itemMap.put("Genres", map);

                            map = new HashMap<>();
                            base = new HashMap<>();
                            base.put("teamBasic1", resultSet.getString("teamBasic1"));
                            base.put("teamBasic2", resultSet.getString("teamBasic2"));
                            base.put("teamBasic3", resultSet.getString("teamBasic3"));
                            map.put("teamBasic", base);
                            map.put("team1", resultSet.getFloat("team1"));
                            map.put("team2", resultSet.getFloat("team2"));
                            map.put("team3", resultSet.getFloat("team3"));
                            map.put("team4", resultSet.getFloat("team4"));
                            map.put("team5", resultSet.getFloat("team5"));
                            map.put("team6", resultSet.getFloat("team6"));
                            map.put("team7", resultSet.getFloat("team7"));
                            map.put("teamMax", resultSet.getFloat("teamMax"));
                            map.put("teamSelf", resultSet.getFloat("teamSelf"));
                            map.put("teamMutual", resultSet.getFloat("teamMutual"));
                            itemMap.put("team", map);

                            map = new HashMap<>();
                            map.put("extraMax", resultSet.getFloat("extraMax"));
                            map.put("extraSelf", resultSet.getFloat("extraSelf"));
                            map.put("extraMutual", resultSet.getFloat("extraMutual"));
                            itemMap.put("extra", map);

                            itemMap.put("schoolYear", resultSet.getString("schoolYear"));
                            itemMap.put("stuId", resultSet.getString("stuId"));
                            itemMap.put("time", resultSet.getString("time"));
                            itemMap.put("checkedStuId", resultSet.getString("checkedStuId"));
                            itemMap.put("checkedTime", resultSet.getString("checkedTime"));
                            if (resultSet.getString("isConfirm").equals("false")) {
                                itemMap.put("isConfirm", false);
                            } else {
                                itemMap.put("isConfirm", true);
                            }
                        }
                        resultSet.close();
                        dbHelper.close();

                        hashMap.put("data", itemMap);
                        hashMap.put("status", 200);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);

                    } else {
                        hashMap.put("status", 103);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);
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

    /**
     * 确认综测成绩
     *
     * @param userId     用户ID
     * @param schoolYear 学年
     * @return 200->成功 101->还没有审核记录 102->数据库IO错误 103->已经确认过了
     */
    public static String confirmASCQ(String userId, String schoolYear) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String sql = "select table_name from information_schema.tables where table_name = 'CheckedASCQ'";
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();

            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //审核表还不存在，互评小组还没有审核综测成绩
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //审核表已经存在

                sql = "select isConfirm from CheckedASCQ where stuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                String isConfirm = "false";

                while (resultSet.next()) {
                    isConfirm = resultSet.getString("isConfirm");
                }

                resultSet.close();
                dbHelper.close();

                if (isConfirm.equals("true")) {
                    //该用户已经确认过该学年的综测成绩了
                    hashMap.put("status", 103);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    sql = "update CheckedASCQ set isConfirm = 'true' where stuId = '" + userId + "' " +
                            "and schoolYear = '" + schoolYear + "'";
                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

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

    /**
     * 请求修改综测成绩
     *
     * @param userId     用户ID
     * @param schoolYear 学年
     * @return 200->成功 101->还没有审核记录 102->数据库IO错误 103->已经请求修改过了 104->已经确认过得分了，不能再请求修改
     */
    public static String modifyASCQ(String userId, String schoolYear) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String sql = "select table_name from information_schema.tables where table_name = 'CheckedASCQ'";
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();

            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //审核表还不存在，互评小组还没有审核综测成绩
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //审核表已经存在

                sql = "select isConfirm from CheckedASCQ where stuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                String isConfirm = "false";

                while (resultSet.next()) {
                    isConfirm = resultSet.getString("isConfirm");
                }

                resultSet.close();
                dbHelper.close();

                if (isConfirm.equals("true")) {
                    //已经确认过得分了，不能再请求修改
                    hashMap.put("status", 104);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    sql = "select isModify from CheckedASCQ where stuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    String isModify = "false";

                    while (resultSet.next()) {
                        isModify = resultSet.getString("isModify");
                    }

                    resultSet.close();
                    dbHelper.close();

                    if (isModify.equals("true")) {
                        //已经请求修改过了
                        hashMap.put("status", 103);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);
                    } else {
                        //还没有请求修改过
                        sql = "update CheckedASCQ set isModify = 'true' where stuId = '" + userId + "' " +
                                "and schoolYear = '" + schoolYear + "'";
                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();

                        hashMap.put("status", 200);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }

    }

    /**
     * 获取某个学年的互评记录
     *
     * @param userId     用户ID
     * @param schoolYear 学年
     * @return 200->成功 101->还没有审核记录 102->数据库IO错误
     */
    public static String getMutualRecord(String userId, String schoolYear) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            String sql = "select table_name from information_schema.tables where table_name = 'CheckedASCQ'";
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //审核表还不存在，没有审核记录
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //审核表已经存在，查看审核记录
                sql = "select * from CheckedASCQ where checkedStuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                tabCount = 0;

                while (resultSet.next()) {

                    tabCount++;

                    HashMap<String, Object> itemMap = new HashMap<>();
                    HashMap<String, Object> map = new HashMap<>();
                    HashMap<String, Object> base = new HashMap<>();

                    map.put("moral1", resultSet.getFloat("moral1"));
                    map.put("moral2", resultSet.getFloat("moral2"));
                    map.put("moral3", resultSet.getFloat("moral3"));
                    map.put("moral4", resultSet.getFloat("moral4"));
                    map.put("moral5", resultSet.getFloat("moral5"));
                    map.put("moral6", resultSet.getFloat("moral6"));
                    map.put("moralMax", resultSet.getFloat("moralMax"));
                    map.put("moralSelf", resultSet.getFloat("moralSelf"));
                    map.put("moralMutual", resultSet.getFloat("moralMutual"));
                    itemMap.put("moral", map);

                    map = new HashMap<>();
                    map.put("GPA", resultSet.getFloat("GPA"));
                    map.put("witMax", resultSet.getFloat("witMax"));
                    map.put("witSelf", resultSet.getFloat("witSelf"));
                    map.put("witMutual", resultSet.getFloat("witMutual"));
                    itemMap.put("wit", map);

                    map = new HashMap<>();
                    map.put("level", resultSet.getInt("level"));
                    map.put("sportsMax", resultSet.getInt("sportsMax"));
                    map.put("sportsSelf", resultSet.getInt("sportsSelf"));
                    map.put("sportsMutual", resultSet.getInt("sportsMutual"));
                    itemMap.put("sports", map);

                    map = new HashMap<>();
                    base.put("practiceBasic1", resultSet.getString("practiceBasic1"));
                    base.put("practiceBasic2", resultSet.getString("practiceBasic2"));
                    base.put("practiceBasic3", resultSet.getString("practiceBasic3"));
                    map.put("practiceBasic", base);
                    map.put("practice1", resultSet.getFloat("practice1"));
                    map.put("practice2", resultSet.getFloat("practice2"));
                    map.put("practice3", resultSet.getFloat("practice3"));
                    map.put("practice4", resultSet.getFloat("practice4"));
                    map.put("practice5", resultSet.getFloat("practice5"));
                    map.put("practice6", resultSet.getFloat("practice6"));
                    map.put("practice7", resultSet.getFloat("practice7"));
                    map.put("practiceMax", resultSet.getFloat("practiceMax"));
                    map.put("practiceSelf", resultSet.getFloat("practiceSelf"));
                    map.put("practiceMutual", resultSet.getFloat("practiceMutual"));
                    itemMap.put("practice", map);

                    map = new HashMap<>();
                    base = new HashMap<>();
                    base.put("GenresBasic1", resultSet.getString("GenresBasic1"));
                    base.put("GenresBasic2", resultSet.getString("GenresBasic2"));
                    base.put("GenresBasic3", resultSet.getString("GenresBasic3"));
                    map.put("GenresBasic", base);
                    map.put("Genres1", resultSet.getFloat("Genres1"));
                    map.put("Genres2", resultSet.getFloat("Genres2"));
                    map.put("Genres3", resultSet.getFloat("Genres3"));
                    map.put("Genres4", resultSet.getFloat("Genres4"));
                    map.put("Genres5", resultSet.getFloat("Genres5"));
                    map.put("GenresMax", resultSet.getFloat("GenresMax"));
                    map.put("GenresSelf", resultSet.getFloat("GenresSelf"));
                    map.put("GenresMutual", resultSet.getFloat("GenresMutual"));
                    itemMap.put("Genres", map);

                    map = new HashMap<>();
                    base = new HashMap<>();
                    base.put("teamBasic1", resultSet.getString("teamBasic1"));
                    base.put("teamBasic2", resultSet.getString("teamBasic2"));
                    base.put("teamBasic3", resultSet.getString("teamBasic3"));
                    map.put("teamBasic", base);
                    map.put("team1", resultSet.getFloat("team1"));
                    map.put("team2", resultSet.getFloat("team2"));
                    map.put("team3", resultSet.getFloat("team3"));
                    map.put("team4", resultSet.getFloat("team4"));
                    map.put("team5", resultSet.getFloat("team5"));
                    map.put("team6", resultSet.getFloat("team6"));
                    map.put("team7", resultSet.getFloat("team7"));
                    map.put("teamMax", resultSet.getFloat("teamMax"));
                    map.put("teamSelf", resultSet.getFloat("teamSelf"));
                    map.put("teamMutual", resultSet.getFloat("teamMutual"));
                    itemMap.put("team", map);

                    map = new HashMap<>();
                    map.put("extraMax", resultSet.getFloat("extraMax"));
                    map.put("extraSelf", resultSet.getFloat("extraSelf"));
                    map.put("extraMutual", resultSet.getFloat("extraMutual"));
                    itemMap.put("extra", map);

                    itemMap.put("schoolYear", resultSet.getString("schoolYear"));
                    itemMap.put("stuId", resultSet.getString("stuId"));
                    itemMap.put("time", resultSet.getString("time"));

                    jsonObjects.add(itemMap);
                }
                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    hashMap.put("status", 101);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
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

    /**
     * 获取某个学年审核的所有学生学号
     *
     * @param userId     用户ID
     * @param schoolYear 学年
     * @return 200->成功 101->还没有审核记录 102->数据库IO错误
     */
    public static String getMutualIDs(String userId, String schoolYear) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            String sql = "select table_name from information_schema.tables where table_name = 'CheckedASCQ'";
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //互评表还不存在，也就是没有审核记录
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //互评表已经存在，查找对应的数据
                sql = "select stuId from CheckedASCQ where checkedStuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                tabCount = 0;

                while (resultSet.next()) {
                    tabCount++;
                    HashMap<String, Object> itemMap = new HashMap<>();
                    itemMap.put("stuId", resultSet.getString("stuId"));
                    jsonObjects.add(itemMap);
                }

                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //没有改用户的审核记录
                    hashMap.put("status", 101);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    //存在该用户的审核记录
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

    /**
     * 获取申请修改综测成绩学生的互评成绩
     *
     * @param userId     用户ID
     * @param schoolYear 学年
     * @return 200->成功 101->还没有审核记录 102->成绩已被学生本人确定 103->数据库IO错误 104->该学生未申请修改成绩
     */
    public static String getModifyASCQ(String userId, String schoolYear) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            String sql = "select table_name from information_schema.tables where table_name = 'CheckedASCQ'";
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //审核表还不存在，该条记录就不存在
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //审核表已经存在，查询相应的数据
                sql = "select * from CheckedASCQ where stuId = '" + userId + "' and schoolYear = '" + schoolYear + "'";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                tabCount = 0;

                while (resultSet.next()) {
                    tabCount++;
                    if (resultSet.getString("isConfirm").equals("true")) {
                        //该条记录已被学生本人确定，不可再修改
                        resultSet.close();
                        dbHelper.close();
                        hashMap.put("status", 102);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);
                    } else if (resultSet.getString("isModify").equals("true")) {
                        HashMap<String, Object> itemMap = new HashMap<>();
                        HashMap<String, Object> map = new HashMap<>();
                        HashMap<String, Object> base = new HashMap<>();
                        map.put("moral1", resultSet.getFloat("moral1"));
                        map.put("moral2", resultSet.getFloat("moral2"));
                        map.put("moral3", resultSet.getFloat("moral3"));
                        map.put("moral4", resultSet.getFloat("moral4"));
                        map.put("moral5", resultSet.getFloat("moral5"));
                        map.put("moral6", resultSet.getFloat("moral6"));
                        map.put("moralMax", resultSet.getFloat("moralMax"));
                        map.put("moralSelf", resultSet.getFloat("moralSelf"));
                        map.put("moralMutual", resultSet.getFloat("moralMutual"));
                        itemMap.put("moral", map);

                        map = new HashMap<>();
                        map.put("GPA", resultSet.getFloat("GPA"));
                        map.put("witMax", resultSet.getFloat("witMax"));
                        map.put("witSelf", resultSet.getFloat("witSelf"));
                        map.put("witMutual", resultSet.getFloat("witMutual"));
                        itemMap.put("wit", map);

                        map = new HashMap<>();
                        map.put("level", resultSet.getInt("level"));
                        map.put("sportsMax", resultSet.getInt("sportsMax"));
                        map.put("sportsSelf", resultSet.getInt("sportsSelf"));
                        map.put("sportsMutual", resultSet.getInt("sportsMutual"));
                        itemMap.put("sports", map);

                        map = new HashMap<>();
                        base.put("practiceBasic1", resultSet.getString("practiceBasic1"));
                        base.put("practiceBasic2", resultSet.getString("practiceBasic2"));
                        base.put("practiceBasic3", resultSet.getString("practiceBasic3"));
                        map.put("practiceBasic", base);
                        map.put("practice1", resultSet.getFloat("practice1"));
                        map.put("practice2", resultSet.getFloat("practice2"));
                        map.put("practice3", resultSet.getFloat("practice3"));
                        map.put("practice4", resultSet.getFloat("practice4"));
                        map.put("practice5", resultSet.getFloat("practice5"));
                        map.put("practice6", resultSet.getFloat("practice6"));
                        map.put("practice7", resultSet.getFloat("practice7"));
                        map.put("practiceMax", resultSet.getFloat("practiceMax"));
                        map.put("practiceSelf", resultSet.getFloat("practiceSelf"));
                        map.put("practiceMutual", resultSet.getFloat("practiceMutual"));
                        itemMap.put("practice", map);

                        map = new HashMap<>();
                        base = new HashMap<>();
                        base.put("GenresBasic1", resultSet.getString("GenresBasic1"));
                        base.put("GenresBasic2", resultSet.getString("GenresBasic2"));
                        base.put("GenresBasic3", resultSet.getString("GenresBasic3"));
                        map.put("GenresBasic", base);
                        map.put("Genres1", resultSet.getFloat("Genres1"));
                        map.put("Genres2", resultSet.getFloat("Genres2"));
                        map.put("Genres3", resultSet.getFloat("Genres3"));
                        map.put("Genres4", resultSet.getFloat("Genres4"));
                        map.put("Genres5", resultSet.getFloat("Genres5"));
                        map.put("GenresMax", resultSet.getFloat("GenresMax"));
                        map.put("GenresSelf", resultSet.getFloat("GenresSelf"));
                        map.put("GenresMutual", resultSet.getFloat("GenresMutual"));
                        itemMap.put("Genres", map);

                        map = new HashMap<>();
                        base = new HashMap<>();
                        base.put("teamBasic1", resultSet.getString("teamBasic1"));
                        base.put("teamBasic2", resultSet.getString("teamBasic2"));
                        base.put("teamBasic3", resultSet.getString("teamBasic3"));
                        map.put("teamBasic", base);
                        map.put("team1", resultSet.getFloat("team1"));
                        map.put("team2", resultSet.getFloat("team2"));
                        map.put("team3", resultSet.getFloat("team3"));
                        map.put("team4", resultSet.getFloat("team4"));
                        map.put("team5", resultSet.getFloat("team5"));
                        map.put("team6", resultSet.getFloat("team6"));
                        map.put("team7", resultSet.getFloat("team7"));
                        map.put("teamMax", resultSet.getFloat("teamMax"));
                        map.put("teamSelf", resultSet.getFloat("teamSelf"));
                        map.put("teamMutual", resultSet.getFloat("teamMutual"));
                        itemMap.put("team", map);

                        map = new HashMap<>();
                        map.put("extraMax", resultSet.getFloat("extraMax"));
                        map.put("extraSelf", resultSet.getFloat("extraSelf"));
                        map.put("extraMutual", resultSet.getFloat("extraMutual"));
                        itemMap.put("extra", map);

                        itemMap.put("schoolYear", resultSet.getString("schoolYear"));
                        itemMap.put("stuId", resultSet.getString("stuId"));
                        itemMap.put("time", resultSet.getString("time"));
                        itemMap.put("mutualCode", resultSet.getString("mutualCode"));

                        jsonObjects.add(itemMap);
                    } else if (resultSet.getString("isModify").equals("false")) {
                        //该学生未申请修改成绩
                        resultSet.close();
                        dbHelper.close();

                        hashMap.put("status", 104);
                        hashMap.put("time", TimeUtils.getCurrentTime());
                        return JSON.toJSONString(hashMap);
                    }
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
            hashMap.put("status", 103);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }

    /**
     * 更新某个学年某个学生的综测成绩
     *
     * @param ASCQBean UpdateASCQBean
     * @return 200->成功 101->还没有审核记录 102->该条记录不存在数据库中 103->数据库IO错误
     */
    public static String updateASCQ(UpdateASCQBean ASCQBean) {
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            String sql = "select table_name from information_schema.tables where table_name = 'CheckedASCQ'";
            DBHelper dbHelper = new DBHelper(sql);

            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;

            while (resultSet.next()) {
                tabCount++;
            }

            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //审核表还不存在
                hashMap.put("status", 101);
                hashMap.put("time", TimeUtils.getCurrentTime());
                return JSON.toJSONString(hashMap);
            } else {
                //审核表已经存在,查看该条记录是不是存在
                sql = "select stuId from CheckedASCQ where (stuId,schoolYear) in (('" + ASCQBean.getData().get(0).getStuId() + "','"
                        + ASCQBean.getData().get(0).getSchoolYear() + "'))";
                dbHelper = new DBHelper(sql);
                resultSet = dbHelper.pst.executeQuery();
                tabCount = 0;

                while (resultSet.next()) {
                    tabCount++;
                }

                resultSet.close();
                dbHelper.close();

                if (tabCount == 0) {
                    //该条记录不存在数据库中
                    hashMap.put("status", 102);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                } else {
                    //获取该学年第二台账的前10%，判断当前用户的互评得分是不是要加上0.5分
                    sql = "select ceil(count(stuId) / 5) as totalStudent from students where (stuGrade,stuMajor,stuClass) " +
                            "in ((select stuGrade,stuMajor,stuClass from students where stuId = '" + ASCQBean.getData().get(0).getStuId() + "'))";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    int totalStudent = 5;
                    while (resultSet.next()) {
                        totalStudent = resultSet.getInt("totalStudent");
                    }
                    resultSet.close();
                    dbHelper.close();

                    sql = "select @rownum:=@rownum+1 as rownum,if(@total=total,@rank,@rank:=@rownum) as rank,@total:=total,stuId,A.* " +
                            "from(select stuId," +
                            "sum(count) as total " +
                            "from secondledger " +
                            "where (stuId) in " +
                            "((select stuId from students " +
                            "where (stuGrade,stuMajor,stuClass) in " +
                            "((select stuGrade,stuMajor,stuClass from students where stuId = '"
                            + ASCQBean.getData().get(0).getStuId() + "')))) " +
                            "group by stuId order by total desc)A,(select @rank:=0,@rownum:=0,@total:=null)B;";

                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();

                    int rank = 0;
                    int rankAscq = 1;

                    while (resultSet.next()) {
                        if (resultSet.getInt("rownum") == totalStudent) {
                            rank = resultSet.getInt("rank");
                        }

                        if (resultSet.getString("stuId").equals(ASCQBean.getData().get(0).getStuId())) {
                            rankAscq = resultSet.getInt("rank");
                        }
                    }
                    resultSet.close();
                    dbHelper.close();

                    if (rankAscq <= rank) {
                        ASCQBean.getData().get(0).getGenres().setGenresMutual(ASCQBean.getData().get(0).getGenres().getGenresMutual() + 0.5f);
                    }
                    //该条记录存在数据中，更新数据
                    sql = "replace into CheckedASCQ(stuId,checkedStuId,schoolYear,time,moral1,moral2,moral3,moral4,moral5,moral6," +
                            "moralSelf,moralMutual,GPA,witSelf,witMutual,level,sportsSelf,sportsMutual," +
                            "practiceBasic1,practiceBasic2,practiceBasic3,practice1,practice2,practice3,practice4,practice5," +
                            "practice6,practice7,practiceSelf,practiceMutual,GenresBasic1,GenresBasic2,GenresBasic3," +
                            "Genres1,Genres2,Genres3,Genres4,Genres5,GenresSelf,GenresMutual,teamBasic1,teamBasic2," +
                            "teamBasic3,team1,team2,team3,team4,team5,team6,team7,teamSelf,teamMutual,extraSelf,extraMutual," +
                            "isConfirm,isModify,checkedTime,mutualCode)" +
                            " values ('" + ASCQBean.getData().get(0).getStuId() + "'," +
                            "'" + ASCQBean.getData().get(0).getCheckedStuId() + "'," +
                            "'" + ASCQBean.getData().get(0).getSchoolYear() + "'," +
                            "'" + ASCQBean.getData().get(0).getTime() + "'," +
                            "'" + ASCQBean.getData().get(0).getMoral().getMoral1() + "'," +
                            "'" + ASCQBean.getData().get(0).getMoral().getMoral2() + "'," +
                            "'" + ASCQBean.getData().get(0).getMoral().getMoral3() + "'," +
                            "'" + ASCQBean.getData().get(0).getMoral().getMoral4() + "'," +
                            "'" + ASCQBean.getData().get(0).getMoral().getMoral5() + "'," +
                            "'" + ASCQBean.getData().get(0).getMoral().getMoral6() + "'," +
                            "'" + ASCQBean.getData().get(0).getMoral().getMoralSelf() + "'," +
                            "'" + ASCQBean.getData().get(0).getMoral().getMoralMutual() + "'," +
                            "'" + ASCQBean.getData().get(0).getWit().getGPA() + "'," +
                            "'" + ASCQBean.getData().get(0).getWit().getWitSelf() + "'," +
                            "'" + ASCQBean.getData().get(0).getWit().getWitMutual() + "'," +
                            "'" + ASCQBean.getData().get(0).getSports().getLevel() + "'," +
                            "'" + ASCQBean.getData().get(0).getSports().getSportsSelf() + "'," +
                            "'" + ASCQBean.getData().get(0).getSports().getSportsMutual() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPracticeBasic().getPracticeBasic1() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPracticeBasic().getPracticeBasic2() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPracticeBasic().getPracticeBasic3() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPractice1() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPractice2() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPractice3() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPractice4() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPractice5() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPractice6() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPractice7() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPracticeSelf() + "'," +
                            "'" + ASCQBean.getData().get(0).getPractice().getPracticeMutual() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenresBasic().getGenresBasic1() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenresBasic().getGenresBasic2() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenresBasic().getGenresBasic3() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenres1() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenres2() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenres3() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenres4() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenres5() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenresSelf() + "'," +
                            "'" + ASCQBean.getData().get(0).getGenres().getGenresMutual() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeamBasic().getTeamBasic1() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeamBasic().getTeamBasic2() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeamBasic().getTeamBasic3() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeam1() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeam2() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeam3() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeam4() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeam5() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeam6() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeam7() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeamSelf() + "'," +
                            "'" + ASCQBean.getData().get(0).getTeam().getTeamMutual() + "'," +
                            "'" + ASCQBean.getData().get(0).getExtra().getExtraSelf() + "'," +
                            "'" + ASCQBean.getData().get(0).getExtra().getExtraMutual() + "'," +
                            "'true','true'," +
                            "'" + TimeUtils.getCurrentTime() + "'," +
                            "'" + ASCQBean.getData().get(0).getMutualCode() + "')";

                    dbHelper = new DBHelper(sql);
                    dbHelper.pst.executeUpdate();
                    dbHelper.close();

                    hashMap.put("status", 200);
                    hashMap.put("time", TimeUtils.getCurrentTime());
                    return JSON.toJSONString(hashMap);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 103);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }
}















