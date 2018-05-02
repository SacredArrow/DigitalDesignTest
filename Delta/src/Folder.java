import java.util.TreeMap;

public class Folder extends TreeMap<String, Folder> { //Contains files(with MD5) and other folders
    FileContainer files;

    public Folder() {
        this.files = new FileContainer();
    }
}
