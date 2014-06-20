package com.jni;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-19
 * Time: 下午5:31
 * <p/>
 * TODO
 */
public class UNIX {

    /**
     * @param param
     * @param path
     * @deprecated 由invoke取代
     */
    public native static void wc(String param, String path);

    /**
     * @param param
     * @param path
     * @deprecated 由invoke取代
     */
    public native static void grep(String param, String path);

    /**
     * 调用Unix系统命令
     *
     * @param cmd
     * @param path
     */
    public native static void invoke(String cmd, String path);

}
