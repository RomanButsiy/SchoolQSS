import java.util.ListResourceBundle;

public class resource_en  extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] {
                {"OkKey", "Ok"},
                {"CancelKey", "Cancel"},
                {"DBConnect","Connect to database"}
        };
    }
}
