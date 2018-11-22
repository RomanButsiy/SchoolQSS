import javax.swing.*;

public class dataError {
    public dataError(Exception e, Boolean bool){
        JOptionPane.showMessageDialog(null, "У програмі виникла внутріння помилка\n" + e,
                "Помилка! Author Roman Butsiy", JOptionPane.ERROR_MESSAGE);
        if (bool) System.exit(0);
    }
}
