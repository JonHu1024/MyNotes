package com.jonhu.core.parser;

import com.jonhu.core.styles.Format;
import com.jonhu.core.styles.Type;

import java.util.List;

public interface Parser {

    /**
     * 根据指定文本解析其类型
     * @param text 指定文本
     * @return Format对象
     */
    Format parseText(String text);

    /**
     * 获取指定字符对应的类型
     * @param text 指定文本
     * @return 类型
     */
    Type getType(String text);

    List<RichText> getRichTextList(String text);
}
