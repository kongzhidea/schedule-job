package com.schedule.conste;

/**
 * @auther zhihui.kzh
 * @create 14/8/1721:11
 */
public enum JobHistoryStatusEnum {

    RUNING(1, "执行中"),

    SUCCESS(2, "成功"),

    FAILED(3, "失败");

    private int value;
    private String desc;

    private JobHistoryStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static JobHistoryStatusEnum getJobStatusEnumByStatus(int status) {
        for (JobHistoryStatusEnum jobStatusEnum : JobHistoryStatusEnum.values()) {
            if (jobStatusEnum.value == status) {
                return jobStatusEnum;
            }
        }
        return null;
    }
}
