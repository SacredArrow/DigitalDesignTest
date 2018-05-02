public class FileWithMD5 {
    private String name;

    public FileWithMD5(String name, String md5) {
        this.name = name;
        this.md5 = md5;
    }

    private String md5;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
