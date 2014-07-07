/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.util;

import com.lich.platform.service.JavaConstants;

public class TextUtils {

    private TextUtils() { /* cannot be instantiated */
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    /**
     * @param fullText    原始文本
     * @param maxLine     最大显示行
     * @param ellipsisTip 提示语
     * @return
     */
    public static String ellipsizingText(String fullText, int maxLine, String ellipsisTip) {
        String ellipsizedText = "";
        int lineNumber = 1;

        if (maxLine > 0) {
            boolean overMaxLine = false;
            int i;
            if (maxLine == 1) {
                overMaxLine = false;
                int location = fullText.indexOf('\n');
                ellipsizedText = (location != -1) ? fullText.substring(0, location) : fullText;
            } else {
                for (i = 0; i < fullText.length(); i++) {
                    if (fullText.charAt(i) == '\n') {
                        lineNumber++;
                        if (!overMaxLine) {
                            if (lineNumber == maxLine) {
                                overMaxLine = true;
                                ellipsizedText = fullText.substring(0, i);
                            }
                        }
                    }
                }

                if (!overMaxLine) {
                    ellipsizedText = fullText;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(ellipsizedText);
            if (overMaxLine) {
                sb.append('\n');
                if (ellipsisTip.contains("%d")) {
                    sb.append(String.format(ellipsisTip, lineNumber - maxLine));
                } else {
                    sb.append(ellipsisTip);
                }
            }
            return sb.toString();
        }
        return "";


    }


    public static boolean isSpace(char ch) {
        return ch == ' ';
    }

    public static boolean isTab(char ch) {
        return ch == '\t';
    }

    public static boolean isAlphabet(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    public static boolean isNumber(char ch) {
        return (ch >= '0' && ch <= '9');
    }

    public static boolean isEnter(char ch) {
        return ch == '\n';
    }

    /**
     * 读取一行，从当前字符向后的一行内容
     *
     * @param data
     * @param start
     * @return
     */
    public static String getLine(final char[] data, final int start) {
        int pos = start;

        while (!TextUtils.isEnter(data[pos])) {
            pos++;
            if (pos >= data.length) {
                break;
            }
        }

        return String.valueOf(data, start, pos - start);
    }

    /**
     * 从当前字符向后，查找第1个字母单词
     *
     * @param data
     * @param start
     * @return 如果当前为非字母，则结束，返回起止之间的字符串作为单词，异常返回"".
     */
    public static String getWord(final char[] data, final int start) {
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
    public static String getVariableName(final char[] data, final int start) {
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

    public static char getChar(final char[] data, final int index) {
        return data[index];
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
    public static int countChar(char ch, char[] data, int start, int length) {
        int count = 0;
        for (int i = start; i < start + length; i++) {
            if (data[i] == ch) {
                count++;
            }
        }
        return count;
    }


    /**
     * 从 start 开始向后查找第1个匹配字符，成功返回字符索引，否则返回 -1.
     *
     * @param data
     * @param start
     * @param ch
     * @return
     */
    public static int charAt(char[] data, int start, char ch) {
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
}