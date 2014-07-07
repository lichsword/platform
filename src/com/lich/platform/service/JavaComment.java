package com.lich.platform.service;

import com.log.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-25
 * Time: 下午2:43
 * <p/>
 * TODO
 */
public class JavaComment extends JavaElement {


    public JavaComment(char[] data, int cursor, int cursorLine) {

        char atChar = data[cursor];
        char nextChar = data[cursor + 1];

        if (atChar != '/') {
            Assert.say("JavaComment.construct.atChar.Error, reason => atChar=" + atChar);
            return;
        }// end if

        if (nextChar != '/' && nextChar != '*') {
            Assert.say("JavaComment.construct.nextChar.Error, reason => nextChar=" + nextChar);
            return;
        }// end if

        int end = cursor;// 结束字符索引
        int enterNum = 0;
        if (nextChar == '*') {
            // 多行注释
            for (end = cursor; end < data.length; end++) {
                if (data[end] == '\n') {
                    enterNum++;
                }// end if

                if (data[end] == '*' && data[end + 1] == '/') {
                    end++;// 加一个字符，吃掉'/'收尾符
                    break;
                }
            }
        } else if (nextChar == '/') {
            // 单行注释
            if (data[end] == '\n') {
                enterNum++;
            }// end if

            for (end = cursor; end < data.length; end++) {
                if (data[end] == '\n') {
                    end--;// 回退一个字符，吐出回车符
                    break;
                }
            }

        }

        super.start = cursor;
        super.length = end - cursor + 1;
        super.data = data;
        super.lineStart = cursorLine;
        super.lineEnd = cursorLine + enterNum;
    }

}