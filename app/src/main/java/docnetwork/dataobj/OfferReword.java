package docnetwork.dataobj;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fileselecter.SelectAdapter;

/**
 * Created by zy on 15-12-7.
 */
public class OfferReword implements Serializable{
    private String code;
    private List<OfferMes> offer;

    public OfferReword(){
        code = "false";
        offer = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<OfferMes> getOffer() {
        return offer;
    }

    public void setOffer(List<OfferMes> offer) {
        this.offer = offer;
    }

    public void appendOffer(OfferReword o){
        if (o == null){
            return ;
        }
        offer.addAll(o.getOffer());
    }

    public void reAddOffer(OfferReword o){
        offer.clear();
        if (o == null){
            return ;
        }
        offer.addAll(o.getOffer());
    }

    public static class OfferMes implements Serializable{
        private String offerId;
        private String writterName;
        private String writterNum;
        private String writterHeadImg;
        private String title;
        private String content;
        private String time;
        private String school;
        private String college;

        public String getOfferId() {
            return offerId;
        }

        public void setOfferId(String offerId) {
            this.offerId = offerId;
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

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }
    }

    @Override
    public String toString() {

        return new Gson().toJson(this);
    }
}
