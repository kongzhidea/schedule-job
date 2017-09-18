package com.schedule.conste;

/**
 * @auther zhihui.kzh
 * @create 14/8/1721:11
 */
public enum JobStatusEnum {

    RUN(1, "调度中"),

    HANGUP(2, "暂停");

    private int value;
    private String desc;

    private JobStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static JobStatusEnum getJobStatusEnumByStatus(int status) {
        for (JobStatusEnum jobStatusEnum : JobStatusEnum.values()) {
            if (jobStatusEnum.value == status) {
                return jobStatusEnum;
            }
        }
        return null;
    }
}
