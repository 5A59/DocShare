package docnetwork;

import android.content.Context;

import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import docnetwork.dataobj.Answer;
import docnetwork.dataobj.CollegeRes;
import docnetwork.dataobj.Doc;
import docnetwork.dataobj.DocCom;
import docnetwork.dataobj.OfferReword;
import docnetwork.dataobj.SchoolRes;
import network.MyUploadManager;
import network.listener.DownloadProcessListener;
import network.Network;
import docnetwork.dataobj.Info;
import docnetwork.dataobj.Login;
import docnetwork.dataobj.Register;
import docnetwork.dataobj.Res;
import docnetwork.dataobj.UserData;
import network.listener.UploadProcessListener;
import utils.GeneralUtils;
import utils.Logger;

/**
 * Created by zy on 15-12-1.
 */
public class DocNetwork {

    private static DocNetwork docNetwork;
    private Network network;

    private DocNetwork(){
        network = Network.getInstance();
    }

    private static synchronized void syncInit(){
        if (docNetwork == null){
            docNetwork = new DocNetwork();
        }
    }

    public static DocNetwork getInstance(){
        if (docNetwork == null){
            syncInit();
        }

        return docNetwork;
    }

    public void setCookie(Context context){
        network.setDefaultCookie(context);
    }

    public Login login(String username,String password){
        Map<String,String> para = new HashMap<>();
        para.put("username",username);
        para.put("password",password);
        Response response = null;
        try {
            response = network.post(HttpUrl.loginUrl, para);
            Login l = JsonUtil.getInstance().loginParse(response.body().string());

            if (SuccessCheck.ifSuccess(l.getCode())){
                UserData.userNum = l.getUserNum();
                Logger.d("login success  usernum is " + UserData.userNum);
                return l;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("login exception");
        }
        return new Login();
    }

    public Register register(String username,String password){
        Map<String,String> para = new HashMap<>();
        para.put("username",username);
        para.put("password",password);

        Response response = null;
        try {
            response = network.post(HttpUrl.registerUrl, para);
            Register r = JsonUtil.getInstance().registerParse(response.body().string());

            if (SuccessCheck.ifSuccess(r.getCode())){
                Logger.d("register success  usernum is " + r.getUserNum());
                return r;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("register exception");
        }
        return null;
    }

    public Login noBoundLogin(String username){
        Map<String,String> para = new HashMap<>();
        para.put("username",username);
        Response response = null;
        try {
            response = network.post(HttpUrl.noBoundLoginUrl, para);
            Login l = JsonUtil.getInstance().loginParse(response.body().string());

            if (SuccessCheck.ifSuccess(l.getCode())){
                UserData.userNum = l.getUserNum();
                Logger.d("login success usernum is " + UserData.userNum);
                return l;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("no bound login exception");
        }
        return new Login();
    }

    public boolean boundMobile(String userNum, String mobile, String password){
        Map<String,String> para = new HashMap<>();
        para.put("userNum", userNum);
        para.put("mobile", mobile);
        para.put("password", password);
        Response response = null;
        try {
            response = network.post(HttpUrl.boundMobile, para);
            Res res = JsonUtil.getInstance().resParse(response.body().string());

            if (SuccessCheck.ifSuccess(res.getCode())){
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("bound mobile exception");
        }
        return false;
    }

    public boolean unBoundMobile(String userNum, String mobile, String password){
        Map<String,String> para = new HashMap<>();
        para.put("userNum", userNum);
        para.put("mobile", mobile);
        para.put("password", password);
        Response response = null;
        try {
            response = network.post(HttpUrl.unBoundMobile, para);
            Res res = JsonUtil.getInstance().resParse(response.body().string());

            if (SuccessCheck.ifSuccess(res.getCode())){
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("bound mobile exception");
        }
        return false;
    }

    public Info info(String userNum){
        Map<String,String> para = new HashMap<>();
        para.put("userNum",userNum);

        Response response = null;
        try {
            response = network.get(HttpUrl.infoUrl, para);
            Info info = JsonUtil.getInstance().infoParse(response.body().string());
            return info;
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("info exception");
        }

        return null;
    }

    public boolean changeInfo(String name, String sex, String school, String college, String headImg){
        Map<String,String> para = new HashMap<>();
        para.put("name", name);
        para.put("sex", sex);
        para.put("school", school);
        para.put("college", college);

        Map<String,File> file = null;
        if (headImg != null && !headImg.isEmpty()){
            file = new HashMap<>();
            file.put("headImg", new File(headImg));
        }

        Response response = null;
        try {
            if (file != null){
                response = network.postFile(HttpUrl.changeInfoUrl, para, file);
            }else {
                response = network.post(HttpUrl.changeInfoUrl, para);
            }
            Res res = JsonUtil.getInstance().resParse(response.body().string());

            if (SuccessCheck.ifSuccess(res.getCode())){
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("change info exception");
        }

        return false;
    }

    public boolean uploadDoc(String title, String content, String school, String college,
                             String subject, List<File> files){
        Map<String, UploadProcessListener> listener = new HashMap<>();
        for (final File f : files){
            listener.put(f.getAbsolutePath(), new UploadProcessListener() {
                @Override
                public void update(long hasWrite, long length, boolean done) {
                    MyUploadManager.getInstance().updateUploadMes(f, hasWrite, length);
                }
            });
        }
        return uploadDoc(title, content, school, college, subject, files, listener);
    }

    public boolean uploadDoc(String title, String content, String school, String college,
                             String subject, List<File> files, Map<String, UploadProcessListener> listener){

        Map<String,String> para = new HashMap<>();
        para.put("title",title);
        para.put("content",content);
        para.put("school",school);
        para.put("college",college);
        para.put("subject",subject);

        Map<String,List<File>> fileMap = new HashMap<>();
        fileMap.put("files",files);

        Response response = null;
        try {
//            if (listener == null){
//                response = network.postFile(HttpUrl.uploadDocUrl, para, fileMap, true);
//            }else {
            response = network.postFile(HttpUrl.uploadDocUrl, para, fileMap, listener);
//            }

            Res res = JsonUtil.getInstance().resParse(response.body().string());

            if (SuccessCheck.ifSuccess(res.getCode())){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("uploadDoc error");
        }

        return false;
    }

    public Doc getDoc(int page, String school, String college, String subject){
        Map<String,String> para = new HashMap<>();
        para.put("page","" + page);
        para.put("school",school);
        para.put("college",college);
        para.put("subject",subject);

        Response response = null;
        try {
            response = network.get(HttpUrl.getDocUrl, para);
            Doc doc = JsonUtil.getInstance().docParse(response.body().string());

            if (SuccessCheck.ifSuccess(doc.getCode())){
                return doc;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("getDoc error");
        }
        return null;
    }

    public DocCom getDocComm(String docId, int page){
        Map<String,String> para = new HashMap<>();
        para.put("page","" + page);
        para.put("docId",docId);

        Response response = null;
        try {
            response = network.get(HttpUrl.getDocCommUrl, para);
            DocCom com = JsonUtil.getInstance().commParse(response.body().string());

            if (SuccessCheck.ifSuccess(com.getCode())){
                return com;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("getDocComm error");
        }

        return null;
    }

    public boolean comm(String docId, String content){
        Map<String,String> para = new HashMap<>();
        para.put("docId", docId);
        para.put("content", content);

        Response response = null;
        try {
            response = network.post(HttpUrl.commUrl, para);
            Res res = JsonUtil.getInstance().resParse(response.body().string());

            if (SuccessCheck.ifSuccess(res.getCode())){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("comm error");
        }

        return false;
    }

    public boolean uploadOfferReword(String title, String content, String school, String college, String subject){
        Map<String,String> para = new HashMap<>();
        para.put("title", title);
        para.put("content", content);
        para.put("school", school);
        para.put("college", college);
        para.put("subject", subject);

        Response response = null;
        try {
            response = network.post(HttpUrl.uploadOfferUrl, para);
            Res res = JsonUtil.getInstance().resParse(response.body().string());

            if (SuccessCheck.ifSuccess(res.getCode())){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("offerReword error");
        }

        return false;
    }

    public OfferReword getOffer(int page, String school, String college, String subject){
        Map<String,String> para = new HashMap<>();
        para.put("page", "" + page);
        para.put("school", school);
        para.put("college", college);
        para.put("subject", subject);

        Response response = null;
        try {
            response = network.get(HttpUrl.getOfferUrl, para);
            OfferReword offerReword = JsonUtil.getInstance().offerParse(response.body().string());

            if (SuccessCheck.ifSuccess(offerReword.getCode())){
                return offerReword;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("getOffer error");
        }

        return null;
    }

    /**
     *
     * @param offerId
     * @param content
     * @param files
     * @return
     */
    public boolean answer(String offerId, String content, List<File> files){
        Map<String,String> para = new HashMap<>();
        para.put("offerId", offerId);
        para.put("content", content);

//        Map<String,List<File>> fileMap = new HashMap<>();
//        fileMap.put("files", files);

        Response response = null;
        try {
            response = network.post(HttpUrl.uploadAnswerUrl, para);
            Res res = JsonUtil.getInstance().resParse(response.body().string());

            if (SuccessCheck.ifSuccess(res.getCode())){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("answer error");
        }
        return false;
    }

    public Answer getAnswer(int page, String offerId){
        Map<String,String> para = new HashMap<>();
        para.put("page","" + page);
        para.put("offerId", offerId);

        Response response = null;
        try {
            response = network.get(HttpUrl.getAnswerUrl, para);
            Answer answer = JsonUtil.getInstance().ansParse(response.body().string());

            if (SuccessCheck.ifSuccess(answer.getCode())){
                return answer;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.d("getAnswer error");
        }

        return null;
    }

    public Doc getMyDoc(int page) {
        Map<String,String> para = new HashMap<>();
        para.put("page","" + page);

        Response response = null;
        try{
            response = network.get(HttpUrl.getMyDocUrl, para);
            Doc doc = JsonUtil.getInstance().docParse(response.body().string());
            if (SuccessCheck.ifSuccess(doc.getCode())){
                return doc;
            }
        }catch (Exception e) {
            Logger.d("exception in get my download");
        }
        return new Doc();
    }

    public OfferReword getMyOffer(int page) {
        Map<String,String> para = new HashMap<>();
        para.put("page","" + page);

        Response response = null;
        try{
            response = network.get(HttpUrl.getMyOfferUrl, para);
            OfferReword offer = JsonUtil.getInstance().offerParse(response.body().string());
            if (SuccessCheck.ifSuccess(offer.getCode())){
                return offer;
            }
        }catch (Exception e) {
            Logger.d("exception in get my download");
        }
        return new OfferReword();

    }

    public Doc search(int page, String school, String college, String content){
        Map<String, String> para = new HashMap<>();
        para.put("school", school);
        para.put("college", college);
        para.put("content", content);
        para.put("page", "" + page);

        Response response = null;
        try{
            response = network.get(HttpUrl.searchUrl, para);
            Doc doc = JsonUtil.getInstance().docParse(response.body().string());
            if (SuccessCheck.ifSuccess(doc.getCode())){
                return doc;
            }
        }catch (Exception e){
            Logger.d("exception in search");
        }
        return new Doc();
    }

    public SchoolRes getSchool(){
        Response response = null;
        try{
            response = network.get(HttpUrl.getSchoolUrl);
            SchoolRes res = JsonUtil.getInstance().schoolResParse(response.body().string());
            if (SuccessCheck.ifSuccess(res.getCode())){
                return res;
            }
        }catch (Exception e){
            Logger.d("exception in get school");
        }
        return new SchoolRes();
    }

    public CollegeRes getCollege(String sch){
        Response response = null;
        Map<String, String> para = new HashMap<>();
        para.put("sch", sch);
        try {
            response = network.get(HttpUrl.getCollegeUrl, para);
            CollegeRes res = JsonUtil.getInstance().collegeResParse(response.body().string());
            if (SuccessCheck.ifSuccess(res.getCode())){
                return res;
            }
        }catch (Exception e){
            Logger.d("exception in get college");
        }

        return new CollegeRes();
    }

    public void downloadFile(DownloadProcessListener listener, String url, String fileName){
        File fileDir = new File(GeneralUtils.getInstance().getFileSavePath());
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File toFile = new File(GeneralUtils.getInstance().getFileSavePath() + "/" + fileName);
        downloadFile(listener, url, toFile);
    }

    public void downloadFile(DownloadProcessListener listener, String url, File toFile){
        network.downloadFile(listener, url, toFile);
    }

}
