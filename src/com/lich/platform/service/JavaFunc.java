package com.lich.platform.service;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 下午2:29
 * <p/>
 * TODO
 */
public class JavaFunc {

    private ArrayList<JavaVariable> mems = new ArrayList<JavaVariable>();

    private int flag;

    private String returnValue;
    private String name;
    private String param;

    private String data;

    private int lineStart;

    private int lineSum;

    public ArrayList<JavaVariable> getMems() {
        return mems;
    }

    public void setMems(ArrayList<JavaVariable> mems) {
        this.mems = mems;
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLineStart() {
        return lineStart;
    }

    public void setLineStart(int lineStart) {
        this.lineStart = lineStart;
    }

    public int getLineSum() {
        return lineSum;
    }

    public void setLineSum(int lineSum) {
        this.lineSum = lineSum;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
