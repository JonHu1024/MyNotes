import com.jonhu.core.builder.ConvertingTask;
import com.jonhu.core.builder.document.DocumentBuilder;
import com.jonhu.core.parser.MyNotesFormatParser;
import com.jonhu.util.Settings;

import java.nio.file.Paths;


public class Test05 {
    public static void main(String[] args) throws Exception {
        Settings.defaultInitialize();

        //D:\SourceCodes\MyNotes\MyNotesBuilder\src\main\resources\example\presentation\example.txt
        String src1 = "src\\main\\resources\\example\\presentation\\example.txt";
        String src2 = "src\\main\\resources\\example\\presentation\\example2.txt";
        String dest1 = "src\\main\\resources\\example\\presentation\\example.docx";
        String dest2 = "src\\main\\resources\\example\\presentation\\example2.docx";

        ConvertingTask task1 = ConvertingTask.createTask(
                Paths.get(src1),
                Paths.get(dest1),
                new DocumentBuilder(),
                new MyNotesFormatParser()
        );

        ConvertingTask task2 = ConvertingTask.createTask(
                Paths.get(src2),
                Paths.get(dest2),
                new DocumentBuilder(),
                new MyNotesFormatParser()
        );

        task1.start();
        task2.start();
    }
}