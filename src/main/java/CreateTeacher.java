import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CreateTeacher  extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPasswordField PassWordPasswordField0, PassWordPasswordField1;
    private boolean TestLogin = true;

    public CreateTeacher(Frame frame, final Connection connection, final String query0,
                         final String query1, final String query2, final String query3,
                         final String query4, final String query5, ResourceBundle bundle,
                         final int selectLanguage, int index, final boolean bool) {

        super(frame, "Створити нового користувача",true);
        setSize(600, 200);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLayout(new GridBagLayout());
        final JLabel UserNameLabel0 = new JLabel("Прізвище");
        final JLabel UserNameLabel1 = new JLabel("Ім'я");
        final JLabel UserNameLabel2 = new JLabel("По батькові");
        final JLabel AccessLevel = new JLabel("Рівень доступу");
        final JLabel UserNameLabel = new JLabel("Логін");
        final JLabel PassWordLabel0 = new JLabel("Пароль");
        final JLabel PassWordLabel1 = new JLabel("Підтвердити пароль");
        final JTextField UserNameTextField = new JTextField(35);
        final JTextField UserNameTextField0 = new JTextField(35);
        final JTextField UserNameTextField1 = new JTextField(35);
        final JTextField UserNameTextField2 = new JTextField(35);
        UserNameTextField0.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() < 40) { super.insertString(offs, str, a); }}
        });
        UserNameTextField1.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() < 40) { super.insertString(offs, str, a); }}});
        UserNameTextField2.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() < 40) { super.insertString(offs, str, a); }}
        });
        final JComboBox<Integer> AccessLevelComboBox = new JComboBox<Integer>();
        for (int i = index; i >= 0; AccessLevelComboBox.addItem(i--));
        if (bool) AccessLevelComboBox.setEnabled(false);
        PassWordPasswordField0 = new JPasswordField(35);
        PassWordPasswordField1 = new JPasswordField(35);
        JButton OkKey = new JButton(bundle.getString("OkKey"));
        JButton CancelKey = new JButton(bundle.getString("CancelKey"));
        JPanel ButtonsPanel = new JPanel(new GridBagLayout());
        OkKey.addActionListener(e -> {
            if((!UserNameTextField.getText().trim().isEmpty()) && (!UserNameTextField0.getText().trim().isEmpty())
                    && (!UserNameTextField1.getText().trim().isEmpty()) && (!UserNameTextField2.getText().trim().isEmpty())) {
                if (Arrays.equals(PassWordPasswordField0.getPassword(), PassWordPasswordField1.getPassword())) {
                    if (PassWordPasswordField0.getPassword().length > 0) {
                        try {
                            if (bool) {
                                Statement statement = connection.createStatement();
                                connection.setAutoCommit(false);
                                statement.addBatch(query0);
                                statement.addBatch(query1);
                                statement.addBatch(query2);
                                statement.addBatch(query3);
                                statement.addBatch(query4);
                                statement.addBatch(query5);
                                statement.executeBatch();
                                connection.commit();
                                statement.clearBatch();
                                statement.close();
                                connection.setAutoCommit(true);
                            }
                            if (!bool) {
                                Statement statement = connection.createStatement();
                                ResultSet resSet = statement.executeQuery("select Login from SchoolGymnasiumQSS.Teachers where Login = \"" +
                                        new MD5CheckSum().getHash(UserNameTextField.getText()) + "\"");
                                TestLogin = !resSet.next();
                                resSet.close();
                                statement.close();
                            }
                            if (TestLogin) {
                            String insert = "insert into SchoolGymnasiumQSS.Teachers (NameTeachers0, NameTeachers1," +
                                    " NameTeachers2, Login, Password, Level, SelectedLanguage) values (?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement preparedStatement = connection.prepareStatement(insert);
                            MD5CheckSum MD5 = new MD5CheckSum();
                            preparedStatement.setString(1, UserNameTextField0.getText());
                            preparedStatement.setString(2, UserNameTextField1.getText());
                            preparedStatement.setString(3, UserNameTextField2.getText());
                            preparedStatement.setString(4, MD5.getHash(UserNameTextField.getText()));
                            preparedStatement.setString(5, MD5.getHash(String.valueOf(PassWordPasswordField0.getPassword())));
                            preparedStatement.setInt(6, (Integer) AccessLevelComboBox.getSelectedItem());
                            preparedStatement.setInt(7, selectLanguage);
                            preparedStatement.execute();
                            preparedStatement.close();
                            //connection.close();
                            JOptionPane.showMessageDialog(null,
                                    UserNameLabel0.getText() + ": " + UserNameTextField0.getText() + "\n" +
                                            UserNameLabel1.getText() + ": " + UserNameTextField1.getText() + "\n" +
                                            UserNameLabel2.getText() + ": " + UserNameTextField2.getText() + "\n" +
                                            AccessLevel.getText() + ": " + AccessLevelComboBox.getSelectedItem() + "\n" +
                                            UserNameLabel.getText() + ": " + UserNameTextField.getText() + "\n" +
                                            PassWordLabel0.getText() + ": " + String.valueOf(PassWordPasswordField0.getPassword()),
                                    "Користувача створено", JOptionPane.INFORMATION_MESSAGE);
                            setVisible(false);
                            } else {
                                JOptionPane.showMessageDialog(null, "Користувач з логіном " +
                                                UserNameTextField.getText() + " вже існує!",
                                        "Помилка", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, "У програмі виникла внутріння помилка\n" + e1,
                                    "Помилка! Author Roman Butsiy", JOptionPane.ERROR_MESSAGE);
                            if (bool) System.exit(0);
                            else setVisible(false);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Введіть пароль",
                                "Помилка", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Паролі не співпадають",
                            "Помилка", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Заповніть всі поля",
                        "Помилка", JOptionPane.WARNING_MESSAGE);
            }
        });
        CancelKey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (bool) {
                    try {
                        connection.close();
                    } catch (SQLException e1) {}
                    System.exit(0);
                } else setVisible(false);
            }
        });
        add(UserNameLabel0, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameTextField0, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameLabel1, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameTextField1, new GridBagConstraints(1, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameLabel2, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameTextField2, new GridBagConstraints(1, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(AccessLevel, new GridBagConstraints(0, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(AccessLevelComboBox, new GridBagConstraints(1, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameLabel, new GridBagConstraints(0, 4, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameTextField, new GridBagConstraints(1, 4, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordLabel0, new GridBagConstraints(0, 5, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordPasswordField0, new GridBagConstraints(1, 5, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordLabel1, new GridBagConstraints(0, 6, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordPasswordField1, new GridBagConstraints(1, 6, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        ButtonsPanel.add(CancelKey, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        ButtonsPanel.add(OkKey, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(ButtonsPanel, new GridBagConstraints(0, 7, 2, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
