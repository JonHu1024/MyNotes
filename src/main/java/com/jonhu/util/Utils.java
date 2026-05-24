package com.jonhu.util;

import com.jonhu.core.styles.Format;
import com.jonhu.core.styles.Style;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {
    public static String rgbToHex(Color color) {
        String red = Integer.toHexString(color.getRed());
        String green = Integer.toHexString(color.getGreen());
        String blue = Integer.toHexString(color.getBlue());
        red = addColorZero(red);
        green = addColorZero(green);
        blue = addColorZero(blue);
        return (red + green + blue).toUpperCase();
    }

    private static String addColorZero(String color) {
        if (color.length() <= 1) {
            return "0"+color;
        }
        return color;
    }

    /**
     * 获取制定字符串的起始空格数目
     * @param string
     * @return
     */
    public static int getStartingSpaceNumber(String string) {
        char[] chars = string.toCharArray();
        int count = 0;
        for (; count < chars.length; count++) {
            if (chars[count] == ' ') {
                count++;
            } else {
                return count;
            }
        }
        return count;
    }

    /**
     * 查找某小段字符串在整个字符串出现的索引
     * @param string 整个字符串
     * @param symbol 要查找的内容
     */
    public static List<Integer> findIndexesOf(String string,String symbol) {
        if (string == null || symbol == null) {
            throw new NullPointerException("字符串不可以为空");
        }
        List<Integer> list = new ArrayList<>();
        int index = -1;
        int symbolLength = symbol.length();
        do {
            index = string.indexOf(symbol);
            if (index != -1) {
                string.substring(index + symbolLength);
            }
        } while (index != -1);
        return list;
    }

    /**
     * 弹出一个JavaFX提示框(要确保已经有JavaFX应用程序正在运行,否则会抛出异常)
     * @param header 标题
     * @param content 内容
     */
    public static void alert(String header,String content) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setHeaderText(header);
        dialog.setContentText("加载中");
        dialog.setContentText(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Constant.ICON_IMAGE_PATH));
        dialog.show();
    }

    /**
     * 根据指定样式列表获取其对应的Format对象
     * @param styles 指定的样式列表
     * @param defaultFormat 默认的Format对象
     * @return 对应的Format对象
     */
    public static Format getFormatOf(Set<Style> styles,Format defaultFormat) {
        for (Style style : styles) {
            defaultFormat = style.getStyleSetter().apply(defaultFormat);
        }
        return defaultFormat;
    }

    /**
     * 根据指定PPTX文件读取其中第一张幻灯片上的第一个矩形
     * @param file 指定PPTX文件
     * @return 对应的Rectangle对象
     */
    public static Rectangle2D getRectangleFromPresentation(File file) {
        try {
            XMLSlideShow pptx = new XMLSlideShow(Files.newInputStream(file.toPath()));
            return pptx.getSlides().get(0).getShapes().get(0).getAnchor();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把指定的对象存储到指定文件中
     */
    public static void saveObjectToFile(Object object, File file) {
        try (ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
            os.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从指定文件中读取一个对象
     */
    public static Object readAnObjectFromFile(File file) {
        try (ObjectInputStream is = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
            return is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拷贝文件,如果目标文件已经存在则替换掉
     * @param source
     * @param target
     */
    public static void copyAndReplace(Path source, Path target) {
        try {
            if (Files.exists(target)) {
                Files.delete(target);
            }
            Files.copy(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除目录下的文件，但保留目录
     */
    public static void clearFilesOfDir(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);

        //If the file is a directory then the directory must be empty.This method can be used with the walkFileTree method to delete a directory and all entries in the directory, or an entire file-tree where required
        //Files.delete(path);
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static class ZipUtils {
        /**
         * 解压缩文件
         * @param srcPath 要解压的文件
         * @param destDirPath 目的地文件夹
         * @throws FileAlreadyExistsException 如果遍历时目标文件已经存在
         */
        public static void decompressFile(String srcPath, String destDirPath) throws IOException {
            decompress(srcPath, destDirPath, false);
        }

        /**
         * 解压缩文件,如果遍历时目标文件存在则替换掉
         * @param srcPath
         * @param destDirPath
         * @throws IOException
         */
        public static void decompressAndReplaceFile(String srcPath, String destDirPath) throws IOException {
            decompress(srcPath, destDirPath, true);
        }

        private static void decompress(String srcPath, String destDirPath, boolean replace) throws IOException {
            Path destDir = Paths.get(destDirPath);

            if (Files.notExists(destDir)) {
                Files.createDirectories(destDir);
            }

            FileSystem fileSystem = FileSystems.newFileSystem(Paths.get(srcPath), ZipUtils.class.getClassLoader());
            Files.walkFileTree(fileSystem.getPath("/"), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path dest = Paths.get(destDirPath, file.toString());
                    Path parent = dest.getParent();
                    if (Files.notExists(parent)) {
                        Files.createDirectories(parent);
                    } if (replace && Files.exists(dest)) {
                        Files.delete(dest);
                    }
                    Files.copy(file, dest);
                    return FileVisitResult.CONTINUE;
                }
            });
            fileSystem.close();
        }

        /**
         * 压缩目录
         * @param srcDirPath 要压缩的目录
         * @param destPath 输出文件(*.zip),如果已经存在则替换掉
         * @throws IOException
         */
        public static void compressAndReplaceDir(String srcDirPath, String destPath) throws IOException {
            Path path = Paths.get(destPath);
            OutputStream os = Files.newOutputStream(path);
            ZipOutputStream zos = new ZipOutputStream(os);
            compress(srcDirPath, zos, 1);
            zos.finish();
            zos.close();
            os.close();
        }
        private static void compress(String srcDirPath, ZipOutputStream zos, int level) throws IOException {
            Files.list(Paths.get(srcDirPath)).forEach(path -> {
                try {
                    if (Files.isDirectory(path)) {
                        compress(path.toString(), zos, level+1);
                    } else {
                        String[] paths = path.toString().replaceAll("\\\\", "/").split("/");
                        StringBuilder name = new StringBuilder(paths[paths.length - 1]);
                        for (int i = paths.length - 1 - 1; i >= paths.length - level; i--) {
                            name.insert(0, String.format("%s/", paths[i]));
                        }
                        zos.putNextEntry(new ZipEntry(name.toString()));
                        writeTo(zos, path);
                        zos.closeEntry();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        private static void writeTo(ZipOutputStream zos, Path path) throws IOException {
            InputStream is = Files.newInputStream(path);
            byte[] flush = new byte[1024 * 10];
            int length = 0;
            while ((length = is.read(flush)) != -1) {
                zos.write(flush, 0, length);
            }
            is.close();
        }

    }

    public static class StringUtils {
        /**
         * 支持正则表达式的indexOf()
         */
        public static int indexOfAll(String text, String regex) {
            Matcher matcher = Pattern.compile(regex).matcher(text);
            if (matcher.find()) {
                return matcher.start();
            }
            return -1;
        }
    }
}