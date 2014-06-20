package com.lich.platform.service;

import com.jni.UNIX;
import com.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
    public String getReport() {
        return result;
    }

    private String result = "";

    @Override
    public void sortByLines(int flag) {
        List<CodeRefactorResult> list = new ArrayList<CodeRefactorResult>();
        list.addAll(cache.values());
        Collections.sort(list, new CodeRefactorComparator(flag));

        StringBuilder sb = new StringBuilder();
        for (CodeRefactorResult result : list) {
            switch (result.getFileType()) {
                case CodeRefactorResult.FILE_TYPE_DIR:
                    sb.append("Dir");
                    sb.append(" ");
                    sb.append(result.getPath());
                    sb.append("\n");
                    break;
                case CodeRefactorResult.FILE_TYPE_FILE_JAVA:
                    sb.append(result.getLines());
                    sb.append(" ");
                    sb.append(result.getPath());
                    sb.append("\n");
                    break;
                case CodeRefactorResult.FILE_TYPE_FILE_OTHER:
                    break;
                default:
                    break;
            }
        }
        result = sb.toString();
    }

    public void parsePath(String path) {
        if (!cache.containsKey(path)) {
            File file = new File(path);
            if (file.isDirectory()) {
                parseDir(file, cache);
            } else {
                parseFile(file, cache);
            }
        }// end if
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
            result.setFileType(CodeRefactorResult.FILE_TYPE_DIR);
            result.setPath(key);
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
            result.setFileType(CodeRefactorResult.FILE_TYPE_FILE_OTHER);
            result.setPath(key);
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
        // 填写参数
        result.setFileType(CodeRefactorResult.FILE_TYPE_FILE_JAVA);
        result.setPath(javaFilePath);
        result.setLines(Integer.valueOf(parts[0]));
        result.setChildFileCount(1);
        // 保存
        cache.put(javaFilePath, result);
    }
}