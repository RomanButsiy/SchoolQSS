import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class GetSemester extends JDialog {
    private Boolean bool;
    private int SelectedYear;
    private int SelectedSemester;
    private Statement statement = null;
    private ResultSet resSet = null;

    public GetSemester(Connection connection, ResourceBundle bundle, Frame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.add(new JLabel("Виберіть навчальний рік та семестр"),
                new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        final JComboBox<String> YearComboBox = new JComboBox<String>();
        panel.add(YearComboBox, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        try {
            statement = connection.createStatement();
            resSet = statement.executeQuery("select  distinct AcademicYear, Semester from SchoolGymnasiumQSS.Classes order by AcademicYear asc");
            while (resSet.next()) {
                YearComboBox.addItem(resSet.getString("AcademicYear") + "_" + resSet.getString("Semester"));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        final JOptionPane optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        final JDialog dialog = new JDialog(frame, "Вибрати навчальний рік", true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();
                if (dialog.isVisible()
                        && (e.getSource() == optionPane)
                        && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                    int value = ((Integer)optionPane.getValue()).intValue();
                    try {
                    if (value == JOptionPane.YES_OPTION) {
                        if (YearComboBox.getSelectedIndex() == -1) {
                            resSet.close();
                            statement.close();
                            bool = false;
                            dialog.setVisible(false);
                        } else {
                            resSet.absolute(YearComboBox.getSelectedIndex() + 1);
                            SelectedYear = resSet.getInt("AcademicYear");
                            SelectedSemester = resSet.getInt("Semester");
                            resSet.close();
                            statement.close();
                            bool = true;
                            dialog.setVisible(false);
                        }

                    } else if (value == JOptionPane.NO_OPTION) {
                        resSet.close();
                        statement.close();
                        bool = false;
                        dialog.setVisible(false);
                    }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
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

