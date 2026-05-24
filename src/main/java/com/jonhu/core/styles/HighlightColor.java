package com.jonhu.core.styles;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor.Enum;

import java.awt.*;



/**
 * 统一各Builder类的高亮文本色
 */
public class HighlightColor {

    Enum enumColor;

    private HighlightColor() {}

    /**
     * 构建HighLightColor对象
     */
    public HighlightColor(Enum color) {
        this.enumColor = color;
    }

    public Enum getEnumColor() {
        return enumColor;
    }

    public void setEnumColor(Enum enumColor) {
        this.enumColor = enumColor;
    }

    /**
     * 获取对应的java.awt.Color对象,对应方式参见org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor.Enum;
     * 默认为Color.YELLOW
     */
    public Color getColor() {
        int intValue = enumColor.intValue();
        switch (intValue) {
            case 1:
                return Color.BLACK;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.CYAN;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.MAGENTA;
            case 6:
                return Color.RED;
            case 7:
                return Color.YELLOW;
            case 8:
                return Color.WHITE;
            case 9:
                return getDarkColor(Color.BLUE);
            case 10:
                return getDarkColor(Color.CYAN);
            case 11:
                return getDarkColor(Color.GREEN);
            case 12:
                return getDarkColor(Color.MAGENTA);
            case 13:
                return getDarkColor(Color.RED);
            case 14:
                return getDarkColor(Color.YELLOW);
            case 15:
                return Color.DARK_GRAY;
            case 16:
                return Color.LIGHT_GRAY;
        }
        return Color.YELLOW;
    }

    private Color getDarkColor(Color color) {
        return new Color(color.getRed()/2,color.getGreen()/2,color.getBlue()/2);
    }
}
