import com.jonhu.core.builder.ConvertingTask;
import com.jonhu.core.builder.document.DocumentBuilder;
import com.jonhu.core.parser.MyNotesFormatParser;
import com.jonhu.util.Settings;

import java.io.File;

public class Test03 {
    public static void main(String[] args) throws Exception {
        Settings.defaultInitialize();
        String src = "D:\\Codes\\MyNotesBuilder\\src\\main\\resources\\example\\example3.txt";
        String dest = "D:\\Codes\\MyNotesBuilder\\src\\main\\resources\\example\\example3.docx";
        ConvertingTask task = new ConvertingTask(new File(src), new File(dest), new DocumentBuilder(), new MyNotesFormatParser());
        task.addCustomizedAction((msg)-> msg.replaceAll("\\[","\\*\\*").replaceAll("\\]","\\*\\*"));
        task.start();
    }
}
