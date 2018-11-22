import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CreateClass extends JDialog implements ActionListener {

private JButton OkKey, CancelKey;
private JTextField fieldBABA, fieldDIDO;
private Statement statement;
private Boolean bool;

    public CreateClass(Frame frame, Statement statement, ResourceBundle bundle) {
        super(frame, "Cтворення нового класу",true);
        setSize(600, 200);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLayout(new GridBagLayout());
        this.statement = statement;
        fieldBABA = new JTextField(35);
        fieldDIDO = new JTextField(35);
        OkKey = new JButton(bundle.getString("OkKey"));
        CancelKey = new JButton(bundle.getString("CancelKey"));
        JPanel ButtonsPanel = new JPanel(new GridBagLayout());
        fieldBABA.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() < 40) { super.insertString(offs, str, a); }}
        });
        fieldDIDO.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() < 40) { super.insertString(offs, str, a); }}
        });
        OkKey.addActionListener(this);
        CancelKey.addActionListener(this);
        add(new JLabel("Введіть назву класу:"),
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(fieldBABA, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(new JLabel("Введіть прізвище та ініціали клаcного керівника:"),
                new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        add(fieldDIDO, new GridBagConstraints(1, 1, 1, 1, 1, 1,
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
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == OkKey) {
            if ((!fieldBABA.getText().trim().isEmpty()) && (!fieldDIDO.getText().trim().isEmpty())) {
                        bool = true;
                        setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Заповніть всі поля!",
                        "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (event.getSource() == CancelKey) {
            bool = false;
            setVisible(false);
        }
    }

    public String getFieldBABA() {
        return fieldBABA.getText();
    }

    public String getFieldDIDO() {
        return fieldDIDO.getText();
    }

    public Boolean getBool() {
        return bool;
    }
}
