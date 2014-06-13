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

    public native static void flash();

    public native static void echo();

    public native static void noecho();

    public native static void reset();

    public native static void initscr();

    public native static void endwin();

    public native static void refresh();

    public native static void clear();

    public native static void clrtobot();

    public native static void clrtoeol();

    public native static void erase();

    public native static void addstr(String string);

    public native static String getstr();

    public native static int getch();

    public native static void move(int line, int col);

    public native static boolean hasColors();// 终端是否支持颜色

    public native static int startColor();// 终端开启颜色

    public native static void attroff(int attr);

    public native static void attrset(int attr);

    public native static void initPair(int pair_number, int foreground, int background);// 初始化颜色值对 pair_number (1<= value <= 7)

    public native static int getColorPair(int pair_number);// 取得颜色值对

    // colors
    public static int COLOR_BLACK = 0;
    public static int COLOR_RED = 1;
    public static int COLOR_GREEN = 2;
    public static int COLOR_YELLOW = 3;
    public static int COLOR_BLUE = 4;
    public static int COLOR_MAGENTA = 5;
    public static int COLOR_CYAN = 6;
    public static int COLOR_WHITE = 7;
    // attributes

    public static native byte attrUnderline();

    public static int A_NORMAL = 0x10;// Normal display (no highlight)
    public static int A_STANDOUT = 0x20;// Best highlighting mode of the terminal.
    public static int A_UNDERLINE = 0x30;// Underlining
    public static int A_REVERSE = 0x40;// Reverse video
    public static int A_BLINK = 0x50;// Blinking
    public static int A_DIM = 0x60;// Half bright
    public static int A_BOLD = 0x70;// Extra bright or bold
    public static int A_PROTECT = 0x80;// Protected mode
    public static int A_INVIS = 0x90;// Invisible or blank mode
    public static int A_ALTCHARSET = 0xa0;// Alternate character set
    public static int A_CHARTEXT = 0xb0;// Bit-mask to extract a character
}
