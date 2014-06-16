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

    public native static void attron(int attr);

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

    public static native long mapAttr();

    // attributes
    public static native byte attrNormal();

    public static native byte attrStandout();

    public static native byte attrUnderline();

    public static native byte attrReverse();

    public static native byte attrBlink();

    public static native byte attrDim();

    public static native byte attrBold();

    public static native byte attrProtect();

    public static native byte attrInvis();

    public static native byte attrAltcharset();

    public static native byte attrChartext();

    public static int MASK_A_NORMAL = 0x010;// Normal display (no highlight)
    public static int MASK_A_STANDOUT = 0x020;// Best highlighting mode of the terminal.
    public static int MASK_A_UNDERLINE = 0x040;// Underlining
    public static int MASK_A_REVERSE = 0x080;// Reverse video
    public static int MASK_A_BLINK = 0x100;// Blinking
    public static int MASK_A_DIM = 0x200;// Half bright
    public static int MASK_A_BOLD = 0x400;// Extra bright or bold
    public static int MASK_A_PROTECT = 0x800;// Protected mode
    public static int MASK_A_INVIS = 0x1000;// Invisible or blank mode
    public static int MASK_A_ALTCHARSET = 0x2000;// Alternate character set
    public static int MASK_A_CHARTEXT = 0x4000;// Bit-mask to extract a character
}