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
public class JavaFile {

    private String path;
    private JavaClass javaClass;
    private JavaPackage javaPackage;
    private ArrayList<JavaImport> imports = new ArrayList<JavaImport>();

    public JavaClass getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(JavaClass javaClass) {
        this.javaClass = javaClass;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String setSimpleName() {
        String name;
        if (path.contains("/")) {
            name = path.substring(path.lastIndexOf('/'));
        } else {
            name = path;
        }
        return name;
    }

    public JavaPackage getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(JavaPackage javaPackage) {
        this.javaPackage = javaPackage;
    }


    public void addImport(JavaImport javaImport) {
        imports.add(javaImport);
    }
}
