public class Student {

    private int idStudent;
    private String NameStudent;
    private int idClasses;

    public Student(int idStudent, int idClasses, String NameStudent) {
        this.idStudent = idStudent;
        this.idClasses = idClasses;
        this.NameStudent = NameStudent;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public String getNameStudent() {
        return NameStudent;
    }

    public int getIdClasses() {
        return idClasses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "idStudent=" + idStudent +
                ", NameStudent='" + NameStudent + '\'' +
                ", idClasses=" + idClasses +
                '}';
    }
}
