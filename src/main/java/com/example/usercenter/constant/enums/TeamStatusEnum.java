package com.example.usercenter.constant.enums;

/**
 * 队伍状态枚举
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-01-23-13:05
 */
public enum TeamStatusEnum {

    PUBLIC(0, "公开"),
    PRIVATE(0, "私有"),
    SECRET(0, "加密");

    private int value;

    private String text;

    public static TeamStatusEnum getTeamEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if (value == teamStatusEnum.getValue()) {
                return teamStatusEnum;
            }
        }
        return null;
    }

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
