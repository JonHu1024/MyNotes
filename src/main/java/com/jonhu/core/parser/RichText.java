package com.jonhu.core.parser;


import com.jonhu.core.styles.Style;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 富文本对象
 */
public class RichText {
    /**
     * 文本内容
     */
    private String text;

    /**
     * 文本样式
     */
    private Set<Style> styles;

    /**
     * 标记:如果为false则表示本富文本没有下一层了
     */
    private boolean mark = true;

    public RichText(String text) {
        this.text = text;
        styles = new HashSet<>();
    }

    public String getText() {
        return text;
    }

    public RichText setText(String text) {
        this.text = text;
        return this;
    }

    public Set<Style> getStyles() {
        return styles;
    }

    public RichText setStyles(Set<Style> styles) {
        this.styles = styles;
        return this;
    }

    public RichText addStyles(Style[] styles) {
        this.styles.addAll(Arrays.asList(styles));
        return this;
    }

    public RichText addStyles(Set<Style> styles) {
        this.styles.addAll(styles);
        return this;
    }

    public RichText setMark(boolean mark) {
        this.mark = mark;
        return this;
    }

    public boolean isMarked() {
        return mark;
    }
}
