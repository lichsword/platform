package com.lich.platform.service;

import com.lich.platform.Constants;
import com.log.TimeInfo;
import com.util.TextUtils;
import com.util.UnixUtils;

import java.io.File;
import java.util.*;

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
//        service.parsePath("/Users/lichsword/Documents/workspace_github/platform/src/com/lich/platform/service");
//        service.parsePath("/Users/lichsword/Documents/workspace_github/platform/src/com/log/Log.java");
        service.parsePath("/Users/lichsword/Documents/workspace_github/platform/src/com/jni");
        service.sortByImportNum(Constants.fDesc);
    }

    @Override
    public String getReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(parsePathReport);


        sb.append(" in ");
        if (null != parsePathTimeInfo) {
            sb.append(parsePathTimeInfo.getReport());
            sb.append("(parse)");
        }// end if

        if (null != sortedByLineTimeInfo) {
            sb.append(" & ");
            sb.append(sortedByLineTimeInfo.getReport());
            sb.append("(sort)");
        }// end if

        sb.append("\n");
        if (!TextUtils.isEmpty(sortedByLineReport)) {
            sb.append(sortedByLineReport);
        }   // end if

        if (!TextUtils.isEmpty(sortedByImportReport)) {
            sb.append(sortedByImportReport);
        }   // end if
        result = sb.toString();
        return result;
    }

    private String sortedByLineReport = "";
    private TimeInfo sortedByLineTimeInfo = null;

    private String parsePathReport = "";
    private TimeInfo parsePathTimeInfo = null;

    private String sortedByImportReport = "";
    private TimeInfo sortedByImportTimeInfo = null;

    private String result = "";

    @Override
    public void sortByLines(int flag) {
        long start = System.currentTimeMillis();

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

        long end = System.currentTimeMillis();
        sortedByLineTimeInfo = new TimeInfo(start, end);
        sortedByLineReport = sb.toString();
    }

    @Override
    public void sortByImportNum(int flag) {
        long start = System.currentTimeMillis();

        List<CodeRefactorResult> list = new ArrayList<CodeRefactorResult>();
        list.addAll(cache.values());
        Collections.sort(list, new CodeRefactorComparatorImportCount(flag));

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
                    sb.append(result.getImportCount());
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

        long end = System.currentTimeMillis();
        sortedByImportTimeInfo = new TimeInfo(start, end);
        sortedByImportReport = sb.toString();
    }

    public void parsePath(String path) {
        cache.clear();
        sortedByImportReport = "";
        sortedByImportTimeInfo = null;
        sortedByLineReport = "";
        sortedByLineTimeInfo = null;
        result = "";
        parsePathReport = "";
        parsePathTimeInfo = null;

        long start = System.currentTimeMillis();
        if (!cache.containsKey(path)) {
            File file = new File(path);
            if (file.isDirectory()) {
                parseDir(file, cache);
            } else {
                parseFile(file, cache);
            }
        }// end if

//        try {
//            Thread.sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace(); // TODO
//        }
        long end = System.currentTimeMillis();
        parsePathTimeInfo = new TimeInfo(start, end);
        Collection<CodeRefactorResult> values = cache.values();
        parsePathReport = String.format(Constants.FORMAT_HANDLE_FILES, values.size());
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
        // 解析行数
        int lines = UnixUtils.getFileLineCount(javaFilePath);
        // 解析import
        int imports = UnixUtils.getImportCount(javaFilePath);
        // ------------ 填写参数 ------------
        result.setFileType(CodeRefactorResult.FILE_TYPE_FILE_JAVA);
        result.setPath(javaFilePath);
        result.setLines(lines);
        result.setImportCount(imports);
        result.setChildFileCount(1);
        // 保存
        cache.put(javaFilePath, result);
    }
}