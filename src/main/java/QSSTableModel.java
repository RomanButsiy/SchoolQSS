import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class QSSTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private int columnCount = 11;
    private ArrayList<String []> dataArrayList;

    public QSSTableModel() {
        dataArrayList = new ArrayList<String []>();
        for(int i = 0; i < dataArrayList.size(); i++) {
            dataArrayList.add(new String[getColumnCount()]);
        }
    }

    public String getColumnName(int columnIndex){
        switch(columnIndex){
            case 0: return "Клас";
            case 1: return "<html>К-сть<br>учнів</html>";
            case 2: return "<html>Високий<br>рівень</html>";
            case 3: return "%";
            case 4: return "<html>Достатній<br>рівень</html>";
            case 5: return "%";
            case 6: return "<html>Середній<br>рівень</html>";
            case 7: return "%";
            case 8: return "<html>Початковий<br>рівень</html>";
            case 9: return "%";
            case 10: return "ПЯУ";
        }
        return "";
    }

    public int getRowCount() {
        return dataArrayList.size();
    }

    public int getColumnCount() {
        return columnCount;
    }

    public  Object getValueAt(int arg0, int arg1) {
        String []rows = dataArrayList.get(arg0);
        return rows[arg1];
    }

    public void addData(String []row){
        String []rowTable = new String[getColumnCount()];
        rowTable = row;
        dataArrayList.add(rowTable);
    }
}
