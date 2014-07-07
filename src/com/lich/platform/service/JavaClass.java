package com.lich.platform.service;

import com.log.Assert;
import com.log.Log;
import com.util.TextUtils;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 下午2:40
 * <p/>
 * TODO
 */
public class JavaClass extends JavaElement {

    private static final String TAG = JavaClass.class.getSimpleName();

    private int flag;
    private String name;
    private JavaComment comment;
    private ArrayList<JavaFunc> funcs = new ArrayList<JavaFunc>();
    private ArrayList<JavaVariable> memses = new ArrayList<JavaVariable>();
    private ArrayList<JavaClass> innerClasses = new ArrayList<JavaClass>();

    public JavaClass(char[] data, int cursor, int cursorLine) {

        int end;
        int enterNum = 0;

        char[] pair = {'{', '}'};
        end = findPairEnd(data, cursor, pair);
        for (int i = cursor; i < end; i++) {
            if (data[i] == '\n') {
                enterNum++;
            }// end if
        }

        super.start = cursor;
        super.length = end - cursor + 1;
        super.data = data;
        super.lineStart = cursorLine;
        super.lineEnd = cursorLine + enterNum;

        this.cursor = cursor;
        this.cursorLine = cursorLine;

        doClass();
    }


    int state;
    int cursor;
    int cursorLine;

    private JavaFunc mJavaFunc;
    private JavaVariable mJavaVariable;
    private JavaComment mJavaComment;

    private void doClass() {
        state = JavaConstants.STATE_LINE_START;
        // 开始解析
        int end = super.start + super.length;
        while (true) {
            doChar(data[cursor], data, cursor);
            if (cursor >= end) {
                break;
            }// end if

            // 打印日志，并加延时可见性
            Log.e(TAG, "JavaClass.doJava.While.char= " + data[cursor]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace(); // TODO
            }
        }// end while
    }

    private void doChar(final char ch, final char[] data, final int index) throws IllegalStateException {
        Log.e(TAG, JavaConstants.getStateName(state) + " L=" + cursorLine + " C=" + cursor);
        switch (state) {
            case JavaConstants.STATE_CLASS_START:
                state = JavaConstants.STATE_CLASS_HEAD_START;
                break;
            case JavaConstants.STATE_CLASS_END:
                Assert.say("reach class end.");
                break;
            case JavaConstants.STATE_CLASS_HEAD_START: {

                int start = cursor;
                int end = start;
                for (; end < data.length; end++) {
                    if (data[end] == '{') {
                        break;
                    }
                }
                int length = end - start + 1;
                String headLine = String.valueOf(data, start, length);
                parseHeadLine(headLine);
                cursor += length;
                state = JavaConstants.STATE_CLASS_HEAD_END;
                break;
            }
            case JavaConstants.STATE_CLASS_HEAD_END: {
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
                break;
            }
            case JavaConstants.STATE_LINE_START: {
                String line = TextUtils.getLine(data, index);
                String trimLine = line.trim();
                if (trimLine.startsWith("//") || trimLine.startsWith("/*")) {
                    state = JavaConstants.STATE_COMMENTS_START;
                } else if (trimLine.contains("class ")) {
                    state = JavaConstants.STATE_CLASS_START;
                } else if (trimLine.endsWith(";")) {
                    state = JavaConstants.STATE_MEMBER_START;
                } else {
                    state = JavaConstants.STATE_FUNC_START;
                }
                break;
            }
            case JavaConstants.STATE_LINE_END: {
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
                break;
            }
            case JavaConstants.STATE_MEMBER_START: {
                mJavaVariable = new JavaVariable();
                int start = cursor;
                int end = start;
                for (; end < data.length; end++) {
                    if (data[end] == ';') {
                        break;
                    }
                }

                int len = end - start + 1;

                mJavaVariable.start = start;
                mJavaVariable.length = len;
                mJavaVariable.data = data;
                mJavaVariable.lineStart = cursorLine;
                mJavaVariable.lineEnd = cursorLine;// 因为只有一行

                cursor += len;

                addMember(mJavaVariable);
                mJavaVariable = null;// reset

                state = JavaConstants.STATE_MEMBER_END;
                break;
            }
            case JavaConstants.STATE_MEMBER_END: {
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
                break;
            }
            case JavaConstants.STATE_VARIABLE_START: {
                Assert.say();
                break;
            }
            case JavaConstants.STATE_VARIABLE_END: {
                Assert.say();
                break;
            }
            case JavaConstants.STATE_FUNC_START: {
                char pair[] = {'{', '}'};
                int start = cursor;
                int end = findPairEnd(data, start, pair);
                int len = end - start + 1;
                mJavaFunc = new JavaFunc(data, cursor, cursorLine);

                addFunc(mJavaFunc);
                mJavaFunc = null;// reset
                cursor += len;

                cursorLine += TextUtils.countChar('\n', data, start, len);
                state = JavaConstants.STATE_FUNC_END;
                break;
            }
            case JavaConstants.STATE_FUNC_END: {
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
                break;
            }
            case JavaConstants.STATE_IN_ERROR: {
                Assert.say();
                break;
            }
        }
    }


    private void skipEnter() {
        while (TextUtils.getChar(data, cursor) == '\n') {
            cursor++;// skip '\n'
            cursorLine++;

            if (cursor >= data.length) {
                break;
            }// end if
        }
    }

    private void parseHeadLine(String headLine) {
        String line = headLine;
        char[] classChars = line.trim().toCharArray();
        String word = null;
        int offset = 0;
        int flag = 0;
        while (true) {
            word = TextUtils.getWord(classChars, offset);
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
                // 检查类注释
                if (mJavaComment != null) {
                    setComment(mJavaComment);
                    mJavaComment = null;// reset
                }// end if

                offset += word.length();

                // 跳过空格
                while (' ' == TextUtils.getChar(classChars, offset)) {
                    offset++;

                    if (offset >= line.length()) {
                        break;
                    }// end if
                }

                // 取类名
                String name = TextUtils.getWord(classChars, offset);
                setName(name);
                setFlag(flag);
                offset += name.length();
                break;
            } else {
                Assert.say("[" + word + "]");
            }
            offset += word.length();
            if (offset >= line.length()) {
                break;
            }// end if

            // 跳过空格字符
            while (' ' == TextUtils.getChar(classChars, offset)) {
                offset++;

                if (offset >= line.length()) {
                    break;
                }// end if
            }
        }
    }

    public ArrayList<JavaFunc> getFuncs() {
        return funcs;
    }

    public void addFunc(JavaFunc func) {
        funcs.add(func);
    }

    public ArrayList<JavaVariable> getMemses() {
        return memses;
    }

    public void setMemses(ArrayList<JavaVariable> memses) {
        this.memses = memses;
    }

    public void addMember(JavaVariable member) {
        memses.add(member);
    }

    public ArrayList<JavaClass> getInnerClasses() {
        return innerClasses;
    }

    public void addInnerClass(JavaClass innerClass) {
        innerClasses.add(innerClass);
    }

    public JavaComment getComment() {
        return comment;
    }

    public void setComment(JavaComment comment) {
        this.comment = comment;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}