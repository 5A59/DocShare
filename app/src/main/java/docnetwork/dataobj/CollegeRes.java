package docnetwork.dataobj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 16-2-21.
 */
public class CollegeRes {
    private String code;
    private List<College> college;

    public CollegeRes(){
        code = "fail";
        college = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<College> getCollege() {
        return college;
    }

    public void setCollege(List<College> college) {
        this.college = college;
    }

    public static class College{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
