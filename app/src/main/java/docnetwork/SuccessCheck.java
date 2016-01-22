package docnetwork;

/**
 * Created by zy on 15-12-1.
 */
public class SuccessCheck {

    public static boolean ifSuccess(String code){
        if (code.equals("success")){
            return true;
        }

        return false;
    }
}
