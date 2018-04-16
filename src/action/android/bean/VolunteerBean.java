package action.android.bean;

import java.util.List;

/**
 * @author zhoukp
 * @time 2018/3/19 15:14
 * @email 275557625@qq.com
 * @function
 */

public class VolunteerBean {

    /**
     * time : 2018-03-19 15:14:09
     * volunteers : [{"volTime":"3月2日-7月5日","enroll":false,"volName":"关爱宗达长征行"},{"volTime":"9月1日-15日","enroll":false,"volName":"电脑义诊"},{"volTime":"9月1日-3日","enroll":false,"volName":"迎新活动"}]
     * status : 200
     */

    private String time;
    private int status;
    /**
     * volTime : 3月2日-7月5日
     * enroll : false
     * volName : 关爱宗达长征行
     */

    private List<VolunteersBean> volunteers;

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

    public List<VolunteersBean> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<VolunteersBean> volunteers) {
        this.volunteers = volunteers;
    }

    public static class VolunteersBean {
        private String volTime;
        private boolean enroll;
        private String volName;

        public String getVolTime() {
            return volTime;
        }

        public void setVolTime(String volTime) {
            this.volTime = volTime;
        }

        public boolean isEnroll() {
            return enroll;
        }

        public void setEnroll(boolean enroll) {
            this.enroll = enroll;
        }

        public String getVolName() {
            return volName;
        }

        public void setVolName(String volName) {
            this.volName = volName;
        }
    }
}
