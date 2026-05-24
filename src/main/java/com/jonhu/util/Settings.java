package com.jonhu.util;

import com.jonhu.core.styles.Format;
import com.jonhu.core.styles.Style;
import com.jonhu.core.styles.Type;
import com.spire.presentation.drawing.animation.AnimationEffectType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor.Enum;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 格式设置
 */
public class Settings {
    public static int indentSpaces = 4;

    public static String formatSplitCharacter = "\\s";

    /**
     * 对应类型的格式
     */
    public static Map<Type, Format> formats = new HashMap<>();

    /**
     * 文本中添加的样式,即Markdown类型符号标记
     */
    public static List<Style.StyleSymbol> styleSymbols = new ArrayList<>();

    public static Map<String, Rectangle> textFrameRectangles;

    public static Map<String, AnimationEffectType> animationEffectTypes;

    public static Enum highlightColor;

    public static Color specialColor;

    public static void defaultInitialize() {
        setDefaultFormats();
        setDefaultStyleSymbols();
        sortStyleSymbols();
        setDefaultTextFrameRectangles();
        setDefaultAnimationEffectTypes();
        highlightColor = STHighlightColor.YELLOW;
        specialColor = Color.RED;
    }

    private static void setDefaultFormats() {
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
    }

    private static void setDefaultStyleSymbols() {
        styleSymbols.add(new Style.StyleSymbol("*", "*", Style.ITALIC));
        styleSymbols.add(new Style.StyleSymbol("**", "**", Style.BOLD));
        styleSymbols.add(new Style.StyleSymbol("***", "***", Style.ITALIC, Style.BOLD));
        styleSymbols.add(new Style.StyleSymbol("==", "==", Style.HIGHLIGHTED));
        styleSymbols.add(new Style.StyleSymbol("__", "__", Style.UNDERLINED));
        styleSymbols.add(new Style.StyleSymbol("++", "++", Style.SPECIAL_COLOR));
        styleSymbols.add(new Style.StyleSymbol("<quoteAddition>", " </quoteAddition>" ,Style.QUOTE_STYLE));
    }

    private static void sortStyleSymbols() {
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
    }

    private static void setDefaultTextFrameRectangles() {
        textFrameRectangles = new HashMap<>();
        Rectangle defaultRectangle = new Rectangle(30, 30, 650, 480);
        textFrameRectangles.put(Constant.DEFAULT_TYPE.getDescription(), defaultRectangle);
        textFrameRectangles.put(Constant.TEMP_STRING, defaultRectangle);
    }

    private static void setDefaultAnimationEffectTypes() {
        animationEffectTypes = new HashMap<>();
        animationEffectTypes.put(Constant.DEFAULT_TYPE.getDescription(), AnimationEffectType.CHECKERBOARD);
        animationEffectTypes.put(Constant.TEMP_STRING, AnimationEffectType.CHECKERBOARD);
    }

    public static void saveFormatsToFile(File file) {
        Utils.saveObjectToFile(formats, file);
    }

    public static void loadFormatsFromFile(File file) {
        Object object = Utils.readAnObjectFromFile(file);
        if (object instanceof Map) {
            formats = (Map) object;
        }
    }

    public static void saveStyleSymbolToFile(File file) {
        Utils.saveObjectToFile(styleSymbols, file);
    }

    public static void loadStyleSymbolsFromFile(File file) {
        Object object = Utils.readAnObjectFromFile(file);
        if (object instanceof List) {
            styleSymbols = (List) object;
        }
    }

    public static void saveTextFrameRectanglesToFile(File file) {
        Utils.saveObjectToFile(textFrameRectangles, file);
    }

    public static void loadTextFrameRectanglesFromFile(File file) {
        Object object = Utils.readAnObjectFromFile(file);
        if (object instanceof Map) {
            textFrameRectangles = (Map<String, Rectangle>) object;
        }
    }

    public static void saveAnimationEffectTypesToFile(File file) {
        Utils.saveObjectToFile(animationEffectTypes, file);
    }

    public static void loadAnimationEffectTypesFromFile(File file) {
        Object object = Utils.readAnObjectFromFile(file);
        if (object instanceof Map) {
            animationEffectTypes = (Map<String, AnimationEffectType>) object;
        }
    }

    public static int getIndentSpaces() {
        return indentSpaces;
    }

    public static void setIndentSpaces(int indentSpaces) {
        Settings.indentSpaces = indentSpaces;
    }

    public static String getFormatSplitCharacter() {
        return formatSplitCharacter;
    }

    public static void setFormatSplitCharacter(String formatSplitCharacter) {
        Settings.formatSplitCharacter = formatSplitCharacter;
    }

    public static Map<Type, Format> getFormats() {
        return formats;
    }

    public static void setFormats(Map<Type, Format> formats) {
        Settings.formats = formats;
    }

    public static List<Style.StyleSymbol> getStyleSymbols() {
        return styleSymbols;
    }

    public static void setStyleSymbols(List<Style.StyleSymbol> styleSymbols) {
        Settings.styleSymbols = styleSymbols;
    }

    public static Map<String, Rectangle> getTextFrameRectangles() {
        return textFrameRectangles;
    }

    public static void setTextFrameRectangles(Map<String, Rectangle> textFrameRectangles) {
        Settings.textFrameRectangles = textFrameRectangles;
    }

    public static Map<String, AnimationEffectType> getAnimationEffectTypes() {
        return animationEffectTypes;
    }

    public static void setAnimationEffectTypes(Map<String, AnimationEffectType> animationEffectTypes) {
        Settings.animationEffectTypes = animationEffectTypes;
    }

    public static Enum getHighlightColor() {
        return highlightColor;
    }

    public static void setHighlightColor(Enum highlightColor) {
        Settings.highlightColor = highlightColor;
    }

    public static Color getSpecialColor() {
        return specialColor;
    }

    public static void setSpecialColor(Color specialColor) {
        Settings.specialColor = specialColor;
    }
}
