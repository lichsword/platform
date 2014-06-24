package com.lich.platform.service;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 下午2:29
 * <p/>
 * TODO
 */
public class JavaMem {

    public JavaMem() {

    }

    private int flag;

    private String name;

    private String initValue;

    private int linePos;


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitValue() {
        return initValue;
    }

    public void setInitValue(String initValue) {
        this.initValue = initValue;
    }

    public int getLinePos() {
        return linePos;
    }

    public void setLinePos(int linePos) {
        this.linePos = linePos;
    }
}