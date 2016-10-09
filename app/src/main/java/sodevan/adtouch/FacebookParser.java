package sodevan.adtouch;

import java.util.List;

/**
 * Created by ronaksakhuja on 9/10/16.
 */

public class FacebookParser {

    /**
     * id : 10152832773477094
     * birthday : 05/04/1998
     * education : [{"school":{"id":"115245895156153","name":"bhatnagar international summit"},"type":"High School","year":{"id":"141778012509913","name":"2008"},"id":"10151228155672094"},{"school":{"id":"131530413548221","name":"Sachdeva Public School"},"type":"High School","year":{"id":"118118634930920","name":"2012"},"id":"10150117435857094"},{"school":{"id":"188610041175952","name":"Delhi Technological University (Formerly DCE)"},"type":"College","year":{"id":"508544319204481","name":"2020"},"id":"10154425677937094"}]
     * email : ronak.sakhuja@ymail.com
     * favorite_athletes : [{"id":"123161697720637","name":"Mark Cavendish"},{"id":"161367337249459","name":"Novak Djokovic"},{"id":"20242388857","name":"Usain Bolt"},{"id":"226520244049765","name":"Rohit Sharma"},{"id":"221818111232966","name":"I don't know what to talk about, but i wanna talk to you."}]
     * first_name : Ronak
     * gender : male
     * hometown : {"id":"106517799384578","name":"New Delhi, India"}
     * languages : [{"id":"108106272550772","name":"French"},{"id":"106059522759137","name":"English"},{"id":"112969428713061","name":"Hindi"}]
     * last_name : Sakhuja
     * link : https://www.facebook.com/app_scoped_user_id/10152832773477094/
     * location : {"id":"102161913158207","name":"Delhi, India"}
     * locale : en_US
     * name : Ronak Sakhuja
     * relationship_status : Single
     * sports : [{"id":"103992339636529","name":"Cricket","with":[{"name":"Harshit Thakur","id":"858126957535839"}]}]
     * timezone : 5.5
     * updated_time : 2016-07-24T05:03:39+0000
     * verified : true
     */

    private String id;
    private String birthday;
    private String email;
    private String first_name;
    private String gender;
    private HometownEntity hometown;
    private String last_name;
    private String link;
    private LocationEntity location;
    private String name;
    private String relationship_status;
    private double timezone;
    private List<EducationEntity> education;
    private List<FavoriteAthletesEntity> favorite_athletes;
    private List<LanguagesEntity> languages;
    private List<SportsEntity> sports;

    public void setId(String id) {
        this.id = id;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHometown(HometownEntity hometown) {
        this.hometown = hometown;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelationship_status(String relationship_status) {
        this.relationship_status = relationship_status;
    }

    public void setTimezone(double timezone) {
        this.timezone = timezone;
    }

    public void setEducation(List<EducationEntity> education) {
        this.education = education;
    }

    public void setFavorite_athletes(List<FavoriteAthletesEntity> favorite_athletes) {
        this.favorite_athletes = favorite_athletes;
    }

    public void setLanguages(List<LanguagesEntity> languages) {
        this.languages = languages;
    }

    public void setSports(List<SportsEntity> sports) {
        this.sports = sports;
    }

    public String getId() {
        return id;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getGender() {
        return gender;
    }

    public HometownEntity getHometown() {
        return hometown;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getLink() {
        return link;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getRelationship_status() {
        return relationship_status;
    }

    public double getTimezone() {
        return timezone;
    }

    public List<EducationEntity> getEducation() {
        return education;
    }

    public List<FavoriteAthletesEntity> getFavorite_athletes() {
        return favorite_athletes;
    }

    public List<LanguagesEntity> getLanguages() {
        return languages;
    }

    public List<SportsEntity> getSports() {
        return sports;
    }

    public static class HometownEntity {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class LocationEntity {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class EducationEntity {
        /**
         * school : {"id":"115245895156153","name":"bhatnagar international summit"}
         * type : High School
         * year : {"id":"141778012509913","name":"2008"}
         * id : 10151228155672094
         */

        private SchoolEntity school;
        private String type;
        private YearEntity year;

        public void setSchool(SchoolEntity school) {
            this.school = school;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setYear(YearEntity year) {
            this.year = year;
        }

        public SchoolEntity getSchool() {
            return school;
        }

        public String getType() {
            return type;
        }

        public YearEntity getYear() {
            return year;
        }

        public static class SchoolEntity {
            private String name;

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        public static class YearEntity {
            private String name;

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }
    }

    public static class FavoriteAthletesEntity {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class LanguagesEntity {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class SportsEntity {
        private String name;
        private List<WithEntity> with;

        public void setName(String name) {
            this.name = name;
        }

        public void setWith(List<WithEntity> with) {
            this.with = with;
        }

        public String getName() {
            return name;
        }

        public List<WithEntity> getWith() {
            return with;
        }

        public static class WithEntity {
            /**
             * name : Harshit Thakur
             * id : 858126957535839
             */

            private String name;
            private String id;

            public void setName(String name) {
                this.name = name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public String getId() {
                return id;
            }
        }
    }
}
