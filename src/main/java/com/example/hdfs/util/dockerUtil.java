package com.example.hdfs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class dockerUtil {
    public static List<String> parseUrl(String result){
        List<String> urls = new ArrayList<String>();
        int begin = 0;
        while ((result.indexOf("WEBSSH_LOGIN",begin)) != -1){
            int index = result.indexOf("WEBSSH_LOGIN",begin);
            String loginUrl = result.substring(index+13,result.indexOf("LOGIN_END",begin));
            //System.out.println(loginUrl);
            urls.add(loginUrl);
            begin = result.indexOf("LOGIN_END",begin)+10;
        }
        return urls;
    }

    public static String execCmd(String cmd, File dir) throws Exception {
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            process = Runtime.getRuntime().exec(cmd, null, dir);
            //https://www.cnblogs.com/fclbky/p/6112180.html
            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }
        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }
        // 返回执行结果
        return result.toString();
    }

    private static void closeStream(BufferedReader stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }
}
