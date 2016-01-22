package docnetwork.dataobj;

import java.io.Serializable;

/**
 * Created by zy on 15-12-1.
 */
public class Login implements Serializable{

    private String code;
    private String userNum;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

}
