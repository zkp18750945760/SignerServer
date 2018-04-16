package action.js;

import utils.aes.AesUtil;
import utils.aes.MD5;
import com.opensymphony.xwork2.ActionSupport;
import db.DBHelper;
import utils.FileUtils;

import java.io.*;
import java.sql.ResultSet;

/**
 * 网站Action
 */
public class UploadStudentsInfoAction extends ActionSupport {

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

    /**
     * 将学生信息表录入数据库中
     *
     * @param inputStream InputStream
     */
    private void insertDataToDB(InputStream inputStream) {
        try {
            String[][] values = FileUtils.readXlsFiles(inputStream, 1, 0);
            String key = MD5.encode("hqu");

            String sql = "select table_name from information_schema.tables where table_name = 'students'";
            DBHelper dbHelper = new DBHelper(sql);
            int tabCount = 0;
            ResultSet resultSet = dbHelper.pst.executeQuery();

            while (resultSet.next()) {
                tabCount++;
            }
            //关闭resultSet
            resultSet.close();
            dbHelper.close();

            if (tabCount == 0) {
                //数据库中还没有学生表
                sql = "create table students(" +
                        "stuId varchar(11) primary key," +
                        "stuName varchar(20) not null," +
                        "stuDuty varchar(20) not null," +
                        "stuGrade varchar(6) not null," +
                        "stuMajor varchar(20) not null," +
                        "stuClass varchar(3) not null," +
                        "stuPassword varchar(44) not null," +
                        "stuSerialNo varchar(40)," +
                        "stuUnBindTime varchar(19))";

                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            if (values != null) {
                for (int i = 1; i < values.length; i++) {
                    tabCount = 0;
                    //查看当前学生的信息是否在学生表中，若有则更新，否则直接插入
                    sql = "select * from students where stuId = '" + values[i][1] + "'";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    while (resultSet.next()) {
                        tabCount++;
                    }
                    resultSet.close();
                    dbHelper.close();
                    if (tabCount == 0) {
                        sql = "insert into students (stuId,stuName,stuDuty,stuGrade,stuMajor,stuClass,stuPassword) " +
                                "values ('" + values[i][1] + "'," +
                                "'" + values[i][2] + "'," +
                                "'" + values[i][3] + "'," +
                                "'" + values[i][4] + "'," +
                                "'" + values[i][5] + "'," +
                                "'" + values[i][6] + "'," +
                                "'" + AesUtil.encrypt(values[i][1].substring(values[i][1].length() - 6), key) + "')";

                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();
                    } else {
                        sql = "update students set " +
                                "stuName = '" + values[i][2] + "', " +
                                "stuDuty = '" + values[i][3] + "'," +
                                "stuGrade = '" + values[i][4] + "'," +
                                "stuMajor = '" + values[i][5] + "'" +
                                ",stuClass = '" + values[i][6] + "' where stuId = '" + values[i][1] + "'";
                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();
                    }
                }
            }
        } catch (Exception e) {
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
