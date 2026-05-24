package com.jonhu.core.builder.presentation;

import com.jonhu.util.Constant;
import com.jonhu.util.Settings;
import com.jonhu.util.Utils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 处理演示文稿的合并问题
 */
public class PresentationMerger {

    private String destPath;

    private String[] srcPaths;

    /**
     * dest中的幻灯片总数
     */
    private int counter;

    /**
     * 第一个srcPath中的幻灯皮页数
     */
    private int firstSrcCounter;

    /**
     * 构造合并文件对象
     * @param destPath 合并输出文件
     * @param srcPaths 待合并的文件
     */
    public PresentationMerger(String destPath, String... srcPaths) {
        this.destPath = destPath;
        this.srcPaths = srcPaths;
        this.counter = 0;
    }

    /**
     * 删除源文件
     */
    public void deleteSrcFiles() {}

    /**
     * 开始合并
     */
    public void merge() throws IOException {
        //1. 统计个数创建dest.pptx
        countSlides();
        createDestPptx();
        //2. 重命名
        rename("zip");
        //3. 拷贝slide\d.xml文件
        decompressFile();
        renameSlideXMLs();
        copyAndReplace();
        //4. 重命名
        rename("pptx");
    }

    private void createDestPptx() throws IOException {
        Rectangle rectangle = Settings.textFrameRectangles.get(Constant.TEMP_STRING);
        XMLSlideShow pptx = new XMLSlideShow();
        OutputStream os = Files.newOutputStream(Paths.get(destPath));
        for (int i = 0; i < counter; i++) {
            XSLFSlide slide = pptx.createSlide();
            slide.createAutoShape().setAnchor(rectangle);
        }
        pptx.write(os);

        os.close();
        pptx.close();
    }

    private void countSlides() throws IOException {
        for (int i = 0; i < srcPaths.length; i++) {
            InputStream is = Files.newInputStream(Paths.get(srcPaths[i]));
            XMLSlideShow srcSlideShow = new XMLSlideShow(is);
            counter += srcSlideShow.getSlides().size();
            if (i == 0) {
                firstSrcCounter = counter;
            }
            srcSlideShow.close();
            is.close();
        }
    }

    private void rename(String extension) {
        for (int i = 0; i < srcPaths.length; i++) {
            String dest = getDestPath(srcPaths[i], extension);
            new File(srcPaths[i]).renameTo(new File(dest));
            srcPaths[i] = dest;
        }
        String dest = getDestPath(destPath, extension);
        new File(destPath).renameTo(new File(dest));
        destPath = dest;
    }

    private String getDestPath(String srcPath, String extension) {
        Path path = Paths.get(srcPath);
        return String.format("%s%s%s.%s", path.getParent(), File.separator, path.getFileName().toString().split("\\.")[0], extension);
    }

    private void copyAndReplace() throws IOException {
        String slideXMLFilePath = "/ppt/slides";
        String targetDirPath = getDepressedPath(destPath)+"/ppt/slides/";
        for (String srcPath : srcPaths) {
            String dirPath = getDepressedPath(srcPath) + slideXMLFilePath;
            Files.list(Paths.get(dirPath)).forEach(path -> {
                if (Files.isRegularFile(path)) {
                    Utils.copyAndReplace(path, Paths.get(targetDirPath+path.getFileName().toString()));
                }
            });
        }
        Utils.ZipUtils.compressAndReplaceDir(getDepressedPath(destPath), destPath);
    }

    private void renameSlideXMLs() throws IOException {
        //从第二个srcPath开始遍历重命名slide\d.xml
        String slideXMLFilePath = "/ppt/slides";
        for (int i = 1, renamed = firstSrcCounter; i < srcPaths.length; i++) {
            String path = getDepressedPath(srcPaths[i]) + slideXMLFilePath;
            long size = Files.list(Paths.get(path)).count() - 1;
            //为了避免重名应当倒着遍历
            for (long j = size; j > 0; j--) {
                new File(String.format("%s%sslide%d.xml", path, File.separator, j)).renameTo(new File(String.format("%s%sslide%d.xml", path, File.separator, j + renamed)));
            }
            renamed+=size;
        }
    }

    private String getDepressedPath(String srcPath) {
        return String.format("%s%s%s", Constant.TEMP_DIRECTORY_PATH, File.separator, Paths.get(srcPath).getFileName().toString().split("\\.")[0]);
    }

    private void decompressFile() throws IOException {
        for (String srcPath : srcPaths) {
            Utils.ZipUtils.decompressAndReplaceFile(srcPath, getDestDirPath(srcPath));
        }
        Utils.ZipUtils.decompressAndReplaceFile(destPath, getDestDirPath(destPath));
    }

    private String getDestDirPath(String srcPath) {
        return String.format("%s%s%s", Constant.TEMP_DIRECTORY_PATH, File.separator, Paths.get(srcPath).getFileName().toString().split("\\.")[0]);
    }
}
