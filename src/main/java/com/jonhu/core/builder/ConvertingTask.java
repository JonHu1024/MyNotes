package com.jonhu.core.builder;

import com.jonhu.core.parser.Parser;
import com.jonhu.core.styles.Type;
import com.jonhu.util.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * 文件转化任务启动类
 */
public class ConvertingTask {
    /**
     * 源文件和目标目录
     */
    private Path src,dest;

    private Builder builder;

    private Parser parser;

    /**
     * 是否有自定义的方法
     */
    private Function<String, String> action;

    public ConvertingTask(File src, File dest, Builder builder, Parser parser) throws IOException {
        this.src = src.toPath();
        this.dest = dest.toPath();
        this.builder = builder;
        this.parser = parser;
        this.action = null;
    }

    private ConvertingTask(Path src, Path dest, Builder builder, Parser parser) throws IOException {
        this.src = src;
        this.dest = dest;
        this.builder = builder;
        this.parser = parser;
        this.action = null;

        if(src == null || dest == null) {
            throw new IOException("The srcFile or the destFile can't be null");
        }
    }

    /**
     * 创建一个文件转换任务
     * @param src 要进行转换的源文件
     * @param dest 生成的文件,含路径和后缀名
     * @param builder 执行转换任务的构建构建器
     * @param parser 执行文件格式分析的解析器.
     */
    public static ConvertingTask createTask(Path src, Path dest, Builder builder, Parser parser) throws IOException {
        return new ConvertingTask(src, dest, builder, parser);
    }

    /**
     * 添加自定义的行为.
     * @param customizedAction 该函数式对象用于用户处理源文件的每一行文字,并返回处理后的结果.
     */
    public ConvertingTask addCustomizedAction(Function<String, String> customizedAction) {
        this.action = action;
        return this;
    }

    public void start() throws Exception {
        BufferedReader reader = Files.newBufferedReader(src);
        String msg;
        while ((msg = reader.readLine()) != null) {
            if (action != null) {
                msg = action.apply(msg);
            }
            msg = handleMessage(msg);
            builder.addText(parser.getRichTextList(msg),parser.parseText(msg));
        }
        reader.close();
        builder.save(dest);
        //重置builder
        builder = builder.createNewBuilder();
    }

    private String handleMessage(String msg) {
        //除去[ZWNBSP]零宽度空白字符
        if (msg.startsWith(Constant.UTF8_BOM)) {
            msg = msg.substring(1);
        }
        //处理空白行,用零宽度占位符解决
        if (msg.length() == 0 || msg.equals("\n")) {
            msg = Constant.UTF8_BOM;
        }
        //处理引用格式
        if(parser.getType(msg).equals(new Type(">", null)) ) {
            msg = msg.replace(">", "<quoteAddition> </quoteAddition>");
        }
        return msg;
    }
}
