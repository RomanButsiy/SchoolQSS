public class Subject {

    private int idSubject;
    private String Subject;
    private int idClasses;

    public Subject(int idSubject, int idClasses, String Subject) {
        this.idSubject = idSubject;
        this.idClasses = idClasses;
        this.Subject = Subject;
    }

    public int getIdSubject() {
        return idSubject;
    }

    public String getSubject() {
        return Subject;
    }

    public int getIdClasses() {
        return idClasses;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "idSubject=" + idSubject +
                ", Subject='" + Subject + '\'' +
                ", idClasses=" + idClasses +
                '}';
    }
}
