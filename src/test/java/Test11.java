import com.jonhu.core.builder.ConvertingTask;
import com.jonhu.core.builder.document.DocumentBuilder;
import com.jonhu.core.parser.MyNotesFormatParser;
import com.jonhu.core.styles.Style;
import com.jonhu.util.Settings;
import java.nio.file.Paths;
import java.util.List;

/**
 * 测试非MyNotes格式的转化
 */
public class Test11 {
    public static void main(String[] args) throws Exception {
        Settings.defaultInitialize();

        List<Style.StyleSymbol> list = List.of(
                new Style.StyleSymbol("<h1>", "</h1>", Style.BOLD),
                new Style.StyleSymbol("<p>", "<p>", Style.ITALIC),
                new Style.StyleSymbol("<pre>", "<pre>", Style.SPECIAL_COLOR)
        );

        Settings.setStyleSymbols(list);

        ConvertingTask.createTask(
                Paths.get("src/main/resources/example/Test11.txt"),
                Paths.get("src/main/resources/example/Test11.docx"),
                new DocumentBuilder(),
                new MyNotesFormatParser()
        ).start();
    }
}