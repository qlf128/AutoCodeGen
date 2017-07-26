package com.codeautogen.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Clob;

/**
 * Created by lianfei.qu on 2017/7/22.
 * 一些公用的方法写在这里面
 */
public class Common {

    /**
     * 把字符串中的下划线替换成大写字母
     * 比如ab_cd_e替换成abCdE
     * @param oriStr
     * @return
     */
    public String convertUnderscodeToCapitals(String oriStr){
        StringBuilder result = new StringBuilder();
        if (oriStr != null && oriStr.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < oriStr.length(); i++) {
                char ch = oriStr.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 驼峰转下划线
     * @param oriStr
     * @return
     */
    public String convertCapitalsToUnderscode(String oriStr){
        StringBuilder result = new StringBuilder();
        if (oriStr != null && oriStr.length() > 0) {
            result.append(oriStr.substring(0, 1).toLowerCase());
            for (int i = 1; i < oriStr.length(); i++) {
                char ch = oriStr.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    public String captureFirst(String str) {
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return  str;

    }
    /**
     * 切换jdbc类型成java类型，含路径，比如lang.java.Integer
     * 来源：http://www.cnblogs.com/kagome2014/p/5619677.html
     INTEGER	 int or java.lang.Integer
     BIGINT	 long or java.lang.Long
     SMALLINT	 short or java.lang.Short
     FLOAT	 float or java.lang.Float
     DOUBLE	 double or java.lang.Double
     NUMERIC	 java.math.BigDecimal
     CHAR	 java.lang.String
     VARCHAR	 java.lang.String
     TINYINT	 byte or java.lang.Byte
     BIT	 boolean or java.lang.Boolean
     DATE	 java.util.Date or java.sql.Date
     TIME	 java.util.Date or java.sql.Time
     TIMESTAMP	 java.util.Date or java.sql.Timestamp
     TIMESTAMP	 java.util.Calendar
     DATE	 java.util.Calendar
     VARBINARY (or BLOB)	 byte[]
     CLOB	 java.lang.String
     VARBINARY (or BLOB)	 any Java class that implements java.io.Serializable
     CLOB	 java.sql.Clob
     BLOB	 java.sql.Blob
     * @param jdbcType
     * @return
     */
    public String switchJdbcTypeToJavaClassIncludePath(String jdbcType){
        String jdbcTypeLowCase = jdbcType.toLowerCase();
        String javaType = "";

        switch(jdbcTypeLowCase){
            case "int":
            case "integer":
                javaType = "java.lang.Integer";
                break;
            case "char":
            case "varchar":
                javaType = "java.lang.String";
                break;
            case "tinyint":
                javaType = "java.lang.Byte";
                break;
            case "bigint":
                javaType = "java.lang.Long";
                break;
            case "smallint":
                javaType = "java.lang.Short";
                break;
            case "float":
                javaType = "java.lang.Float";
                break;
            case "double":
                javaType = "java.lang.Double";
                break;
            case "numeric":
                javaType = "java.math.BigDecimal";
                break;
            case "bit":
                javaType = "java.lang.Boolean";
                break;
            case "date":
                javaType = "java.sql.Date";
                break;
            case "time":
                javaType = "java.sql.Time";
                break;
            case "timestamp":
                javaType = "java.sql.Timestamp";
                break;
            case "varbinary":
                javaType = "byte[]";
                break;
            case "clob":
                javaType = "java.sql.Clob";
                break;
            case "blob":
                javaType = "java.sql.Blob";
                break;
            case "enum": //mysql会有enum属性，简直了。。
                javaType = "java.lang.String";
            default:
                javaType = "";
                break;
        }

        return javaType;
    }

    /**
     * 切换jdbc类型成java类型，不含路径，比如Integer
     *
     * @param jdbcType
     * @return
     */
    public String switchJdbcTypeToJavaClass(String jdbcType){
        String jdbcTypeLowCase = jdbcType.toLowerCase();
        String javaType = "";

        switch(jdbcTypeLowCase){
            case "int":
            case "integer":
                javaType = "Integer";
                break;
            case "char":
            case "varchar":
                javaType = "String";
                break;
            case "tinyint":
                javaType = "Byte";
                break;
            case "bigint":
                javaType = "Long";
                break;
            case "smallint":
                javaType = "Short";
                break;
            case "float":
                javaType = "Float";
                break;
            case "double":
                javaType = "Double";
                break;
            case "numeric":
                javaType = "BigDecimal";
                break;
            case "bit":
                javaType = "Boolean";
                break;
            case "date":
                javaType = "Date";
                break;
            case "time":
                javaType = "Time";
                break;
            case "timestamp":
                javaType = "Timestamp";
                break;
            case "varbinary":
                javaType = "byte[]";
                break;
            case "clob":
                javaType = "Clob";
                break;
            case "blob":
                javaType = "Blob";
                break;
            case "enum": //mysql会有enum属性，简直了。。
                javaType = "String";
            default:
                javaType = "";
                break;
        }

        return javaType;
    }

    public String swithJdbcVarToJavaUseInMybatisXml(String jdbcName, String jdbcType){
        //a_bc_cd => #{aBcCd，jdbcType={jdbcType}}
        return String.format("#{%s,jdbcType=%s}",convertUnderscodeToCapitals(jdbcName),jdbcType);
    }

    /**
     * 根据内容创建和输出文件
     * @param fileName
     * @param content
     */
    public void outPutFile(String fileName, String content){
        try{
            File file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
