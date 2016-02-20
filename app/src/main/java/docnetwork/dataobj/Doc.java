package docnetwork.dataobj;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 15-12-7.
 */
public class Doc implements Serializable{
    private String code;
    private List<DocMes> doc;

    public Doc(){
        code = "false";
        doc = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DocMes> getDoc() {
        return doc;
    }

    public void setDoc(List<DocMes> doc) {
        this.doc = doc;
    }

    public void appendDocMes(Doc d){
        if (d == null){
            return ;
        }
        doc.addAll(d.getDoc());
    }

    public void reAddDocMes(Doc d){
        if (d == null){
            return ;
        }
        doc.clear();
        doc.addAll(d.getDoc());
    }

    public static class DocMes implements Serializable{
        private String docId;
        private String docCode;
        private String writterName;
        private String writterNum;
        private String writterHeadImg;
        private String title;
        private String content;
        private String files;
        private String time;

        private int like;
        private int dislike;

        public String getDocCode() {
            return docCode;
        }

        public void setDocCode(String docCode) {
            this.docCode = docCode;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
        }

        public int getDislike() {
            return dislike;
        }

        public void setDislike(int dislike) {
            this.dislike = dislike;
        }

        public String getFiles() {
            return files;
        }

        public void setFiles(String files) {
            this.files = files;
        }
    }

    @Override
    public String toString() {

        return new Gson().toJson(this);
    }
}
