import java.io.Serializable;

public class DataBaseResourseIO  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String URL;
    private String USERNAME;
    private String PASSWORD;


    public DataBaseResourseIO(String URL, String USERNAME, String PASSWORD) {
        this.URL = URL;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    public String getURL() {
        return URL;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    @Override
    public String toString() {
        return "DataBaseResourseIO{" +
                "URL='" + URL + '\'' +
                ", USERNAME='" + USERNAME + '\'' +
                ", PASSWORD='" + PASSWORD + '\'' +
                '}';
    }
}
