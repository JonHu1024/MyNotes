package com.jonhu.core.builder.presentation;

import com.jonhu.core.parser.RichText;
import com.jonhu.core.styles.Format;

import java.awt.*;
import java.util.List;

/**
 * 用于处理演示文稿的翻页问题
 */
public class PresentationWordsCounter {

    private final Rectangle rectangle;

    private double characterHeightCounter = 0;

    private double characterHeightAddition = 0;

    public PresentationWordsCounter(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void loadText(List<RichText> richTextList, Format format) {
        loadText(RichTextListToString(richTextList), format);
    }

    public void loadText(String text, Format format) {
//        System.out.println(text+" "+countLines(text, format.getSize()));
        //魔法值:根据实地证明,实际height = height*1.2275-0.243
        characterHeightAddition = (format.getSize() * 1.2275 - 0.243) * countLines(text, format.getSize());
        characterHeightCounter += characterHeightAddition;

        //System.err.printf("\"%s\"\n\t行数为:%s\n", text, countLines(text, format.getSize()));
    }

    public boolean isFullOfWords() {
//        System.out.println("counter:"+characterHeightCounter+"\taddition:"+characterHeightAddition);

        if (characterHeightCounter > rectangle.getHeight()) {
            characterHeightCounter = characterHeightAddition;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将指定的富文本列表还原成字符串
     * @param list 指定的富文本列表
     */
    private String RichTextListToString(List<RichText> list) {
        StringBuilder builder = new StringBuilder();
        for (RichText richText : list) {
            builder.append(richText.getText());
        }
        return String.valueOf(builder);
    }

    private int countLines(String text, int size) {
        int result = text.getBytes().length + text.length();
        //return (int) Math.ceil( result*size / 2 / rectangle.width );
        /*
        魔法代码的解释:一个中文字符占3个字节
        于是我们有:
            3chi + eng = text.getBytes().length;
            chi + eng = text.length();
        解得:
            chi = 0.5(text.getBytes().length - text.length());
            eng = text.length() - chi;
        rectangle.width / size = 中文字符数;英文字符数大致需翻倍.
        即:rectangle.width / size = chi + eng / 2 = chi = chi + 0.5text.length() - 0.5chi = 0.5(chi+text.length()) = 0.5(0.5text.getBytes().length+0.5text.length())
         */

        return Math.round((result / 4) / (rectangle.width / size)) +1;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public double getCharacterHeightCounter() {
        return characterHeightCounter;
    }

    public double getCharacterHeightAddition() {
        return characterHeightAddition;
    }
}
