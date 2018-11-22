import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


public class getUserData extends JDialog implements ActionListener {

    private boolean bool, eBool = true;
    private JTextField UserNameTextField;
    private JPasswordField PassWordPasswordField;
    private Statement statement;
    private JButton OkKey, CancelKey;
    private Teachers teachers;

    public getUserData(Frame frame, Statement statement, ResourceBundle bundle, boolean bool) {
        super(frame, "Автентифікація",true);
        setSize(600, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLayout(new GridBagLayout());
        this.bool = bool;
        this.statement = statement;
        JLabel UserNameLabel = new JLabel("Логін");
        JLabel PassWordLabel = new JLabel("Пароль");
        UserNameTextField = new JTextField(35);
        PassWordPasswordField = new JPasswordField(35);
        OkKey = new JButton(bundle.getString("OkKey"));
        CancelKey = new JButton(bundle.getString("CancelKey"));
        JPanel ButtonsPanel = new JPanel(new GridBagLayout());
        OkKey.addActionListener(this);
        CancelKey.addActionListener(this);
        add(UserNameLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameTextField, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordLabel, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordPasswordField, new GridBagConstraints(1, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        ButtonsPanel.add(CancelKey, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        ButtonsPanel.add(OkKey, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(ButtonsPanel, new GridBagConstraints(0, 2, 2, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == OkKey) {
            try {
                if ((!UserNameTextField.getText().trim().isEmpty()) && (PassWordPasswordField.getPassword().length > 0)){
                    ResultSet resSet = statement.executeQuery("select * from SchoolGymnasiumQSS.Teachers where Login = \"" +
                            new MD5CheckSum().getHash(UserNameTextField.getText()) + "\" and Password = \"" +
                            new MD5CheckSum().getHash(String.valueOf(PassWordPasswordField.getPassword())) + "\"");
                    if (resSet.next()) {
                            teachers = new Teachers(resSet.getInt("idTeachers"),
                                    resSet.getString("NameTeachers0"), resSet.getString("NameTeachers1"),
                                    resSet.getString("NameTeachers2"), resSet.getString("Login"),
                                    resSet.getString("Password"), resSet.getInt("Level"),
                                    resSet.getInt("SelectedLanguage"));
                            resSet.close();
                            statement.close();
                            eBool = true;
                            setVisible(false);
                    } else {
                        resSet.close();
                        JOptionPane.showMessageDialog(null, "Невірний логін або пароль!",
                                "Помилка", JOptionPane.ERROR_MESSAGE);
                    }
            } else {
                JOptionPane.showMessageDialog(null, "Введіть логін або пароль!",
                        "Помилка", JOptionPane.ERROR_MESSAGE);
            }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "У програмі виникла внутріння помилка\n" + e,
                        "Помилка! Author Roman Butsiy", JOptionPane.ERROR_MESSAGE);
                if (bool) System.exit(0);
                else {
                    eBool = false;
                    setVisible(false);
                }
            }
        }
        if (event.getSource() == CancelKey) {
            if (bool) {
                System.exit(0);
            } else {
                eBool = false;
                setVisible(false);
            }
        }
    }

    public Teachers getTeachers(){
        return teachers;
    }

    public boolean getBool(){
        return eBool;
    }
}
