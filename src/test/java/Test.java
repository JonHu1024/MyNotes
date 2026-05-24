import com.jonhu.core.builder.Builder;
import com.jonhu.core.builder.ConvertingTask;
import com.jonhu.core.builder.presentation.AnimatedPresentationBuilder;
import com.jonhu.core.parser.MyNotesFormatParser;
import com.jonhu.core.parser.Parser;
import com.jonhu.core.styles.Format;
import com.jonhu.core.styles.Type;
import com.jonhu.util.Constant;
import com.jonhu.util.Settings;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args)  {
        Settings.defaultInitialize();

        //设置样式
        Map<Type, Format> formats = new HashMap<>();
        setFormats(formats);
        Settings.setFormats(formats);

        File src = new File("src/main/resources/example/presentation/example3.txt");
        File dest = new File("src/main/resources/example/presentation/example3.pptx");
        Builder builder = new AnimatedPresentationBuilder();
        Parser parser = new MyNotesFormatParser();

        try {
            new ConvertingTask(src,dest,builder,parser).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setFormats(Map<Type, Format> formats) {
        formats.put(new Type("(\\d)+\\.", "一级标题"),
                Format.getInstance(false, Color.BLUE, "微软雅黑", 24, true, false, false, null, false));
        formats.put(new Type("(\\d)+\\.\\d", "二级标题"),
                Format.getInstance(false, Color.RED, "宋体", 21, true, false, false, null, false));
        formats.put(new Type("((\\d)+\\.){2}\\d", "三级标题"),
                Format.getInstance(false, Color.RED, "宋体", 18, true, true, false, null, false));
        formats.put(new Type("((\\d)+\\.)+\\d", "小标题"),
                Format.getInstance(false, Color.BLACK, "宋体", 18, true, true, false, null, false));
        formats.put(new Type("-", "无序列表"),
                Format.getInstance(false, Color.BLACK, "宋体", 18, false, false, false, null, false));
        formats.put(new Type("(\\d)+", "有序列表"),
                Format.getInstance(false, Color.BLACK, "宋体", 18, false, false, false, null, false));
        formats.put(new Type(">", "引用"),
                Format.getInstance(true, Color.GRAY, "宋体", 16, false, false, false, null, false));
        formats.put(Constant.DEFAULT_TYPE,
                Format.getInstance(true, Color.BLACK, "宋体", 18, false, false, false, null, false));
    }


}
