import java.io.Console;
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

    LinkedList<String> mMessageQueue = new LinkedList<String>();

    static final int MAX = 5;

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public Main() {

    }

    public void start() {
        log("Main enter...");

        BackWork backWork = new BackWork();
        backWork.start();

        String message = null;
        while (true) {
            synchronized (mMessageQueue) {
                try {
                    if (mMessageQueue.size() == MAX) {
                        log("mMessageQueue is full, wait...");
                        mMessageQueue.wait();
                    }

                    if (null != message) {
                        System.out.print("Send msg:");

                        if (mMessageQueue.add(String.valueOf(message))) {
                            log("Produce message into queue: " + message);
                            mMessageQueue.notify();
                        }// end if

                        message = null;// clear after send.
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }// end sync.

            // wait input.
            Console console = System.console();
            String input = console.readLine();
            message = input;
        }// end while
    }

    class BackWork extends Thread {


        @Override
        public void run() {
            super.run();
            log("BackWork...started");

            while (true) {

                synchronized (mMessageQueue) {
                    try {
                        if (mMessageQueue.size() == 0) {
                            log("Queue empty, please wait.");
                            mMessageQueue.wait();
                        }

                        String item = mMessageQueue.removeFirst();
                        log("handle msg: " + item);
                        mMessageQueue.notify();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }// end sync

                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }// end while

        }
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}
