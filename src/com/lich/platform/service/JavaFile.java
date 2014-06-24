package com.lich.platform.service;

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
    private String packageDir;
    private JavaClass javaClass;

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

    public String getPackageDir() {
        return packageDir;
    }

    public void setPackageDir(String packageDir) {
        this.packageDir = packageDir;
    }
}
