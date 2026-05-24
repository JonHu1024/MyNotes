package com.jonhu.core.styles;

import java.awt.*;
import java.io.Serializable;

/**
 * 文本的格式对象,是可变的,因此其线程并不安全!
 * 后期可以考虑使用Builder模式修改
 */
public final class Format implements Serializable {

    /**
     * 格式字符串是否出现在输出内容中,即该Type是否只表示格式
     */
    private boolean onlyFormat;

    /**
     * 颜色
     */
    private Color color;

    /**
     * 字体
     */
    private String textFont;

    /**
     * 字体大小
     */
    private int size;

    /**
     * 粗体
     */
    private boolean bold;

    /**
     * 斜体
     */
    private boolean italic;

    /**
     * 下划线
     */
    private boolean underlined;

    private HighlightColor highlightColor;

    private boolean center;

    public static final Format DEFAULT_INSTANCE = getInstance(false, Color.BLACK, "宋体", 14, false, false, false, null, false);

    /**
     * 构建Format对象
     * @param onlyFormat 格式字符串是否出现在输出内容中,即该Type是否只表示格式.如果onlyFormat=true则不会出现在输出文本中
     * @param color 颜色
     * @param textFont 字体样式
     * @param size 字体大小
     * @param bold 是否加粗
     * @param italic 是否使用斜体
     * @param underlined 是否添加下划线
     * @param highlightColor 高亮颜色
     * @param center 文字是否居中
     */
    public static Format getInstance(
            boolean onlyFormat,
            Color color,
            String textFont,
            int size,
            boolean bold,
            boolean italic,
            boolean underlined,
            HighlightColor highlightColor,
            boolean center) {
        return new Format().setOnlyFormat(onlyFormat).setColor(color).setTextFont(textFont).setSize(size)
                .setBold(bold).setItalic(italic).setUnderlined(underlined).setHighlightColor(highlightColor)
                .setCenter(center);
    }

    private Format() {}

    public boolean isOnlyFormat() {
        return onlyFormat;
    }

    public Format setOnlyFormat(boolean onlyFormat) {
        this.onlyFormat = onlyFormat;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public Format setColor(Color color) {
        this.color = color;
        return this;
    }

    public String getTextFont() {
        return textFont;
    }

    public Format setTextFont(String textFont) {
        this.textFont = textFont;
        return this;
    }

    public int getSize() {
        return size;
    }

    public Format setSize(int size) {
        this.size = size;
        return this;
    }

    public boolean isBold() {
        return bold;
    }

    public Format setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public boolean isItalic() {
        return italic;
    }

    public Format setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public Format setUnderlined(boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    public boolean isHighlighted() {
        return highlightColor != null;
    }

    public HighlightColor getHighlightColor() {
        return highlightColor;
    }

    public Format setHighlightColor(HighlightColor highlightColor) {
        this.highlightColor = highlightColor;
        return this;
    }

    public boolean isCenter() {
        return center;
    }

    public Format setCenter(boolean center) {
        this.center = center;
        return this;
    }

    @Override
    public String toString() {
        return "Format{" +
                "onlyFormat=" + onlyFormat +
                ", color=" + color +
                ", textFont='" + textFont + '\'' +
                ", size=" + size +
                ", bold=" + bold +
                ", italic=" + italic +
                ", underlined=" + underlined +
                ", center=" + center +
                '}';
    }

    @Override
    public Format clone() throws CloneNotSupportedException {
        return getInstance(onlyFormat, color, textFont, size, bold, italic, underlined,highlightColor, center);
    }
}
