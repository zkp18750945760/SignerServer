package action.android.bean;

/**
 * @author zhoukp
 * @time 2018/4/11 16:16
 * @email 275557625@qq.com
 * @function
 */
public class ASCQBean {

    /**
     * extraMutual : 0
     * extraMax : 5
     * extraSelf : 1
     */

    private ExtraBean extra;
    /**
     * extra : {"extraMutual":0,"extraMax":5,"extraSelf":1}
     * schoolYear : 2017-2018
     * wit : {"witMutual":0,"witMax":60,"witSelf":51.36,"GPA":3.92}
     * practice : {"practice1":1,"practice4":0,"practice5":0,"practiceSelf":10,"practiceBasic":{"practiceBasic3":true,"practiceBasic2":false,"practiceBasic1":true},"practice6":0,"practiceMax":12,"practice7":0,"practiceMutual":0,"practice2":3,"practice3":0}
     * time : 2018-04-11 21:58:35
     * status : 200
     * userId : 1425122042
     * team : {"teamMax":6,"team2":0.5,"team6":0,"team5":0,"team7":0,"teamMutual":0,"teamBasic":{"teamBasic3":true,"teamBasic1":false,"teamBasic2":true},"team1":0,"team4":0.5,"teamSelf":4,"team3":0}
     * Genres : {"Genres1":0,"GenresSelf":6.5,"Genres5":0.5,"GenresMutual":0,"Genres2":0,"GenresBasic":{"GenresBasic1":true,"GenresBasic3":false,"GenresBasic2":true},"GenresMax":7,"Genres4":0,"Genres3":0}
     * moral : {"moral4":0,"moral1":0,"moralMutual":0,"moral5":0,"moralMax":10,"moral6":0,"moralSelf":10,"moral3":0,"moral2":0}
     * sports : {"sportsMax":5,"sportsSelf":4,"sportsMutual":0,"level":3}
     */

    private String schoolYear;
    /**
     * witMutual : 0
     * witMax : 60
     * witSelf : 51.36
     * GPA : 3.92
     */

    private WitBean wit;
    /**
     * practice1 : 1
     * practice4 : 0
     * practice5 : 0
     * practiceSelf : 10
     * practiceBasic : {"practiceBasic3":true,"practiceBasic2":false,"practiceBasic1":true}
     * practice6 : 0
     * practiceMax : 12
     * practice7 : 0
     * practiceMutual : 0
     * practice2 : 3
     * practice3 : 0
     */

    private PracticeBean practice;
    private String time;
    private int status;
    private String userId;
    /**
     * teamMax : 6
     * team2 : 0.5
     * team6 : 0
     * team5 : 0
     * team7 : 0
     * teamMutual : 0
     * teamBasic : {"teamBasic3":true,"teamBasic1":false,"teamBasic2":true}
     * team1 : 0
     * team4 : 0.5
     * teamSelf : 4
     * team3 : 0
     */

    private TeamBean team;
    /**
     * Genres1 : 0
     * GenresSelf : 6.5
     * Genres5 : 0.5
     * GenresMutual : 0
     * Genres2 : 0
     * GenresBasic : {"GenresBasic1":true,"GenresBasic3":false,"GenresBasic2":true}
     * GenresMax : 7
     * Genres4 : 0
     * Genres3 : 0
     */

    private GenresBean Genres;
    /**
     * moral4 : 0
     * moral1 : 0
     * moralMutual : 0
     * moral5 : 0
     * moralMax : 10
     * moral6 : 0
     * moralSelf : 10
     * moral3 : 0
     * moral2 : 0
     */

    private MoralBean moral;
    /**
     * sportsMax : 5
     * sportsSelf : 4
     * sportsMutual : 0
     * level : 3
     */

    private SportsBean sports;

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public WitBean getWit() {
        return wit;
    }

    public void setWit(WitBean wit) {
        this.wit = wit;
    }

    public PracticeBean getPractice() {
        return practice;
    }

    public void setPractice(PracticeBean practice) {
        this.practice = practice;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }

    public GenresBean getGenres() {
        return Genres;
    }

    public void setGenres(GenresBean Genres) {
        this.Genres = Genres;
    }

    public MoralBean getMoral() {
        return moral;
    }

    public void setMoral(MoralBean moral) {
        this.moral = moral;
    }

    public SportsBean getSports() {
        return sports;
    }

    public void setSports(SportsBean sports) {
        this.sports = sports;
    }

    public static class ExtraBean {
        private float extraMutual;
        private float extraMax;
        private float extraSelf;

        public float getExtraMutual() {
            return extraMutual;
        }

        public void setExtraMutual(float extraMutual) {
            this.extraMutual = extraMutual;
        }

        public float getExtraMax() {
            return extraMax;
        }

        public void setExtraMax(float extraMax) {
            this.extraMax = extraMax;
        }

        public float getExtraSelf() {
            return extraSelf;
        }

        public void setExtraSelf(float extraSelf) {
            this.extraSelf = extraSelf;
        }
    }

    public static class WitBean {
        private float witMutual;
        private float witMax;
        private float witSelf;
        private double GPA;

        public float getWitMutual() {
            return witMutual;
        }

        public void setWitMutual(float witMutual) {
            this.witMutual = witMutual;
        }

        public float getWitMax() {
            return witMax;
        }

        public void setWitMax(float witMax) {
            this.witMax = witMax;
        }

        public float getWitSelf() {
            return witSelf;
        }

        public void setWitSelf(float witSelf) {
            this.witSelf = witSelf;
        }

        public double getGPA() {
            return GPA;
        }

        public void setGPA(double GPA) {
            this.GPA = GPA;
        }
    }

    public static class PracticeBean {
        private float practice1;
        private float practice4;
        private float practice5;
        private float practiceSelf;
        /**
         * practiceBasic3 : true
         * practiceBasic2 : false
         * practiceBasic1 : true
         */

        private PracticeBasicBean practiceBasic;
        private float practice6;
        private float practiceMax;
        private float practice7;
        private float practiceMutual;
        private float practice2;
        private float practice3;

        public float getPractice1() {
            return practice1;
        }

        public void setPractice1(float practice1) {
            this.practice1 = practice1;
        }

        public float getPractice4() {
            return practice4;
        }

        public void setPractice4(float practice4) {
            this.practice4 = practice4;
        }

        public float getPractice5() {
            return practice5;
        }

        public void setPractice5(float practice5) {
            this.practice5 = practice5;
        }

        public float getPracticeSelf() {
            return practiceSelf;
        }

        public void setPracticeSelf(float practiceSelf) {
            this.practiceSelf = practiceSelf;
        }

        public PracticeBasicBean getPracticeBasic() {
            return practiceBasic;
        }

        public void setPracticeBasic(PracticeBasicBean practiceBasic) {
            this.practiceBasic = practiceBasic;
        }

        public float getPractice6() {
            return practice6;
        }

        public void setPractice6(float practice6) {
            this.practice6 = practice6;
        }

        public float getPracticeMax() {
            return practiceMax;
        }

        public void setPracticeMax(float practiceMax) {
            this.practiceMax = practiceMax;
        }

        public float getPractice7() {
            return practice7;
        }

        public void setPractice7(float practice7) {
            this.practice7 = practice7;
        }

        public float getPracticeMutual() {
            return practiceMutual;
        }

        public void setPracticeMutual(float practiceMutual) {
            this.practiceMutual = practiceMutual;
        }

        public float getPractice2() {
            return practice2;
        }

        public void setPractice2(float practice2) {
            this.practice2 = practice2;
        }

        public float getPractice3() {
            return practice3;
        }

        public void setPractice3(float practice3) {
            this.practice3 = practice3;
        }

        public static class PracticeBasicBean {
            private boolean practiceBasic3;
            private boolean practiceBasic2;
            private boolean practiceBasic1;

            public boolean isPracticeBasic3() {
                return practiceBasic3;
            }

            public void setPracticeBasic3(boolean practiceBasic3) {
                this.practiceBasic3 = practiceBasic3;
            }

            public boolean isPracticeBasic2() {
                return practiceBasic2;
            }

            public void setPracticeBasic2(boolean practiceBasic2) {
                this.practiceBasic2 = practiceBasic2;
            }

            public boolean isPracticeBasic1() {
                return practiceBasic1;
            }

            public void setPracticeBasic1(boolean practiceBasic1) {
                this.practiceBasic1 = practiceBasic1;
            }
        }
    }

    public static class TeamBean {
        private float teamMax;
        private float team2;
        private float team6;
        private float team5;
        private float team7;
        private float teamMutual;
        /**
         * teamBasic3 : true
         * teamBasic1 : false
         * teamBasic2 : true
         */

        private TeamBasicBean teamBasic;
        private float team1;
        private double team4;
        private float teamSelf;
        private float team3;

        public float getTeamMax() {
            return teamMax;
        }

        public void setTeamMax(float teamMax) {
            this.teamMax = teamMax;
        }

        public float getTeam2() {
            return team2;
        }

        public void setTeam2(float team2) {
            this.team2 = team2;
        }

        public float getTeam6() {
            return team6;
        }

        public void setTeam6(float team6) {
            this.team6 = team6;
        }

        public float getTeam5() {
            return team5;
        }

        public void setTeam5(float team5) {
            this.team5 = team5;
        }

        public float getTeam7() {
            return team7;
        }

        public void setTeam7(float team7) {
            this.team7 = team7;
        }

        public float getTeamMutual() {
            return teamMutual;
        }

        public void setTeamMutual(float teamMutual) {
            this.teamMutual = teamMutual;
        }

        public TeamBasicBean getTeamBasic() {
            return teamBasic;
        }

        public void setTeamBasic(TeamBasicBean teamBasic) {
            this.teamBasic = teamBasic;
        }

        public float getTeam1() {
            return team1;
        }

        public void setTeam1(float team1) {
            this.team1 = team1;
        }

        public double getTeam4() {
            return team4;
        }

        public void setTeam4(double team4) {
            this.team4 = team4;
        }

        public float getTeamSelf() {
            return teamSelf;
        }

        public void setTeamSelf(float teamSelf) {
            this.teamSelf = teamSelf;
        }

        public float getTeam3() {
            return team3;
        }

        public void setTeam3(float team3) {
            this.team3 = team3;
        }

        public static class TeamBasicBean {
            private boolean teamBasic3;
            private boolean teamBasic1;
            private boolean teamBasic2;

            public boolean isTeamBasic3() {
                return teamBasic3;
            }

            public void setTeamBasic3(boolean teamBasic3) {
                this.teamBasic3 = teamBasic3;
            }

            public boolean isTeamBasic1() {
                return teamBasic1;
            }

            public void setTeamBasic1(boolean teamBasic1) {
                this.teamBasic1 = teamBasic1;
            }

            public boolean isTeamBasic2() {
                return teamBasic2;
            }

            public void setTeamBasic2(boolean teamBasic2) {
                this.teamBasic2 = teamBasic2;
            }
        }
    }

    public static class GenresBean {
        private float Genres1;
        private float GenresSelf;
        private float Genres5;
        private float GenresMutual;
        private float Genres2;
        /**
         * GenresBasic1 : true
         * GenresBasic3 : false
         * GenresBasic2 : true
         */

        private GenresBasicBean GenresBasic;
        private float GenresMax;
        private float Genres4;
        private float Genres3;

        public float getGenres1() {
            return Genres1;
        }

        public void setGenres1(float Genres1) {
            this.Genres1 = Genres1;
        }

        public float getGenresSelf() {
            return GenresSelf;
        }

        public void setGenresSelf(float GenresSelf) {
            this.GenresSelf = GenresSelf;
        }

        public float getGenres5() {
            return Genres5;
        }

        public void setGenres5(float Genres5) {
            this.Genres5 = Genres5;
        }

        public float getGenresMutual() {
            return GenresMutual;
        }

        public void setGenresMutual(float GenresMutual) {
            this.GenresMutual = GenresMutual;
        }

        public float getGenres2() {
            return Genres2;
        }

        public void setGenres2(float Genres2) {
            this.Genres2 = Genres2;
        }

        public GenresBasicBean getGenresBasic() {
            return GenresBasic;
        }

        public void setGenresBasic(GenresBasicBean GenresBasic) {
            this.GenresBasic = GenresBasic;
        }

        public float getGenresMax() {
            return GenresMax;
        }

        public void setGenresMax(float GenresMax) {
            this.GenresMax = GenresMax;
        }

        public float getGenres4() {
            return Genres4;
        }

        public void setGenres4(float Genres4) {
            this.Genres4 = Genres4;
        }

        public float getGenres3() {
            return Genres3;
        }

        public void setGenres3(float Genres3) {
            this.Genres3 = Genres3;
        }

        public static class GenresBasicBean {
            private boolean GenresBasic1;
            private boolean GenresBasic3;
            private boolean GenresBasic2;

            public boolean isGenresBasic1() {
                return GenresBasic1;
            }

            public void setGenresBasic1(boolean GenresBasic1) {
                this.GenresBasic1 = GenresBasic1;
            }

            public boolean isGenresBasic3() {
                return GenresBasic3;
            }

            public void setGenresBasic3(boolean GenresBasic3) {
                this.GenresBasic3 = GenresBasic3;
            }

            public boolean isGenresBasic2() {
                return GenresBasic2;
            }

            public void setGenresBasic2(boolean GenresBasic2) {
                this.GenresBasic2 = GenresBasic2;
            }
        }
    }

    public static class MoralBean {
        private float moral4;
        private float moral1;
        private float moralMutual;
        private float moral5;
        private float moralMax;
        private float moral6;
        private float moralSelf;
        private float moral3;
        private float moral2;

        public float getMoral4() {
            return moral4;
        }

        public void setMoral4(float moral4) {
            this.moral4 = moral4;
        }

        public float getMoral1() {
            return moral1;
        }

        public void setMoral1(float moral1) {
            this.moral1 = moral1;
        }

        public float getMoralMutual() {
            return moralMutual;
        }

        public void setMoralMutual(float moralMutual) {
            this.moralMutual = moralMutual;
        }

        public float getMoral5() {
            return moral5;
        }

        public void setMoral5(float moral5) {
            this.moral5 = moral5;
        }

        public float getMoralMax() {
            return moralMax;
        }

        public void setMoralMax(float moralMax) {
            this.moralMax = moralMax;
        }

        public float getMoral6() {
            return moral6;
        }

        public void setMoral6(float moral6) {
            this.moral6 = moral6;
        }

        public float getMoralSelf() {
            return moralSelf;
        }

        public void setMoralSelf(float moralSelf) {
            this.moralSelf = moralSelf;
        }

        public float getMoral3() {
            return moral3;
        }

        public void setMoral3(float moral3) {
            this.moral3 = moral3;
        }

        public float getMoral2() {
            return moral2;
        }

        public void setMoral2(float moral2) {
            this.moral2 = moral2;
        }
    }

    public static class SportsBean {
        private float sportsMax;
        private float sportsSelf;
        private float sportsMutual;
        private int level;

        public float getSportsMax() {
            return sportsMax;
        }

        public void setSportsMax(float sportsMax) {
            this.sportsMax = sportsMax;
        }

        public float getSportsSelf() {
            return sportsSelf;
        }

        public void setSportsSelf(float sportsSelf) {
            this.sportsSelf = sportsSelf;
        }

        public float getSportsMutual() {
            return sportsMutual;
        }

        public void setSportsMutual(float sportsMutual) {
            this.sportsMutual = sportsMutual;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
