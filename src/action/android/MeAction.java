package action.android;

import action.android.bean.ASCQBean;
import action.android.bean.UpdateASCQBean;
import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class MeAction extends ActionSupport {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 被邀请进入互评小组的ID
     */
    private String mutualId;

    /**
     * 用户年级
     */
    private String userGrade;
    /**
     * 用户专业
     */
    private String userMajor;
    /**
     * 用户班级
     */
    private String userClazz;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * 头像文件
     */
    private File headIcon;
    /**
     * 头像文件名
     */
    private String headIconName;
    /**
     * 设备序列号
     */
    private String stuSerialNo;
    /**
     * 文体活动名称
     */
    private String actName;
    /**
     * 文体活动时间
     */
    private String actTime;
    /**
     * 志愿活动名称
     */
    private String volName;
    /**
     * 志愿活动时间
     */
    private String volTime;

    /**
     * 发起签到类型 1->课堂签到  2->会议签到
     */
    private int type;
    /**
     * 事项开始时间
     */
    private String time;
    /**
     * 主题
     */
    private String content;

    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 定位精度
     */
    private float radius;

    /**
     * 月份
     */
    private int month;
    /**
     * 年份
     */
    private int year;

    /**
     * 支书会议主题
     */
    private String theme;

    /**
     * 支书会议日期
     */
    private String date;

    /**
     * 综测成绩json
     */
    private String ASCQ;

    /**
     * 学年 2017-2018
     */
    private String schoolYear;

    /**
     * 学期
     */
    private String term;
    /**
     * 摘要
     */
    private String Abstract;

    private File uploadFile; //上传的文件
    private String uploadFileContentType; //文件类型
    private String uploadFileFileName; //文件名字

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMutualId() {
        return mutualId;
    }

    public void setMutualId(String mutualId) {
        this.mutualId = mutualId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public File getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(File headIcon) {
        this.headIcon = headIcon;
    }

    public String getHeadIconName() {
        return headIconName;
    }

    public void setHeadIconName(String headIconName) {
        this.headIconName = headIconName;
    }

    public String getStuSerialNo() {
        return stuSerialNo;
    }

    public void setStuSerialNo(String stuSerialNo) {
        this.stuSerialNo = stuSerialNo;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    public String getVolName() {
        return volName;
    }

    public void setVolName(String volName) {
        this.volName = volName;
    }

    public String getVolTime() {
        return volTime;
    }

    public void setVolTime(String volTime) {
        this.volTime = volTime;
    }

    public String getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(String userGrade) {
        this.userGrade = userGrade;
    }

    public String getUserMajor() {
        return userMajor;
    }

    public void setUserMajor(String userMajor) {
        this.userMajor = userMajor;
    }

    public String getUserClazz() {
        return userClazz;
    }

    public void setUserClazz(String userClazz) {
        this.userClazz = userClazz;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getASCQ() {
        return ASCQ;
    }

    public void setASCQ(String ASCQ) {
        this.ASCQ = ASCQ;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    /**
     * 登录
     */
    public void login() {
        System.out.println(userId + "," + userPassword);
        String login = MeDB.login(userId, userPassword, stuSerialNo);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传头像
     */
    public void uploadHeadIcon() {
        System.out.println(userId + "头像" + headIconName);
        String uploadHeadIcon = MeDB.uploadHeadIcon(userId, headIcon, headIconName);
        System.out.println(uploadHeadIcon);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(uploadHeadIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取头像
     */
    public void getHeadIcons() {
        System.out.println(userId + "请求获取头像");
        String headIcon = MeDB.getHeadIcon(userId);
        System.out.println(headIcon);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(headIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解绑设备
     */
    public void unBindDevice() {
        System.out.println(userId + "请求解绑设备");
        String unBindDevice = MeDB.unBindDevice(userId);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(unBindDevice);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 绑定设备
     */
    public void bindDevice() {
        System.out.println(userId + "请求绑定设备" + stuSerialNo);
        String bindDevice = MeDB.bindDevice(userId, stuSerialNo);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(bindDevice);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改密码
     */
    public void modifyPassword() {
        System.out.println(userId + "," + userPassword + "," + newPassword);
        String modifyPassword = MeDB.modifyPassword(userId, userPassword, newPassword);
        System.out.println(modifyPassword);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(modifyPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文体活动信息
     */
    public void getActivities() {
        System.out.println(userId + "请求所有文体活动信息");
        String activities = MeDB.getActivities(userId);
        System.out.println(activities);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(activities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 报名文体活动
     */
    public void applyActivities() {
        System.out.println(userId + "请求报名文体活动：" + actName + actTime);
        String applyActivities = MeDB.applyActivities(userId, actName, actTime);
        System.out.println(applyActivities);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(applyActivities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消报名文体活动
     */
    public void cancelApplyActivities() {
        System.out.println(userId + "请求取消报名文体活动：" + actName + actTime);
        String cancelApplyActivities = MeDB.cancelApplyActivities(userId, actName, actTime);
        System.out.println(cancelApplyActivities);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(cancelApplyActivities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取志愿活动信息
     */
    public void getVolunteers() {
        System.out.println(userId + "请求所有志愿活动信息");
        String volunteers = MeDB.getVolunteers(userId);
        System.out.println(volunteers);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(volunteers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 报名志愿活动
     */
    public void applyVolunteers() {
        System.out.println(userId + "请求报名志愿活动：" + volName + volTime);
        String applyVolunteers = MeDB.applyVolunteers(userId, volName, volTime);
        System.out.println(applyVolunteers);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(applyVolunteers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消报名志愿活动
     */
    public void cancelApplyVolunteers() {
        System.out.println(userId + "请求取消报名志愿活动：" + volName + volTime);
        String cancelApplyVolunteers = MeDB.cancelApplyVolunteers(userId, volName, volTime);
        System.out.println(cancelApplyVolunteers);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(cancelApplyVolunteers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取今天所有日程
     */
    public void getAllSchedule() {
        System.out.println(userId + "请求今天所有日程");
        String allSchedule = MeDB.getAllSchedule(userId);
        System.out.println(allSchedule);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(allSchedule);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有banner图片信息
     */
    public void getBanners() {
        System.out.println("获取所有banner图片信息");
        String banners = MeDB.getBanners();
        System.out.println(banners);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(banners);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取签到事项
     */
    public void getSignEvents() {
        System.out.println(userId + "请求所有签到事项");
        String signEvents = MeDB.getSignEvents(userId);
        System.out.println(signEvents);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(signEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取事项的发起签到状态
     */
    public void getEventsSponsorSignStatus() {
        System.out.println(userId + "请求获取" + content + "的发起签到状态");
        String eventsSponsorSignStatus = MeDB.getEventsSponsorSignStatus(content);
        System.out.println(eventsSponsorSignStatus);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(eventsSponsorSignStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发起签到
     */
    public void sponsorSign() {
        System.out.println(userId + ","
                + userGrade + ","
                + userMajor + ","
                + userClazz + "发起"
                + content + ","
                + time + ","
                + type + "签到,经度："
                + longitude + "，纬度："
                + latitude + ",精度："
                + radius);

        String sponsorSign = MeDB.sponsorSign(userId, userGrade, userMajor, userClazz,
                type, time, content, longitude, latitude, radius);

        System.out.println(sponsorSign);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(sponsorSign);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 签到
     */
    public void sign() {
        System.out.println(userId + ","
                + userGrade + ","
                + userMajor + ","
                + userClazz + "请求"
                + content + ","
                + time + ","
                + type + "签到,经度："
                + longitude + "，纬度："
                + latitude + ",精度："
                + radius);
        String sign = MeDB.sign(userId, userGrade, userMajor, userClazz,
                type, time, content, longitude, latitude, radius, stuSerialNo);

        System.out.println(sign);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(sign);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某个事项已签到人的头像连接
     */
    public void getSignedHeadIcons() {
        System.out.println(userId + ","
                + userGrade + ","
                + userMajor + ","
                + userClazz + "请求"
                + content + ","
                + time + ","
                + type + "已签到学生的头像");

        String signedHeadIcons = MeDB.getSignedHeadIcons(userId, userGrade, userMajor, userClazz, type, time, content);

        System.out.println(signedHeadIcons);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(signedHeadIcons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 上传台账
     */
    public void uploadLedger() {
        System.out.println(userId + "上传" + month + "月第二台账");
        String uploadLedger = UploadFile.uploadLedger(month, uploadFile, uploadFileFileName);
        System.out.println("uploadLedger==" + uploadLedger);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(uploadLedger);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某年的所有第二台账数据
     */
    public void getSecondLedger() {
        System.out.println(userId + "请求" + year + "年的所有第二台账");
        String secondLedger = UploadFile.getSecondLedger(userId, year);
        System.out.println(secondLedger);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(secondLedger);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取版本更新信息
     */
    public void getUpdateInfo() {
        System.out.println("请求版本更新信息");
        String updateInfo = UploadFile.getUpdateInfo();

        System.out.println(updateInfo);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(updateInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传Android端错误日志
     */
    public void uploadCrashLogcat() {
        System.out.println("上传错误日志");
        String uploadCrashLogcat = UploadFile.uploadCrashLogcat(uploadFile, uploadFileFileName);

        System.out.println(uploadCrashLogcat);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(uploadCrashLogcat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取课程表
     */
    public void getCourse() {
        System.out.println(userId + "请求获取课表");
        String course = UploadFile.getCourse(userId, userGrade, userMajor, userClazz);
        System.out.println(course);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(course);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取签到记录
     */
    public void getSignRecord() {
        System.out.println(userId + "请求获取签到记录");
        String signRecord = UploadFile.getSignRecord(userId, userGrade, userMajor, userClazz);

        System.out.println(signRecord);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(signRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取第一台账
     */
    public void getFirstLedger() {
        System.out.println(userId + "请求第一台账");
        String firstLedger = UploadFile.getFirstLedger(userId, userGrade, userMajor, userClazz, schoolYear, term);

        System.out.println(firstLedger);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(firstLedger);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传班长支书会议记录
     */
    public void uploadDiscussion() {
        System.out.println(userId + "请求上传会议记录");
        String uploadDiscussion = UploadFile.uploadDiscussion(uploadFile, uploadFileFileName, userId, schoolYear, term, content,
                Abstract, userGrade, userMajor, userClazz);

        System.out.println(uploadDiscussion);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(uploadDiscussion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取支书会议记录
     */
    public void getDiscussion() {
        System.out.println(userId + "请求获取支书会议记录");
        String discussion = UploadFile.getDiscussion(userId, userGrade, userMajor, userClazz, schoolYear, term);

        System.out.println(discussion);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(discussion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 标记支书会议已读
     */
    public void updateDiscussionRead() {
        System.out.println(userId + "标记" + theme + "," + date + "已读");
        String updateDiscussionRead = UploadFile.updateDiscussionRead(userId, theme, date);

        System.out.println(updateDiscussionRead);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(updateDiscussionRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传综测成绩
     */
    public void uploadASQC() {
        System.out.println(ASCQ);
        String uploadASQC = UploadFile.uploadASQC(JSON.parseObject(ASCQ, ASCQBean.class), userId, userGrade, userMajor, userClazz);
        System.out.println(uploadASQC);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(uploadASQC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本班级所有学生的姓名和学号
     */
    public void getClazzStudents() {
        System.out.println(userId + "获取班级学生信息");
        String clazzStudents = action.android.ASCQ.getClazzStudents(userId, userGrade, userMajor, userClazz);
        System.out.println(clazzStudents);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(clazzStudents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 邀请某个用户进入互评小组
     */
    public void applyMutual() {
        System.out.println(userId + "邀请" + mutualId + "进入测评小组");
        String applyMutual = action.android.ASCQ.applyMutual(userId, userGrade, userMajor, userClazz, mutualId);
        System.out.println(applyMutual);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(applyMutual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消邀请某个用户进入互评小组
     */
    public void cancelMutual() {
        System.out.println(userId + "取消邀请" + mutualId + "进入测评小组");
        String cancelMutual = action.android.ASCQ.cancelMutual(userId, userGrade, userMajor, userClazz, mutualId);
        System.out.println(cancelMutual);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(cancelMutual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取该班级测评小组人数
     */
    public void getMutualNum() {
        System.out.println(userId + "获取班级测评小组人数");
        String mutualNum = action.android.ASCQ.getMutualNum(userId, userGrade, userMajor, userClazz);
        System.out.println(mutualNum);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(mutualNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机分配综测表给互评小组
     */
    public void groupMutual() {
        System.out.println(userId + "请求分配综测表给测评小组");
        String groupMutual = action.android.ASCQ.groupMutual(userId, userGrade, userMajor, userClazz);
        System.out.println(groupMutual);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(groupMutual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取综测表分配状态
     */
    public void getGroupMutualStatus() {
        System.out.println(userId + "请求获取综测表分配状态");
        String groupMutualStatus = action.android.ASCQ.getGroupMutualStatus(userId, userGrade, userMajor, userClazz);

        System.out.println(groupMutualStatus);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(groupMutualStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断某个用户是不是互评小组成员
     */
    public void isMutualMembers() {
        System.out.println(userId + "请求判断自己是不是互评小组成员");

        String mutualMembers = action.android.ASCQ.isMutualMembers(userId, userGrade, userMajor, userClazz);
        System.out.println(mutualMembers);


        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(mutualMembers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是不是分配的任务都完成了
     */
    public void isMutualFinish() {
        System.out.println(userId + "请求判断是不是分配的任务都完成了");
        String mutualFinish = action.android.ASCQ.isMutualFinish(userId, userGrade, userMajor, userClazz);

        System.out.println(mutualFinish);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(mutualFinish);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取未完成的任务
     */
    public void getMutualTask() {
        System.out.println(userId + "请求获取未完成的任务");
        String mutualTask = action.android.ASCQ.getMutualTask(userId, userGrade, userMajor, userClazz);

        System.out.println(mutualTask);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(mutualTask);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 审核综测成绩
     */
    public void uploadMutual() {
        System.out.println(userId + "审核综测成绩");
        String uploadMutual = action.android.ASCQ.uploadMutual(JSON.parseObject(ASCQ, ASCQBean.class), userId, userGrade, userMajor, userClazz);
        System.out.println(uploadMutual);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(uploadMutual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取综测成绩
     */
    public void getASCQScore() {
        System.out.println(userId + "请求获取" + schoolYear + "学年的综测成绩");
        String ascq = action.android.ASCQ.getASCQ(userId, schoolYear, userGrade, userMajor, userClazz);

        System.out.println(ascq);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(ascq);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 确认综测成绩
     */
    public void confirmASCQ() {
        System.out.println(userId + "请求确认" + schoolYear + "学年的综测成绩");
        String confirmASCQ = action.android.ASCQ.confirmASCQ(userId, schoolYear);
        System.out.println(confirmASCQ);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(confirmASCQ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求修改综测成绩
     */
    public void modifyASCQ() {
        System.out.println(userId + "请求修改" + schoolYear + "学年的综测成绩");
        String modifyASCQ = action.android.ASCQ.modifyASCQ(userId, schoolYear);
        System.out.println(modifyASCQ);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(modifyASCQ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某个学年的互评记录
     */
    public void getMutualRecord() {
        System.out.println(userId + "请求获取" + schoolYear + "学年的审核记录");
        String mutualRecord = action.android.ASCQ.getMutualRecord(userId, schoolYear);

        System.out.println(mutualRecord);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(mutualRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某个学年审核的所有学生ID
     */
    public void getMutualIDs() {
        System.out.println(userId + "请求获取" + schoolYear + "学年的审核的所有学生ID");
        String mutualIDs = action.android.ASCQ.getMutualIDs(userId, schoolYear);

        System.out.println(mutualIDs);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(mutualIDs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某个学生某个学年需要修改的成绩
     */
    public void getModifyASCQ() {
        System.out.println(userId + "请求获取" + schoolYear + "学年的成绩需要修改");
        String modifyASCQ = action.android.ASCQ.getModifyASCQ(userId, schoolYear);

        System.out.println(modifyASCQ);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(modifyASCQ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改学生的综测成绩
     */
    public void updateASCQ() {
        System.out.println(ASCQ);
        String updateASCQ = action.android.ASCQ.updateASCQ(JSON.parseObject(ASCQ, UpdateASCQBean.class));

        System.out.println(updateASCQ);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(updateASCQ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
