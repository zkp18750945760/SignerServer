package action.android.bean;

import java.util.HashMap;

public class UserBean {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 职务 班长 学生...
     */
    private String userDuty;
    /**
     * 年级 2014级 2015级...
     */
    private String userGrade;
    /**
     * 专业 软件工程...
     */
    private String userMajor;
    /**
     * 班级 1班 2班...
     */
    private String userClass;
    /**
     * 密码 AES加密之后的密码
     */
    private String userPassword;
    /**
     * 设备号
     */
    private String userSerialNo;
    /**
     * 上一次绑定或解绑时间
     */
    private String userUnBindTime;

    public UserBean(String userId, String userName, String userDuty, String userGrade, String userMajor, String userClass, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userDuty = userDuty;
        this.userGrade = userGrade;
        this.userMajor = userMajor;
        this.userClass = userClass;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDuty() {
        return userDuty;
    }

    public void setUserDuty(String userDuty) {
        this.userDuty = userDuty;
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

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserSerialNo() {
        return userSerialNo;
    }

    public void setUserSerialNo(String userSerialNo) {
        this.userSerialNo = userSerialNo;
    }

    public String getUserUnBindTime() {
        return userUnBindTime;
    }

    public void setUserUnBindTime(String userUnBindTime) {
        this.userUnBindTime = userUnBindTime;
    }

    public HashMap getUser() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userName", userName);
        map.put("userDuty", userDuty);
        map.put("userGrade", userGrade);
        map.put("userMajor", userMajor);
        map.put("userClass", userClass);
        map.put("userPassword", userPassword);
        map.put("userSerialNo", userSerialNo);
        map.put("userUnBindTime", userUnBindTime);
        return map;
    }
}
