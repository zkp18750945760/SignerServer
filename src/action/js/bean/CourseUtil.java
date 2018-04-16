package action.js.bean;

public class CourseUtil {
    /**
     * 根据i计算这是星期几的课
     *
     * @param i
     * @return
     */
    public static String countWeek(int i) {
        String week = "1";
        switch (i) {
            case 3:
                week = "1";
                break;
            case 7:
                week = "2";
                break;
            case 11:
                week = "3";
                break;
            case 15:
                week = "4";
                break;
            case 19:
                week = "5";
                break;
        }
        return week;
    }

    /**
     * 根据j计算出第几节课
     *
     * @param j
     * @param isThree 是否是三节连续的课
     * @return
     */
    public static String countTime(int j, boolean isThree) {
        String time = "1-2节";
        switch (j) {
            case 4:
                time = "1-2节";
                if (isThree) {
                    time = "1-3节";
                }
                break;
            case 5:
                time = "3-4节";
                break;
            case 6:
                time = "5-6节";
                break;
            case 7:
                time = "7-8节";
                if (isThree) {
                    time = "7-9节";
                }
                break;
            case 8:
                time = "9-10节";
                break;
            case 9:
                time = "11-12节";
                if (isThree) {
                    time = "11-13节";
                }
                break;
        }
        return time;
    }
}
