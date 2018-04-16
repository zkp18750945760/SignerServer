package action.js;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import db.DBHelper;
import utils.FileUtils;
import utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * banner图片管理action
 */
public class UpdateBannersAction extends ActionSupport {
    /**
     * banner图片
     */
    private static File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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
     * 更新banner图片
     *
     * @param bannerIndex bannerIndex
     * @return 200->更新成功 101->文件IO错误 102->数据库IO错误
     */
    public static String updateBannerByID(int bannerIndex) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<HashMap> jsonObjects = new ArrayList<>();

        try {
            String time = TimeUtils.getCurrentTime();
            FileUtils.uploadFile(file, (time + ".jpg"), "/banner");
            String sql = "update banner set uploadTime = '" + time + "'," +
                    "bannerUrl = '" + ("/banner/" + time + ".jpg") + "' " +
                    "where bannerIndex = '" + bannerIndex + "'";
            DBHelper dbHelper = new DBHelper(sql);
            dbHelper.pst.executeUpdate();
            dbHelper.close();
            HashMap<String, Object> map = new HashMap<>();
            map.put("uploadTime", time);
            map.put("bannerUrl", "/banner/" + time + ".jpg");
            map.put("bannerIndex", bannerIndex);
            jsonObjects.add(map);
            hashMap.put("status", 200);
            hashMap.put("time", time);
            hashMap.put("data", jsonObjects);
            return JSON.toJSONString(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
            hashMap.put("status", 101);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        } catch (SQLException e) {
            e.printStackTrace();
            hashMap.put("status", 102);
            hashMap.put("time", TimeUtils.getCurrentTime());
            return JSON.toJSONString(hashMap);
        }
    }


}
