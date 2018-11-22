import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;


public class getDataBaseResource extends JDialog {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String URL = "jdbc:mysql://localhost:3306/mysql?useSSL=false&useUnicode=true&characterEncoding=UTF8";
    private static final long serialVersionUID = 1L;
    JPasswordField PassWordPasswordField0, PassWordPasswordField1;

    public getDataBaseResource(final String DataBaseResource, Frame frame, ResourceBundle bundle, final boolean bool) {
        super(frame, bundle.getString("DBConnect"),true);
        setSize(600, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLayout(new GridBagLayout());
        JLabel UserNameLabel = new JLabel("Логін");
        JLabel PassWordLabel0 = new JLabel("Пароль");
        JLabel PassWordLabel1 = new JLabel("Підтвердити пароль");
        JLabel URLLabel = new JLabel("Адреса бази даних");
        JPanel ButtonsPanel = new JPanel(new GridBagLayout());
        final JTextField UserNameTextField = new JTextField(60);
        PassWordPasswordField0 = new JPasswordField(60);
        PassWordPasswordField1 = new JPasswordField(60);
        final JTextField URLTextField = new JTextField(60);
        final JCheckBox URLEnable = new JCheckBox("Редагувати адресу бази даних");
        JButton OkKey = new JButton(bundle.getString("OkKey"));
        JButton CancelKey = new JButton(bundle.getString("CancelKey"));
        URLTextField.setEnabled(false);
        URLTextField.setText(URL);
        UserNameTextField.setText(USERNAME);
        PassWordPasswordField0.setText(PASSWORD);
        PassWordPasswordField1.setText(PASSWORD);
        URLEnable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                URLTextField.setEnabled(URLEnable.isSelected());
                JOptionPane.showMessageDialog(null, "Зміна поля \"Адреса бази даних\" може призвести " +
                        "до пошкодження бази даних!!!",
                        "Увага!", JOptionPane.WARNING_MESSAGE);
            }
        });
        OkKey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!URLTextField.getText().trim().isEmpty()) {
                    if (!UserNameTextField.getText().trim().isEmpty()) {
                    if (Arrays.equals(PassWordPasswordField0.getPassword(), PassWordPasswordField1.getPassword())) {
                        if (PassWordPasswordField0.getPassword().length == 0) {
                            JOptionPane.showMessageDialog(null, "Введіть пароль",
                                    "Помилка", JOptionPane.WARNING_MESSAGE);
                        } else {
                            try {
                                DataBaseProcessor db = new DataBaseProcessor();
                                Connection connection = db.getConnection(URLTextField.getText(), UserNameTextField.getText(),
                                        String.valueOf(PassWordPasswordField0.getPassword()));
                                connection.close();
                                JOptionPane.showMessageDialog(null, "З'єднання з базою даних встановлено",
                                        "З'єднання", JOptionPane.INFORMATION_MESSAGE);
                                File DataFile = new File(DataBaseResource);
                                FileOutputStream fos;
                                ObjectOutputStream oos;
                                try {
                                    fos = new FileOutputStream(DataFile);
                                    oos = new ObjectOutputStream(fos);
                                    EncryptDecryptMode encryptDecryptMode = new EncryptDecryptMode();
                                    oos.writeObject(new DataBaseResourseIO(URLTextField.getText(),
                                            encryptDecryptMode.encrypt(UserNameTextField.getText()),
                                            encryptDecryptMode.encrypt(String.valueOf(PassWordPasswordField0.getPassword()))));
                                    oos.close();
                                    fos.close();
                                    setVisible(false);
                                } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                                        BadPaddingException | IllegalBlockSizeException | IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Не вдалося зберегти дані у файл.\n" + ex,
                                            "Помилка вводу/виводу", JOptionPane.ERROR_MESSAGE);
                                }
                                } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Не вдалося встановити з'єднання з базою даних.\n" + ex,
                                        "Помилка", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Паролі не співпадають",
                                "Помилка", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Введіть логін",
                            "Помилка", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                    JOptionPane.showMessageDialog(null, "Введіть адресу бази даних",
                            "Помилка", JOptionPane.WARNING_MESSAGE);
            }
            }
        });
        CancelKey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (bool) System.exit(0);
                else setVisible(false);
            }
        });
        add(URLLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(URLTextField, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(URLEnable, new GridBagConstraints(0, 1, 2, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameLabel, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(UserNameTextField, new GridBagConstraints(1, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordLabel0, new GridBagConstraints(0, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordPasswordField0, new GridBagConstraints(1, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordLabel1, new GridBagConstraints(0, 4, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(PassWordPasswordField1, new GridBagConstraints(1, 4, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        ButtonsPanel.add(CancelKey, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        ButtonsPanel.add(OkKey, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(ButtonsPanel, new GridBagConstraints(0, 5, 2, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        pack();
        setVisible(true);
    }
}
