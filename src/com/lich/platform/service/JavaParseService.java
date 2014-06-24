package com.lich.platform.service;

import com.log.Log;
import com.util.FileUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 下午2:27
 * <p/>
 * TODO
 */
public class JavaParseService implements IJavaParse {

    public static final String TAG = JavaParseService.class.getSimpleName();

    public static void main(String[] args) {
        JavaParseService service = new JavaParseService();
        service.test();
    }

    public JavaParseService() {

    }

    private HashMap<String, JavaFile> cache = new HashMap<String, JavaFile>();

    public void test() {
        parsePath("/Users/lichsword/Documents/workspace_github/platform/src/com/lich/platform/service/JavaParseService.java");
    }

    private void parsePath(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            parseDir(file, cache);
        } else {
            parseFile(file, cache);
        }
    }

    private void parseDir(File dir, HashMap<String, JavaFile> cache) {
        // TODO
    }

    private void parseFile(File file, HashMap<String, JavaFile> cache) {
        String path = file.getAbsolutePath();
        if (path.endsWith(".java")) {
            parseJavaFile(file, cache);
        } else {
            parseOtherFile(file, cache);
        }
    }

    private void parseJavaFile(File javaFile, HashMap<String, JavaFile> cache) {
        String path = javaFile.getAbsolutePath();
        JavaFile java = new JavaFile();
        java.setPath(path);
        doJava(java);
        cache.put(path, java);
    }

    private void parseOtherFile(File file, HashMap<String, JavaFile> cache) {
        // TODO
    }

    private void doJava(JavaFile java) {
        String path = java.getPath();
        String content = FileUtils.readFile(path);
        javaFile = java;
        state = JavaConstants.STATE_DOCUMENT_START;
        data = content.toCharArray();

        while (true) {
            doChar(data[cursor], data, cursor);
            if (cursor >= data.length) {
                break;
            }// end if

            // 打印日志，并加延时可见性
            Log.e(TAG, "" + data[cursor]);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace(); // TODO
            }
        }
    }

    private int state;

    private JavaFile javaFile;
    private char[] data;
    private int cursor;


    private static final int STATE_TAG_BRACKET_ROUND_BEGIN = 0;
    private static final int STATE_TAG_BRACKET_ROUND_END = 0;
    private static final int STATE_TAG_BRACKET_ANGLE_BEGIN = 0;
    private static final int STATE_TAG_BRACKET_ANGLE_END = 0;
    private static final int STATE_TAG_BRACKET_BIG_BEGIN = 0;
    private static final int STATE_TAG_BRACKET_BIG_END = 0;

    private void doChar(final char ch, final char[] data, final int index) {
        switch (state) {
            case JavaConstants.STATE_DOCUMENT_START:
                switch (ch) {
                    case '/':
                        char next = data[index + 1];
                        if (next == '/' || next == '*') {
                            state = JavaConstants.STATE_COMMENTS_START;
                        } else {
                            state = JavaConstants.STATE_IN_ERROR;
                        }
                        break;
                    case 'p':
                        String word = getWord(data, index);
                        if (word.equals("package")) {
                            // 包名一行，跳过
                            String packageDir = getLine(data, index);
                            Log.e(TAG, "package name=" + packageDir);
                            javaFile.setPackageDir(packageDir);
                            cursor += packageDir.length();
                            // TODO
                        }// end if
                        break;
                    default:
                        break;
                }
                break;
            case JavaConstants.STATE_DOCUMENT_END:

                break;
            case JavaConstants.STATE_CLASS_START:

                break;
            case JavaConstants.STATE_CLASS_END:

                break;
            case JavaConstants.STATE_IN_ERROR:
                Log.e(TAG, "[Error]" + errorContent(data, index));
                break;
            default:
                break;
        }

    }

    private String errorContent(char[] data, int errorPos) {
        return "reason: " + data[errorPos];
    }

    /**
     * 从当前字符向后，查找第1个词
     *
     * @param data
     * @param start
     * @return
     */
    private String getWord(final char[] data, final int start) {
        int end = start;
        while (isAlphabet(data[end])) {
            end++;
        }

        return String.valueOf(data, start, end - start);
    }

    private String getLine(final char[] data, final int start) {
        int pos = start;

        while (!isEnter(data[pos])) {
            pos++;
            if (pos >= data.length) {
                break;
            }
        }

        return String.valueOf(data, start, pos - start);
    }

    private void nextLine() {
        int pos = cursor;
        boolean isLast = false;
        while (!isEnter(data[pos])) {
            pos++;
            if (pos >= data.length) {
                isLast = true;
                break;
            }
        }

        if (!isLast) {
            cursor = pos;
        }
    }

    private boolean isAlphabet(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    private boolean isEnter(char ch) {
        return ch == '\n';
    }


}