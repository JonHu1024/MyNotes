package com.jonhu.core.builder;

import com.jonhu.core.parser.RichText;
import com.jonhu.util.Settings;
import com.jonhu.util.Utils;

import java.util.List;

public class Builders {
    /**
     * 去除不必要的格式字符串
     * @param richTextList 指定的富文本列表
     * @param onlyFormat 是否去除(即<code>Format#isOnlyFormat()</code>)
     * @return 处理后的列表
     */
    public static List<RichText> deleteFormatStrings(List<RichText> richTextList, boolean onlyFormat) {
        RichText firstRichText = richTextList.get(0);
        String firstText = firstRichText.getText().trim();
        richTextList.remove(0);
        if (onlyFormat) {
            String[] strings = firstText.split(Settings.formatSplitCharacter);
            //根本没有Type的情况
            if (strings.length < 2) {
                richTextList.add(0, new RichText(firstText));
            } else {
                richTextList.add(0, new RichText(strings[1]));
            }
        } else {
            richTextList.add(0, new RichText(firstText));
        }
        richTextList.get(0).addStyles(firstRichText.getStyles());
        return richTextList;
    }

    /**
     * 各大扭矩指定文本的空格数来确定其缩进数
     * @param text 指定文本
     * @return 缩进等级
     */
    public static int getIndentLevelBySpaces(String text) {
        return Utils.getStartingSpaceNumber(text) / Settings.indentSpaces;
    }

}
