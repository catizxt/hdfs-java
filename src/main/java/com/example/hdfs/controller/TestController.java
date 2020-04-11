package com.example.hdfs.controller;

import com.example.hdfs.config.HdfsConfig;
import com.example.hdfs.util.HdfsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

	//public static fpath = "hdfs://"+NamenodeIP+":"+NamenodePort+"/user/helloworld.txt";
	// 文件下载
	 @RequestMapping("/downloadhdfs")
     @ResponseBody
	 public String downloadhdfs() {
		HdfsConfig config = new HdfsConfig("39.96.93.7", "9000","hadoop");
		String source = "/user/data/helloworld.txt"; // windows 文件
		String destination = "/user/data/hello.txt"; //centos7 hdfs 文件存储地址
		HdfsUtil.download(config, source, destination);
		return "hello world";
	}
}

