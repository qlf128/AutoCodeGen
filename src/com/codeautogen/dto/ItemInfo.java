package com.codeautogen.dto;

/**
 * Created by lianfei.qu on 2017/7/22.
 */
public class ItemInfo {
    private String name;
    private String type;
    private Integer typeLength;//长度
    private Integer typeDecimal;//小数长度
    private String defaultValue;//默认值
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTypeLength() {
        return typeLength;
    }

    public void setTypeLength(Integer typeLength) {
        this.typeLength = typeLength;
    }

    public Integer getTypeDecimal() {
        return typeDecimal;
    }

    public void setTypeDecimal(Integer typeDecimal) {
        this.typeDecimal = typeDecimal;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
