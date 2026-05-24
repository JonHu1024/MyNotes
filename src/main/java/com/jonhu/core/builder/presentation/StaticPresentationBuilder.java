package com.jonhu.core.builder.presentation;

import com.jonhu.core.builder.Builder;
import com.jonhu.core.builder.Builders;
import com.jonhu.core.parser.RichText;
import com.jonhu.core.styles.Format;
import com.jonhu.util.Constant;
import com.jonhu.util.Settings;
import com.jonhu.util.Utils;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 静态(无动画)幻灯片构造
 */
public class StaticPresentationBuilder implements Builder {

    private XMLSlideShow pptx;

    private XSLFSlide slide = null;

    private XSLFAutoShape shape = null;

    private XSLFTextParagraph paragraph = null;

    private XSLFTextRun run = null;

    private PresentationWordsCounter counter;

    /**
     * 统计每一张幻灯片文字已占的面积(字符大小*长度)
     */
    private double characterHeightCounter = 0;

    private double characterHeightAddition = 0;

    public StaticPresentationBuilder() {
        pptx = new XMLSlideShow();
        counter = new PresentationWordsCounter(Settings.textFrameRectangles.get(Constant.TEMP_STRING));
    }

    @Override
    public void addText(List<RichText> richTextList, Format format) {

        //1. 创建幻灯片和文本框
        counter.loadText(richTextList, format);
        createNewSlideIfNeeded();

        //2. 设置段落缩进
        paragraph = shape.addNewTextParagraph();
        paragraph.setIndentLevel(Builders.getIndentLevelBySpaces(richTextList.get(0).getText()));

        //3. 去除不必要的格式字符串
        Builders.deleteFormatStrings(richTextList, format.isOnlyFormat());

        //4. 遍历列表设置样式
        try {
            for (RichText richText : richTextList) {
                run = paragraph.addNewTextRun();
                run.setText(richText.getText());
                setRunStyle(Utils.getFormatOf(richText.getStyles(), format.clone()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Path file) throws IOException {
        OutputStream os = Files.newOutputStream(file);
        pptx.write(os);
        os.close();
    }

    @Override
    public Builder createNewBuilder() {
        return new StaticPresentationBuilder();
    }

    private void setRunStyle(Format format) {
        run.setFontColor(format.getColor());
        run.setFontFamily(format.getTextFont());
        run.setFontSize((double) format.getSize());
        run.setBold(format.isBold());
        run.setItalic(format.isItalic());
        run.setUnderlined(format.isUnderlined());
        if (format.isHighlighted()) {
            Color color = format.getHighlightColor().getColor();
            run.getRPr(true).addNewHighlight().addNewSrgbClr().setVal(
                    new byte[]{(byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue()}
            );
        }
        if (format.isCenter()) {
            paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        }

    }

    private void createNewSlideIfNeeded() {
        //如果不存在就创建
        if (slide == null || counter.isFullOfWords()) {
            slide = pptx.createSlide();
            shape = slide.createAutoShape();
            shape.setAnchor(counter.getRectangle());
        }
    }
}
