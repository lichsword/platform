package com.lich.platform.service;

import com.log.Log;
import com.util.FileUtils;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-24
 * Time: 上午11:23
 * <p/>
 * TODO
 */
public class TF_IDFService implements ITF_IDF {

    public static final String TAG = TF_IDFService.class.getSimpleName();

    public static void main(String[] args) {
        TF_IDFService itf_idf = new TF_IDFService();
        itf_idf.run();
    }

    public TF_IDFService() {
    }

    private void clearArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }
    }

    public void run() {
        testcase(test1, test2);
        testcase(test1, test3);
        testcase(test2, test3);
    }

    private void testcase(String left, String right) {
        double similarity = simulate(left, right);
        double dPercent = similarity * 100;
        int iPercent = (int) dPercent;
        int valve = 50;
        String desc;
        if (iPercent > valve) {
            desc = "相似";
        } else {
            desc = "不同";
        }

        Log.e(TAG, String.format("%s 与 %s 的相似度\n\t = %.2f (%d%%) 结论: %s", left, right, similarity, iPercent, desc));
    }


    private double simulate(String left, String right) {
        buildTF(left, tf1);
        buildTF(right, tf2);
        double similarity = compareTFs(tf1, tf2);
        return similarity;
    }

    private void buildTF(String src, int[] tf) {
        clearArray(tf);
        for (char ch : src.toCharArray()) {
            if (ch >= Constants.VISUAL_CHAR_INDEX_START &&
                    ch <= Constants.VISUAL_CHAR_INDEX_END) {
                // 是可见字符
                int index = ch - Constants.VISUAL_CHAR_INDEX_START;
                tf[index]++;
            } else {
                // log
            }
        }
    }

    private double compareTFs(int[] left, int[] right) {
        double cross = cross(left, right);
        double mode1 = modeTF(left);
        double mode2 = modeTF(right);
        double value = cross / (mode1 * mode2);

        return value;
    }

    private double cross(int[] vector1, int[] vector2) {
        if (vector1.length != vector2.length) {
            return 0;
        }// end if

        double sum = 0;
        int len = vector1.length;
        for (int i = 0; i < len; i++) {
            sum += vector1[i] * vector2[i];
        }
        return sum;
    }

    /**
     * 求模
     *
     * @param tf
     * @return
     */
    private double modeTF(int[] tf) {
        int power = 0;
        for (int value : tf) {
            if (value != 0) {
                power += (value * value);
            }
        }

        return Math.sqrt(power);
    }


    private static final String test1 = "aaabbbccc";//"show me the money";
    private static final String test2 = "bbbaaaccc";//"black sheep wall";
    private static final String test3 = "aaabbbcccXYSDK";//"show me the moneyasd";


    private int[] tf1 = new int[Constants.VISUAL_CHAR_INDEX_END - Constants.VISUAL_CHAR_INDEX_START + 1];
    private int[] tf2 = new int[Constants.VISUAL_CHAR_INDEX_END - Constants.VISUAL_CHAR_INDEX_START + 1];

    private HashMap<String, String> cache = new HashMap<String, String>();

    private void parseJavaFile(String path) {
        String text = FileUtils.readFile(path);
        char[] data = text.toCharArray();
        for (int i = 0; i < data.length; i++) {
            char ch = data[i];
            if (ch >= Constants.VISUAL_CHAR_INDEX_START && ch <= Constants.VISUAL_CHAR_INDEX_END) {
                // 是可见字符
                if (ch == ' ') {
                    // 空格可能是由于代码格式化不同，引入，故忽略意义。
                    continue;
                }// end if

                // TODO
            } else {
                continue;
            }
        }
    }

}