package com.jni;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-11
 * Time: 下午6:42
 * <p/>
 * TODO
 */
public class TTY {

    public native static void echo();

    public native static void noecho();

    public native static void reset();

    public native static void initscr();

    public native static void endwin();

    public native static void refresh();

    public native static void clear();

    public native static void erase();

    public native static void cls();

    public native static void addstr(String string);

    public native static int getch();

    public native static void move(int x, int y);
}
