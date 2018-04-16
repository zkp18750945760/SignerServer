package action.android.bean;

import java.util.List;

/**
 * @author zhoukp
 * @time 2018/3/19 15:16
 * @email 275557625@qq.com
 * @function
 */

public class ActivityBean {

    /**
     * activities : [{"actTime":"3月19日-21日 16:00","enroll":false,"actLocation":"灯光篮球场","actName":"\u201c新生杯\u201d篮球赛"},{"actTime":"3月22日-23日 14:30","enroll":false,"actLocation":"学生活动中心","actName":"学院辩论赛"},{"actTime":"3月24日-27日 15:00","enroll":false,"actLocation":"学生活动中心","actName":"歌唱比赛"}]
     * time : 2018-03-19 15:15:59
     * status : 200
     */

    private String time;
    private int status;
    /**
     * actTime : 3月19日-21日 16:00
     * enroll : false
     * actLocation : 灯光篮球场
     * actName : “新生杯”篮球赛
     */

    private List<ActivitiesBean> activities;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ActivitiesBean> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivitiesBean> activities) {
        this.activities = activities;
    }

    public static class ActivitiesBean {
        private String actTime;
        private boolean enroll;
        private String actLocation;
        private String actName;

        public String getActTime() {
            return actTime;
        }

        public void setActTime(String actTime) {
            this.actTime = actTime;
        }

        public boolean isEnroll() {
            return enroll;
        }

        public void setEnroll(boolean enroll) {
            this.enroll = enroll;
        }

        public String getActLocation() {
            return actLocation;
        }

        public void setActLocation(String actLocation) {
            this.actLocation = actLocation;
        }

        public String getActName() {
            return actName;
        }

        public void setActName(String actName) {
            this.actName = actName;
        }
    }
}
