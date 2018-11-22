public class Teachers {

    private static final long serialVersionUID = 1L;
    private int idTeachers;
    private String nameTeachers0;
    private String nameTeachers1;
    private String nameTeachers2;
    private String login;
    private String password;
    private int level;
    private int selectedLanguage;

    public Teachers(int idTeachers, String nameTeachers0, String nameTeachers1, String nameTeachers2, String login, String password, int level, int selectedLanguage) {
        this.idTeachers = idTeachers;
        this.nameTeachers0 = nameTeachers0;
        this.nameTeachers1 = nameTeachers1;
        this.nameTeachers2 = nameTeachers2;
        this.login = login;
        this.password = password;
        this.level = level;
        this.selectedLanguage = selectedLanguage;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIdTeachers() {
        return idTeachers;
    }

    public String getNameTeachers0() {
        return nameTeachers0;
    }

    public String getNameTeachers1() {
        return nameTeachers1;
    }

    public String getNameTeachers2() {
        return nameTeachers2;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getLevel() {
        return level;
    }

    public int getSelectedLanguage() {
        return selectedLanguage;
    }

    @Override
    public String toString() {
        return "Teachers{" +
                "idTeachers=" + idTeachers +
                ", nameTeachers0='" + nameTeachers0 + '\'' +
                ", nameTeachers1='" + nameTeachers1 + '\'' +
                ", nameTeachers2='" + nameTeachers2 + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", level=" + level +
                ", selectedLanguage=" + selectedLanguage +
                '}';
    }
}
