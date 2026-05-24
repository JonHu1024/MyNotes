package com.jonhu.util;

import com.jonhu.core.styles.Type;
/**
 * 储存一些常量
 */
public class Constant {

    public static final int WINDOW_HEIGHT = 450;

    public static final int WINDOW_WIDTH = 650;

    /**
     * ZWNBSP零宽度空白字符
     */
    public static final String UTF8_BOM = "\uFEFF";

    /**
     * 选择文件时支持的后缀类型
     */
    public static final String[] EXTENSIONS = {"*.txt"};

    /**
     * 应用程序的图标
     */
    public static final String ICON_IMAGE_PATH = "/img/MyNotesBuilderIcon.png";

    public static final Type DEFAULT_TYPE = new Type("default","当类型不存在时默认为这种类型");

    /**
     * Word 2007 的默认单位缩进值是210
     */
    public static final int DOC_INDENT = 210;

    public static final String DOC_TYPE_INFO = "docx";

    public static final String PPT_TYPE_INFO = "ppt";

    public static final String MD_TYPE_INFO = "markdown";

    public static final String DATA_DIRECTORY_PATH = "src/main/resources/data";

    public static final String TEMP_DIRECTORY_PATH = "src/main/resources/temp";

    public static final String TEMP_STRING = "TEMP";
}
