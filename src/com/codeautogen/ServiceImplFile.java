package com.codeautogen;

import com.codeautogen.common.Common;
import com.codeautogen.dto.TableInfo;

/**
 * Created by lianfei.qu on 2017/7/22.
 */
public class ServiceImplFile extends Common {

    public void autoGenServiceImplFile(String namespace, TableInfo tableInfo){
        String modelClassName = captureFirst(convertUnderscodeToCapitals(tableInfo.getTableName()));
        String modelName = convertUnderscodeToCapitals(tableInfo.getTableName());
        String primaryKeyJavaType = switchJdbcTypeToJavaClass(tableInfo.getPrimaryKeyType());
        String primaryKeyJavaName = convertUnderscodeToCapitals(tableInfo.getPrimaryKeyName());

        String daoName = convertUnderscodeToCapitals(tableInfo.getTableName())+"DAO";

        StringBuilder methodBuilder = new StringBuilder();

        methodBuilder.append(
                String.format("\t@Resource \n" +
                        "\tprivate %s %s;\n\r",modelClassName+"DAO",modelName+"DAO")
        );
        //selectAll
        methodBuilder.append(
                String.format("\t@Override \n" +
                        "\tpublic List<%s> selectAll(){ \n\r" +
                        "\t\treturn %s.selectAll(); \n" +
                        "\t} \n\r",modelClassName,daoName)
        );

        //selectByPrimaryKey
        methodBuilder.append(
                String.format("\t@Override \n" +
                        "\tpublic %s selectByPrimaryKey(%s %s){ \n\r" +
                        "\t\treturn %s.selectByPrimaryKey(%s %s); \n" +
                        "\t} \n\r",
                        modelClassName,
                        primaryKeyJavaType,primaryKeyJavaName,
                        daoName,
                        primaryKeyJavaType,primaryKeyJavaName)
        );

        //updateByPrimaryKey
        methodBuilder.append(
                String.format("\t@Override \n" +
                        "\tpublic int updateByPrimaryKey(%s %s){ \n\r" +
                        "\t\treturn %s.updateByPrimaryKey(%s %s); \n" +
                        "\t} \n\r",
                        primaryKeyJavaType,primaryKeyJavaName,
                        daoName,
                        primaryKeyJavaType,primaryKeyJavaName)
        );
        //updateByPrimaryKeySelective
        methodBuilder.append(
                String.format("\t@Override \n" +
                        "\tpublic int updateByPrimaryKeySelective(%s %s){ \n\r" +
                        "\t\treturn %s.updateByPrimaryKeySelective(%s %s); \n" +
                        "\t} \n\r",
                        primaryKeyJavaType,primaryKeyJavaName,
                        daoName,
                        primaryKeyJavaType,primaryKeyJavaName)
        );

        //insert
        methodBuilder.append(
                String.format("\t@Override \n " +
                        "\tpublic int insert(%s %s){ \n\r" +
                        "\t\treturn %s.insert(%s %s); \n" +
                        "\t} \n\r",
                        modelClassName,modelName,
                        daoName,
                        modelClassName,modelName)
        );

        //delete
        methodBuilder.append(
                String.format("\t@Override \n" +
                        "\tpublic int delete(%s %s){ \n\r" +
                        "\t\treturn %s.delete(%s %s); \n" +
                        "\t} \n\r",
                        primaryKeyJavaType,primaryKeyJavaName,
                        daoName,
                        primaryKeyJavaType,primaryKeyJavaName
                        )
        );


        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("package "+namespace+".service.impl \n\r");
        strBuilder.append("@Service \n");
        strBuilder.append(
                String.format("public class %sServiceImpl implements I%sService { \n\r%s }",
                        convertUnderscodeToCapitals(captureFirst(tableInfo.getTableName())),
                        convertUnderscodeToCapitals(captureFirst(tableInfo.getTableName())),
                        methodBuilder.toString())
        );

        outPutFile(".\\code\\"+modelClassName+"ServiceImpl"+".java",strBuilder.toString());
    }

}
