package com.example.comundademo1;

public enum ComundaParamType {

    STRING("字符串", "1","string"),
    INTEGER("整数", "2","number"),
    MONEY("金额（2位小数位）", "3","number"),
    FLOAT("小数（5位小数位）", "4","number"),
    PERCENT("百分比", "5","number"),
    DATE("日期", "6","date"),
    TIME("时间", "7","time"),
    LIST("列表", "8","list"),
    DATETIME("日期时间","9","date-time"),
    BOOLEAN("布尔值","10","boolean"),
    BUTTON("按钮","11","null"),
    LINK("链接","12","null"),

    ;

    private String desc;

    private String value;

    private String comundaType;

    public String getDesc() {
        return desc;
    }

    ComundaParamType(String desc, String value,String comundaType) {
        this.desc = desc;
        this.value = value;
        this.comundaType = comundaType;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComundaType() {
        return comundaType;
    }

    public void setComundaType(String comundaType) {
        this.comundaType = comundaType;
    }
}
