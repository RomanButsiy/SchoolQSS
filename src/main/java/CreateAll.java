import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CreateAll {

    private Boolean bool;
    private String DataStr;


    public CreateAll(Frame frame, String text, String title) {
        JPanel panel = new JPanel();
        JTextField textField = new JTextField(35);
        textField.setDocument(new PlainDocument() {
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() < 40) {
                    super.insertString(offs, str, a);
                }
            }
        });
        panel.setLayout(new GridBagLayout());
        panel.add(new JLabel(text),
                new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel.add(textField, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        final JOptionPane optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        final JDialog dialog = new JDialog(frame, title, true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                bool = false;
                dialog.setVisible(false);
            }
        });
        optionPane.addPropertyChangeListener(e -> {
            String prop = e.getPropertyName();
            if (dialog.isVisible()
                    && (e.getSource() == optionPane)
                    && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                int value = ((Integer) optionPane.getValue()).intValue();
                if (value == JOptionPane.YES_OPTION) {
                    if ((DataStr = textField.getText()).trim().isEmpty())
                        bool = false;
                    else bool = true;
                    dialog.setVisible(false);
                } else if (value == JOptionPane.NO_OPTION) {
                    bool = false;
                    dialog.setVisible(false);
                }
            }
        });
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    public String getData() {
        return DataStr;
    }

    public Boolean getBool() {

        return bool;
    }
}