package com.jonhu.core.styles;

import com.jonhu.util.Settings;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor;

import java.util.Arrays;
import java.util.function.Function;

public enum Style {
    BOLD(format -> format.setBold(true)),
    ITALIC(format -> format.setItalic(true)),
    UNDERLINED(format -> format.setUnderlined(true)),
    HIGHLIGHTED(format -> format.setHighlightColor(new HighlightColor(Settings.highlightColor))),
    SPECIAL_COLOR(format -> format.setColor(Settings.specialColor)),
    QUOTE_STYLE(format -> format.setHighlightColor(new HighlightColor(STHighlightColor.DARK_GRAY))),
    /**
     * 留给用户自定义
     */
    CUSTMIZED_STYLE(Function.identity());



    private final Function<Format, Format> styleSetter;

    Style(Function<Format, Format> styleSetter) {
        this.styleSetter = styleSetter;
    }

    public Function<Format, Format> getStyleSetter() {
        return styleSetter;
    }

    /**
     * 类Markdown类型标记
     * 注意:每一个Symbol的start与end字段不可以相同
     */
    public static final class StyleSymbol {
        private final String start;
        private final String end;

        private String startRegex;

        private String endRegex;

        private final Style[] styles;

        public StyleSymbol(String start, String end,Style... styles) {
            this.start = start;
            this.startRegex = start;
            this.end = end;
            this.endRegex = end;
            this.styles = styles;
            dealWithRegexes();
        }

        private String[] dealWithRegexes() {
            String[] regexes = {"^", "$", "*", "+", "{", "}", "(", ")",".","[","]"};
            for (String regex : regexes) {
                StringBuilder replacement = new StringBuilder(regex).insert(0,"\\");
                startRegex = this.startRegex.replace(regex,replacement);
                endRegex = this.endRegex.replace(regex, replacement);
            }
            return new String[]{String.valueOf(start), String.valueOf(end)};
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }

        public String getStartRegex() {
            return startRegex;
        }

        public String getEndRegex() {
            return endRegex;
        }

        public Style[] getStyles() {
            return styles;
        }

        @Override
        public String toString() {
            return "StyleSymbol{" +
                    "start='" + start + '\'' +
                    ", end='" + end + '\'' +
                    ", styles=" + Arrays.toString(styles) +
                    '}';
        }
    }
}