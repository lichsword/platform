package com.lich.platform.service;

import com.log.Assert;
import com.log.Log;
import com.util.TextUtils;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 下午2:29
 * <p/>
 * TODO
 */
public class JavaFunc extends JavaElement {

    private static final String TAG = JavaFunc.class.getSimpleName();

    private int flag;

    private String returnValue;
    private String name;
    private String param;

    private int lineStart;

    private int lineSum;

    public JavaFunc(char[] data, int cursor, int cursorLine) {

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

        doFunc();
    }

    int state;
    int cursor;
    int cursorLine;

    private void doFunc() {
        state = JavaConstants.STATE_FUNC_HEADER_START;

        // 开始解析
        int end = super.start + super.length;
        while (true) {
            doChar(data[cursor], data, cursor);
            if (cursor >= end) {
                break;
            }// end if

            // 打印日志，并加延时可见性
            Log.e(TAG, "JavaFunc.doJava.While.char= " + data[cursor]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace(); // TODO
            }
        }// end while
    }


    private void doChar(char ch, char[] data, int index) throws IllegalStateException {
        switch (state) {
            case JavaConstants.STATE_FUNC_HEADER_START:
                String word;
                int offset = 0;
                int flag = 0;
                String line = TextUtils.getLine(data, index);
                char[] variableChars = line.trim().toCharArray();
                while (true) {
                    word = TextUtils.getWord(variableChars, offset);
                    if (TextUtils.isEmpty(word)) {
                        char currentChar = TextUtils.getChar(variableChars, offset);
                        if (TextUtils.isSpace(currentChar) || TextUtils.isTab(currentChar)) {
                            offset++;
                        } else if (currentChar == '(') {
                            if (TextUtils.isEmpty(getParam())) {
                                int paramEnd = TextUtils.charAt(variableChars, offset, ')');
                                String param = String.valueOf(variableChars, offset, paramEnd - offset);
                                setParam(param);
                                offset += param.length();
                                break;
                            } else {
                                Assert.say("" + currentChar);
                            }
                        } else {
                            Assert.say("" + currentChar);
                        }

                        if (offset >= variableChars.length) {
                            break;
                        } else {
                            continue;
                        }
                    } else {
                        word = TextUtils.getWord(variableChars, offset);
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
                            String element = TextUtils.getVariableName(variableChars, offset);
                            if (TextUtils.isEmpty(getReturnValue())) {
                                setReturnValue(element);
                            } else if (TextUtils.isEmpty(getName())) {
                                setName(element);
                            } else {
                                Assert.say(element);
                            }
                        }

                        offset += word.length();
                    }// end if
                }

                setFlag(flag);

                cursor += line.length();

                state = JavaConstants.STATE_FUNC_HEADER_END;
                break;
            case JavaConstants.STATE_FUNC_HEADER_END:
                skipEnter();
                state = JavaConstants.STATE_LINE_START;
                break;
            case JavaConstants.STATE_LINE_START: {
                Assert.say();
                break;
            }
            case JavaConstants.STATE_LINE_END: {
                Assert.say();
                break;
            }
            case JavaConstants.STATE_IN_ERROR: {
                Assert.say();
                break;
            }
        }
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

    public int getLineStart() {
        return lineStart;
    }

    public void setLineStart(int lineStart) {
        this.lineStart = lineStart;
    }

    public int getLineSum() {
        return lineSum;
    }

    public void setLineSum(int lineSum) {
        this.lineSum = lineSum;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
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
}
