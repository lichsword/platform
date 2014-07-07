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
//        parsePath("/Users/lichsword/Documents/workspace_github/platform/src/com/lich/platform/service/JavaParseService.java");
        parsePath("/Users/lichsword/Documents/workspace_github/platform/src/com/lich/platform/service/JavaClass.java");
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
            Log.e(TAG, "JavaParseService.doJava.While.char= " + data[cursor]);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace(); // TODO
//            }

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
    /**
     * // TODO 这一块要让cursor 与 cursorLine 同步
     */
    private int cursor;
    private int cursorLine;

    private void doChar(final char ch, final char[] data, final int index) throws IllegalStateException {
        Log.e(TAG, JavaConstants.getStateName(state) + " L=" + cursorLine + " C=" + cursor);
//        Log.e(TAG, JavaConstants.getStateName(state) + " char = " + index);
        switch (state) {
            case JavaConstants.STATE_DOCUMENT_START:
                mJavaFile = new JavaFile();
                state = JavaConstants.STATE_LINE_START;
                break;
            case JavaConstants.STATE_DOCUMENT_END: {
                asset();
                // TODO
                break;
            }
            case JavaConstants.STATE_LINE_START: {
                String line = getLine(data, index);

//                {
//                    检测正确性
//                    String checkLine = getLineByLineIndex(data, cursorLine);
//                    if (!checkLine.equals(line)) {
//                        say("line number mistake!!! \nleft = " + checkLine + "\nright = " + line);
//                    } else {
//                        Log.e(TAG, "line = " + line);
//                    }
//                }

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
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
                break;
            case JavaConstants.STATE_COMMENTS_START:
                mJavaComment = new JavaComment(data, cursor, cursorLine);
                cursor += mJavaComment.getLength();
                cursorLine += (mJavaComment.getLineCount() - 1);
                mJavaComment = null;// reset TODO hold wait class new.
                state = JavaConstants.STATE_COMMENTS_END;
                break;
            case JavaConstants.STATE_COMMENTS_END: {
                // mJavaComment 的 reset 放到 JavaClass 之后
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
                break;
            }
            case JavaConstants.STATE_CLASS_START: {
                mJavaClass = new JavaClass(data, cursor, cursorLine);
                cursor += mJavaClass.getLength();
                cursorLine += (mJavaClass.getLineCount() - 1);
                mJavaClass = null;// reset
                state = JavaConstants.STATE_CLASS_END;
                break;
            }
            case JavaConstants.STATE_CLASS_END:
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
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
                mJavaPackage = new JavaPackage(data, cursor, cursorLine);
                state = JavaConstants.STATE_PACKAGE_END;
                break;
            }
            case JavaConstants.STATE_PACKAGE_END:
                mJavaFile.setJavaPackage(mJavaPackage);
                cursor += mJavaPackage.getLength();
                mJavaPackage = null;// reset
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
                break;
            case JavaConstants.STATE_IMPORT_START: {
                mJavaImport = new JavaImport(data, cursor, cursorLine);
                state = JavaConstants.STATE_IMPORT_END;
                break;
            }
            case JavaConstants.STATE_IMPORT_END:
                mJavaFile.addImport(mJavaImport);
                cursor += mJavaImport.getLength();
                mJavaImport = null;// reset
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
                break;
            case JavaConstants.STATE_VARIABLE_START: {

//                if (cursorLine == 77) {
//                    Log.e(TAG, "");
//                }// end if

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
                        if (TextUtils.isSpace(currentChar) || TextUtils.isTab(currentChar)) {
                            offset++;
                        } else if (currentChar == '=') {
                            // 变量赋值表达式
                            offset++;
                            String initValue = String.valueOf(variableChars, offset, variableChars.length - offset);
                            mJavaVariable.setInitValue(initValue);
                            offset += initValue.length();
                        } else if (currentChar == '<') {
                            char[] pair = {'<', '>'};
                            String expand = pickData(variableChars, offset, pair);
                            if (!TextUtils.isEmpty(mJavaVariable.getType())) {
                                // 说明是泛型类型
                                String totalType = mJavaVariable.getType() + expand;
                                mJavaVariable.setType(totalType);
                                offset += expand.length();
                            } else {
                                // 说明是'='之后的泛型定义，可以跳过
                                break;
                            }
                            break;
                        } else if (currentChar == ';') {
                            // To the end.
                            offset++;
                        } else {
                            // TODO <,>  hashmap
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
//                            say("不识别 word=" + word);
                        }

                        offset += word.length();

                    }// end if
                }// end while

                mJavaVariable.setFlag(flag);
//                mJavaVariable.setLinePos(cursorLine);

                cursor += line.length();

                state = JavaConstants.STATE_VARIABLE_END;
                break;
            }
            case JavaConstants.STATE_VARIABLE_END: {
                mJavaClass.addMember(mJavaVariable);
                mJavaVariable = null;// reset

                skipEnter();

                state = JavaConstants.STATE_LINE_START;
                break;
            }
            case JavaConstants.STATE_FUNC_START: {
                mJavaFunc = new JavaFunc(data, cursor, cursorLine);
                cursor += mJavaFunc.getLength();
                break;
            }
            case JavaConstants.STATE_FUNC_END: {
                if (null != mJavaClass) {
                    mJavaClass.addFunc(mJavaFunc);
                }// end if

                mJavaFunc = null;// reset

                if (getChar(data, cursor) == '}') {
                    cursor++;// skip function's '}'
                }// end if

                skipEnter();
                state = JavaConstants.STATE_LINE_START;
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

        while (TextUtils.isAlphabet(data[end])) {
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
        if (TextUtils.isAlphabet(data[end]) || '_' == data[end]) {
            end++;
        } else {
            return "";
        }

        // 后续字符
        while (TextUtils.isAlphabet(data[end]) || TextUtils.isNumber(data[end]) || '_' == data[end]) {
            end++;
        }

        return String.valueOf(data, start, end - start);
    }

    /**
     * 读取一行，从当前字符向后的一行内容
     *
     * @param data
     * @param start
     * @return
     */
    private String getLine(final char[] data, final int start) {
        int pos = start;

        while (!TextUtils.isEnter(data[pos])) {
            pos++;
            if (pos >= data.length) {
                break;
            }
        }

        return String.valueOf(data, start, pos - start);
    }

    private String getLineByLineIndex(final char[] data, int lineIndex) {
        int now = 0;
        int startCharIndex = 0;

        if (lineIndex == 0) {
            startCharIndex = 0;
            return getLine(data, startCharIndex);
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == '\n') {
                if (now == lineIndex) {
                    startCharIndex = i + 1;// 下一个字符就是起始行
                    break;
                }// end if
                now++;
            }// end if
        }

        return getLine(data, startCharIndex);
    }

    private char nextChar() {
        return data[cursor + 1];
    }

    private void nextLine() {
        int pos = cursor;
        boolean isLast = false;
        while (!TextUtils.isEnter(data[pos])) {
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
        LinkedList<Character> operStack = new LinkedList<Character>();

        int s = start;
        int e = start;
//        operStack.push(left);
        while (true) {
            if (data[e] == left) {
                // 找到起始符
                operStack.push(left);
            } else if (data[e] == right) {
                operStack.pop();
                if (operStack.isEmpty()) {
                    // 找到匹配的终止符
                    pairContentList.push(String.valueOf(data, s, e - s + 1));
                    break;
                } else {
                    // 找到小块的匹配
                }
            }
            e++;
            if (e >= data.length) {
                break;
            }// end if

        }
        return pairContentList.pop();
    }

    private void asset() {
        throw new IllegalStateException("custom say");
    }

    private void asset(String info) {
        throw new IllegalStateException("custom say: 不识别" + info);
    }

    private void skipEnter() {
        while (getChar(data, cursor) == '\n') {
            cursor++;// skip '\n'
            cursorLine++;

            if (cursor >= data.length) {
                break;
            }// end if
        }
    }

}