package action.js;

import action.js.bean.ParticipatorBean;
import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import db.DBHelper;
import utils.FileUtils;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 网站Action
 */
public class UploadMeetingAction extends ActionSupport {

    /**
     * 上传的文件
     */
    private File uploadFile;
    /**
     * 文件类型
     */
    private String uploadFileContentType;
    /**
     * 文件名字
     */
    private String uploadFileFileName;


    /**
     * 会议名称
     */
    private String meetName;
    /**
     * 会议时间
     */
    private String meetTime;
    /**
     * 会议地点
     */
    private String meetLocation;
    /**
     * 发起者ID
     */
    private String sponsorID;
    /**
     * 参与者班级 例如：2014级软件工程2班
     */
    private String participator;
    /**
     * 通知类型  通知-->1/文件-->2
     */
    private String messageType;
    /**
     * 通知内容
     */
    private String messageContent;

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

    public String getMeetName() {
        return meetName;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public String getMeetTime() {
        return meetTime;
    }

    public void setMeetTime(String meetTime) {
        this.meetTime = meetTime;
    }

    public String getMeetLocation() {
        return meetLocation;
    }

    public void setMeetLocation(String meetLocation) {
        this.meetLocation = meetLocation;
    }

    public String getSponsorID() {
        return sponsorID;
    }

    public void setSponsorID(String sponsorID) {
        this.sponsorID = sponsorID;
    }

    public String getParticipator() {
        return participator;
    }

    public void setParticipator(String participator) {
        this.participator = participator;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * 将学生信息表录入数据库中
     */
    private void insertDataToDB() {
        try {
            String sql = "select table_name from information_schema.tables where table_name = 'meetings'";
            DBHelper dbHelper = new DBHelper(sql);
            ResultSet resultSet = dbHelper.pst.executeQuery();
            int tabCount = 0;
            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //数据库中还没有会议表，新建会议表
                sql = "create table meetings(metName varchar(40) not null," +
                        "metTime char(19) not null," +
                        "metLocation varchar(40)," +
                        "metSponsorID char(11) not null," +
                        "metType varchar(2) not null," +
                        "metContent varchar(300)," +
                        "metFilePath varchar(68)," +
                        "primary key(metName,metTime))";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }
            //数据库中存在会议表，直接插入数据
            String realPath;
            sql = "select * from meetings where (metName,metTime) in (('" + meetName + "','" + meetTime + "'))";
            tabCount = 0;
            dbHelper = new DBHelper(sql);
            resultSet = dbHelper.pst.executeQuery();
            while (resultSet.next()) {
                tabCount++;
            }
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                if (messageContent == null || messageContent.equals("")) {
                    messageType = "2";
                    realPath = "/files/" + uploadFileFileName;
                    System.out.println(realPath);
                    sql = "insert into meetings(metName,metTime,metLocation,metSponsorID,metType,metFilePath) " +
                            "values('" + meetName + "'," +
                            "'" + meetTime + "'," +
                            "'" + meetLocation + "'," +
                            "'" + sponsorID + "'," +
                            "'" + messageType + "'," +
                            "'" + realPath + "')";
                } else {
                    messageType = "1";
                    sql = "insert into meetings(metName,metTime,metLocation,metSponsorID,metType,metContent) " +
                            "values('" + meetName + "'," +
                            "'" + meetTime + "'," +
                            "'" + meetLocation + "'," +
                            "'" + sponsorID + "'," +
                            "'" + messageType + "'," +
                            "'" + messageContent + "')";
                }
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();

                //创建对应的会议签到表
                sql = "create table `" + (meetName + meetTime) + "`(" +
                        "userId char(11)," +
                        "userName varchar(20)," +
                        "foreign key(userId) references students(stuId))";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();

                //找出对应学生的信息插入会议签到表中
                System.out.println(participator);
                ParticipatorBean bean = JSON.parseObject(participator, ParticipatorBean.class);
                for (int i = 0; i < bean.getData().size(); i++) {
                    sql = "select * from students where stuGrade = '" + bean.getData().get(i).getStuGrade() + "' and " +
                            "stuMajor = '" + bean.getData().get(i).getStuMajor() + "' and " +
                            "stuClass = '" + bean.getData().get(i).getStuClass() + "'";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    DBHelper helper;
                    while (resultSet.next()) {
                        sql = "insert into `" + (meetName + meetTime) + "`(userId,userName) " +
                                "values ('" + resultSet.getString("stuId") + "','"
                                + resultSet.getString("stuName") + "')";
                        helper = new DBHelper(sql);
                        helper.pst.executeUpdate();
                        helper.close();
                    }
                    resultSet.close();
                    dbHelper.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String execute() throws Exception {
        setMeetName("2014级年级大会");
        setMeetTime("2018-03-21 15:00");
        setMeetLocation("王源兴国际会议中心 G-201");
        setSponsorID("1425122042");
        setParticipator("{\"data\":[{\"stuClass\":\"2班\", \"stuGrade\":\"2014级\",\"stuMajor\":\"软件工程\"},{\"stuClass\":\"1班\", \"stuGrade\":\"2014级\",\"stuMajor\":\"软件工程\"},{\"stuClass\":\"1班\", \"stuGrade\":\"2014级\",\"stuMajor\":\"计算机科学与技术\"}]}");
        FileUtils.uploadFile(uploadFile, uploadFileFileName, "/files");
        insertDataToDB();
        return SUCCESS;
    }
}
