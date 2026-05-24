package com.jonhu.core.parser;

import com.jonhu.core.styles.Format;
import com.jonhu.core.styles.Style;
import com.jonhu.core.styles.Style.StyleSymbol;
import com.jonhu.core.styles.Type;
import com.jonhu.util.Constant;
import com.jonhu.util.Settings;
import com.jonhu.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MyNotesFormatParser implements Parser {

    private static final String SYMBOL_PATTERNS;

    static {
        StringBuilder startSymbols = new StringBuilder();
        for (StyleSymbol symbol : Settings.styleSymbols) {
            startSymbols.append("|").append(symbol.getStartRegex());
        }
        startSymbols.deleteCharAt(0);
        SYMBOL_PATTERNS = startSymbols.toString();
    }

    private List<RichText> richTextList;

    @Override
    public Format parseText(String text) {
        return Settings.formats.get(getType(text));
    }

    @Override
    public Type getType(String text) {
        String formatText = text.trim().split(Settings.formatSplitCharacter)[0];
        for (Type type : Settings.formats.keySet()) {
            boolean matches = formatText.matches(type.getModel());
            if (matches) {
                return type;
            }
        }
        return Constant.DEFAULT_TYPE;
    }

    @Override
    public List<RichText> getRichTextList(String text) {
        richTextList = new ArrayList<>();
        richTextList.add(new RichText(text));

        //记录当前要解析分裂的RichText的索引
        int index = 0;
        //是否有下一层节点
        boolean hasNextLevel;

        while (index < richTextList.size()) {
            hasNextLevel = parseToTextLevel(richTextList.get(index), index);
            if (!hasNextLevel) {
                index++;
            }
        }

        return richTextList;
    }

    /**
     * 往下解析一层
     */
    private boolean parseToTextLevel(RichText richText, int index) {
        if (!richText.isMarked()) {
            return false;
        }

        String text = richText.getText();
        int indexOfHeadSymbol, indexOfEndSymbol;

        indexOfHeadSymbol = Utils.StringUtils.indexOfAll(text, SYMBOL_PATTERNS);
        if (indexOfHeadSymbol == -1) {
            return false;
        }

        StyleSymbol symbol = getSymbolByStartIndexFromText(text, indexOfHeadSymbol);

        int indexOfEndSymbol0 = text.substring(indexOfHeadSymbol + Objects.requireNonNull(symbol).getStart().length()).indexOf(symbol.getEnd());
        if (indexOfEndSymbol0 == -1) {
            return false;
        }
        indexOfEndSymbol = indexOfEndSymbol0 + symbol.getEnd().length() + indexOfHeadSymbol;

        String text1 = text.substring(0, indexOfHeadSymbol);
        String text2 = text.substring(indexOfHeadSymbol + symbol.getStart().length(), indexOfEndSymbol);
        String text3 = text.substring(indexOfEndSymbol + symbol.getEnd().length());

        Set<Style> styles = richTextList.get(index).getStyles();
        splitRichTextList(index, new RichText(text1).setMark(false).addStyles(styles), new RichText(text2).addStyles(symbol.getStyles()).addStyles(styles), new RichText(text3).addStyles(styles));

        return true;
    }

    /**
     * 分裂,将richTextList中的RichText对象由一个变为三个
     * @param index 要分裂的元素索引
     */
    private void splitRichTextList(int index,RichText... texts) {
        richTextList.remove(index);
        for (int i = 0,addition = 0; i < texts.length; i++) {
            if (texts[i].getText().length() != 0) {
                richTextList.add(index + addition, texts[i]);
                addition++;
            }
        }
    }

    /**
     * 根据指定文本的指定索引获取对应的StyleSymbol对象
     *
     * @param text       指定文本
     * @param startIndex StyleSymbol对象getStart()的索引
     */
    private StyleSymbol getSymbolByStartIndexFromText(String text, int startIndex) {
        for (StyleSymbol symbol : Settings.styleSymbols) {
            if (text.startsWith(symbol.getStart(), startIndex)) {
                return symbol;
            }
        }
        return null;
    }
}
