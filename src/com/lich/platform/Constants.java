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

    static final int Undefined = -1;

    static final int STATE_START = 0;
    static final int STATE_IDLE = 1;
    static final int STATE_WORK = 2;
    static final int STATE_REFRESH = 3;
    static final int STATE_ENDED = 4;

    static final int EVENT_EXIT = 0;
    static final int EVENT_INPUT = 1;

    static final int STATUS_BAR_LINE_BEGIN = 0;
    static final int STATUS_BAR_HEIGHT = 1;
    static final int STATUS_BAR_LINE_END = STATUS_BAR_LINE_BEGIN + STATUS_BAR_HEIGHT;

    static final int TASK_BAR_LINE_BEGIN = STATUS_BAR_LINE_END;
    static final int TASK_BAR_HEIGHT = 1;
    static final int TASK_BAR_LINE_END = TASK_BAR_LINE_BEGIN + TASK_BAR_HEIGHT;

    static final int OUTPUT_AREA_LINE_BEGIN = TASK_BAR_LINE_END;
    static final int OUTPUT_AREA_HEIGHT = 8;
    static final int OUTPUT_AREA_LINE_END = OUTPUT_AREA_LINE_BEGIN + OUTPUT_AREA_HEIGHT;

    static final int DIVIDER_HEIGHT = 1;
    static final int INPUT_AREA_LINE_BEGIN = OUTPUT_AREA_LINE_END + DIVIDER_HEIGHT;
    static final int INPUT_AREA_LINE_END = Undefined;

    // 常量字符串
    static final String STR_INPUT = "Input";
    static final String STR_OUTPUT = "Output";

    // 常量格式化串
    static final String FORMAT_STATUS_BAR = "STATE:%s\tDATE:%s";
    static final String FORMAT_TASK_BAR = "TASK:%s";
    static final String FORMAT_INPUT_AREA = "Input: (Tips: %s)";
}
