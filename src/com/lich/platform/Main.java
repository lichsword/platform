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

//    public native void hello();

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

    public void start() {
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
            int ch = TTY.getch();
            if (ch == 'q' || ch == 'Q') {
                break;
            } else {
                message = String.valueOf((char) ch);
            }
        }// end while

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
                        render();
                        mMessageQueue.notify();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }// end sync

                try {
//                    Thread.sleep(1200);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }// end while

        }
    }

    /**
     * 不要做全屏渲染（清屏操作等），只做上下文渲染。
     */
    private void render() {
//        TTY.cls();
        TTY.reset();
        TTY.noecho();
        TTY.clear();
        TTY.move(0, 0);
        TTY.addstr(String.format("SM: ?\n-----\nTASK:%d\n-----\nOUT:%s", mMessageQueue.size(), "invoke render()"));
        TTY.refresh();
    }


}