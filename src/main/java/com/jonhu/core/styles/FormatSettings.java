package com.jonhu.core.styles;

import com.jonhu.util.Constant;
import com.spire.presentation.drawing.animation.AnimationEffectType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;

import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * TODO:转化格式设置，利用面向对象的思维替代旧版本的静态工具类Settings
 */
public class FormatSettings {
    public int indentSpaces = 4;

    public String formatSplitCharacter = "\\s";

    /**
     * 对应类型的格式
     */
    public Map<Type, Format> formats = new HashMap<>();

    /**
     * 文本中添加的样式,即Markdown类型符号标记
     */
    public List<Style.StyleSymbol> styleSymbols = new ArrayList<>();

    public Map<String, Rectangle> textFrameRectangles;

    public Map<String, AnimationEffectType> animationEffectTypes;

    public STHighlightColor.Enum highlightColor;

    public Color specialColor;

    public static FormatSettings getDefaultFormatSettings() {
        return new FormatSettings()
                .setDefaultFormats()
                .setDefaultStyleSymbols()
                .setDefaultFormats()
                .setDefaultTextFrameRectangles()
                .setDefaultAnimationEffectTypes()
                .setHighlightColor(STHighlightColor.YELLOW)
                .setSpecialColor(Color.RED);
    }

    private FormatSettings(Map<Type, Format> formats,
                          List<Style.StyleSymbol> styleSymbols,
                          Map<String, Rectangle> textFrameRectangles,
                          Map<String, AnimationEffectType> animationEffectTypes,
                          STHighlightColor.Enum highlightColor,
                          Color specialColor) {
        this.formats = formats;
        this.styleSymbols = styleSymbols;
        this.textFrameRectangles = textFrameRectangles;
        this.animationEffectTypes = animationEffectTypes;
        this.highlightColor = highlightColor;
        this.specialColor = specialColor;
    }

    private FormatSettings() {}

    public FormatSettings setDefaultFormats() {
        formats.put(new Type("(\\d)+\\.", "一级标题"),
                Format.getInstance(false, Color.BLUE, "微软雅黑", 16, true, false, false, null, false));
        formats.put(new Type("(\\d)+\\.\\d", "二级标题"),
                Format.getInstance(false, Color.RED, "宋体", 14, true, false, false, null, false));
        formats.put(new Type("((\\d)+\\.){2}\\d", "三级标题"),
                Format.getInstance(false, Color.RED, "宋体", 12, true, true, false, null, false));
        formats.put(new Type("((\\d)+\\.)+\\d", "小标题"),
                Format.getInstance(false, Color.BLACK, "宋体", 12, true, true, false, null, false));
        formats.put(new Type("-", "无序列表"),
                Format.getInstance(false, Color.BLACK, "宋体", 12, false, false, false, null, false));
        formats.put(new Type("(\\d)+", "有序列表"),
                Format.getInstance(false, Color.BLACK, "宋体", 12, false, false, false, null, false));
        formats.put(new Type(">", "引用"),
                Format.getInstance(true, Color.GRAY, "宋体", 11, false, false, false, null, false));
        formats.put(Constant.DEFAULT_TYPE,
                Format.getInstance(true, Color.BLACK, "宋体", 12, false, false, false, null, false));
        return this;
    }

    public FormatSettings setDefaultStyleSymbols() {
        styleSymbols.add(new Style.StyleSymbol("*", "*", Style.ITALIC));
        styleSymbols.add(new Style.StyleSymbol("**", "**", Style.BOLD));
        styleSymbols.add(new Style.StyleSymbol("***", "***", Style.ITALIC, Style.BOLD));
        styleSymbols.add(new Style.StyleSymbol("==", "==", Style.HIGHLIGHTED));
        styleSymbols.add(new Style.StyleSymbol("__", "__", Style.UNDERLINED));
        styleSymbols.add(new Style.StyleSymbol("++", "++", Style.SPECIAL_COLOR));
        styleSymbols.add(new Style.StyleSymbol("<quoteAddition>", " </quoteAddition>" ,Style.QUOTE_STYLE));
        return this.sortStyleSymbols();
    }

    private FormatSettings sortStyleSymbols() {
        //按照styleSymbol的符号长度从小到大排序
        styleSymbols.sort((o1, o2) -> {
            int startLength1 = o1.getStart().length();
            int startLength2 = o2.getStart().length();
            int endLength1 = o1.getEnd().length();
            int endLength2 = o2.getEnd().length();
            if (startLength1 == startLength2) {
                if (endLength1 == endLength2)
                    return 0;
                else if (endLength1 > endLength2)
                    return -1;
                return 1;
            }
            if (startLength1 > startLength2)
                return -1;
            return 1;
        });
        return this;
    }

    public FormatSettings setDefaultTextFrameRectangles() {
        textFrameRectangles = new HashMap<>();
        Rectangle defaultRectangle = new Rectangle(30, 30, 650, 480);
        textFrameRectangles.put(Constant.DEFAULT_TYPE.getDescription(), defaultRectangle);
        textFrameRectangles.put(Constant.TEMP_STRING, defaultRectangle);
        return this;
    }

    public FormatSettings setDefaultAnimationEffectTypes() {
        animationEffectTypes = new HashMap<>();
        animationEffectTypes.put(Constant.DEFAULT_TYPE.getDescription(), AnimationEffectType.CHECKERBOARD);
        animationEffectTypes.put(Constant.TEMP_STRING, AnimationEffectType.CHECKERBOARD);
        return this;
    }

    public int getIndentSpaces() {
        return indentSpaces;
    }

    public FormatSettings setIndentSpaces(int indentSpaces) {
        this.indentSpaces = indentSpaces;
        return this;
    }

    public String getFormatSplitCharacter() {
        return formatSplitCharacter;
    }

    public FormatSettings setFormatSplitCharacter(String formatSplitCharacter) {
        this.formatSplitCharacter = formatSplitCharacter;
        return this;
    }

    public Map<Type, Format> getFormats() {
        return formats;
    }

    public FormatSettings setFormats(Map<Type, Format> formats) {
        this.formats = formats;
        return this;
    }

    public List<Style.StyleSymbol> getStyleSymbols() {
        return styleSymbols;
    }

    public FormatSettings setStyleSymbols(List<Style.StyleSymbol> styleSymbols) {
        this.styleSymbols = styleSymbols;
        return this;
    }

    public Map<String, Rectangle> getTextFrameRectangles() {
        return textFrameRectangles;
    }

    public FormatSettings setTextFrameRectangles(Map<String, Rectangle> textFrameRectangles) {
        this.textFrameRectangles = textFrameRectangles;
        return this;
    }

    public Map<String, AnimationEffectType> getAnimationEffectTypes() {
        return animationEffectTypes;
    }

    public FormatSettings setAnimationEffectTypes(Map<String, AnimationEffectType> animationEffectTypes) {
        this.animationEffectTypes = animationEffectTypes;
        return this;
    }

    public STHighlightColor.Enum getHighlightColor() {
        return highlightColor;
    }

    public FormatSettings setHighlightColor(STHighlightColor.Enum highlightColor) {
        this.highlightColor = highlightColor;
        return this;
    }

    public Color getSpecialColor() {
        return specialColor;
    }

    public FormatSettings setSpecialColor(Color specialColor) {
        this.specialColor = specialColor;
        return this;
    }
}