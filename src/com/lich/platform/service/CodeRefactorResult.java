package com.lich.platform.service;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-20
 * Time: 上午11:41
 * <p/>
 * TODO
 */
public class CodeRefactorResult implements Comparable<CodeRefactorResult> {

    private int fileType;
    private int childFileCount;// 子文件数量
    private int lines;// 文件行数
    private int importCount;// 引用其它类数
    private String path;// 路径

    public static final int FILE_TYPE_DIR = 0;
    public static final int FILE_TYPE_FILE = 1;
    public static final int FILE_TYPE_FILE_JAVA = 2;
    // 更多的的文件类型定义
    public static final int FILE_TYPE_FILE_OTHER = 3;


    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    int getChildFileCount() {
        return childFileCount;
    }

    void setChildFileCount(int childFileCount) {
        this.childFileCount = childFileCount;
    }

    @Override
    public int compareTo(CodeRefactorResult another) {
        return this.childFileCount - another.childFileCount;
    }

    public int getImportCount() {
        return importCount;
    }

    public void setImportCount(int importCount) {
        this.importCount = importCount;
    }
}
