package com.codeautogen;

import com.codeautogen.common.Common;
import com.codeautogen.dto.ItemInfo;
import com.codeautogen.dto.TableInfo;

/**
 * Created by lianfei.qu on 2017/7/22.
 */
public class ModelFile extends Common {
    public void autoGenModelFile(String namespace, TableInfo tableInfo){

    StringBuilder strBuilder = new StringBuilder();

    StringBuilder attributeStrBuilder = new StringBuilder();
    for(ItemInfo info: tableInfo.getItemInfoList()){
        String attributeStr = String.format("private %s %s;",
                switchJdbcTypeToJavaClass(info.getType()),
                convertUnderscodeToCapitals(info.getName()));
        attributeStrBuilder.append(attributeStr);
    }

    strBuilder.append("package "+namespace+".model");
    strBuilder.append(
            String.format("public class %s { %s }",
                    convertUnderscodeToCapitals(tableInfo.getTableName()),
                    attributeStrBuilder.toString())
    );

    outPutFile(".\\code\\"+convertUnderscodeToCapitals(captureFirst(tableInfo.getTableName()))+".java",strBuilder.toString());

    }
}
