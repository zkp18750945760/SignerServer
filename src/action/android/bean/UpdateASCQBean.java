package action.android.bean;

import java.util.List;

/**
 * @author zhoukp
 * @time 2018/4/15 15:55
 * @email 275557625@qq.com
 * @function
 */
public class UpdateASCQBean {

    /**
     * data : [{"practice":{"practice1":1,"practiceSelf":10,"practice2":3,"practice7":0,"practice5":0,"practice6":0,"practice3":0,"practiceBasic":{"practiceBasic3":"true","practiceBasic2":"false","practiceBasic1":"true"},"practice4":0,"practiceMax":12,"practiceMutual":10},"wit":{"witSelf":51.36,"GPA":5,"witMax":60,"witMutual":60},"sports":{"sportsMax":5,"level":1,"sportsMutual":5,"sportsSelf":4},"stuId":"1425122042","extra":{"extraSelf":1,"extraMax":5,"extraMutual":5},"schoolYear":"2017-2018","Genres":{"Genres5":0.5,"Genres4":0,"GenresMutual":3.5,"Genres3":0,"Genres2":0,"Genres1":0,"GenresSelf":3.5,"GenresMax":7,"GenresBasic":{"GenresBasic3":"true","GenresBasic2":"false","GenresBasic1":"true"}},"team":{"teamMax":6,"teamSelf":4,"teamBasic":{"teamBasic1":"true","teamBasic2":"false","teamBasic3":"true"},"team3":0,"team4":0.5,"team1":0,"team2":0.5,"team7":0,"team5":0,"team6":0,"teamMutual":4},"time":"2018-04-11 22:14:49","moral":{"moral2":0,"moral1":0,"moral4":0,"moral3":0,"moral6":0,"moral5":0,"moralMax":10,"moralSelf":10,"moralMutual":10}}]
     * time : 2018-04-15 15:54:34
     * status : 200
     */

    private String time;
    private int status;
    /**
     * practice : {"practice1":1,"practiceSelf":10,"practice2":3,"practice7":0,"practice5":0,"practice6":0,"practice3":0,"practiceBasic":{"practiceBasic3":"true","practiceBasic2":"false","practiceBasic1":"true"},"practice4":0,"practiceMax":12,"practiceMutual":10}
     * wit : {"witSelf":51.36,"GPA":5,"witMax":60,"witMutual":60}
     * sports : {"sportsMax":5,"level":1,"sportsMutual":5,"sportsSelf":4}
     * stuId : 1425122042
     * extra : {"extraSelf":1,"extraMax":5,"extraMutual":5}
     * schoolYear : 2017-2018
     * Genres : {"Genres5":0.5,"Genres4":0,"GenresMutual":3.5,"Genres3":0,"Genres2":0,"Genres1":0,"GenresSelf":3.5,"GenresMax":7,"GenresBasic":{"GenresBasic3":"true","GenresBasic2":"false","GenresBasic1":"true"}}
     * team : {"teamMax":6,"teamSelf":4,"teamBasic":{"teamBasic1":"true","teamBasic2":"false","teamBasic3":"true"},"team3":0,"team4":0.5,"team1":0,"team2":0.5,"team7":0,"team5":0,"team6":0,"teamMutual":4}
     * time : 2018-04-11 22:14:49
     * moral : {"moral2":0,"moral1":0,"moral4":0,"moral3":0,"moral6":0,"moral5":0,"moralMax":10,"moralSelf":10,"moralMutual":10}
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * practice1 : 1
         * practiceSelf : 10
         * practice2 : 3
         * practice7 : 0
         * practice5 : 0
         * practice6 : 0
         * practice3 : 0
         * practiceBasic : {"practiceBasic3":"true","practiceBasic2":"false","practiceBasic1":"true"}
         * practice4 : 0
         * practiceMax : 12
         * practiceMutual : 10
         */

        private PracticeBean practice;
        /**
         * witSelf : 51.36
         * GPA : 5
         * witMax : 60
         * witMutual : 60
         */

        private WitBean wit;
        /**
         * sportsMax : 5
         * level : 1
         * sportsMutual : 5
         * sportsSelf : 4
         */

        private SportsBean sports;
        private String stuId;
        private String checkedStuId;
        /**
         * extraSelf : 1
         * extraMax : 5
         * extraMutual : 5
         */

        private ExtraBean extra;
        private String schoolYear;
        /**
         * Genres5 : 0.5
         * Genres4 : 0
         * GenresMutual : 3.5
         * Genres3 : 0
         * Genres2 : 0
         * Genres1 : 0
         * GenresSelf : 3.5
         * GenresMax : 7
         * GenresBasic : {"GenresBasic3":"true","GenresBasic2":"false","GenresBasic1":"true"}
         */

        private GenresBean Genres;
        /**
         * teamMax : 6
         * teamSelf : 4
         * teamBasic : {"teamBasic1":"true","teamBasic2":"false","teamBasic3":"true"}
         * team3 : 0
         * team4 : 0.5
         * team1 : 0
         * team2 : 0.5
         * team7 : 0
         * team5 : 0
         * team6 : 0
         * teamMutual : 4
         */

        private TeamBean team;
        private String time;
        private String mutualCode;
        /**
         * moral2 : 0
         * moral1 : 0
         * moral4 : 0
         * moral3 : 0
         * moral6 : 0
         * moral5 : 0
         * moralMax : 10
         * moralSelf : 10
         * moralMutual : 10
         */

        private MoralBean moral;

        public PracticeBean getPractice() {
            return practice;
        }

        public void setPractice(PracticeBean practice) {
            this.practice = practice;
        }

        public WitBean getWit() {
            return wit;
        }

        public void setWit(WitBean wit) {
            this.wit = wit;
        }

        public SportsBean getSports() {
            return sports;
        }

        public void setSports(SportsBean sports) {
            this.sports = sports;
        }

        public String getStuId() {
            return stuId;
        }

        public void setStuId(String stuId) {
            this.stuId = stuId;
        }

        public String getCheckedStuId() {
            return checkedStuId;
        }

        public void setCheckedStuId(String checkedStuId) {
            this.checkedStuId = checkedStuId;
        }

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

        public GenresBean getGenres() {
            return Genres;
        }

        public void setGenres(GenresBean Genres) {
            this.Genres = Genres;
        }

        public TeamBean getTeam() {
            return team;
        }

        public void setTeam(TeamBean team) {
            this.team = team;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMutualCode() {
            return mutualCode;
        }

        public void setMutualCode(String mutualCode) {
            this.mutualCode = mutualCode;
        }

        public MoralBean getMoral() {
            return moral;
        }

        public void setMoral(MoralBean moral) {
            this.moral = moral;
        }

        public static class PracticeBean {
            private float practice1;
            private float practiceSelf;
            private float practice2;
            private float practice7;
            private float practice5;
            private float practice6;
            private float practice3;
            /**
             * practiceBasic3 : true
             * practiceBasic2 : false
             * practiceBasic1 : true
             */

            private PracticeBasicBean practiceBasic;
            private float practice4;
            private float practiceMax;
            private float practiceMutual;

            public float getPractice1() {
                return practice1;
            }

            public void setPractice1(float practice1) {
                this.practice1 = practice1;
            }

            public float getPracticeSelf() {
                return practiceSelf;
            }

            public void setPracticeSelf(float practiceSelf) {
                this.practiceSelf = practiceSelf;
            }

            public float getPractice2() {
                return practice2;
            }

            public void setPractice2(float practice2) {
                this.practice2 = practice2;
            }

            public float getPractice7() {
                return practice7;
            }

            public void setPractice7(float practice7) {
                this.practice7 = practice7;
            }

            public float getPractice5() {
                return practice5;
            }

            public void setPractice5(float practice5) {
                this.practice5 = practice5;
            }

            public float getPractice6() {
                return practice6;
            }

            public void setPractice6(float practice6) {
                this.practice6 = practice6;
            }

            public float getPractice3() {
                return practice3;
            }

            public void setPractice3(float practice3) {
                this.practice3 = practice3;
            }

            public PracticeBasicBean getPracticeBasic() {
                return practiceBasic;
            }

            public void setPracticeBasic(PracticeBasicBean practiceBasic) {
                this.practiceBasic = practiceBasic;
            }

            public float getPractice4() {
                return practice4;
            }

            public void setPractice4(float practice4) {
                this.practice4 = practice4;
            }

            public float getPracticeMax() {
                return practiceMax;
            }

            public void setPracticeMax(float practiceMax) {
                this.practiceMax = practiceMax;
            }

            public float getPracticeMutual() {
                return practiceMutual;
            }

            public void setPracticeMutual(float practiceMutual) {
                this.practiceMutual = practiceMutual;
            }

            public static class PracticeBasicBean {
                private String practiceBasic3;
                private String practiceBasic2;
                private String practiceBasic1;

                public String getPracticeBasic3() {
                    return practiceBasic3;
                }

                public void setPracticeBasic3(String practiceBasic3) {
                    this.practiceBasic3 = practiceBasic3;
                }

                public String getPracticeBasic2() {
                    return practiceBasic2;
                }

                public void setPracticeBasic2(String practiceBasic2) {
                    this.practiceBasic2 = practiceBasic2;
                }

                public String getPracticeBasic1() {
                    return practiceBasic1;
                }

                public void setPracticeBasic1(String practiceBasic1) {
                    this.practiceBasic1 = practiceBasic1;
                }
            }
        }

        public static class WitBean {
            private float witSelf;
            private float GPA;
            private float witMax;
            private float witMutual;

            public float getWitSelf() {
                return witSelf;
            }

            public void setWitSelf(float witSelf) {
                this.witSelf = witSelf;
            }

            public float getGPA() {
                return GPA;
            }

            public void setGPA(float GPA) {
                this.GPA = GPA;
            }

            public float getWitMax() {
                return witMax;
            }

            public void setWitMax(float witMax) {
                this.witMax = witMax;
            }

            public float getWitMutual() {
                return witMutual;
            }

            public void setWitMutual(float witMutual) {
                this.witMutual = witMutual;
            }
        }

        public static class SportsBean {
            private float sportsMax;
            private int level;
            private float sportsMutual;
            private float sportsSelf;

            public float getSportsMax() {
                return sportsMax;
            }

            public void setSportsMax(float sportsMax) {
                this.sportsMax = sportsMax;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public float getSportsMutual() {
                return sportsMutual;
            }

            public void setSportsMutual(float sportsMutual) {
                this.sportsMutual = sportsMutual;
            }

            public float getSportsSelf() {
                return sportsSelf;
            }

            public void setSportsSelf(float sportsSelf) {
                this.sportsSelf = sportsSelf;
            }
        }

        public static class ExtraBean {
            private float extraSelf;
            private float extraMax;
            private float extraMutual;

            public float getExtraSelf() {
                return extraSelf;
            }

            public void setExtraSelf(float extraSelf) {
                this.extraSelf = extraSelf;
            }

            public float getExtraMax() {
                return extraMax;
            }

            public void setExtraMax(float extraMax) {
                this.extraMax = extraMax;
            }

            public float getExtraMutual() {
                return extraMutual;
            }

            public void setExtraMutual(float extraMutual) {
                this.extraMutual = extraMutual;
            }
        }

        public static class GenresBean {
            private float Genres5;
            private float Genres4;
            private float GenresMutual;
            private float Genres3;
            private float Genres2;
            private float Genres1;
            private float GenresSelf;
            private float GenresMax;
            /**
             * GenresBasic3 : true
             * GenresBasic2 : false
             * GenresBasic1 : true
             */

            private GenresBasicBean GenresBasic;

            public float getGenres5() {
                return Genres5;
            }

            public void setGenres5(float Genres5) {
                this.Genres5 = Genres5;
            }

            public float getGenres4() {
                return Genres4;
            }

            public void setGenres4(float Genres4) {
                this.Genres4 = Genres4;
            }

            public float getGenresMutual() {
                return GenresMutual;
            }

            public void setGenresMutual(float GenresMutual) {
                this.GenresMutual = GenresMutual;
            }

            public float getGenres3() {
                return Genres3;
            }

            public void setGenres3(float Genres3) {
                this.Genres3 = Genres3;
            }

            public float getGenres2() {
                return Genres2;
            }

            public void setGenres2(float Genres2) {
                this.Genres2 = Genres2;
            }

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

            public float getGenresMax() {
                return GenresMax;
            }

            public void setGenresMax(float GenresMax) {
                this.GenresMax = GenresMax;
            }

            public GenresBasicBean getGenresBasic() {
                return GenresBasic;
            }

            public void setGenresBasic(GenresBasicBean GenresBasic) {
                this.GenresBasic = GenresBasic;
            }

            public static class GenresBasicBean {
                private String GenresBasic3;
                private String GenresBasic2;
                private String GenresBasic1;

                public String getGenresBasic3() {
                    return GenresBasic3;
                }

                public void setGenresBasic3(String GenresBasic3) {
                    this.GenresBasic3 = GenresBasic3;
                }

                public String getGenresBasic2() {
                    return GenresBasic2;
                }

                public void setGenresBasic2(String GenresBasic2) {
                    this.GenresBasic2 = GenresBasic2;
                }

                public String getGenresBasic1() {
                    return GenresBasic1;
                }

                public void setGenresBasic1(String GenresBasic1) {
                    this.GenresBasic1 = GenresBasic1;
                }
            }
        }

        public static class TeamBean {
            private float teamMax;
            private float teamSelf;
            /**
             * teamBasic1 : true
             * teamBasic2 : false
             * teamBasic3 : true
             */

            private TeamBasicBean teamBasic;
            private float team3;
            private float team4;
            private float team1;
            private float team2;
            private float team7;
            private float team5;
            private float team6;
            private float teamMutual;

            public float getTeamMax() {
                return teamMax;
            }

            public void setTeamMax(float teamMax) {
                this.teamMax = teamMax;
            }

            public float getTeamSelf() {
                return teamSelf;
            }

            public void setTeamSelf(float teamSelf) {
                this.teamSelf = teamSelf;
            }

            public TeamBasicBean getTeamBasic() {
                return teamBasic;
            }

            public void setTeamBasic(TeamBasicBean teamBasic) {
                this.teamBasic = teamBasic;
            }

            public float getTeam3() {
                return team3;
            }

            public void setTeam3(float team3) {
                this.team3 = team3;
            }

            public float getTeam4() {
                return team4;
            }

            public void setTeam4(float team4) {
                this.team4 = team4;
            }

            public float getTeam1() {
                return team1;
            }

            public void setTeam1(float team1) {
                this.team1 = team1;
            }

            public float getTeam2() {
                return team2;
            }

            public void setTeam2(float team2) {
                this.team2 = team2;
            }

            public float getTeam7() {
                return team7;
            }

            public void setTeam7(float team7) {
                this.team7 = team7;
            }

            public float getTeam5() {
                return team5;
            }

            public void setTeam5(float team5) {
                this.team5 = team5;
            }

            public float getTeam6() {
                return team6;
            }

            public void setTeam6(float team6) {
                this.team6 = team6;
            }

            public float getTeamMutual() {
                return teamMutual;
            }

            public void setTeamMutual(float teamMutual) {
                this.teamMutual = teamMutual;
            }

            public static class TeamBasicBean {
                private String teamBasic1;
                private String teamBasic2;
                private String teamBasic3;

                public String getTeamBasic1() {
                    return teamBasic1;
                }

                public void setTeamBasic1(String teamBasic1) {
                    this.teamBasic1 = teamBasic1;
                }

                public String getTeamBasic2() {
                    return teamBasic2;
                }

                public void setTeamBasic2(String teamBasic2) {
                    this.teamBasic2 = teamBasic2;
                }

                public String getTeamBasic3() {
                    return teamBasic3;
                }

                public void setTeamBasic3(String teamBasic3) {
                    this.teamBasic3 = teamBasic3;
                }
            }
        }

        public static class MoralBean {
            private float moral2;
            private float moral1;
            private float moral4;
            private float moral3;
            private float moral6;
            private float moral5;
            private float moralMax;
            private float moralSelf;
            private float moralMutual;

            public float getMoral2() {
                return moral2;
            }

            public void setMoral2(float moral2) {
                this.moral2 = moral2;
            }

            public float getMoral1() {
                return moral1;
            }

            public void setMoral1(float moral1) {
                this.moral1 = moral1;
            }

            public float getMoral4() {
                return moral4;
            }

            public void setMoral4(float moral4) {
                this.moral4 = moral4;
            }

            public float getMoral3() {
                return moral3;
            }

            public void setMoral3(float moral3) {
                this.moral3 = moral3;
            }

            public float getMoral6() {
                return moral6;
            }

            public void setMoral6(float moral6) {
                this.moral6 = moral6;
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

            public float getMoralSelf() {
                return moralSelf;
            }

            public void setMoralSelf(float moralSelf) {
                this.moralSelf = moralSelf;
            }

            public float getMoralMutual() {
                return moralMutual;
            }

            public void setMoralMutual(float moralMutual) {
                this.moralMutual = moralMutual;
            }
        }
    }
}
