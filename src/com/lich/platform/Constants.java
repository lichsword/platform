package com.lich.platform;

/**
 * Created with IntelliJ IDEA.
 * User: lichsword
 * Date: 14-6-13
 * Time: 上午9:48
 * <p/>
 * TODO
 */
public class Constants {

    public static final int Undefined = -1;

    public static final int STATE_START = 0;
    public static final int STATE_IDLE = 1;
    public static final int STATE_WORK = 2;
    public static final int STATE_REFRESH = 3;
    public static final int STATE_ENDED = 4;

    public static final int EVENT_EXIT = 0;
    public static final int EVENT_INPUT = 1;

    public static final int STATUS_BAR_LINE_BEGIN = 0;
    public static final int STATUS_BAR_HEIGHT = 1;
    public static final int STATUS_BAR_LINE_END = STATUS_BAR_LINE_BEGIN + STATUS_BAR_HEIGHT;

    public static final int TASK_BAR_LINE_BEGIN = STATUS_BAR_LINE_END;
    public static final int TASK_BAR_HEIGHT = 1;
    public static final int TASK_BAR_LINE_END = TASK_BAR_LINE_BEGIN + TASK_BAR_HEIGHT;

    public static final int OUTPUT_AREA_LINE_BEGIN = TASK_BAR_LINE_END;
    public static final int OUTPUT_AREA_HEIGHT = 10;
    public static final int OUTPUT_AREA_LINE_END = OUTPUT_AREA_LINE_BEGIN + OUTPUT_AREA_HEIGHT;

    public static final int DIVIDER_HEIGHT = 1;
    public static final int INPUT_AREA_LINE_BEGIN = OUTPUT_AREA_LINE_END + DIVIDER_HEIGHT;
    public static final int INPUT_AREA_LINE_END = Undefined;

    // 常量字符串
    public static final String STR_INPUT = "Input";
    public static final String STR_OUTPUT = "Output";

    // 常量格式化串
    public static final String FORMAT_STATUS_BAR = "STATE:%s\tDATE:%s";
    public static final String FORMAT_TASK_BAR = "TASK:%s";
    public static final String FORMAT_INPUT_AREA = "Input: (Tips: %s)";
    public static final String FORMAT_ELLIPIZED = "(%d more...)";
    public static final String FORMAT_HANDLE_FILES = "do %d files";

    // 常量标志位
    public static final int fInc = 0;
    public static final int fDesc = 1;

}
