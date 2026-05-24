package com.jonhu.core.builder;

import com.jonhu.core.parser.RichText;
import com.jonhu.core.styles.Format;

import java.nio.file.Path;
import java.util.List;

public interface Builder {

    /**
     * 根据指定格式添加文字到文件中
     * @param richTextList 要添加的富文本列表
     * @param format 文本格式
     */
    void addText(List<RichText> richTextList, Format format);

    /**
     * 保存文件
     * @param file 保存的目的地
     */
    void save(Path file) throws Exception;

    /**
     * 根据已有Builder对象创建新的对象
     * @return 新的同类型的Builder对象
     */
    Builder createNewBuilder();

}