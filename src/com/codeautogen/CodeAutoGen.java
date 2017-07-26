package com.codeautogen;

import com.codeautogen.dto.ItemInfo;
import com.codeautogen.dto.TableInfo;

import java.io.*;

/**
 * Created by lianfei.qu on 2017/7/22.
 */
public class CodeAutoGen {

    public void codeAutoGen(String namespace) throws IOException {
        System.out.println("Begin Generate...");

        //读取文件信息
        TableInfo tableInfo = new TableInfo();
        //BufferedReader是可以按行读取文件
        FileInputStream inputStream = new FileInputStream(".\\resource\\info.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        //第一行，读取数据库表名
        str = bufferedReader.readLine();
        String[] targetStr = str.split("`");
        tableInfo.setTableName(targetStr[1]);

        while((str = bufferedReader.readLine()) != null)
        {
            ItemInfo itemInfo = new ItemInfo();
            String[] strLine = str.split("`");
            if(strLine[0].equals("  ")){
                itemInfo.setName(strLine[1]);

                String[] strLine2 = strLine[2].split("\\(");
                itemInfo.setType(strLine2[0].substring(1));

                tableInfo.getItemInfoList().add(itemInfo);
            }else if(strLine[0].equals("  PRIMARY KEY (")){
                tableInfo.setPrimaryKeyName(strLine[1]);
                break;
            }else{
                break;
            }

            //System.out.println(str);
        }

        for(ItemInfo itemInfo : tableInfo.getItemInfoList()){
            if(itemInfo.getName().equals(tableInfo.getPrimaryKeyName())){
                tableInfo.setPrimaryKeyType(itemInfo.getType());
            }
        }

        //close
        inputStream.close();
        bufferedReader.close();

        File file = new File(".\\code");
        if(file.exists()){

        }else{
            file.mkdir();
        }

        //自动生成mybatis配置文件
        MybatisFile mybatisFile = new MybatisFile();
        mybatisFile.autoGenMybatisFile(namespace,tableInfo);

        //生成model层文件
        ModelFile modelFile = new ModelFile();
        modelFile.autoGenModelFile(namespace,tableInfo);

        //自动生成dao层文件
        DAOFile daoFile = new DAOFile();
        daoFile.autoGenDAOFile(namespace,tableInfo);

        //自动生成service层文件
        ServiceFile serviceFile = new ServiceFile();
        serviceFile.autoGenServiceFile(namespace,tableInfo);

        //自动生成service impl文件
        ServiceImplFile serviceImplFile = new ServiceImplFile();
        serviceImplFile.autoGenServiceImplFile(namespace,tableInfo);

        System.out.println("Generate Success!");
        return;
    }
}
