package com.lich.platform;

import com.jni.TTY;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-13
 * Time: 上午9:38
 * <p/>
 * TODO
 */
public class TTYTest {

    /**
     * system("reset")清屏
     * <p/>
     * OK
     */
    public static void testReset() {
        TTY.reset();
    }

    /**
     * clear()清屏
     * <p/>
     * OK
     */
    public static void testClear() {
        TTY.initscr();
        TTY.clear();
        TTY.refresh();
        TTY.getch();
        TTY.endwin();
    }

    /**
     * erase()清屏
     * <p/>
     * OK
     */
    public static void testErase() {
        TTY.initscr();
        TTY.erase();
        TTY.refresh();
        TTY.getch();
        TTY.endwin();
    }

    /**
     * move()移动光标
     * <p/>
     * OK
     */
    public static void testMove() {
        TTY.initscr();
        TTY.move(0, 3);
        TTY.addstr("AAA");
        TTY.refresh();
        TTY.getch();
        TTY.endwin();
    }

    /**
     * 写字符串
     * <p/>
     * OK
     */
    public static void testAddstr(int line, int col, String string) {
        TTY.initscr();
        TTY.move(line, col);
        TTY.addstr(string);
        TTY.refresh();
        TTY.getch();
        TTY.endwin();
    }

    /**
     * 清除至行尾
     *
     * @param line OK
     */
    public static void testClearToEndOfLine(int line, int col) {
        TTY.initscr();
        TTY.move(line, col);
        TTY.clrtoeol();
        TTY.refresh();
        TTY.getch();
        TTY.endwin();
    }

    /**
     * 清除至屏幕底
     *
     * @param line OK
     */
    public static void testClearToEndOfScreen(int line, int col) {
        TTY.initscr();
        TTY.move(line, col);
        TTY.clrtobot();
        TTY.refresh();
        TTY.getch();
        TTY.endwin();
    }

    /**
     * 闪屏
     * <p/>
     * OK
     */
    public static void testFlash() {
        TTY.initscr();
        TTY.flash();
        TTY.endwin();
    }

    /**
     * 读取1个字符
     * <p/>
     * OK
     *
     * @return
     */
    public static int testGetChar() {
        return TTY.getch();
    }

    /**
     * 读取字符串（回车确认）
     * <p/>
     * OK
     *
     * @return
     */
    public static String testGetString() {
        return TTY.getstr();
    }

}
