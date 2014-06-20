package com.lich.platform.service;

import com.jni.UNIX;
import com.util.FileUtil;

import java.io.File;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-19
 * Time: 下午3:06
 * <p/>
 * 基于根目录的代码重构
 */
public class CodeRefactorService implements ICodeRefactor {

    private HashMap<String, CodeRefactorResult> cache = new HashMap<String, CodeRefactorResult>();

    class CodeRefactorResult {

        private int childFileCount;// 子文件数量
        private int lines;// 文件行数
        private String desc;// 描述

        int getChildFileCount() {
            return childFileCount;
        }

        void setChildFileCount(int childFileCount) {
            this.childFileCount = childFileCount;
        }
    }

    public static void main(String[] args) {
        System.loadLibrary("unix");
        HashMap<String, CodeRefactorResult> cache = new HashMap<String, CodeRefactorResult>();
        CodeRefactorService service = new CodeRefactorService();
    }

    @Override
    public int getFileCount(String rootDir) {
        CodeRefactorResult result;

        if (cache.containsKey(rootDir)) {

        } else {
            File file = new File(rootDir);
            if (file.isDirectory()) {
                parseDir(file, cache);
            } else {
                parseFile(file, cache);
            }
        }

        result = cache.get(rootDir);
        return result.getChildFileCount();
    }

    @Override
    public String getReport(String rootDir) {
        if (!cache.containsKey(rootDir)) {
            parsePath(rootDir);
        }// end if

        CodeRefactorResult result = cache.get(rootDir);

        return null;  // TODO
    }

    private void parsePath(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            parseDir(file, cache);
        } else {
            parseFile(file, cache);
        }
    }

    /**
     * @param dir
     * @return
     */
    private void parseDir(File dir, HashMap<String, CodeRefactorResult> cache) {

        String key = dir.getAbsolutePath();
        if (cache.containsKey(key)) {
            // 已经缓存，就不再解析
            return;
        } else {
            // 开始解析
            File[] childFiles = dir.listFiles();
            CodeRefactorResult result = new CodeRefactorResult();
            result.setChildFileCount(childFiles.length);
            cache.put(key, result);
            // 继续解析子文件
            for (File file : childFiles) {
                if (file.isDirectory()) {
                    parseDir(file, cache);
                } else {
                    parseFile(file, cache);
                }
            }
        }

    }

    private void parseFile(File file, HashMap<String, CodeRefactorResult> cache) {
        String key = file.getAbsolutePath();
        if (key.endsWith(".java")) {
            parseJavaFile(file, cache);
        } else {
            // do nothing
            CodeRefactorResult result = new CodeRefactorResult();
            result.setChildFileCount(1);

            cache.put(key, result);
        }
    }

    private void parseJavaFile(File javaFile, HashMap<String, CodeRefactorResult> cache) {
        CodeRefactorResult result = new CodeRefactorResult();

        String javaFilePath = javaFile.getAbsolutePath();
        result.setChildFileCount(1);
        // 读取行数
        UNIX.wc("-l", javaFilePath);
        String content = FileUtil.readFile("wc.log").trim();
        String[] parts = content.split(" ");
        result.setChildFileCount(Integer.valueOf(parts[0]));
        // 保存
        cache.put(javaFilePath, result);
    }
}
