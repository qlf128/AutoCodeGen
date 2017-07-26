package com.codeautogen;

import com.codeautogen.common.Common;
import com.codeautogen.dto.TableInfo;

/**
 * Created by lianfei.qu on 2017/7/22.
 */
public class ServiceFile extends Common{

    public void autoGenServiceFile(String namespace,TableInfo tableInfo){
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
                String.format("\tpublic int updateByPrimaryKey(%s %s); \n\r",primaryKeyJavaType,primaryKeyJavaName)
        );
        //updateByPrimaryKeySelective
        methodBuilder.append(
                String.format("\tpublic int updateByPrimaryKeySelective(%s %s); \n\r",primaryKeyJavaType,primaryKeyJavaName)
        );

        //insert
        methodBuilder.append(
                String.format("\tpublic int insert(%s %s); \n\r",modelClassName,modelName)
        );

        //delete
        methodBuilder.append(
                String.format("\tpublic int delete(%s %s); \n\r",primaryKeyJavaType,primaryKeyJavaName)
        );


        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("package "+namespace+".service \n\r");
        strBuilder.append(
                String.format("public interface I%sService { \n\r%s }",
                        convertUnderscodeToCapitals(captureFirst(tableInfo.getTableName())),
                        methodBuilder.toString())
        );

        outPutFile(".\\code\\"+"I"+modelClassName+"Service"+".java",strBuilder.toString());
    }

}
