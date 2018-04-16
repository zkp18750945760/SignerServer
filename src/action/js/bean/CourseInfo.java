package action.js.bean;

public class CourseInfo {
    /**
     * 课程名
     */
    private String courseName;
    /**
     * 上课地点
     */
    private String courseLocation;
    /**
     * 这是星期几的课
     */
    private String courseWeek;
    /**
     * 这是第几节课
     */
    private String courseTime;
    /**
     * 课程持续时间 2节还是3节
     */
    private String courseLong;
    /**
     * 上课周数 例如4-18周
     */
    private String courseWeeks;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
    }

    public String getCourseWeek() {
        return courseWeek;
    }

    public void setCourseWeek(String courseWeek) {
        this.courseWeek = courseWeek;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getCourseLong() {
        return courseLong;
    }

    public void setCourseLong(String courseLong) {
        this.courseLong = courseLong;
    }

    public String getCourseWeeks() {
        return courseWeeks;
    }

    public void setCourseWeeks(String courseWeeks) {
        this.courseWeeks = courseWeeks;
    }

    @Override
    public String toString() {
        return courseName + "," +
                courseLocation + "," +
                courseWeek + "," +
                courseTime + "," +
                courseLong + "," +
                courseWeeks + "@";
    }
}