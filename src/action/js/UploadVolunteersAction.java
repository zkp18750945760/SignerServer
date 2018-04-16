package action.js;

import com.opensymphony.xwork2.ActionSupport;
import db.DBHelper;
import utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;

public class UploadVolunteersAction extends ActionSupport {
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

            String sql = "select table_name from information_schema.tables where table_name = 'volunteers'";
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
                //数据库中还没有活动表
                sql = "create table volunteers(volName varchar(20) not null," +
                        "volTime varchar(20) not null," +
                        "primary key(volName,volTime))";
                dbHelper = new DBHelper(sql);
                dbHelper.pst.executeUpdate();
                dbHelper.close();
            }

            if (values != null) {
                for (int i = 1; i < values.length; i++) {
                    tabCount = 0;
                    sql = "select * from volunteers where (volName,volTime) in (('" + values[i][0] + "','" + values[i][1] + "'))";
                    dbHelper = new DBHelper(sql);
                    resultSet = dbHelper.pst.executeQuery();
                    while (resultSet.next()) {
                        tabCount++;
                    }
                    resultSet.close();
                    dbHelper.close();
                    if (tabCount == 0) {
                        //直接插入
                        sql = "insert into volunteers(volName,volTime) values('"
                                + values[i][0] + "','"
                                + values[i][1] + "')";
                        dbHelper = new DBHelper(sql);
                        dbHelper.pst.executeUpdate();
                        dbHelper.close();

                        sql = "create table `" + (values[i][0] + values[i][1]) + "`(stuId varchar(11) not null," +
                                "enrollTime varchar(19)," +
                                "foreign key(stuId) references students(stuId))";
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
