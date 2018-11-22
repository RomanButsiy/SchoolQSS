import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;


public class SchoolQSS {

    private static final long serialVersionUID = 1L;
    private static final String DataBaseResource = "DataBaseResource.qdt";
    private static final String[][] BundleResource = {{"English", "Українська"},
                                                      {"en", "uk"}};
    private static final String query0 = "CREATE SCHEMA SchoolGymnasiumQSS CHARACTER SET utf8 COLLATE utf8_general_ci";
    private static final String query1 = "CREATE TABLE SchoolGymnasiumQSS.Classes " +
                                            "(idClasses INTEGER NOT NULL AUTO_INCREMENT, " +
                                            " ClassName VARCHAR(45) NOT NULL, " +
                                            " ClassTeacher VARCHAR(45) NOT NULL, " +
                                            " AcademicYear INTEGER NOT NULL, " +
                                            " Semester INTEGER NOT NULL, " +
                                            " Boolean INTEGER NOT NULL, " +
                                            " PRIMARY KEY (idClasses))";
    private static final String query2 = "CREATE TABLE SchoolGymnasiumQSS.Student " +
                                            "(idStudent INTEGER NOT NULL AUTO_INCREMENT, " +
                                            " idClasses INTEGER NOT NULL, " +
                                            " NameStudent VARCHAR(45) NOT NULL, " +
                                            " PRIMARY KEY (idStudent), " +
                                            " FOREIGN KEY (idClasses) REFERENCES " +
                                            " SchoolGymnasiumQSS.Classes (idClasses))";
    private static final String query3 = "CREATE TABLE SchoolGymnasiumQSS.Teachers " +
                                            "(idTeachers INTEGER NOT NULL AUTO_INCREMENT, " +
                                            " NameTeachers0 VARCHAR(45) NOT NULL, " +
                                            " NameTeachers1 VARCHAR(45) NOT NULL, " +
                                            " NameTeachers2 VARCHAR(45) NOT NULL, " +
                                            " Login VARCHAR(45) NOT NULL, " +
                                            " Password VARCHAR(45) NOT NULL, " +
                                            " Level INTEGER NOT NULL, " +
                                            " SelectedLanguage INTEGER NOT NULL, " +
                                            " PRIMARY KEY (idTeachers))";
    private static final String query4 = "CREATE TABLE SchoolGymnasiumQSS.Subject " +
                                            "(idSubject INTEGER NOT NULL AUTO_INCREMENT, " +
                                            " idClasses INTEGER NOT NULL, " +
                                            " Subject VARCHAR(45) NOT NULL, " +
                                            " PRIMARY KEY (idSubject), " +
                                            " FOREIGN KEY (idClasses) REFERENCES " +
                                            " SchoolGymnasiumQSS.Classes (idClasses))";
    private static final String query5 = "CREATE TABLE SchoolGymnasiumQSS.Marks " +
                                            "(idMarks INTEGER NOT NULL AUTO_INCREMENT, " +
                                            " idStudent INTEGER NOT NULL, " +
                                            " idClasses INTEGER NOT NULL, " +
                                            " idSubject INTEGER NOT NULL, " +
                                            " Marks INTEGER NOT NULL, " +
                                            " PRIMARY KEY (idMarks), " +
                                            " FOREIGN KEY (idStudent) REFERENCES " +
                                            " SchoolGymnasiumQSS.Student (idStudent), " +
                                            " FOREIGN KEY (idClasses) REFERENCES " +
                                            " SchoolGymnasiumQSS.Classes (idClasses), " +
                                            " FOREIGN KEY (idSubject) REFERENCES " +
                                            " SchoolGymnasiumQSS.Subject (idSubject))";

    private static String URL, USERNAME, PASSWORD;
    private static int SelectLanguage = 1;
    private static ResourceBundle bundle = null;
    private static Teachers teachers = null;
    private static SchoolQSSPanel panel = null;
    private static JFrame frame = null;
    private static JMenuBar menuBar = null;
    private static DataBaseProcessor db = null;
    private static Connection connection = null;
    private SchoolQSS(String s) {
        frame = new JFrame(s);
        Boolean LanguageFlag = true;
        File DataFile = new File(DataBaseResource);
        while (true) {
            try {
                FileInputStream fis = new FileInputStream(DataFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                DataBaseResourseIO getData = (DataBaseResourseIO) ois.readObject();
                ois.close();
                fis.close();
                EncryptDecryptMode encryptDecryptMode = new EncryptDecryptMode();
                URL = getData.getURL();
                USERNAME = encryptDecryptMode.decrypt(getData.getUSERNAME());
                PASSWORD = encryptDecryptMode.decrypt(getData.getPASSWORD());
                //System.out.println(PASSWORD);
                Connection connection = new DataBaseProcessor().getConnection(URL, USERNAME, PASSWORD);
                connection.close();
                break;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                    BadPaddingException | IllegalBlockSizeException | SQLException | ClassNotFoundException | IOException ex) {
                SelectYourLanguage(true);
                JOptionPane.showMessageDialog(null, "Не вдалося встановити з'єднання з базою даних.\n" + ex,
                        "Помилка", JOptionPane.ERROR_MESSAGE);
                LanguageFlag = false;
                new getDataBaseResource(DataBaseResource, frame, bundle, true);
            }
        }
        try {
            db = new DataBaseProcessor();
            connection = db.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            boolean IsDatabase = false;
            ResultSet rs = statement.executeQuery("SHOW DATABASES;");
            while(rs.next()){
                if(rs.getString("Database").equals("SchoolGymnasiumQSS")){
                    IsDatabase = true;
                    break;
                }
            }
            rs.close();
            if (!IsDatabase) {
                if (LanguageFlag) SelectYourLanguage(true);
                new CreateTeacher(frame, connection, query0, query1, query2, query3, query4, query5, bundle, SelectLanguage, 16, true);
            }
        } catch (SQLException e) {
            new dataError(e, true);
        }
        TestClasses testClasses = null;
        try {
            connection = db.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            if (bundle == null) bundle = ResourceBundle.getBundle("resource", new Locale(BundleResource[1][SelectLanguage]));
            teachers = new getUserData(frame, statement, bundle, true).getTeachers();
            bundle = ResourceBundle.getBundle("resource", new Locale(BundleResource[1][teachers.getSelectedLanguage()]));
            testClasses = new TestClasses(frame, connection, teachers, bundle);
            frame.setTitle("SchoolQSS " + teachers.getNameTeachers0() + " " + teachers.getNameTeachers1() + " " +
                    teachers.getNameTeachers2() + " " + testClasses.getAcademicYear() + " " + testClasses.getSemester());
            if (!testClasses.isBool()) {
                connection.close();
                System.exit(0);
            }
        } catch (SQLException e) {
            new dataError(e, true);
        }
        menuBar = new JMenuBar();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,600);
        frame.setResizable(false);
        panel  = new SchoolQSSPanel(frame, connection, bundle);
        frame.add(panel, BorderLayout.CENTER);
        panel.init();
        panel.setData(testClasses.getAcademicYear(), testClasses.getSemester());
        panel.RedefineData(teachers.getLevel());
        menuBar.add(createFileMenu());
        if (teachers.getLevel() > 14) menuBar.add(createRootMenu());
        menuBar.add(createHelpMenu());
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private static void SelectYourLanguage(Boolean bool) {
        String getLocale = (String) JOptionPane.showInputDialog(null,
                "Select your language", "Language",
                JOptionPane.QUESTION_MESSAGE, null, BundleResource[0], BundleResource[0][SelectLanguage]);
        if(getLocale != null) {
            for (int i = 0; i < BundleResource[0].length; i++) {
                if (BundleResource[0][i].equals(getLocale)) {
                    bundle = null;
                    bundle = ResourceBundle.getBundle("resource", new Locale(BundleResource[1][i]));
                    SelectLanguage = i;
                    break;
                }
            }
        } else if (bool) System.exit(0);
    }

    private  JMenu createRootMenu() {
        JMenu RootMenu = new JMenu("ROOT");
        JMenuItem AcSemester = new JMenuItem(new SemesterAction());
        JMenuItem addUser = new JMenuItem(new addUserAction());
        JMenuItem addSemester = new JMenuItem(new addSemesterAction());
        RootMenu.add(AcSemester);
        RootMenu.addSeparator();
        RootMenu.add(addUser);
        RootMenu.addSeparator();
        RootMenu.add(addSemester);
        return RootMenu;
    }

    class SemesterAction extends AbstractAction {
        SemesterAction() {
            putValue(NAME, "Зробити цей семестр поточним");
        }
        public void actionPerformed(ActionEvent e) {
            try {
                if (JOptionPane.showConfirmDialog(null,
                        "Зробити цей семестр поточним?", "Параметри семестру",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select AcademicYear, Semester from SchoolGymnasiumQSS.Classes " +
                            "where AcademicYear = " + panel.getAcademicYear() + " and Semester = " +
                            panel.getSemester() + " and Boolean = 1 limit 1");
                    if (!resultSet.next()) {
                        resultSet.close();
                        statement.close();
                        String insert_ = "update SchoolGymnasiumQSS.Classes set Boolean = 0 where  Boolean = 1";
                        String insert = "update SchoolGymnasiumQSS.Classes set Boolean = 1 where  AcademicYear = " +
                                panel.getAcademicYear() + " and Semester = " + panel.getSemester();
                        statement = connection.createStatement();
                        connection.setAutoCommit(false);
                        statement.addBatch(insert_);
                        statement.addBatch(insert);
                        statement.executeBatch();
                        connection.commit();
                        statement.clearBatch();
                        statement.close();
                        connection.setAutoCommit(true);
                    } else {
                        resultSet.close();
                    }
                }
            } catch (SQLException e1) {
                new dataError(e1, false);
            }
        }
    }

    class addUserAction extends AbstractAction {
        addUserAction() {
            putValue(NAME, "Додати користувача");
        }
        public void actionPerformed(ActionEvent e) {
            try {
                //db = new DataBaseProcessor();
                connection = db.getConnection(URL, USERNAME, PASSWORD);
                new CreateTeacher(frame, connection, null, null, null, null,
                        null, null, bundle, SelectLanguage, teachers.getLevel() - 1, false);
            } catch (SQLException e1) {
                new dataError(e1, false);
            }
        }
    }

    class addSemesterAction extends AbstractAction {
        addSemesterAction() {
            putValue(NAME, "Додати семестр");
        }
        public void actionPerformed(ActionEvent e) {
            try {
            //connection = db.getConnection(URL, USERNAME, PASSWORD);
                CreateSemester createSemester = new CreateSemester(frame, bundle);
                if (createSemester.getBool()) {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select AcademicYear, Semester from SchoolGymnasiumQSS.Classes " +
                            "where AcademicYear = " + createSemester.getSelectedYear() + " and Semester = " +
                            createSemester.getSelectedSemester() +" limit 1");
                    if (resultSet.next()) {
                        resultSet.close();
                        statement.close();
                        JOptionPane.showMessageDialog(null, "Такий семестр вже існує!",
                                "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        resultSet.close();
                        CreateClass createClass = new CreateClass(frame, statement, bundle);
                        statement.close();
                        if (createClass.getBool()) {
                        String insert_ = "insert into SchoolGymnasiumQSS.Classes (ClassName, ClassTeacher," +
                                " AcademicYear, Semester, Boolean) values (?, ?, ?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insert_);
                        preparedStatement.setString(1, createClass.getFieldBABA());
                        preparedStatement.setString(2, createClass.getFieldDIDO());
                        preparedStatement.setInt(3, createSemester.getSelectedYear());
                        preparedStatement.setInt(4, createSemester.getSelectedSemester());
                            if (JOptionPane.showConfirmDialog(null,
                                    "Зробити цей семестр поточним?", "Параметри семестру",
                                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        preparedStatement.setInt(5, 3);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        String insert = "update SchoolGymnasiumQSS.Classes set Boolean = ? where  Boolean = ?";
                        preparedStatement = connection.prepareStatement(insert);
                        connection.setAutoCommit(false);
                        preparedStatement.setInt(1, 0);
                        preparedStatement.setInt(2, 1);
                        preparedStatement.addBatch();
                        preparedStatement.setInt(1, 1);
                        preparedStatement.setInt(2, 3);
                        preparedStatement.addBatch();
                        preparedStatement.executeBatch();
                        connection.setAutoCommit(true);
                        preparedStatement.close();
                        } else {
                                preparedStatement.setInt(5, 0);
                                preparedStatement.executeUpdate();
                                preparedStatement.close();
                            }
                            JOptionPane.showMessageDialog(null, "Семестр та клас успішно створино",
                                    "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                            frame.setTitle("SchoolQSS " + teachers.getNameTeachers0() + " " + teachers.getNameTeachers1() + " " +
                                    teachers.getNameTeachers2() + " " + createSemester.getSelectedYear() + " " + createSemester.getSelectedSemester());
                            panel.setData(createSemester.getSelectedYear(), createSemester.getSelectedSemester());
                            panel.RedefineData(teachers.getLevel());
                    }
                    }


            }
            } catch (SQLException e1) {
                new dataError(e1, false);
            }
        }
    }



    private JMenu createHelpMenu() {
        JMenu HelpMenu = new JMenu("Інструменти");
        JMenuItem Help = new JMenuItem(new HelpAction());
        JMenuItem Program = new JMenuItem(new ProgramAction());
        JMenuItem ClassTeacher = new JMenuItem(new ClassTeacherAction());
        HelpMenu.add(Help);
        HelpMenu.addSeparator();
        HelpMenu.add(Program);
        HelpMenu.addSeparator();
        HelpMenu.add(ClassTeacher);
        return HelpMenu;
    }


    private JMenu createFileMenu() {
        JMenu file = new JMenu("Файл");
        JMenuItem ChangeUser = new JMenuItem(new ChangeUserAction());
        JMenuItem Exit = new JMenuItem(new ExitAction());
        file.add(ChangeUser);
        file.addSeparator();
        file.add(Exit);
        return file;
    }

    class ChangeUserAction extends AbstractAction {
        ChangeUserAction() {
            putValue(NAME, "Змінити користувача");
        }
        public void actionPerformed(ActionEvent e) {
            try {
               // db = new DataBaseProcessor();
                connection = db.getConnection(URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
                getUserData UserData = new getUserData(frame, statement, bundle, false);
                if (UserData.getBool()) {
                    teachers = null;
                    teachers = UserData.getTeachers();
                    bundle = ResourceBundle.getBundle("resource", new Locale(BundleResource[1][teachers.getSelectedLanguage()]));
                    TestClasses testClasses = new TestClasses(frame, connection, teachers, bundle);
                    frame.setTitle("SchoolQSS " + teachers.getNameTeachers0() + " " + teachers.getNameTeachers1() + " " +
                            teachers.getNameTeachers2() + " " + testClasses.getAcademicYear() + " " + testClasses.getSemester());
                    frame.setVisible(false);
                    frame.remove(menuBar);
                    menuBar = new JMenuBar();
                    menuBar.add(createFileMenu());
                    if (teachers.getLevel() > 14) menuBar.add(createRootMenu());
                    menuBar.add(createHelpMenu());
                    frame.setJMenuBar(menuBar);
                    panel.setData(testClasses.getAcademicYear(), testClasses.getSemester());
                    panel.RedefineData(teachers.getLevel());
                    frame.pack();
                    frame.setVisible(true);
                }
                statement.close();
            } catch (SQLException e1) {
                new dataError(e1, false);
            }
        }
    }


    class ExitAction extends AbstractAction {
        ExitAction() {
            putValue(NAME, "Вихід");
        }
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class ProgramAction extends AbstractAction {
        ProgramAction() {
            putValue(NAME, "Вибрати навчальний рік");
        }
        public void actionPerformed(ActionEvent e) {
            GetSemester getSemester = new GetSemester(connection, bundle, frame);
            if (getSemester.getBool()) {
                frame.setTitle("SchoolQSS " + teachers.getNameTeachers0() + " " + teachers.getNameTeachers1() + " " +
                        teachers.getNameTeachers2() + " " + getSemester.getSelectedYear() + " " + getSemester.getSelectedSemester());
                panel.setData(getSemester.getSelectedYear(), getSemester.getSelectedSemester());
                panel.RedefineData(teachers.getLevel());
            }
        }
    }

    class ClassTeacherAction extends AbstractAction {
        ClassTeacherAction() {
            putValue(NAME, "Перейменувати класного керівника");
        }
        public void actionPerformed(ActionEvent e) {
            panel.ClassTeacherAction();
        }
    }

    class HelpAction extends AbstractAction {
        HelpAction() {
            putValue(NAME, "Змінити мову");
        }
        public void actionPerformed(ActionEvent e) {
            SelectYourLanguage(false);
              // frame.setVisible(false);

              // frame.remove(menuBar);


          //  menuBar = new JMenuBar();

            // menuBar.add(createFileMenu());
           // menuBar.add(createHelpMenu());
             //  frame.setJMenuBar(menuBar);
                panel.ChangeLanguage("Test ChangeLanguage");
             //  frame.pack();
              // frame.setVisible(true);
            }
        }



    public static void main(String[] args) throws SQLException {

        new SchoolQSS("SchoolQSS");



    }
}
