package com.jonhu.core.builder.document;

import com.jonhu.core.builder.Builder;
import com.jonhu.core.builder.Builders;
import com.jonhu.core.parser.RichText;
import com.jonhu.core.styles.Format;
import com.jonhu.util.Constant;
import com.jonhu.util.Utils;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Word文档构造
 */
public class DocumentBuilder implements Builder {

    private XWPFDocument document;

    private XWPFParagraph paragraph;

    private XWPFRun run;

    public DocumentBuilder() {
        init();
    }

    public void init() {
        document = new XWPFDocument();
    }

    @Override
    public void addText(List<RichText> richTextList, final Format format) {
        //1. 创建
        paragraph = document.createParagraph();

        //2. 设置段落缩进
        paragraph.setIndentationLeft(Constant.DOC_INDENT* Builders.getIndentLevelBySpaces(richTextList.get(0).getText()));

        //3. 去除不必要的格式字符串
        richTextList = Builders.deleteFormatStrings(richTextList,format.isOnlyFormat());

        //4. 遍历列表设置样式
        try {
            for (RichText richText : richTextList) {
                run = paragraph.createRun();
                run.setText(richText.getText());
                setRunStyle(Utils.getFormatOf(richText.getStyles(), format.clone()));
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void setRunStyle(Format format) {
        run.setColor(Utils.rgbToHex(format.getColor()));
        run.setFontFamily(format.getTextFont());
        run.setFontSize(format.getSize());
        run.setBold(format.isBold());
        run.setItalic(format.isItalic());
        if (format.isUnderlined()) {
            run.setUnderline(UnderlinePatterns.SINGLE);
        }
        if (format.isHighlighted()) {
            run.getCTR().addNewRPr().addNewHighlight().setVal(format.getHighlightColor().getEnumColor());
        }
        if (format.isCenter()) {
            paragraph.setAlignment(ParagraphAlignment.CENTER);
        }
    }

    @Override
    public void save(Path file) throws IOException {
        OutputStream os = Files.newOutputStream(file);
        document.write(os);
        os.close();
    }

    @Override
    public Builder createNewBuilder() {
        return new DocumentBuilder();
    }
}
