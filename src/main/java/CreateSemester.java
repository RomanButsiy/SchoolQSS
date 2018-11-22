import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.ResourceBundle;

public class CreateSemester {

    private Boolean bool;
    private int SelectedYear;
    private int SelectedSemester;

    public CreateSemester(Frame frame, ResourceBundle bundle) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.add(new JLabel("Виберіть навчальний рік та семестр"),
                new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        final JComboBox<Integer> YearComboBox = new JComboBox<Integer>();
        final JComboBox<Integer> SemesterComboBox = new JComboBox<Integer>();
        for(int i = 0; i <= 5; SemesterComboBox.addItem(i++));
        int Year = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = Year - 10; i <= Year + 10; YearComboBox.addItem(i++));
        YearComboBox.setSelectedIndex(10);
        panel.add(YearComboBox, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        panel.add(SemesterComboBox, new GridBagConstraints(1, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        final JOptionPane optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        final JDialog dialog = new JDialog(frame, "Cтворити семестр", true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
//        dialog.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent we) {
//            }
//        });
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        String prop = e.getPropertyName();
                        if (dialog.isVisible()
                                && (e.getSource() == optionPane)
                                && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                            int value = ((Integer)optionPane.getValue()).intValue();
                            if (value == JOptionPane.YES_OPTION) {
                                SelectedYear = (Integer) YearComboBox.getSelectedItem();
                                SelectedSemester = (Integer) SemesterComboBox.getSelectedItem();
                                bool = true;
                                dialog.setVisible(false);
                            } else if (value == JOptionPane.NO_OPTION) {
                                bool = false;
                                dialog.setVisible(false);
                            }
                        }
                    }
                });
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public int getSelectedYear() {
        return SelectedYear;
    }

    public int getSelectedSemester() {
        return SelectedSemester;
    }

    public Boolean getBool() {
        return bool;
    }
}
