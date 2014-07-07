package com.log;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-7-1
 * Time: 下午2:40
 * <p/>
 * TODO
 */
public class Assert {

    public static void say() {
        throw new IllegalStateException("断言");
    }

    public static void say(String info) {
        throw new IllegalStateException("断言原因:" + info);
    }
}
