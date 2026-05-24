import com.jonhu.core.builder.ConvertingTask;
import com.jonhu.core.builder.presentation.AnimatedPresentationBuilder;
import com.jonhu.core.builder.presentation.PresentationWordsCounter;
import com.jonhu.core.parser.MyNotesFormatParser;
import com.jonhu.util.Constant;
import com.jonhu.util.Settings;
import com.spire.presentation.*;
import com.spire.presentation.drawing.FillFormatType;
import com.spire.presentation.drawing.animation.AnimationEffect;
import com.spire.presentation.drawing.animation.AnimationEffectType;

import java.awt.*;
import java.nio.file.Paths;

/**
 * 测试Spire.Presentation for Java的动画效果
 */
public class Test07 {

    private Presentation pptx;

    private ISlide slide;

    private IAutoShape shape;

    private ParagraphEx paragraph;

    private PortionEx portion;

    private PresentationWordsCounter counter;

    private int indexOfParagraph = 0;

    public void test() throws Exception {
        Settings.defaultInitialize();

        String path = "D:\\Codes\\MyNotesBuilder\\src\\main\\resources\\example\\presentation\\effects.pptx";
        pptx = new Presentation();
        pptx.getSlides().removeAt(0);

        counter = new PresentationWordsCounter(Settings.textFrameRectangles.get(Constant.TEMP_STRING));

        AnimationEffectType[] effectTypes = AnimationEffectType.values();
        for (AnimationEffectType animationEffectType : effectTypes) {
            String name = animationEffectType.getName();
            counter.loadText(name, Settings.formats.get(Constant.DEFAULT_TYPE).setSize(18));

            paragraph = new ParagraphEx();
            portion = new PortionEx();
            portion.setText(name);
            portion.getFormat().getFill().setFillType(FillFormatType.SOLID);
            portion.getFill().getSolidColor().setColor(Color.BLACK);
            paragraph.getTextRanges().append(portion);

            createNewSlideIfNeeded();
            shape.getTextFrame().getParagraphs().removeAt(0);
            shape.getTextFrame().getParagraphs().append(paragraph);

            AnimationEffect animation = shape.getSlide().getTimeline().getMainSequence().addEffect(shape, animationEffectType);
            animation.setStartEndParagraphs(indexOfParagraph+1, indexOfParagraph+1);
            indexOfParagraph++;
        }

        pptx.saveToFile(path, FileFormat.PPTX_2016);
        pptx.dispose();
    }

    private void createNewSlideIfNeeded() {
        //如果不存在就创建
        if (slide == null || counter.isFullOfWords()) {
            try {
                slide = pptx.getSlides().append();
                shape = slide.getShapes().appendShape(ShapeType.RECTANGLE, counter.getRectangle());
                shape.getLine().setFillType(FillFormatType.SOLID);
                shape.getLine().getFillFormat().getSolidFillColor().setColor(Color.WHITE);
                shape.getFill().setFillType(FillFormatType.SOLID);
                shape.getFill().getSolidColor().setColor(Color.WHITE);
                indexOfParagraph = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //new Test07().test();
        Settings.defaultInitialize();
        ConvertingTask.createTask(
                Paths.get("src/main/resources/example/presentation/animations.txt"),
                Paths.get("src/main/resources/example/presentation/animations.pptx"),
                new AnimatedPresentationBuilder(),
                new MyNotesFormatParser()
        ).start();

    }
}
