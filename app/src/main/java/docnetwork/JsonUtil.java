package docnetwork;

import com.google.gson.Gson;

import docnetwork.dataobj.Answer;
import docnetwork.dataobj.CollegeRes;
import docnetwork.dataobj.Doc;
import docnetwork.dataobj.DocCom;
import docnetwork.dataobj.Info;
import docnetwork.dataobj.Login;
import docnetwork.dataobj.OfferReword;
import docnetwork.dataobj.Register;
import docnetwork.dataobj.Res;
import docnetwork.dataobj.SchoolRes;

/**
 * Created by zy on 15-12-1.
 */
public class JsonUtil {
    private static JsonUtil jsonUtil;

    private JsonUtil(){

    }

    public static JsonUtil getInstance(){
        if (jsonUtil == null){
            jsonUtil = new JsonUtil();
        }

        return jsonUtil;
    }

    public Object getGson(String data, Class cl){
        return new Gson().fromJson(data,cl);
    }

    public Login loginParse(String json){
        return (Login) getGson(json, Login.class);
    }

    public Register registerParse(String json){
        return (Register) getGson(json, Register.class);
    }

    public Info infoParse(String json){
        return (Info) getGson(json,Info.class);
    }

    public Res resParse(String json){
        return (Res) getGson(json,Res.class);
    }

    public Doc docParse(String json){
        return (Doc) getGson(json, Doc.class);
    }

    public DocCom commParse(String json){
        return (DocCom) getGson(json, DocCom.class);
    }

    public OfferReword offerParse(String json){
        return (OfferReword) getGson(json, OfferReword.class);
    }

    public Answer ansParse(String json){
        return (Answer) getGson(json, Answer.class);
    }

    public SchoolRes schoolResParse(String json){
        return (SchoolRes) getGson(json, SchoolRes.class);
    }

    public CollegeRes collegeResParse(String json){
        return (CollegeRes) getGson(json, CollegeRes.class);
    }
}
