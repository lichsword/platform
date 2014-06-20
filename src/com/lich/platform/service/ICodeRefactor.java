package com.lich.platform.service;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-19
 * Time: 下午3:06
 * <p/>
 * TODO
 */
public interface ICodeRefactor {

    public int getFileCount(String rootDir);

    public void parsePath(String path);

    public String getReport();

    public void sortByLines(int flag);

    public void sortByImportNum(int flag);

}
