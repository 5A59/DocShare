package docnetwork.dataobj;

import java.io.Serializable;

import docnetwork.HttpUrl;

/**
 * Created by zy on 15-12-1.
 */
public class Info implements Serializable{
    private String code;
    private Inf inf;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Inf getInf() {
        return inf;
    }

    public void setInf(Inf inf) {
        this.inf = inf;
    }

    public static class Inf implements Serializable{
        private String name;
        private String sex;
        private String school;
        private String college;
        private String headImg;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getHeadImgUrl(){
            return HttpUrl.mainUrl + this.headImg;
        }
    }
}
