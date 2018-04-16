package utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * 学年处理工具
 */
public class SchoolYearUtils {

    /**
     * 根据年份和年级获取当前该学生的所有可查学年
     *
     * @param gradeCode 年级代码
     * @param year      年份
     * @return {"schoolYear4":"2017-2018学年","schoolYear3":"2016-2017学年","schoolYear2":"2015-2016学年","schoolYear1":"2014-2015学年"}
     */
    public static HashMap getSchoolYears(String gradeCode, String year) {
        HashMap<String, Object> hashMap = new HashMap<>();
        int grade = Integer.parseInt(gradeCode.substring(2));
        int count = 1;
        while (hashMap.size() < 4 && grade < Integer.parseInt(year.substring(2))) {
            hashMap.put("schoolYear" + count++, "20" + grade + "-" + "20" + (grade + 1) + "学年");
            grade++;
        }
        return hashMap;
    }

    /**
     * 根据年级和学期代码判断是哪个学年的那个学期
     *
     * @param gradeCode 年级代码
     * @param termCode  学期代码
     * @return {"schoolYear":"2017-2018学年","term":"1"}
     */
    public static HashMap getSchoolYear(String gradeCode, String termCode) {
        HashMap<String, Object> hashMap = new HashMap<>();
        int term = Integer.parseInt(termCode);
        SchoolYearBean schoolYearBean = JSON.parseObject(
                JSON.toJSONString(getSchoolYears(gradeCode, TimeUtils.getCurrentYear())),
                SchoolYearBean.class);
        switch (term % 2 == 0 ? term / 2 - 1 : term / 2) {
            case 0:
                hashMap.put("schoolYear", schoolYearBean.getSchoolYear1());
                break;
            case 1:
                hashMap.put("schoolYear", schoolYearBean.getSchoolYear2());
                break;
            case 2:
                hashMap.put("schoolYear", schoolYearBean.getSchoolYear3());
                break;
            case 3:
                hashMap.put("schoolYear", schoolYearBean.getSchoolYear4());
                break;
        }
        hashMap.put("term", term % 2 == 0 ? "2" : "1");
        return hashMap;
    }

    /**
     * 根据年级代码，学年，学期获取学期代码
     *
     * @param gradeCode  年级代码
     * @param schoolYear 学年
     * @param term       学期
     * @return
     */
    public static HashMap getTermCode(String gradeCode, String schoolYear, String term) {
        HashMap<String, Object> hashMap = new HashMap<>();
        int tremInt = Integer.parseInt(term);
        if (tremInt > 2) {
            hashMap.put("status", 101);
            return hashMap;
        }
        SchoolYearBean schoolYearBean =
                JSON.parseObject(
                        JSON.toJSONString(getSchoolYears(gradeCode, TimeUtils.getCurrentYear())),
                        SchoolYearBean.class);
        int schoolYearIndex = 0;
        if (schoolYear.equals(schoolYearBean.getSchoolYear1())) {
            schoolYearIndex = 1;
        } else if (schoolYear.equals(schoolYearBean.getSchoolYear2())) {
            schoolYearIndex = 2;
        } else if (schoolYear.equals(schoolYearBean.getSchoolYear3())) {
            schoolYearIndex = 3;
        } else if (schoolYear.equals(schoolYearBean.getSchoolYear4())) {
            schoolYearIndex = 4;
        }
        hashMap.put("status", 200);
        hashMap.put("termCode", String.valueOf((schoolYearIndex - 1) * 2 + tremInt));
        return hashMap;
    }

    /**
     * 根据用户ID,年份，月份获取学期代码
     *
     * @param userId 用户ID
     * @param year   年份
     * @param month  月份
     * @return 学期代码
     */
    public static String getTermCodeByMonth(String userId, int year, int month) {
        int entranceYear = 2000 + Integer.parseInt(userId.substring(0, 2));
        int termCode;
        int pastYear = (year - 1) - entranceYear;
        if (!(month > 2 && month < 9)) {
            pastYear++;
        }
        if (pastYear < 4) {
            if (month > 2 && month < 9) {
                termCode = 2 + pastYear * 2;
            } else {
                termCode = pastYear * 2;
            }
            return termCode + "";
        } else {
            return "0";
        }
    }

    /**
     * 根据年级获取年级代码
     *
     * @param grade 年级
     * @return 2014
     */
    public static String getGradeCode(String grade) {
        return grade.substring(0, 4);
    }

    /**
     * 根据专业获取专业代码
     *
     * @param major 专业
     * @return 101
     */
    public static String getMajorCode(String major) {
        String result = "";
        switch (major) {
            case "计算机科学与艺术":
                result = "101";
                break;
            case "软件工程":
                result = "102";
                break;
            case "网络工程":
                result = "103";
                break;
            case "数字媒体技术":
                result = "104";
                break;
        }
        return result;
    }

    /**
     * 根据班级获取班级代码
     *
     * @param className 班级
     * @return 班级代码
     */
    public static String getClassCode(String className) {
        return className.substring(0, className.length() - 1);
    }

    /**
     * 根据上课节数获取上课开始时间
     *
     * @param courseTime 上课节数
     * @return 08:00
     */
    public static String getClassBeginTime(String courseTime) {
        int courseTimeInt = Integer.parseInt(StringUtils.getSubString(courseTime, "(.*?)-"));
        String result = "";
        switch (courseTimeInt) {
            case 1:
                result = "08:00:00";
                break;
            case 3:
                result = "10:10:00";
                break;
            case 7:
                result = "14:30:00";
                break;
            case 9:
                result = "16:40:00";
                break;
            case 11:
                result = "19:10:00";
                break;
        }
        return result;
    }
}
