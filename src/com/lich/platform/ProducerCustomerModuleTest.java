package com.lich.platform;

import com.jni.TTY;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-13
 * Time: 上午9:41
 * <p/>
 * TODO
 */
public class ProducerCustomerModuleTest {

    public ProducerCustomerModuleTest() {
    }

    LinkedList<String> mMessageQueue = new LinkedList<String>();

    static final int MAX = 5;


    public void start() {

        if (true) {
//            TTYTest.testReset();
//            TTYTest.testClear();
//            TTYTest.testErase();
//            TTYTest.testMove();
//            TTYTest.testFlash();
//            TTYTest.testAddstr(1, 5, "[______]");
//            TTYTest.testAddstr(0, 0, "show me the money");
//            TTYTest.testAddstr(1, 0, "black sheep wall\n");
//            TTYTest.testClearToEndOfLine(1, 5);
//            TTYTest.testClearToEndOfScreen(0, 5);
            TTY.initscr();
            String str = TTYTest.testGetString();
            TTY.endwin();
            TTYTest.testAddstr(5, 5, str);
            return;
        }// end if

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
}
