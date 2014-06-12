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

    int state = ST_END;
    static final int ST_END = -1;
    static final int ST_IDLE = 0;
    static final int ST_WORK = 1;
    static final int ST_REFRESH = 2;

    void changeState(int newState) {
        this.state = newState;
    }

    void onEvent() {
        switch (state) {
            case ST_IDLE:
                break;
            case ST_WORK:
                break;
            case ST_REFRESH:
                break;
            default:
                break;
        }
    }

    LinkedList<String> mMessageQueue = new LinkedList<String>();

    static final int MAX = 5;

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public Main() {
        try {
            System.loadLibrary("tty");
        } catch (Exception e) {
        }
    }

    /**
     * system("reset")清屏
     * <p/>
     * OK
     */
    private void testReset() {
        TTY.reset();
    }

    /**
     * clear()清屏
     * <p/>
     * OK
     */
    private void testClear() {
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
    private void testErase() {
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
    private void testMove() {
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
    private void testAddstr(int line, int col, String string) {
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
    private void testClearToEndOfLine(int line, int col) {
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
    private void testClearToEndOfScreen(int line, int col) {
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
    private void testFlash() {
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
    private int testGetChar() {
        return TTY.getch();
    }

    /**
     * 读取字符串（回车确认）
     * <p/>
     * OK
     *
     * @return
     */
    private String testGetString() {
        return TTY.getstr();
    }

    public void start() {

        if (true) {
//            testReset();
//            testClear();
//            testErase();
//            testMove();
//            testFlash();
//            testAddstr(1, 5, "[______]");
//            testAddstr(0, 0, "show me the money");
//            testAddstr(1, 0, "black sheep wall\n");
//            testClearToEndOfLine(1, 5);
//            testClearToEndOfScreen(0, 5);
            TTY.initscr();
            String str = testGetString();
            TTY.endwin();
            testAddstr(5, 5, str);
            return;
        }// end if

        changeState(ST_IDLE);

        TTY.initscr();
        BackWork backWork = new BackWork();
        backWork.start();

        String message = null;
        while (true) {
            synchronized (mMessageQueue) {
                try {
                    if (mMessageQueue.size() == MAX) {
                        mMessageQueue.wait();
                    }

                    if (null != message) {
                        if (mMessageQueue.add(String.valueOf(message))) {
                            message = null;// clear after send.
                            mMessageQueue.notify();
                        }// end if

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }// end sync.

//            String input = console.readLine();
//            message = input;
//            int ch = TTY.getch();
            String string = TTY.getstr();
//            if (ch == 'q' || ch == 'Q') {
            if (string.charAt(0) == 'q' || string.charAt(0) == 'Q') {
                TTY.reset();
                changeState(ST_END);
                break;
            } else {
//                message = String.valueOf((char) ch);
                message = string;
            }
        }// end while

        TTY.addstr("Main...end\n");
        TTY.endwin();
    }

    class BackWork extends Thread {


        @Override
        public void run() {
            super.run();

            while (true) {

                synchronized (mMessageQueue) {
                    try {
                        if (mMessageQueue.size() == 0) {
                            mMessageQueue.wait();
                        }

                        String item = mMessageQueue.removeFirst();
                        render(item);

                        if (state == ST_END) {
                            break;
                        }// end if

                        mMessageQueue.notify();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }// end sync

                try {
//                    Thread.sleep(1200);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }// end while

        }
    }

    /**
     * 不要做全屏渲染（清屏操作等），只做上下文渲染。
     */
    private void render(String workstring) {
//        TTY.cls();
        TTY.reset();
        TTY.noecho();
        TTY.clear();
        //TTY.move(0, 0);
        TTY.addstr(String.format("SM: ?\n-----\nTASK:%d\n-----\nOUT:%s\n", mMessageQueue.size(), "invoke render()" + workstring));
        TTY.refresh();
    }


}