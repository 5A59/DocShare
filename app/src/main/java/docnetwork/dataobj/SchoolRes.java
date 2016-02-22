package docnetwork.dataobj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 16-2-21.
 */
public class SchoolRes {
    private String code;
    private List<School> school;

    public SchoolRes(){
        code = "fail";
        school = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<School> getSchool() {
        return school;
    }

    public void setSchool(List<School> school) {
        this.school = school;
    }

    public static class School{
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
