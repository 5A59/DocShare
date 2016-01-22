package fileselecter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by zy on 15-12-21.
 */
public class FileList implements Serializable{
    private List<File> files;

    public FileList(List<File> files){
        this.files = files;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
