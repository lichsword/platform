package com.lich.platform.service;

import com.log.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-7-14
 * Time: 下午1:44
 * <p/>
 * TODO
 */
public class ZipService implements IZip {

    private static final String TAG = ZipService.class.getSimpleName();

    public static final String ZIP_FILENAME = "/Users/lichsword/Documents/workspace_github/platform/data/all_artists.zip";//需要解压缩的文件名
    public static final String UN_ZIP_DIR = "/Users/lichsword/Documents/workspace_github/platform/data/all_artists";//需要压缩的文件夹
    public static final String WAIT_ZIP_DIR = "/Users/lichsword/Documents/workspace_github/platform/out/all_artists.zip";//需要压缩的文件夹


    public static final String OUT_ZIP_FILENAME = "/Users/lichsword/Documents/workspace_github/platform/out/all_artists.zip";//需要解压缩的文件名
    public static final String OUT_UN_ZIP_DIR = "/Users/lichsword/Documents/workspace_github/platform/out/all_artists";//需要压缩的文件夹

    public static final int BUFFER = 1024;//缓存大小

    public static void main(String[] args) {
        try {
            Log.e(TAG, "开始压缩");
            zipFile(UN_ZIP_DIR, OUT_ZIP_FILENAME);
            Log.e(TAG, "完成压缩");

            Log.e(TAG, "开始解压");
            unzipFile(WAIT_ZIP_DIR, OUT_UN_ZIP_DIR);
            Log.e(TAG, "完成解压");

        } catch (Exception e) {
            e.printStackTrace(); // TODO
        }
    }

    /**
     * zip压缩功能.
     * 压缩baseDir(文件夹目录)下所有文件，包括子目录
     *
     * @throws Exception
     */
    public static void zipFile(String baseDir, String outZipFileName) throws Exception {
        List fileList = getSubFiles(new File(baseDir));
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outZipFileName));
        ZipEntry ze = null;
        byte[] buf = new byte[BUFFER];
        int readLen = 0;
        for (int i = 0; i < fileList.size(); i++) {
            File f = (File) fileList.get(i);
            ze = new ZipEntry(getAbsFileName(baseDir, f));
            ze.setSize(f.length());
            ze.setTime(f.lastModified());
            zos.putNextEntry(ze);
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            while ((readLen = is.read(buf, 0, BUFFER)) != -1) {
                zos.write(buf, 0, readLen);
            }
            is.close();
        }
        zos.close();
    }

    /**
     * 取得指定目录下的所有文件列表，包括子目录.
     *
     * @param baseDir File 指定的目录
     * @return 包含java.io.File的List
     */
    private static List getSubFiles(File baseDir) {
        List ret = new ArrayList();
        File[] tmp = baseDir.listFiles();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].isFile())
                ret.add(tmp[i]);
            if (tmp[i].isDirectory())
                ret.addAll(getSubFiles(tmp[i]));
        }
        return ret;
    }

    /**
     * 给定根目录，返回另一个文件名的相对路径，用于zip文件中的路径.
     *
     * @param baseDir      java.lang.String 根目录
     * @param realFileName java.io.File 实际的文件名
     * @return 相对文件名
     */
    private static String getAbsFileName(String baseDir, File realFileName) {
        File real = realFileName;
        File base = new File(baseDir);
        String ret = real.getName();
        while (true) {
            real = real.getParentFile();
            if (real == null)
                break;
            if (real.equals(base))
                break;
            else
                ret = real.getName() + "/" + ret;
        }
        return ret;
    }


    /**
     * 解压缩功能.
     * 将ZIP_FILENAME文件解压到ZIP_DIR目录下.
     *
     * @throws Exception
     */
    public static void unzipFile(String zipFile, String outDirPath) throws Exception {

        File outDir = new File(outDirPath);
        if (outDir.exists()) {
            outDir.deleteOnExit();
        }// end if
        outDir.mkdir();

        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                File f = new File(outDirPath + ze.getName());
                f.mkdir();
                continue;
            }

            File file = getRealFileName(outDirPath, ze.getName());
            if (file == null) {
                continue;
            }// end if

            FileOutputStream fos = new FileOutputStream(file);
            OutputStream os = new BufferedOutputStream(fos);
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                ret = new File(ret, dirs[i]);
            }
            if (!ret.exists()) {
                ret.mkdirs();
            }
        }
        return new File(ret, dirs[dirs.length - 1]);
    }
}
