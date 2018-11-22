import java.util.ListResourceBundle;

public class resource_uk  extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] {
                {"OkKey", "Підтвердити"},
                {"CancelKey", "Cкасувати"},
                {"DBConnect","Підключення до бази даних"}
        };
    }
}
