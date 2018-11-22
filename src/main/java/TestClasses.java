import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ResourceBundle;


public class TestClasses {

    private int AcademicYear = 0;
    private int Semester = 0;
    private boolean bool = false;

    public TestClasses(Frame frame, Connection connection, Teachers teachers, ResourceBundle bundle) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resSet = statement.executeQuery("select AcademicYear, Semester from SchoolGymnasiumQSS.Classes where Boolean = 1 limit 1");
        if (resSet.next()) {
            AcademicYear = resSet.getInt("AcademicYear");
            Semester = resSet.getInt("Semester");
            bool = true;
        }
        resSet.close();
        if (!bool){
            if (teachers.getLevel() > 14) {
                CreateSemester createSemester = new CreateSemester(frame, bundle);
                if (createSemester.getBool()) {
                    CreateClass createClass = new CreateClass(frame, statement, bundle);
                    if (createClass.getBool()) {
                        String insert = "insert into SchoolGymnasiumQSS.Classes (ClassName, ClassTeacher," +
                                " AcademicYear, Semester, Boolean) values (?, ?, ?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insert);
                        preparedStatement.setString(1, createClass.getFieldBABA());
                        preparedStatement.setString(2, createClass.getFieldDIDO());
                        preparedStatement.setInt(3,  AcademicYear = createSemester.getSelectedYear());
                        preparedStatement.setInt(4, Semester = createSemester.getSelectedSemester());
                        preparedStatement.setInt(5, 1);
                        preparedStatement.execute();
                        preparedStatement.close();
                        JOptionPane.showMessageDialog(null, "Семестр та клас успішно створино",
                                "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                        bool = true;
                    }
                }
            }
        }
        statement.close();
    }

    public int getAcademicYear() {
        return AcademicYear;
    }

    public int getSemester() {
        return Semester;
    }

    public boolean isBool() {
        return bool;
    }
}
