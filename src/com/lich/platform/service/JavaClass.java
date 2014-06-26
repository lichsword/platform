package com.lich.platform.service;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 下午2:40
 * <p/>
 * TODO
 */
public class JavaClass {

    private int flag;
    private String name;
    private String data;
    private JavaComment comment;
    private ArrayList<JavaFunc> funcs = new ArrayList<JavaFunc>();
    private ArrayList<JavaVariable> memses = new ArrayList<JavaVariable>();
    private ArrayList<JavaClass> innerClasses = new ArrayList<JavaClass>();

    public JavaClass() {
    }

    public ArrayList<JavaFunc> getFuncs() {
        return funcs;
    }

    public void addFunc(JavaFunc func) {
        funcs.add(func);
    }

    public ArrayList<JavaVariable> getMemses() {
        return memses;
    }

    public void setMemses(ArrayList<JavaVariable> memses) {
        this.memses = memses;
    }

    public void addMember(JavaVariable member) {
        memses.add(member);
    }

    public ArrayList<JavaClass> getInnerClasses() {
        return innerClasses;
    }

    public void addInnerClass(JavaClass innerClass) {
        innerClasses.add(innerClass);
    }

    public JavaComment getComment() {
        return comment;
    }

    public void setComment(JavaComment comment) {
        this.comment = comment;
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
}