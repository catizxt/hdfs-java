package com.example.hdfs.util;

import com.example.hdfs.domain.CreateDocker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zly
 */
public class dockerThread extends Thread{
    //传参

    private String email;
    private String type;
    private CreateDocker createDocker;
    public dockerThread(CreateDocker createDocker){
        this.createDocker = createDocker;
        this.email = createDocker.getEmail();
        this.type = createDocker.getType();
    }

    @Override
    public void run() {

        File file = new File("/data/cloud-lab/create_dockerfile");
        //this.type要经过处理
        String command = "/bin/bash " + this.type;

        //这里会阻塞，在execCmd里面
        try {
            String result= dockerUtil.execCmd(command,file);
            System.out.println(result);
            List<String> urls = new ArrayList<String>();
            urls=dockerUtil.parseUrl(result);
            createDocker.setUrls(urls);
            //DockerController.callback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
