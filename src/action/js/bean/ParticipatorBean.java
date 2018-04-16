package action.js.bean;

import java.util.List;

public class ParticipatorBean {

    /**
     * stuGrade : 2014级
     * stuMajor : 软件工程
     * stuClass : 2班
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String stuGrade;
        private String stuMajor;
        private String stuClass;

        public String getStuGrade() {
            return stuGrade;
        }

        public void setStuGrade(String stuGrade) {
            this.stuGrade = stuGrade;
        }

        public String getStuMajor() {
            return stuMajor;
        }

        public void setStuMajor(String stuMajor) {
            this.stuMajor = stuMajor;
        }

        public String getStuClass() {
            return stuClass;
        }

        public void setStuClass(String stuClass) {
            this.stuClass = stuClass;
        }
    }
}
