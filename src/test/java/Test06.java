import com.jonhu.util.Constant;
import com.spire.presentation.FileFormat;
import com.spire.presentation.IAutoShape;
import com.spire.presentation.Presentation;

public class Test06 {
    public static void main(String[] args) throws Exception {
        String path = "src/main/resources/example/presentation/example3.pptx";

        long time1 = System.currentTimeMillis();
        Presentation presentation = new Presentation(path, FileFormat.PPTX_2016);
        IAutoShape shape = (IAutoShape) presentation.getSlides().get(0).getShapes().get(0);
        //shape.getTextFrame().getParagraphs().get(0).getParagraphProperties().setIndent(20);
        System.out.println(shape.getTextFrame().getParagraphs().get(0).getFirstTextRange().getFontHeight());

        long time2 = System.currentTimeMillis();
        System.out.println("操作用时:" + (time2 - time1));
        presentation.saveToFile(path,FileFormat.PPTX_2016);
        long time3 = System.currentTimeMillis();
        System.out.println("保存用时:"+(time3 - time2));
        presentation.dispose();
    }
}
