import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class AllMarks extends JFrame implements Runnable {
    private static final long serialVersionUID = 1L;
    JTable MarksTable;
    JScrollPane MarksTableScrollPane;

    public void run() {}

    public AllMarks(String s, String[][] data){
        super(s);
        setLayout(new FlowLayout());
        setSize(600,400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        String[] headers = { "Учень", "Оцінка" };
        MarksTable = new JTable(data ,headers);
        MarksTableScrollPane = new JScrollPane(MarksTable);
        MarksTableScrollPane.setPreferredSize(new Dimension(550, 370));
        add(MarksTableScrollPane,new GridBagConstraints(0, 0, 3, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));
    }
}
