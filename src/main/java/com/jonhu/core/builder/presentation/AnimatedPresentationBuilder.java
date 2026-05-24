package com.jonhu.core.builder.presentation;

import com.jonhu.core.builder.Builder;
import com.jonhu.core.builder.Builders;
import com.jonhu.core.parser.RichText;
import com.jonhu.core.styles.Format;
import com.jonhu.util.Constant;
import com.jonhu.util.Settings;
import com.jonhu.util.Utils;
import com.spire.presentation.*;
import com.spire.presentation.drawing.FillFormatType;
import com.spire.presentation.drawing.animation.AnimationEffect;
import com.spire.presentation.drawing.animation.AnimationEffectType;

import java.awt.*;
import java.nio.file.Path;
import java.util.List;

/**
 * 动态(含动画)幻灯片构造
 */
public class AnimatedPresentationBuilder implements Builder {
    private Presentation pptx;

    private ISlide slide;

    private IAutoShape shape;

    private ParagraphEx paragraph;

    private PortionEx portion;//相当于POI中TextRun的概念

    private PresentationWordsCounter counter;

    private int indexOfParagraph = 0;

    private AnimationEffectType effectType;

    /**
     * 由于没有钱，一次只能输出10张幻灯片
     */
    private static final int LIMIT = 10;

    /**
     * 已经创建的pptx数量
     */
    private static int pptxCounter = 0;

    public AnimatedPresentationBuilder() {
        pptx = new Presentation();
        pptxCounter++;
        pptx.getSlides().removeAt(0);
        counter = new PresentationWordsCounter(Settings.textFrameRectangles.get(Constant.TEMP_STRING));
        effectType = Settings.animationEffectTypes.get(Constant.TEMP_STRING);
    }

    @Override
    public void addText(List<RichText> richTextList, Format format) {
        try {
            //1. 创建幻灯片和文本框
            counter.loadText(richTextList, format);
            createNewSlideIfNeeded();

            //2. 设置段落缩进
            setParagraphIndentLevel(richTextList);

            //3. 去除不必要的格式字符串
            Builders.deleteFormatStrings(richTextList, format.isOnlyFormat());

            //4. 遍历列表设置样式
            for (RichText richText : richTextList) {
                portion = new PortionEx();
                portion.setText(richText.getText());
                setPortionStyle(Utils.getFormatOf(richText.getStyles(), format.clone()));
                paragraph.getTextRanges().append(portion);
            }

            //5. 添加动画
            addAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void save(Path file) throws Exception {
        if (pptxCounter == 1) {
            pptx.saveToFile(file.toAbsolutePath().toString(), FileFormat.PPTX_2016);
            pptx.dispose();
        } else {
            pptx.saveToFile(getSavingName(pptxCounter), FileFormat.PPTX_2016);
            String[] srcPaths = new String[pptxCounter];
            for (int i = 0; i < pptxCounter; i++) {
                srcPaths[i] = getSavingName(i+1);
            }
            PresentationMerger merger = new PresentationMerger(file.toAbsolutePath().toString(), srcPaths);
            merger.merge();

            Utils.clearFilesOfDir(Constant.TEMP_DIRECTORY_PATH);
        }
    }

    @Override
    public Builder createNewBuilder() {
        return new AnimatedPresentationBuilder();
    }

    private void setParagraphIndentLevel(List<RichText> richTextList) throws Exception {
        paragraph = new ParagraphEx();
        shape.getTextFrame().getParagraphs().append(paragraph);
        //踩坑警示:设置缩进一定要用setLevel()方法,不要用setIndent()方法
        paragraph.getParagraphProperties().setLevel((short) Builders.getIndentLevelBySpaces(richTextList.get(0).getText()));
        paragraph.setAlignment(TextAlignmentType.LEFT);
    }

    private void setPortionStyle(Format format) {
        portion.getFormat().getFill().setFillType(FillFormatType.SOLID);
        portion.getFill().getSolidColor().setColor(format.getColor());
        portion.setLatinFont(new TextFont(format.getTextFont()));
        portion.setFontHeight(format.getSize());
        portion.isBold(getTriStateByBoolean(format.isBold()));
        portion.isItalic(getTriStateByBoolean(format.isItalic()));
        if (format.isUnderlined()) {
            portion.getUnderlineFormat().setStyle(TextLineStyle.SINGLE);
        }
        if (format.isHighlighted()) {
            //TODO:有bug
            portion.getHighlightColor().setColor(format.getHighlightColor().getColor());
        }
        if (format.isCenter()) {
            paragraph.setAlignment(TextAlignmentType.CENTER);
        }
    }

    private void addAnimation() throws Exception {
        //每张幻灯片的第一个元素不添加动画
        if (indexOfParagraph == 0) {
            indexOfParagraph++;
            System.err.println("Here");
            return;
        }
        AnimationEffect animation = shape.getSlide().getTimeline().getMainSequence().addEffect(shape, effectType);
        animation.setStartEndParagraphs(indexOfParagraph,indexOfParagraph);
        indexOfParagraph++;
    }

    private void createNewSlideIfNeeded() {
        //如果不存在就创建
        if (slide == null || counter.isFullOfWords()) {
            try {
                if (pptx.getSlides().size() >= LIMIT) {
                    pptx.saveToFile(getSavingName(pptxCounter), FileFormat.PPTX_2016);
                    pptx.dispose();
                    pptx = new Presentation();
                    pptxCounter++;
                    pptx.getSlides().removeAt(0);
                }
                createSlideAndShape();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createSlideAndShape() throws Exception {
        slide = pptx.getSlides().append();
        shape = slide.getShapes().appendShape(ShapeType.RECTANGLE, counter.getRectangle());
        shape.getLine().setFillType(FillFormatType.SOLID);
        shape.getLine().getFillFormat().getSolidFillColor().setColor(Color.WHITE);
        shape.getFill().setFillType(FillFormatType.SOLID);
        shape.getFill().getSolidColor().setColor(Color.WHITE);
        shape.getTextFrame().getParagraphs().removeAt(0);
        shape.getTextFrame().setAnchoringType(TextAnchorType.LEFT);
        indexOfParagraph = 0;
    }

    private TriState getTriStateByBoolean(boolean b) {
        return b ? TriState.TRUE : TriState.FALSE;
    }

    private String getSavingName(int number) {
        return String.format("%s/%s_Presentation_Number=%d.pptx", Constant.TEMP_DIRECTORY_PATH, Constant.TEMP_STRING, number);
    }

}
