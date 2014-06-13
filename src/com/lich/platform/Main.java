package com.lich.platform;

import com.jni.TTY;

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

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public Main() {
        loadLibrary(LIB_NAME_TTY);
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
                    TTY.getch();
                    TTY.startColor();
//                    TTY.initPair(1, TTY.COLOR_WHITE, TTY.COLOR_BLACK);
//                    TTY.attrset(TTY.getColorPair(1) | TTY.attrUnderline());
                    TTY.attrset(TTY.attrUnderline());
//                    TTY.attrset(TTY.getColorPair(1));
//                } else {
//
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
                    // do work
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

    /**
     * 绘图
     */
    private void draw() {
        TTY.clear();
        TTY.move(Constants.STATUS_BAR_LINE_BEGIN, 0);
        TTY.addstr(String.format(Constants.FORMAT_STATUS_BAR, "State_Refresh", "2014-06-13"));
        TTY.move(Constants.TASK_BAR_LINE_BEGIN, 0);
        TTY.addstr(String.format(Constants.FORMAT_TASK_BAR, "(1/3)"));
        TTY.move(Constants.OUTPUT_AREA_LINE_BEGIN, 0);
        TTY.addstr(String.format(Constants.FORMAT_OUTPUT_AREA, "Handled work"));
        TTY.move(Constants.OUTPUT_AREA_LINE_END, 0);
        TTY.addstr("--------------------------------------------------");
        TTY.move(Constants.INPUT_AREA_LINE_BEGIN, 0);
        TTY.addstr(String.format(Constants.FORMAT_INPUT_AREA));
        TTY.refresh();
    }

    LinkedList<String> mMessageQueue = new LinkedList<String>();

    static final int MAX = 5;

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

}