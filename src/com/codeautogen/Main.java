package com.codeautogen;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by lianfei.qu on 2017/7/22.
 */
public class Main {
    public static void main(String[] args){
        CodeAutoGen autoGen = new CodeAutoGen();

        Scanner s = new Scanner(System.in);
        String namespace = null;
        System.out.println("请输入namespace：");
        namespace = s.next();
        System.out.println("您输入的namespace是：");
        System.out.println(namespace);

        try {
            autoGen.codeAutoGen(namespace);
        } catch (IOException e) {
            System.out.println("文件操作发生错误！");
            e.printStackTrace();
        }
    }
}
