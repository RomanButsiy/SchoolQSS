import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SchoolQSSPanel extends JPanel implements ActionListener, ItemListener {
    private Connection connection;
    private ResourceBundle bundle;
    private QSSTableModel qsstm = new QSSTableModel();
    private JTable QSSTable = new JTable(qsstm);
    private JButton b1, b2, b3, b5, b6, b7, b8, b9, b10, b11, b12, b13;
    private JLabel l1, l2, l3, l4, l5, l6, l7;
    private JComboBox<String> cb1, cb2, cb3;
    private final int MaximumRowCount = 10;
    private int Level, AcademicYear, Semester, idMark;
    private Statement statement = null;
    private Frame frame;
    private Boolean ResultSetIsClosed = true;
    private ArrayList<Student> student = new ArrayList<Student>();
    private ArrayList<Subject> subject = new ArrayList<Subject>();
    private ArrayList<Classes> classes = new ArrayList<Classes>();

    public SchoolQSSPanel(Frame frame, Connection connection, ResourceBundle bundle) {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<Integer> stringList = new ArrayList<>();
        stringList.stream().forEach(s-> System.out.println(s));
        this.frame = frame;
        this.connection = connection;
        this.bundle = bundle;
        setLayout(new GridBagLayout());
        cb1 = new JComboBox<String>();
        cb2 = new JComboBox<String>();
        cb3 = new JComboBox<String>();
        b1 = new JButton("Перейменувати клас");
        b2 = new JButton("Додати клас");
        b3 = new JButton("Видалити клас");
        b5 = new JButton("Перейменувати предмет");
        b6 = new JButton("Додати предмет");
        b7 = new JButton("Видалити предмет");
        b8 = new JButton("Перейменувати учня");
        b9 = new JButton("Додати учня");
        b10 = new JButton("Видалити учня");
        b11 = new JButton("Змінити оцінку");
       // b12 = new JButton("Розрахувати ПЯУ");
        b13 = new JButton("Оцінки з предмету");
        l1 = new JLabel("Клас");
        l2 = new JLabel("Предмет ");
        l3 = new JLabel("Учень ");
        l4 = new JLabel("Оцінка ");
        l5 = new JLabel(" NULL ");
        l6 = new JLabel("Класний керівник: ");
        l7 = new JLabel();
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);
        b7.addActionListener(this);
        b8.addActionListener(this);
        b9.addActionListener(this);
        b10.addActionListener(this);
        b11.addActionListener(this);
      //  b12.addActionListener(this);
        b13.addActionListener(this);
        cb1.addItemListener(this);
        cb2.addItemListener(this);
        cb3.addItemListener(this);
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.DESELECTED) return;
        if(event.getSource()==cb1){
            b3.setEnabled(true);
            l7.setText(classes.get(cb1.getSelectedIndex()).getClassTeacher());
            getResultSet2();
            getResultSet3();
        }
        if(event.getSource()==cb2){
            idMark = -1;
            l5.setText("NULL");
            ShowMark();
        }
        if(event.getSource()==cb3){
            idMark = -1;
            l5.setText("NULL");
            ShowMark();
        }

    }

    public void actionPerformed(ActionEvent event) {
        if (classes.isEmpty()) return;
        if (event.getSource() == b1) {
            try {
                CreateAll createAll = new CreateAll(frame, "Введіть нову назву класу", "Перейменувати клас");
                if (createAll.getBool()) {
                    if (createAll.getData().equals(classes.get(cb1.getSelectedIndex()).getClassName())) return;
                    String insert = "update SchoolGymnasiumQSS.Classes set ClassName = ? where  idClasses = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.setString(1, createAll.getData());
                    preparedStatement.setInt(2,  classes.get(cb1.getSelectedIndex()).getIdClasses());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    getResultSet1();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == b2) {
            try {
                    CreateClass createClass = new CreateClass(frame, statement, bundle);
                    if (createClass.getBool()) {
                        String insert = "insert into SchoolGymnasiumQSS.Classes (ClassName, ClassTeacher," +
                                " AcademicYear, Semester, Boolean) values (?, ?, ?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insert);
                        preparedStatement.setString(1, createClass.getFieldBABA());
                        preparedStatement.setString(2, createClass.getFieldDIDO());
                        preparedStatement.setInt(3,  classes.get(0).getAcademicYear());
                        preparedStatement.setInt(4, classes.get(0).getSemester());
                        preparedStatement.setInt(5, classes.get(0).getBool());
                        preparedStatement.execute();
                        preparedStatement.close();
                        JOptionPane.showMessageDialog(null, "Клас успішно створино",
                                "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                        getResultSet1();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == b3) {
            if (cb1.getItemCount() == 1) {
                b3.setEnabled(false);
                return;
            }
            int result = JOptionPane.showConfirmDialog(null,
                    "Ви впевнен, що хочете видалити " + cb1.getSelectedItem() + "?", "Видалити клас",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(JOptionPane.YES_OPTION == result){
                try {
                    PreparedStatement preparedStatement;
                        if (!subject.isEmpty() && !student.isEmpty()) {
                            preparedStatement = connection.prepareStatement("delete from SchoolGymnasiumQSS.Marks where idClasses = ?");
                            preparedStatement.setInt(1,  classes.get(cb1.getSelectedIndex()).getIdClasses());
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                        }
                        if (!subject.isEmpty()) {
                            preparedStatement = connection.prepareStatement("delete from SchoolGymnasiumQSS.Subject where idClasses = ?");
                            preparedStatement.setInt(1,  classes.get(cb1.getSelectedIndex()).getIdClasses());
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                        }
                        if (!student.isEmpty()) {
                            preparedStatement = connection.prepareStatement("delete from SchoolGymnasiumQSS.Student where idClasses = ?");
                            preparedStatement.setInt(1,  classes.get(cb1.getSelectedIndex()).getIdClasses());
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                        }
                    preparedStatement = connection.prepareStatement("delete from SchoolGymnasiumQSS.Classes where idClasses = ?");
                    preparedStatement.setInt(1,  classes.get(cb1.getSelectedIndex()).getIdClasses());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    getResultSet1();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (event.getSource() == b5) {
            if (subject.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Предмет не вибрано",
                        "Помилка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                CreateAll createAll = new CreateAll(frame, "Введіть нову назву предмета", "Перейменувати предмет");
                if (createAll.getBool()) {
                    if (createAll.getData().equals(subject.get(cb2.getSelectedIndex()).getSubject())) return;
                    String insert = "update SchoolGymnasiumQSS.Subject set Subject = ? where  idSubject = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.setString(1, createAll.getData());
                    preparedStatement.setInt(2,  subject.get(cb2.getSelectedIndex()).getIdSubject());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    getResultSet2();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == b6) {
            CreateAll createAll = new CreateAll(frame, "Введіть назву предмета", "Додати предмет");
                if (createAll.getBool()) {
                    try {
                        String insert = "insert into SchoolGymnasiumQSS.Subject (idClasses, Subject) values (?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insert);
                        preparedStatement.setInt(1, classes.get(cb1.getSelectedIndex()).getIdClasses());
                        preparedStatement.setString(2, createAll.getData());
                        preparedStatement.execute();
                        preparedStatement.close();
                        JOptionPane.showMessageDialog(null, "Предемет успішно додано",
                                "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                        getResultSet2();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

        }
        if (event.getSource() == b7) {
            if (subject.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Предмет не вибрано",
                        "Помилка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int result = JOptionPane.showConfirmDialog(null,
                    "Ви впевнен, що хочете предмет " + cb2.getSelectedItem() + "?", "Видалити предмет",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(JOptionPane.YES_OPTION == result){
                try {
                    PreparedStatement preparedStatement;
                    if (!subject.isEmpty() && !student.isEmpty()) {
                        preparedStatement = connection.prepareStatement("delete from SchoolGymnasiumQSS.Marks where idSubject = ?");
                        preparedStatement.setInt(1,  subject.get(cb2.getSelectedIndex()).getIdSubject());
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    }
                        preparedStatement = connection.prepareStatement("delete from SchoolGymnasiumQSS.Subject where idSubject = ?");
                        preparedStatement.setInt(1,  subject.get(cb2.getSelectedIndex()).getIdSubject());
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    l5.setText("NULL");
                    subject.remove(cb2.getSelectedIndex());
                    cb2.removeAllItems();
                    getResultSetCB2();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (event.getSource() == b8) {
            if (student.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Учня не вибрано",
                        "Помилка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                CreateAll createAll = new CreateAll(frame, "Введіть нові прізвеще та ініціали учня", "Перейменувати учня");
                if (createAll.getBool()) {
                    if (createAll.getData().equals(student.get(cb3.getSelectedIndex()).getNameStudent())) return;
                    String insert = "update SchoolGymnasiumQSS.Student set NameStudent = ? where  idStudent = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.setString(1, createAll.getData());
                    preparedStatement.setInt(2,  student.get(cb3.getSelectedIndex()).getIdStudent());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    getResultSet3();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == b9) {
            CreateAll createAll = new CreateAll(frame, "Введіть прізвище та ініціали учня", "Додати учня");
                if (createAll.getBool()) {
                    try {
                        String insert = "insert into SchoolGymnasiumQSS.Student (idClasses, NameStudent) values (?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.setInt(1, classes.get(cb1.getSelectedIndex()).getIdClasses());
                    preparedStatement.setString(2, createAll.getData());
                    preparedStatement.execute();
                    preparedStatement.close();
                    JOptionPane.showMessageDialog(null, "Учня успішно створино",
                            "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                    getResultSet3();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                }
        }
        if (event.getSource() == b10) {
            if (student.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Учня не вибрано",
                        "Помилка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int resultName = JOptionPane.showConfirmDialog(null,
                    "Ви впевнен, що хочете видалити " + cb3.getSelectedItem() + "?", "Видалити учня",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(JOptionPane.YES_OPTION == resultName){
                try {
                    PreparedStatement preparedStatement;
                    if (!subject.isEmpty() && !student.isEmpty()) {
                        preparedStatement = connection.prepareStatement("delete from SchoolGymnasiumQSS.Marks where idStudent = ?");
                        preparedStatement.setInt(1,  student.get(cb3.getSelectedIndex()).getIdStudent());
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    }
                    preparedStatement = connection.prepareStatement("delete from SchoolGymnasiumQSS.Student where idStudent = ?");
                    preparedStatement.setInt(1,  student.get(cb3.getSelectedIndex()).getIdStudent());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    l5.setText("NULL");
                    student.remove(cb3.getSelectedIndex());
                    cb3.removeAllItems();
                    getResultSetCB3();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (event.getSource() == b11) {
            if (subject.isEmpty() || student.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Учня або предмет не вибрано",
                        "Помилка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] pizzas = { "12", "11", "10", "9", "8", "7", "6", "5", "4", "3", "2", "1", "0"};
            String favoritePizza = (String) JOptionPane.showInputDialog(null,
                    "Виберіть оцінку", "Змінити оцінку",
                    JOptionPane.QUESTION_MESSAGE, null, pizzas, pizzas[0]);
            if (favoritePizza == null) return;
            try {
            if (idMark == -1) {
                    String insert = "insert into SchoolGymnasiumQSS.Marks (idStudent, idClasses, idSubject, Marks) values (?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insert);
                    preparedStatement.setInt(1, student.get(cb3.getSelectedIndex()).getIdStudent());
                    preparedStatement.setInt(2, classes.get(cb1.getSelectedIndex()).getIdClasses());
                    preparedStatement.setInt(3, subject.get(cb2.getSelectedIndex()).getIdSubject());
                    preparedStatement.setInt(4, Integer.parseInt(favoritePizza));
                    preparedStatement.execute();
                    preparedStatement.close();
                    ShowMark();
            } else {
                statement.executeUpdate("update SchoolGymnasiumQSS.Marks set Marks = " +
                        Integer.parseInt(favoritePizza) + " where  idMarks = " + idMark);
                l5.setText(favoritePizza);
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == b12) {

        }
        if (event.getSource() == b13) {
// Оцінки з предмету
            if (subject.isEmpty() || student.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Учня або предмет не вибрано",
                        "Помилка", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                ResultSet resultSet = statement.executeQuery("select NameStudent, Marks from " +
                        "SchoolGymnasiumQSS.Marks INNER JOIN SchoolGymnasiumQSS.Student ON " +
                        "(SchoolGymnasiumQSS.Marks.idStudent = SchoolGymnasiumQSS.Student.idStudent) where " +
                        "SchoolGymnasiumQSS.Marks.idSubject = " + subject.get(cb2.getSelectedIndex()).getIdSubject() +
                        " and SchoolGymnasiumQSS.Marks.idClasses = " + classes.get(cb1.getSelectedIndex()).getIdClasses() +
                        " order by NameStudent asc");
                String[][] data = new String[student.size()][2];
                for (int i = 0; i < data.length; i++) {
                    if (!resultSet.next()) break;
                    data[i][0] = resultSet.getString("NameStudent");
                    data[i][1] = resultSet.getString("Marks");
                }
                new Thread(new AllMarks(subject.get(cb2.getSelectedIndex()).getSubject(), data)).start();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

//    statement.executeUpdate("update SchoolGymnasiumQSS." + idMarks + " set idStudent = " +
//    LAST_INSERT_ID + " where  idStudent = -1 and idClass = " + classes.getIdClasses())

    public void getResultSet1() {
        classes.clear();
        cb1.removeAllItems();
        try {
            ResultSet resultSet = statement.executeQuery("select * from SchoolGymnasiumQSS.Classes " +
                    "where AcademicYear = " + AcademicYear + " and Semester = " + Semester +" order by ClassName asc");
            while(resultSet.next()) {
                classes.add(new Classes(
                        resultSet.getInt("idClasses"),
                        resultSet.getString("ClassName"),
                        resultSet.getString("ClassTeacher"),
                        resultSet.getInt("AcademicYear"),
                        resultSet.getInt("Semester"),
                        resultSet.getInt("Boolean")
                ));
            }
            resultSet.close();
            getResultSetCB1();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ShowMark() {
        if (subject.isEmpty() || student.isEmpty()) return;
        try {
            ResultSet resultSet = statement.executeQuery("select idMarks, Marks from SchoolGymnasiumQSS.Marks " +
                    "where idStudent = " + student.get(cb3.getSelectedIndex()).getIdStudent() + " and idSubject = " +
                    subject.get(cb2.getSelectedIndex()).getIdSubject() +" limit 1");
            if (resultSet.next()) {
                idMark = resultSet.getInt("idMarks");
                l5.setText(String.valueOf(resultSet.getInt("Marks")));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getResultSet2() {
        cb2.removeAllItems();
        subject.clear();
        if (classes.isEmpty()) return;
        try {
            ResultSet resultSet = statement.executeQuery("select * from SchoolGymnasiumQSS.Subject " +
                    "where idClasses = " + classes.get(cb1.getSelectedIndex()).getIdClasses() + " order by Subject asc");
            while(resultSet.next()) {
            subject.add(new Subject(
                    resultSet.getInt("idSubject"),
                    resultSet.getInt("idClasses"),
                    resultSet.getString("Subject")));
            }
            resultSet.close();
            getResultSetCB2();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getResultSet3() {
        student.clear();
        cb3.removeAllItems();
        if (classes.isEmpty()) return;
        try {
            ResultSet resultSet = statement.executeQuery("select * from SchoolGymnasiumQSS.Student " +
                    "where idClasses = " + classes.get(cb1.getSelectedIndex()).getIdClasses() + " order by NameStudent asc");
            while(resultSet.next()) {
                student.add(new Student(
                        resultSet.getInt("idStudent"),
                        resultSet.getInt("idClasses"),
                        resultSet.getString("NameStudent")));
            }
            resultSet.close();
            getResultSetCB3();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getResultSetCB1() {
        if (classes.isEmpty()) return;
        for (Classes classs : classes) {
            cb1.addItem(cb1.getItemCount() + " - " + classs.getClassName());
        }
    }



    public void getResultSetCB2() {
        if (classes.isEmpty() || subject.isEmpty()) return;
        for (Subject subjects : subject) {
            cb2.addItem(cb2.getItemCount() + " - " + subjects.getSubject());
        }
    }

    public void getResultSetCB3() {
        if (classes.isEmpty() || student.isEmpty()) return;
        for (Student students : student) {
            cb3.addItem(cb3.getItemCount() + " - " + students.getNameStudent());
        }
    }

    public void RedefineData(int level) {
         getResultSet1();
         if (classes.isEmpty()) {
             AccessLevel(0);
             return;
         }
         if (level > 13) {
             AccessLevel(level);
         } else {
             AccessLevel(classes.get(0).getBool() * level);
         }
    }

    public void setData(int AcademicYear, int Semester) {
        this.AcademicYear = AcademicYear;
        this.Semester = Semester;
    }

    public void AccessLevel(int Level) {
        this.Level = Level;
        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b5.setEnabled(false);
        b6.setEnabled(false);
        b7.setEnabled(false);
        b8.setEnabled(false);
        b9.setEnabled(false);
        b10.setEnabled(false);
        b11.setEnabled(false);
        if (Level <= 4) {
            return;
        }
        if (Level <= 13) {
            b11.setEnabled(true);
            return;
        }
        b11.setEnabled(true);
        b1.setEnabled(true);
        b2.setEnabled(true);
        b3.setEnabled(true);
        b5.setEnabled(true);
        b6.setEnabled(true);
        b7.setEnabled(true);
        b8.setEnabled(true);
        b9.setEnabled(true);
        b10.setEnabled(true);

    }
    public void ClassTeacherAction() {
        if ((Level <= 4) || (classes.isEmpty())) return;
        try {
            CreateAll createAll = new CreateAll(frame, "Введіть нові прізвеще та ініціали класного керівника", "Перейменувати класного керівника");
            if (createAll.getBool()) {
                String insert = "update SchoolGymnasiumQSS.Classes set ClassTeacher = ? where  idClasses = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(insert);
                preparedStatement.setString(1, createAll.getData());
                preparedStatement.setInt(2,  classes.get(cb1.getSelectedIndex()).getIdClasses());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                l7.setText(createAll.getData());
                classes.get(cb1.getSelectedIndex()).setClassTeacher(createAll.getData());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getAcademicYear() {
        return AcademicYear;
    }

    public int getSemester() {
        return Semester;
    }

    public void ChangeLanguage(String text) {
        System.out.println(text);
    }

    public void init(){
        cb1.setMaximumRowCount(MaximumRowCount);
        cb2.setMaximumRowCount(MaximumRowCount);
        cb3.setMaximumRowCount(MaximumRowCount);
        cb1.setPreferredSize(new Dimension(200,25));
        cb2.setPreferredSize(new Dimension(200,25));
        cb3.setPreferredSize(new Dimension(200,25));
        JPanel panel = new JPanel();
        panel.add(l6, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        panel.add(l7, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(l1, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(cb1, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b1, new GridBagConstraints(2, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b2, new GridBagConstraints(3, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b3, new GridBagConstraints(4, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(l2, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(cb2, new GridBagConstraints(1, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b5, new GridBagConstraints(2, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b6, new GridBagConstraints(3, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b7, new GridBagConstraints(4, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(l3, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(cb3, new GridBagConstraints(1, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b8, new GridBagConstraints(2, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b9, new GridBagConstraints(3, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b10, new GridBagConstraints(4, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(l4, new GridBagConstraints(0, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(l5, new GridBagConstraints(1, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(b11, new GridBagConstraints(2, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
//        add(b12, new GridBagConstraints(3, 3, 1, 1, 1, 1,
//                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
//                new Insets(2, 2, 2, 2), 0, 0));
        add(b13, new GridBagConstraints(3, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(panel, new GridBagConstraints(0, 4, 5, 1, 0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));
        QSSTable.getTableHeader().setPreferredSize(new Dimension(0, 32));
        QSSTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        QSSTable.getColumnModel().getColumn(1).setPreferredWidth(35);
        QSSTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        QSSTable.getColumnModel().getColumn(3).setPreferredWidth(20);
        QSSTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        QSSTable.getColumnModel().getColumn(5).setPreferredWidth(20);
        QSSTable.getColumnModel().getColumn(6).setPreferredWidth(60);
        QSSTable.getColumnModel().getColumn(7).setPreferredWidth(20);
        QSSTable.getColumnModel().getColumn(8).setPreferredWidth(60);
        QSSTable.getColumnModel().getColumn(9).setPreferredWidth(20);
        QSSTable.getColumnModel().getColumn(10).setPreferredWidth(50);
        JScrollPane QSSTableScrollPane = new JScrollPane(QSSTable);
        QSSTableScrollPane.setPreferredSize(new Dimension(900, 400));
        add(QSSTableScrollPane,new GridBagConstraints(0, 5, 5, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));
    }


}
