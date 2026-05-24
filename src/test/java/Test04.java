import com.jonhu.core.parser.MyNotesFormatParser;
import com.jonhu.core.parser.Parser;
import com.jonhu.core.parser.RichText;
import com.jonhu.util.Settings;

import java.util.List;

public class Test04 {
    public static void main(String[] args) {
        Settings.defaultInitialize();
        Parser parser = new MyNotesFormatParser();
        List<RichText> list = parser.getRichTextList("Text *==__with__==* ==style **is**== so great");
        for (RichText text : list) {
            System.out.println(text.getText()+"\t"+text.getStyles());
        }
    }
}