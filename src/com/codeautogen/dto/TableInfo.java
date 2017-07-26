package com.codeautogen.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lianfei.qu on 2017/7/22.
 * 表信息
 */
public class TableInfo {
    private String tableName;
    private List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
    private String primaryKeyName;
    private String primaryKeyType;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ItemInfo> getItemInfoList() {
        return itemInfoList;
    }

    public void setItemInfoList(List<ItemInfo> itemInfoList) {
        this.itemInfoList = itemInfoList;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public String getPrimaryKeyType() {
        return primaryKeyType;
    }

    public void setPrimaryKeyType(String primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
    }
}
