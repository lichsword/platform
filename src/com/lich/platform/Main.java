package com.lich.platform;

import com.jni.GIT;
import com.jni.TTY;
import com.lich.platform.service.IDatabase;
import com.lich.platform.service.ITime;
import com.lich.platform.service.NBService;
import com.lich.platform.service.SystemService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-8
 * Time: 下午4:38
 * <p/>
 * TODO
 */
public class Main {

    private final String LIB_NAME_TTY = "tty";
    private final String LIB_NAME_GIT = "git";

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public Main() {
        loadLibrary(LIB_NAME_TTY);
        loadLibrary(LIB_NAME_GIT);
        init();
    }

    private void init() {
        mColumns = TTY.getTTYColumns();
        mLines = TTY.getTTYLines();
        // TODO
//        GIT.test();
        String path = "/Users/lichsword/Documents/workspace_company/taoappcenter_android";
        String author = "wangyue";
        String since = "1.weeks";
        GIT.log(path, author, since);
    }

    /**
     * @param path
     * @return
     */

    private boolean loadLibrary(String path) {
        try {
            System.out.println("Main.loadLibrary.Value = " + path);
            System.loadLibrary(path);
            System.out.println("Main.loadLibrary.Success");
            return true;
        } catch (Exception e) {
            System.out.println("Main.loadLibrary.Exception, reason\n " + e.toString());
            return false;
        }
    }

    int state = Constants.STATE_ENDED;


    /**
     * 外部事件
     *
     * @param event
     * @param extra
     */
    void onEvent(int event, Object extra) {
        switch (state) {
            case Constants.STATE_START:
                switch (event) {
                    case Constants.EVENT_EXIT:
                        changeState(Constants.STATE_ENDED);
                        break;
                    case Constants.EVENT_INPUT:
                        mMessageQueue.addLast((String) extra);
                        changeState(Constants.STATE_IDLE);
                        break;
                    default:
                        break;
                }
                break;
            case Constants.STATE_IDLE:
                switch (event) {
                    case Constants.EVENT_EXIT:
                        changeState(Constants.STATE_ENDED);
                        break;
                    case Constants.EVENT_INPUT:
                        mMessageQueue.addLast((String) extra);
                        changeState(Constants.STATE_WORK);
                        break;
                    default:
                        break;
                }
                break;
            case Constants.STATE_WORK:
                switch (event) {
                    case Constants.EVENT_EXIT:
                        changeState(Constants.STATE_ENDED);
                        break;
                    case Constants.EVENT_INPUT:
                        break;
                    default:
                        break;
                }
                break;
            case Constants.STATE_REFRESH:
                switch (event) {
                    case Constants.EVENT_EXIT:
                        changeState(Constants.STATE_ENDED);
                        break;
                    case Constants.EVENT_INPUT:
                        break;
                    default:
                        break;
                }
                break;
            case Constants.STATE_ENDED:
                // do nothing.
                break;
            default:
                break;
        }
    }

    /**
     * 执行状态转换
     *
     * @param newState
     */
    void changeState(int newState) {
        this.state = newState;
        onStateChanged();
    }

    /**
     * 状态转换的回调
     */
    void onStateChanged() {
        switch (state) {
            case Constants.STATE_START:
                TTY.initscr();
                if (TTY.hasColors()) {
//                    TTY.getch();
                    TTY.startColor();
//                    TTY.initPair(0, TTY.COLOR_WHITE, TTY.COLOR_GREEN);
                    TTY.initPair(1, TTY.COLOR_WHITE, TTY.COLOR_BLUE);
                    TTY.initPair(2, TTY.COLOR_WHITE, TTY.COLOR_RED);
                    TTY.initPair(3, TTY.COLOR_WHITE, TTY.COLOR_GREEN);
                    TTY.initPair(4, TTY.COLOR_WHITE, TTY.COLOR_YELLOW);
                    TTY.initPair(5, TTY.COLOR_WHITE, TTY.COLOR_CYAN);
                    TTY.initPair(6, TTY.COLOR_WHITE, TTY.COLOR_MAGENTA);

//                    TTY.attrset(TTY.attrUnderline());
//                    TTY.attrset(TTY.getColorPair(1));
                }
                changeState(Constants.STATE_REFRESH);// 初始化界面
                break;
            case Constants.STATE_IDLE:
                if (mMessageQueue.size() != 0) {
                    changeState(Constants.STATE_WORK);
                } else {
//                    changeState(Constants.STATE_ENDED);
                    // do nothing
                }
                break;
            case Constants.STATE_WORK:
                if (mMessageQueue.size() == 0) {
                    // no work
                } else {
                    String msg = mMessageQueue.removeLast();
                    // do work TODO
                    handleMsg(msg);
                    // finish work
                }
                changeState(Constants.STATE_REFRESH);
                break;
            case Constants.STATE_REFRESH:
                draw();
                changeState(Constants.STATE_IDLE);
                break;
            case Constants.STATE_ENDED:
                TTY.endwin();
                break;
            default:
                break;
        }
    }

    private void handleMsg(String msg) {
        if (msg.equals("time")) {
            ITime iTime = (ITime) SystemService.getInstance().getService(SystemService.LABEL_TIME);
            output = iTime.getTime();
        } else if (msg.equals("sqlite")) {
            IDatabase iDatabase = (IDatabase) SystemService.getInstance().getService(SystemService.LABEL_DATABASE);
            inputTip = "Select database by number.";
            output = iDatabase.handleMsg(msg);
        } else if (msg.equals("nb")) {
            NBService nbService = (NBService) SystemService.getInstance().getService(SystemService.LABEL_NEXTBRAIN);
//            nbService. TODO
        }
        // TODO
    }


    /**
     * 绘图
     */
    private void draw() {
        TTY.clear();
        drawStatebar();
        drawTaskbar();
        drawOutputbar();
        drawInputbar();
        TTY.attroff(1 | TTY.MASK_A_UNDERLINE | TTY.MASK_A_BOLD);
        TTY.refresh();
    }

    private void drawStatebar() {
        TTY.attrset(1 | TTY.MASK_A_BOLD);
        TTY.move(Constants.STATUS_BAR_LINE_BEGIN, 0);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        String dd = format.format(d);
        date = dd;

        drawLineBg();
        TTY.move(Constants.STATUS_BAR_LINE_BEGIN, 0);
        TTY.addstr(String.format(Constants.FORMAT_STATUS_BAR, "State_Refresh", date));
    }

    private void drawTaskbar() {
        TTY.attrset(2 | TTY.MASK_A_PROTECT);
        TTY.move(Constants.TASK_BAR_LINE_BEGIN, 0);
        TTY.addstr(String.format(Constants.FORMAT_TASK_BAR, "(1/3)"));
    }

    private void drawOutputbar() {
        TTY.move(Constants.OUTPUT_AREA_LINE_BEGIN, 0);
        TTY.attrset(3);
        drawLineBg();
        TTY.move(Constants.OUTPUT_AREA_LINE_BEGIN, 0);
        TTY.addstr(Constants.STR_OUTPUT);
        TTY.attroff(3);
        TTY.move(Constants.OUTPUT_AREA_LINE_BEGIN + 1, 0);
        TTY.addstr(output);
    }

    private void drawInputbar() {
        TTY.attrset(3 | TTY.MASK_A_UNDERLINE);
        TTY.move(Constants.OUTPUT_AREA_LINE_END, 0);
        drawLineBg('-');
        TTY.move(Constants.INPUT_AREA_LINE_BEGIN, 0);
        TTY.attroff(3);

        // draw tips
        if (inputTip.equals("")) {
            TTY.addstr(Constants.STR_INPUT);
        } else {
            TTY.addstr(String.format(Constants.FORMAT_INPUT_AREA, inputTip));
        }

        // move next line
        TTY.move(Constants.INPUT_AREA_LINE_BEGIN + 1, 0);
    }

    LinkedList<String> mMessageQueue = new LinkedList<String>();

//    static final int MAX = 5;

    public void run() {
        changeState(Constants.STATE_START);
        while (true) {
            String input = TTY.getstr();
            if (wannaExit(input)) {
                onEvent(Constants.EVENT_EXIT, null);
                break;
            } else {
                onEvent(Constants.EVENT_INPUT, input);
            }
        }
    }

    private boolean wannaExit(String input) {
        return input.equalsIgnoreCase("exit");
    }

    // 环境参数
    private String date;// 日期

    private String inputTip = "";// 输入引导提示
    private String output = "";// 输出信息

    private int mColumns;// tty的列数
    private int mLines;// tty的行数


    public void setHandleResult(String output, String inputTip) {
        this.output = output;
        this.inputTip = inputTip;
    }

    /**
     * 以空字符串绘满一行的背景
     */
    private static void drawLineBg(char fill) {
        TTY.addstr(buildSpaceString(fill));
    }

    /**
     * 以空字符串绘满一行的背景
     */
    private static void drawLineBg() {
        TTY.addstr(buildSpaceString());
    }

    /**
     * 初始化长度为tty的列数的空字符串
     *
     * @return
     */
    private static String buildSpaceString(char fill) {
        char[] chars = new char[TTY.getTTYColumns()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = fill;
        }
        return String.valueOf(chars);
    }

    private static String buildSpaceString() {
        return buildSpaceString(' ');
    }

}