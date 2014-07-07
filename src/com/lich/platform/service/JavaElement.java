package com.lich.platform.service;

import com.log.Assert;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-7-1
 * Time: 下午2:48
 * <p/>
 * TODO
 */
public abstract class JavaElement {

    protected char[] data;
    protected int start;
    protected int length;
    protected int lineStart;
    protected int lineEnd;

    public JavaElement() {

    }

    public int getLineCount() {
        return lineEnd - lineStart + 1;
    }

    public int getLength() {
        return length;
    }

    /**
     * 查找最外层匹配的终止符索引值
     *
     * @param data
     * @param start
     * @param pair
     * @return end of pair matched.
     */
    public static int findPairEnd(char[] data, int start, char[] pair) {
        if (pair.length != 2) {
            Assert.say("pickData.func.Param.pair.Length.Error, reason len=" + pair.length);
        }// end if

        char left = pair[0];
        char right = pair[1];

        LinkedList<String> pairContentList = new LinkedList<String>();
        LinkedList<Character> operStack = new LinkedList<Character>();

        int s = start;
        int e = start;

        while (true) {
            if (data[e] == left) {
                // 找到起始符
                operStack.push(left);
            } else if (data[e] == right) {
                operStack.pop();
                if (operStack.isEmpty()) {
                    // 找到最外层的匹配的终止符
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

        // TODO do more?
        pairContentList.clear();
        operStack.clear();
        return e;
    }

}
