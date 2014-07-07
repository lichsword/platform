package com.lich.platform.service;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-25
 * Time: 下午5:46
 * <p/>
 * TODO
 */
public class JavaPackage extends JavaElement {

    public JavaPackage(char[] data, int cursor, int cursorLine) {
        int end;
        int enterNum = 0;
        for (end = cursor; end < data.length; end++) {
            if (data[end] == '\n') {
                enterNum++;
            }// end if

            if (data[end] == ';') {
                break;
            }// end if
        }

        super.start = cursor;
        super.length = end - cursor + 1;
        super.data = data;
        super.lineStart = cursorLine;
        super.lineEnd = cursorLine + enterNum;

//        String line = getLine(data, index);
//        String trimLine = line.trim();
//        String[] piece = trimLine.split(" ");
//        mJavaPackage.setPath(piece[1]);
    }
}
