public class Classes {

    private int idClasses;
    private String ClassName;
    private String ClassTeacher;
    private int AcademicYear;
    private int Semester;
    private int bool;

    public Classes(int idClasses, String ClassName, String ClassTeacher, int AcademicYear, int Semester, int bool) {
        this.idClasses = idClasses;
        this.ClassName = ClassName;
        this.ClassTeacher = ClassTeacher;
        this.AcademicYear = AcademicYear;
        this.Semester = Semester;
        this.bool = bool;
    }

    public int getIdClasses() {
        return idClasses;
    }

    public String getClassName() {
        return ClassName;
    }

    public String getClassTeacher() {
        return ClassTeacher;
    }

    public int getAcademicYear() {
        return AcademicYear;
    }

    public int getSemester() {
        return Semester;
    }

    public int getBool() {
        return bool;
    }

    public void setClassTeacher(String classTeacher) {
        this.ClassTeacher = classTeacher;
    }

    @Override
    public String toString() {
        return "Classes{" +
                "idClasses=" + idClasses +
                ", ClassName='" + ClassName + '\'' +
                ", ClassTeacher='" + ClassTeacher + '\'' +
                ", AcademicYear=" + AcademicYear +
                ", Semester=" + Semester +
                ", bool=" + bool +
                '}';
    }
}
