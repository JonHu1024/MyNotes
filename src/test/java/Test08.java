import com.jonhu.core.builder.presentation.PresentationMerger;
import com.jonhu.util.Settings;
import java.io.IOException;

/**
 * 测试PPT文件的合并
 */
public class Test08 {
    public static void main(String[] args) throws IOException {
        Settings.defaultInitialize();

        String srcPath1 = "D:\\Codes\\MyNotesBuilder\\src\\main\\resources\\example\\presentation\\example.pptx";
        String srcPath2 = "D:\\Codes\\MyNotesBuilder\\src\\main\\resources\\example\\presentation\\example2.pptx";
        //String srcPath3 = "D:\\Codes\\MyNotesBuilder\\src\\main\\resources\\example\\presentation\\effects.pptx";
        String destPath = "D:\\Codes\\MyNotesBuilder\\src\\main\\resources\\example\\presentation\\merged.pptx";

        PresentationMerger merger = new PresentationMerger(destPath, srcPath1, srcPath2);
        merger.merge();

        //String dir = "D:\\Codes\\MyNotesBuilder\\src\\main\\resources\\temp\\example2";
        //此方法没有问题
        //Utils.ZipUtils.compressAndReplaceDir(dir, Constant.TEMP_DIRECTORY_PATH);
    }
}
