package com.lich.platform.service;

import com.log.Log;
import com.util.FileUtils;
import com.util.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

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
        mJavaFile = java;
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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace(); // TODO
            }
        }
    }

    private int state;

    private JavaFile mJavaFile;
    private JavaComment mJavaComment;
    private JavaPackage mJavaPackage;
    private JavaImport mJavaImport;
    private JavaClass mJavaClass;
    private JavaVariable mJavaVariable;
    private JavaFunc mJavaFunc;

    private char[] data;
    private int cursor;
    private int cursorLine;

    private static final int STATE_TAG_BRACKET_ROUND_BEGIN = 0;
    private static final int STATE_TAG_BRACKET_ROUND_END = 0;
    private static final int STATE_TAG_BRACKET_ANGLE_BEGIN = 0;
    private static final int STATE_TAG_BRACKET_ANGLE_END = 0;
    private static final int STATE_TAG_BRACKET_BIG_BEGIN = 0;
    private static final int STATE_TAG_BRACKET_BIG_END = 0;

    private void doChar(final char ch, final char[] data, final int index) throws IllegalStateException {
        Log.e(TAG, JavaConstants.getStateName(state) + " Line " + cursorLine);
        switch (state) {
            case JavaConstants.STATE_DOCUMENT_START:
                mJavaFile = new JavaFile();
                state = JavaConstants.STATE_LINE_START;
                break;
            case JavaConstants.STATE_DOCUMENT_END:
                asset();
                // TODO
                break;
            case JavaConstants.STATE_LINE_START: {
                String line = getLine(data, index);
                String trimLine = line.trim();
                if (trimLine.startsWith("//") || trimLine.startsWith("/*")) {
                    state = JavaConstants.STATE_COMMENTS_START;
                } else if (trimLine.contains("class ")) {
                    state = JavaConstants.STATE_CLASS_START;
                } else if (trimLine.endsWith(";")) {
                    state = JavaConstants.STATE_MEMBER_START;
                } else if (trimLine.endsWith(") {") || trimLine.endsWith("){")) {
                    state = JavaConstants.STATE_FUNC_START;
                } else {
                    Log.e(TAG, "error info:" + line);
                    state = JavaConstants.STATE_IN_ERROR;
                }
                break;
            }
            case JavaConstants.STATE_LINE_END:
                switch (ch) {
                    case '\n':
                        cursor++;
                        cursorLine++;
                        break;
                    case ' ':
                        break;
                    default:
                        state = JavaConstants.STATE_ELEMENT_START;
                        break;
                }
                break;
            case JavaConstants.STATE_COMMENTS_START:
                mJavaComment = new JavaComment();
                char nextChar = nextChar();
                if (nextChar == '/') {
                    // 单行注释
                    mJavaComment.setLineStart(cursorLine);
                    String commentText = getLine(data, index);
                    mJavaComment.setLineCount(1);
                    mJavaComment.setContent(commentText);
                } else if (nextChar == '*') {
                    // 多行注释
                    mJavaComment.setLineStart(cursorLine);
                    // 查找注释尾
                    int end = index;
                    boolean endOfComment = false;
                    while (true) {
                        if (data[end] == '*' && data[end + 1] == '/') {
                            end += 2;
                            endOfComment = true;
                            break;
                        } else {
                            end += 2;
                            if (end >= data.length) {
                                endOfComment = false;
                                break;
                            }// end if
                        }
                    }

                    if (endOfComment) {
                        String commentText = String.valueOf(data, index, end - index);
                        mJavaComment.setContent(commentText);
                        int lineCount = countChar('\n', data, index, end - index) + 1;
                        mJavaComment.setLineCount(lineCount);
                        cursor += commentText.length();
                        cursorLine += lineCount;
                        state = JavaConstants.STATE_COMMENTS_END;
                    } else {
                        state = JavaConstants.STATE_IN_ERROR;
                    }
                }
                break;
            case JavaConstants.STATE_COMMENTS_END: {
                // TODO
//                mJavaComment = null;// reset
                while (getChar(data, cursor) == '\n') {
                    cursor++;// skip '\n'
                    cursorLine += 1;
                }
                state = JavaConstants.STATE_LINE_START;
                break;
            }
            case JavaConstants.STATE_CLASS_START: {
                // 解析类
                String line = getLine(data, index);
                char[] classChars = line.trim().toCharArray();
                String word = null;
                int offset = 0;
                int flag = 0;
                while (true) {
                    word = getWord(classChars, offset);
                    if (word.equals(JavaConstants.KW_PUBLIC)) {
                        flag |= JavaConstants.VISIT_PUBLIC;
                    } else if (word.equals(JavaConstants.KW_PROTECT)) {
                        flag |= JavaConstants.VISIT_PROTECT;
                    } else if (word.equals(JavaConstants.KW_PRIVATE)) {
                        flag |= JavaConstants.VISIT_PRIVATE;
                    } else if (word.equals(JavaConstants.KW_STATIC)) {
                        flag |= JavaConstants.MODIFY_STATIC;
                    } else if (word.equals(JavaConstants.KW_FINAL)) {
                        flag |= JavaConstants.MODIFY_FINAL;
                    } else if (word.equals(JavaConstants.KW_CLASS)) {
                        mJavaClass = new JavaClass();
                        // 检查类注释
                        if (mJavaComment != null) {
                            mJavaClass.setComment(mJavaComment);
                            mJavaComment = null;// reset
                        }// end if

                        // 跳过空格
                        while (' ' == getChar(classChars, offset)) {
                            offset++;

                            if (offset >= line.length()) {
                                break;
                            }// end if
                        }

                        // 取类名
                        String name = getWord(classChars, offset);
                        mJavaClass.setName(name);
                        mJavaClass.setFlag(flag);
                        offset += name.length();
                        break;
                    } else {
                        asset("[" + word + "]");
                    }
                    offset += word.length();
                    if (offset >= line.length()) {
                        break;
                    }// end if

                    // 跳过空格字符
                    while (' ' == getChar(classChars, offset)) {
                        offset++;

                        if (offset >= line.length()) {
                            break;
                        }// end if
                    }

                }

                cursor += line.length();

                while ('\n' == getChar(data, cursor)) {
                    cursor++;// skip '\n'
                    cursorLine++;
                }
                state = JavaConstants.STATE_LINE_START;
//                asset(getLine(data, index));
                break;
            }
            case JavaConstants.STATE_CLASS_END:
                mJavaClass = null;// reset
                break;
            case JavaConstants.STATE_MEMBER_START: {
                String line = getLine(data, index);
                String trimLine = line.trim();
                if (trimLine.startsWith(JavaConstants.KW_PACKAGE)) {
                    state = JavaConstants.STATE_PACKAGE_START;
                } else if (trimLine.startsWith(JavaConstants.KW_IMPORT)) {
                    state = JavaConstants.STATE_IMPORT_START;
                } else {
                    state = JavaConstants.STATE_VARIABLE_START;
                }
                break;
            }
            case JavaConstants.STATE_MEMBER_END: {
                asset();
                break;
            }
            case JavaConstants.STATE_PACKAGE_START: {
                String line = getLine(data, index);
                String trimLine = line.trim();
                String[] piece = trimLine.split(" ");
                mJavaPackage = new JavaPackage();
                mJavaPackage.setPath(piece[1]);
                state = JavaConstants.STATE_PACKAGE_END;
                cursor += line.length();
                break;
            }
            case JavaConstants.STATE_PACKAGE_END:
                mJavaFile.setJavaPackage(mJavaPackage);
                mJavaPackage = null;// reset
                while (getChar(data, cursor) == '\n') {
                    cursor++;// skip '\n'
                    cursorLine += 1;
                }
                state = JavaConstants.STATE_LINE_START;
                break;
            case JavaConstants.STATE_IMPORT_START: {
                String line = getLine(data, index);
                String trimLine = line.trim();
                String[] piece = trimLine.split(" ");
                mJavaImport = new JavaImport();
                mJavaImport.setPath(piece[1]);
                state = JavaConstants.STATE_IMPORT_END;
                cursor += line.length();

                break;
            }
            case JavaConstants.STATE_IMPORT_END:
                mJavaFile.addImport(mJavaImport);
                mJavaImport = null;// reset
                while (getChar(data, cursor) == '\n') {
                    cursor++;// skip '\n'
                    cursorLine += 1;
                }
                state = JavaConstants.STATE_LINE_START;
                break;
            case JavaConstants.STATE_VARIABLE_START: {
                String line = getLine(data, index);
                char[] variableChars = line.trim().toCharArray();
                mJavaVariable = new JavaVariable();
                String word;
                int offset = 0;
                int flag = 0;
                while (true) {
                    word = getWord(variableChars, offset);
                    if (TextUtils.isEmpty(word)) {
                        char currentChar = getChar(variableChars, offset);
                        if (isSpace(currentChar) || isTab(currentChar)) {
                            offset++;
                        } else if (currentChar == '=') {
                            // 变量赋值表达式
                            offset++;
                            String initValue = String.valueOf(variableChars, offset, variableChars.length - offset);
                            mJavaVariable.setInitValue(initValue);
                            offset += initValue.length();
                        }
                        if (offset >= variableChars.length) {
                            break;
                        } else {
                            continue;
                        }
                    } else {
                        word = getWord(variableChars, offset);
                        if (word.equals(JavaConstants.KW_PUBLIC)) {
                            flag |= JavaConstants.VISIT_PUBLIC;
                        } else if (word.equals(JavaConstants.KW_PROTECT)) {
                            flag |= JavaConstants.VISIT_PROTECT;
                        } else if (word.equals(JavaConstants.KW_PRIVATE)) {
                            flag |= JavaConstants.VISIT_PRIVATE;
                        } else if (word.equals(JavaConstants.KW_STATIC)) {
                            flag |= JavaConstants.MODIFY_STATIC;
                        } else if (word.equals(JavaConstants.KW_FINAL)) {
                            flag |= JavaConstants.MODIFY_FINAL;
                        } else {
                            String element = getVariableName(variableChars, offset);
                            if (TextUtils.isEmpty(mJavaVariable.getType())) {
                                mJavaVariable.setType(element);
                            } else {
                                mJavaVariable.setName(element);
                            }
//                            asset("不识别 word=" + word);
                        }

                        offset += word.length();

                    }// end if
                }

                mJavaVariable.setFlag(flag);
                mJavaVariable.setLinePos(cursorLine);

                cursor += line.length();

                state = JavaConstants.STATE_VARIABLE_END;
                break;
            }
            case JavaConstants.STATE_VARIABLE_END: {
                mJavaClass.addMember(mJavaVariable);
                mJavaVariable = null;// reset

                while (getChar(data, cursor) == '\n') {
                    cursor++;// skip '\n'
                    cursorLine += 1;
                }

                state = JavaConstants.STATE_LINE_START;
                break;
            }
            case JavaConstants.STATE_FUNC_START: {
                String line = getLine(data, index);
                char[] variableChars = line.trim().toCharArray();
                mJavaFunc = new JavaFunc();
                String word;
                int offset = 0;
                int flag = 0;
                while (true) {
                    word = getWord(variableChars, offset);
                    if (TextUtils.isEmpty(word)) {
                        char currentChar = getChar(variableChars, offset);
                        if (isSpace(currentChar) || isTab(currentChar)) {
                            offset++;
                        } else if (currentChar == '(') {
                            if (TextUtils.isEmpty(mJavaFunc.getParam())) {
                                int paramEnd = charAt(variableChars, offset, ')');
                                String param = String.valueOf(variableChars, offset, paramEnd - offset);
                                mJavaFunc.setParam(param);
                                offset += param.length();
                                break;
                            } else {
                                asset("" + currentChar);
                            }
                        } else {
                            asset("" + currentChar);
                        }

                        if (offset >= variableChars.length) {
                            break;
                        } else {
                            continue;
                        }
                    } else {
                        word = getWord(variableChars, offset);
                        if (word.equals(JavaConstants.KW_PUBLIC)) {
                            flag |= JavaConstants.VISIT_PUBLIC;
                        } else if (word.equals(JavaConstants.KW_PROTECT)) {
                            flag |= JavaConstants.VISIT_PROTECT;
                        } else if (word.equals(JavaConstants.KW_PRIVATE)) {
                            flag |= JavaConstants.VISIT_PRIVATE;
                        } else if (word.equals(JavaConstants.KW_STATIC)) {
                            flag |= JavaConstants.MODIFY_STATIC;
                        } else if (word.equals(JavaConstants.KW_FINAL)) {
                            flag |= JavaConstants.MODIFY_FINAL;
                        } else {
                            String element = getVariableName(variableChars, offset);
                            if (TextUtils.isEmpty(mJavaFunc.getReturnValue())) {
                                mJavaFunc.setReturnValue(element);
                            } else if (TextUtils.isEmpty(mJavaFunc.getName())) {
                                mJavaFunc.setName(element);
                            } else {
                                asset(element);
                            }
                        }

                        offset += word.length();
                    }// end if
                }

                mJavaFunc.setFlag(flag);

                cursor += line.length();
                cursorLine++;


                state = JavaConstants.STATE_FUNC_END;
                break;
            }
            case JavaConstants.STATE_FUNC_END: {
                // TODO
                asset();
                break;
            }
            case JavaConstants.STATE_ELEMENT_START:
                asset();
                switch (ch) {
                    case '/':
                        char next = nextChar();
                        if (next == '/' || next == '*') {
                            state = JavaConstants.STATE_COMMENTS_START;
                        } else {
                            state = JavaConstants.STATE_IN_ERROR;
                        }
                        break;
                    case 'p':
                        if (getWord(data, index).equals(JavaConstants.KW_PACKAGE)) {
                            // 包名
                            String packageLine = getLine(data, index);
                            Log.e(TAG, "package name=" + packageLine);
//                            mJavaFile.setPackageDir(packageLine);
                            cursor += packageLine.length();
                            state = JavaConstants.STATE_ELEMENT_END;
                        } else if (getWord(data, index).equals(JavaConstants.KW_PUBLIC)) {
                            // public
//                            String line = getLine(data, index);
//                            if (line.trim().endsWith(";")) {
//                                // 成员
//                                Log.e(TAG, "成员:" + line);
//                                state = JavaConstants.STATE_MEMBER_START;
//                            } else if (line.trim().contains("class")) {
//
//                            } else {
//                                // 方法
//                                Log.e(TAG, "方法:" + line);
//                                state = JavaConstants.STATE_FUNC_START;
//                            }
                            // TODO
                        }
                        break;
                    case 'i':
                        if (getWord(data, index).equals(JavaConstants.KW_IMPORT)) {
                            // import 类
                            String importLine = getLine(data, index);
                            Log.e(TAG, "import item=" + importLine);
                            cursor += importLine.length();
                            state = JavaConstants.STATE_ELEMENT_END;
                        } else {
                            // TODO
                        }
                        break;
                    default:
                        cursor++;
                        break;
                }
                break;
            case JavaConstants.STATE_ELEMENT_END:
                asset();
                // TODO
                break;
            case JavaConstants.STATE_IN_ERROR:
                String e = errorContent(data, index);
                throw new IllegalStateException(e);
            default:
                break;
        }

    }

    private String errorContent(char[] data, int errorPos) {
        return "reason: " + data[errorPos];
    }


    private char getChar(final char[] data, final int index) {
        return data[index];
    }

    /**
     * 从当前字符向后，查找第1个字母单词
     *
     * @param data
     * @param start
     * @return 如果当前为非字母，则结束，返回起止之间的字符串作为单词，异常返回"".
     */
    private String getWord(final char[] data, final int start) {
        int end = start;

        while (isAlphabet(data[end])) {
            end++;
        }

        return String.valueOf(data, start, end - start);
    }

    /**
     * 数字,下划,线字线，且首字符非数字
     *
     * @param data
     * @param start
     * @return 返回变量名，异常返回"".
     */
    private String getVariableName(final char[] data, final int start) {
        int end = start;

        // 首字符
        if (isAlphabet(data[end]) || '_' == data[end]) {
            end++;
        } else {
            return "";
        }

        // 后续字符
        while (isAlphabet(data[end]) || isNumber(data[end]) || '_' == data[end]) {
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

    private char nextChar() {
        return data[cursor + 1];
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

    private boolean isSpace(char ch) {
        return ch == ' ';
    }

    private boolean isTab(char ch) {
        return ch == '\t';
    }

    private boolean isAlphabet(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    private boolean isNumber(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    private boolean isEnter(char ch) {
        return ch == '\n';
    }


    /**
     * 从 start 开始向后查找第1个匹配字符，成功返回字符索引，否则返回 -1.
     *
     * @param data
     * @param start
     * @param ch
     * @return
     */
    private int charAt(char[] data, int start, char ch) {
        boolean found = false;

        int pos = JavaConstants.ERROR;
        for (int i = start; i < data.length; i++) {
            if (data[i] == ch) {
                found = true;
                pos = i;
            }// end if
        }
        if (found) {
            return pos;
        } else {
            return JavaConstants.ERROR;
        }
    }

    /**
     * 从 start 起长度为length的字符中，出现ch字符的次数
     *
     * @param ch
     * @param data
     * @param start
     * @param length
     * @return
     */
    private int countChar(char ch, char[] data, int start, int length) {
        int count = 0;
        for (int i = start; i < start + length; i++) {
            if (data[i] == ch) {
                count++;
            }
        }
        return count;
    }

    private String pickData(char[] data, int start, char[] pair) {
        if (pair.length != 2) {
            asset("pickData.func.Param.pair.Length.Error" + pair.length);
        }// end if

        char left = pair[0];
        char right = pair[1];

        LinkedList<String> pairContentList = new LinkedList<String>();

        int s = start;
        int e = start;
        while (true) {
           if(data[s]==left){
               // 找到起始符

           }
        }

    }

    private void asset() {
        throw new IllegalStateException("custom asset");
    }

    private void asset(String info) {
        throw new IllegalStateException("custom asset: 不识别" + info);
    }

}