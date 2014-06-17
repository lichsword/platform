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

}