package com.example.hdfs.controller;

import com.example.hdfs.domain.CreateDocker;
import com.example.hdfs.domain.DockerFile;
import com.example.hdfs.repository.DockerFileRepository;
import com.example.hdfs.util.dockerThread;
import com.example.hdfs.util.dockerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//https://www.cnblogs.com/smallSevens/p/8874213.html
//开线程池控制并发数

@RestController
@RequestMapping("/docker")
public class DockerController {
    @Autowired
    private DockerFileRepository dockerFileRepository;

    @RequestMapping(value="/test", method= RequestMethod.GET)
    public void test(){
        String ss= "abcWEBSSH_LOGIN:http://123.57.33.125:4433/?ssh=ssh://ubuntu:kdrd9f@172.18.0.3:22LOGIN_END" +
                "llllllllllllllllllllllllllllllllllllllllllllllllllllll"+"WEBSSH_LOGIN:http://123.57.33.125:4433/?ssh=ssh://ubuntu:kdrd9f@172.18.0.3:22LOGIN_END";
        List urls = new ArrayList<String>();
        urls = dockerUtil.parseUrl(ss);
        System.out.println(urls);
        //System.out.println(loginUrl);
    }

    @RequestMapping(value="/create", method= RequestMethod.POST)
    public ResponseEntity<Object> createDockers(@RequestBody Map<String,String> maplist) {
        String email = maplist.get("email");
        String type = maplist.get("type");
        HttpHeaders headers = new HttpHeaders();
        CreateDocker createDocker = new CreateDocker();

        DockerFile dockerFile = new DockerFile();
        dockerFile.setEmail(email);
        dockerFile.setType(type);
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        dockerFile.setCreatedAt(dateFormat.format(date));

        //String result = null;
        try {
            createDocker.setEmail(email);
            createDocker.setType(type);
            Thread thread = new dockerThread(createDocker);
            thread.start();
            thread.join(); //等待子进程结束
            System.out.println(createDocker.getUrls());
            dockerFile.setUrl(createDocker.getUrls().get(0));
            dockerFileRepository.save(dockerFile);
            return new ResponseEntity<Object>(createDocker.getUrls(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Object>("ok", headers, HttpStatus.OK);

        //System.out.println(result);
    }
}
