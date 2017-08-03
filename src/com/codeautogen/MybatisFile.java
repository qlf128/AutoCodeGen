package com.codeautogen;

import com.codeautogen.common.Common;
import com.codeautogen.dto.ItemInfo;
import com.codeautogen.dto.TableInfo;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lianfei.qu on 2017/7/22.
 * mybatis的文件自动生成功能
 */
public class MybatisFile extends Common {

    private String genAllAttributeStr(TableInfo tableInfo){
        StringBuilder sqlContentBuilder = new StringBuilder();
        for(ItemInfo info : tableInfo.getItemInfoList()){
            sqlContentBuilder.append(info.getName());
            sqlContentBuilder.append(", ");
        }
        String sqlContent = sqlContentBuilder.toString();
        return sqlContent.substring(0,sqlContent.length()-2);
    }

    private String genAllJavaAttributeStr(TableInfo tableInfo){
        StringBuilder javaContentBuilder = new StringBuilder();
        for(ItemInfo info : tableInfo.getItemInfoList()){
            javaContentBuilder.append(swithJdbcVarToJavaUseInMybatisXml(info.getName(),info.getType()));
            javaContentBuilder.append(", ");
        }
        String javaContent = javaContentBuilder.toString();
        return javaContent.substring(0,javaContent.length()-2);
    }

    private String genAllAttributeIncludeTestStr(TableInfo tableInfo){
        StringBuilder sqlContentBuilder = new StringBuilder();
        //<trim prefix="(" suffix=")" suffixOverrides="," >
        sqlContentBuilder.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");

        for(ItemInfo info : tableInfo.getItemInfoList()){
            //<if test="id != null" >
            String testStr = String.format("<if test=\"%s != null\" >",info.getName());
            sqlContentBuilder.append(testStr);
            sqlContentBuilder.append(info.getName());
            sqlContentBuilder.append(", ");
            sqlContentBuilder.append("</if>");
        }
        sqlContentBuilder.append("</trim>");
        String sqlContent = sqlContentBuilder.toString();
        return sqlContent;
    }

    private String genAllJavaAttributeIncludeTestStr(TableInfo tableInfo){
        StringBuilder javaContentBuilder = new StringBuilder();

        //<trim prefix="values (" suffix=")" suffixOverrides="," >
        javaContentBuilder.append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");

        for(ItemInfo info : tableInfo.getItemInfoList()){
            //<if test="id != null" >
            String testStr = String.format("<if test=\"%s != null\" >",info.getName());
            javaContentBuilder.append(testStr);
            javaContentBuilder.append(swithJdbcVarToJavaUseInMybatisXml(info.getName(),info.getType()));
            javaContentBuilder.append(", ");
            javaContentBuilder.append("</if>");
        }
        javaContentBuilder.append("</trim>");
        String javaContent = javaContentBuilder.toString();
        return javaContent;
    }

    private String genUpdateMapStr(TableInfo tableInfo){
        StringBuilder updateMapStrBuilder = new StringBuilder();

        for(ItemInfo itemInfo : tableInfo.getItemInfoList()){
            if(itemInfo.getName().equalsIgnoreCase(tableInfo.getPrimaryKeyName())){
                continue;
            }
            updateMapStrBuilder.append(itemInfo.getName());
            updateMapStrBuilder.append(" = ");
            updateMapStrBuilder.append(swithJdbcVarToJavaUseInMybatisXml(itemInfo.getName(),itemInfo.getType()));
            updateMapStrBuilder.append(",");
        }
        String updateMapStr = updateMapStrBuilder.toString();

        return updateMapStr.substring(0,updateMapStr.length()-1);
    }

    private String genUpdateMapIncludeTestStr(TableInfo tableInfo){
        StringBuilder updateMapStrBuilder = new StringBuilder();

        for(ItemInfo itemInfo : tableInfo.getItemInfoList()){
            if(itemInfo.getName().equalsIgnoreCase(tableInfo.getPrimaryKeyName())){
                continue;
            }
            updateMapStrBuilder.append(String.format("<if test=\"%s != null\" >",convertUnderscodeToCapitals(itemInfo.getName())));
            updateMapStrBuilder.append(itemInfo.getName());
            updateMapStrBuilder.append(" = ");
            updateMapStrBuilder.append(swithJdbcVarToJavaUseInMybatisXml(itemInfo.getName(),itemInfo.getType()));
            updateMapStrBuilder.append(",");
            updateMapStrBuilder.append("</if>");
        }
        String updateMapStr = updateMapStrBuilder.toString();

        return updateMapStr;
    }

    public void autoGenMybatisFile(String namespace, TableInfo tableInfo) throws IOException {
        String daoClassName=convertUnderscodeToCapitals(captureFirst(tableInfo.getTableName()))+"DAO";
        String modelClassName=convertUnderscodeToCapitals(captureFirst(tableInfo.getTableName()));

        String daoClass=namespace+".dao."+captureFirst(daoClassName);
        String modelClass=namespace+".model."+captureFirst(modelClassName);

        String allSqlAttributeStr = genAllAttributeStr(tableInfo);
        String allJavaAttributeStr = genAllJavaAttributeStr(tableInfo);

        Document document = new Document();

        /*Head*/

        /*mapper声明*/
        Element mapper = new Element("mapper");
        mapper.setAttribute("namespace",namespace+".dao."+daoClassName);
        document.addContent(mapper);

        /*resultMap*/
        Element resultMap = new Element("resultMap");
        resultMap.setAttribute("id","BaseResultMap");
        resultMap.setAttribute("type",namespace+".model."+modelClassName);

        mapper.addContent(resultMap);
        //pramaryKey
        Element id = new Element("id");
        id.setAttribute("column",tableInfo.getPrimaryKeyName());
        id.setAttribute("property","id");
        id.setAttribute("jdbcType",convertJdbcTypeToCapital(tableInfo.getPrimaryKeyType()));
        resultMap.addContent(id);

        for(ItemInfo info : tableInfo.getItemInfoList()){
            if(info.getName().equals(tableInfo.getPrimaryKeyName())){
                continue;
            }
            Element result = new Element("result");
            result.setAttribute("column",info.getName());
            result.setAttribute("property",convertUnderscodeToCapitals(info.getName()));
            result.setAttribute("jdbcType",convertJdbcTypeToCapital(info.getType()));
            resultMap.addContent(result);
        }
        /*baseColumnList*/
        Element sql = new Element("sql");
        sql.setAttribute("id","Base_Column_List");
        sql.addContent(allSqlAttributeStr);
        mapper.addContent(sql);

        /*selectByPrimaryKey*/
        Element selectByPrimaryKey = new Element("select");
        selectByPrimaryKey.setAttribute("id","selectByPrimaryKey");
        selectByPrimaryKey.setAttribute("resultMap","BaseResultMap");
        selectByPrimaryKey.setAttribute("parameterType",switchJdbcTypeToJavaClassIncludePath(tableInfo.getPrimaryKeyType()));

        String selectStr = String.format("select %s from %s where %s = %s",
                "<include refid=\"Base_Column_List\" />",
                tableInfo.getTableName(),
                tableInfo.getPrimaryKeyName(),
                swithJdbcVarToJavaUseInMybatisXml(tableInfo.getPrimaryKeyName(),convertJdbcTypeToCapital(tableInfo.getPrimaryKeyType())));
        selectByPrimaryKey.addContent(new CDATA(selectStr));
        mapper.addContent(selectByPrimaryKey);

        /*insert*/
        Element insert = new Element("insert");
        insert.setAttribute("id","insert");
        insert.setAttribute("parameterType",modelClass);
        String insertSqlStr = "";
        insertSqlStr = String.format("insert into %s (%s) values (%s) ",tableInfo.getTableName(),allSqlAttributeStr,allJavaAttributeStr);
        insert.addContent(new CDATA(insertSqlStr));

        mapper.addContent(insert);
        /*update*/
        Element updateByPrimaryKey = new Element("update");
        updateByPrimaryKey.setAttribute("id","updateByPrimaryKey");
        updateByPrimaryKey.setAttribute("parameterType",modelClass);

        String updateMapStr = genUpdateMapStr(tableInfo);
        String updateByParimaryKeyStr = String.format("update %s set %s where %s = %s",
                tableInfo.getTableName(),
                updateMapStr,
                tableInfo.getPrimaryKeyName(),
                swithJdbcVarToJavaUseInMybatisXml(tableInfo.getPrimaryKeyName(),convertJdbcTypeToCapital(tableInfo.getPrimaryKeyType())));
        updateByPrimaryKey.addContent(new CDATA(updateByParimaryKeyStr));

        mapper.addContent(updateByPrimaryKey);

        /*updateByPrimaryKeySelective*/
        Element updateByPrimaryKeySelective = new Element("update");
        updateByPrimaryKeySelective.setAttribute("id","updateByPrimaryKeySelective");
        updateByPrimaryKeySelective.setAttribute("parameterType",modelClass);

        String updateByPrimaryKeySelectiveStr = String.format("update %s <set > %s </set> where %s = %s ",
                tableInfo.getTableName(),
                genUpdateMapIncludeTestStr(tableInfo),
                tableInfo.getPrimaryKeyName(),
                swithJdbcVarToJavaUseInMybatisXml(tableInfo.getPrimaryKeyName(),convertJdbcTypeToCapital(tableInfo.getPrimaryKeyType()))
                );
        updateByPrimaryKeySelective.addContent(new CDATA(updateByPrimaryKeySelectiveStr));

        mapper.addContent(updateByPrimaryKeySelective);

        /*delete*/
        Element delete = new Element("delete");
        delete.setAttribute("id","deleteByPrimaryKey");
        delete.setAttribute("parameterType",switchJdbcTypeToJavaClassIncludePath(convertJdbcTypeToCapital(tableInfo.getPrimaryKeyType())));
        String deleteStr = String.format("delete from %s where %s = %s",
                tableInfo.getTableName(),
                tableInfo.getPrimaryKeyName(),
                swithJdbcVarToJavaUseInMybatisXml(tableInfo.getPrimaryKeyName(),convertJdbcTypeToCapital(tableInfo.getPrimaryKeyType()))
                );
        delete.addContent(new CDATA(deleteStr));

        mapper.addContent(delete);

        /*seleteAll*/
        Element selectAll = new Element("select");
        selectAll.setAttribute("id","selectAll");
        selectAll.setAttribute("resultMap","BaseResultMap");
        String selectAllStr = String.format("select * from %s",tableInfo.getTableName());
        selectAll.addContent(new CDATA(selectAllStr));

        mapper.addContent(selectAll);

        /*write to file*/
        XMLOutputter out = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        format.setIndent("    ");
        out.setFormat(format);


        //删除文件中的 <![CDATA[ 和 ]]>

        String xmlStr = out.outputString(document);
        xmlStr = xmlStr.replace("<![CDATA[","");
        xmlStr = xmlStr.replace("]]>","");

        //写文件
        outPutFile(".\\code\\"+"I"+convertUnderscodeToCapitals(captureFirst(tableInfo.getTableName()))+"Mapper"+".xml",xmlStr);

    /*
        方案二，先写入文件，再读出来修改
        String filePath = ".\\code\\"+"I"+convertUnderscodeToCapitals(captureFirst(tableInfo.getTableName()))+"Mapper"+".xml";
        out.output(document, new FileOutputStream(filePath));

        //删除文件中的 <![CDATA[ 和 ]]>
        String xmlStr = readFile(filePath);
        xmlStr = xmlStr.replace("<![CDATA[","");
        xmlStr = xmlStr.replace("]]>","");

        outPutFile(filePath,xmlStr);
    */

    }


}
