package docnetwork.dataobj;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by zy on 15-12-7.
 */
public class DocCom {
    private String code;
    private List<ComMes> com;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ComMes> getCom() {
        return com;
    }

    public void setCom(List<ComMes> com) {
        this.com = com;
    }

    public static class ComMes{
        private String writterName;
        private String writterNum;
        private String writterHeadImg;
        private String content;
        private String time;

        public String getWritterName() {
            return writterName;
        }

        public void setWritterName(String writterName) {
            this.writterName = writterName;
        }

        public String getWritterNum() {
            return writterNum;
        }

        public void setWritterNum(String writterNum) {
            this.writterNum = writterNum;
        }

        public String getWritterHeadImg() {
            return writterHeadImg;
        }

        public void setWritterHeadImg(String writterHeadImg) {
            this.writterHeadImg = writterHeadImg;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
