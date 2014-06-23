package com.util;

import com.jni.UNIX;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-21
 * Time: 下午12:51
 * <p/>
 * TODO
 */
public class UnixUtils {

    public static int getFileLineCount(String filepath) {
        int result = 0;
        File file = new File(filepath);
        if (file.exists()) {
            String cmdWc = "wc -l " + filepath;
            final String TEMP_WC_LOG = "temp_wc.log";
            FileUtils.deleteFile(TEMP_WC_LOG);
            UNIX.invoke(cmdWc, TEMP_WC_LOG);
            String log = FileUtils.readFile(TEMP_WC_LOG).trim();
            String[] logSplit = log.split(" ");
            String numStr = logSplit[0];
            try {
                result = Integer.valueOf(numStr);
            } catch (NumberFormatException e) {
                result = 0;
            }
        } else {
            result = 0;
        }
        return result;
    }

    public static int getImportCount(String filepath) {
        File file = new File(filepath);
        int count = 0;
        if (file.exists()) {
            final String TEMP_IMPORT_LOG = "import.log";
            FileUtils.deleteFile(TEMP_IMPORT_LOG);// 删除旧的
            String cmdGrep = "grep '^import ' " + filepath; // 以import 为行首的
            UNIX.invoke(cmdGrep, TEMP_IMPORT_LOG);
            count = UnixUtils.getFileLineCount(TEMP_IMPORT_LOG);
        } else {
            count = 0;
        }
        return count;
    }
}
