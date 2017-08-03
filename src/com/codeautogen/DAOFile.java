package com.codeautogen;

import com.codeautogen.common.Common;
import com.codeautogen.dto.TableInfo;

/**
 * Created by lianfei.qu on 2017/7/22.
 */
public class DAOFile extends Common {
    public void autoGenDAOFile(String namespace,TableInfo tableInfo){
        String modelClassName = captureFirst(convertUnderscodeToCapitals(tableInfo.getTableName()));
        String modelName = convertUnderscodeToCapitals(tableInfo.getTableName());
        String primaryKeyJavaType = switchJdbcTypeToJavaClass(tableInfo.getPrimaryKeyType());
        String primaryKeyJavaName = convertUnderscodeToCapitals(tableInfo.getPrimaryKeyName());

        StringBuilder methodBuilder = new StringBuilder();
        //selectAll
        methodBuilder.append(
                String.format("\tpublic List<%s> selectAll(); \n\r",modelClassName)
        );

        //selectByPrimaryKey
        methodBuilder.append(
                String.format("\tpublic %s selectByPrimaryKey(%s %s); \n\r",modelClassName,primaryKeyJavaType,primaryKeyJavaName)
        );

        //updateByPrimaryKey
        methodBuilder.append(
                String.format("\tpublic int updateByPrimaryKey(%s %s); \n\r",modelClassName,modelName)
        );
        //updateByPrimaryKeySelective
        methodBuilder.append(
                String.format("\tpublic int updateByPrimaryKeySelective(%s %s); \n\r",modelClassName,modelName)
        );

        //insert
        methodBuilder.append(
                String.format("\tpublic int insert(%s %s); \n\r",modelClassName,modelName)
        );

        //delete
        methodBuilder.append(
                String.format("\tpublic int deleteByPrimaryKey(%s %s); \n\r",primaryKeyJavaType,primaryKeyJavaName)
        );

        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("package "+namespace+".dao; \n\r");
        strBuilder.append(
                String.format("public interface %sDAO { \n\r%s }",
                        convertUnderscodeToCapitals(captureFirst(tableInfo.getTableName())),
                        methodBuilder.toString())
        );

        outPutFile(".\\code\\"+modelClassName+"DAO"+".java",strBuilder.toString());

    }
}
