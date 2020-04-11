package com.example.hdfs.controller;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@Controller
public class HelloController {

    @RequestMapping("/")
    @ResponseBody
    public String getHello() {
        return "hello world";
    }

    //https://stackoverflow.com/questions/20333394/return-a-stream-with-spring-mvcs-responseentity
    //https://blog.csdn.net/septdays/article/details/99422818
    //https://github.com/yeleaveszi/Play-Videos-In-HDFS
    @GetMapping(value = "/playvideo")
    public ResponseEntity<Object> addStu(@RequestHeader(value = "Range",required = false) String range, @RequestParam(value = "fpath",required = false) String fpath) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Custom-Header", "foo");
        if (fpath==null) {
            return new ResponseEntity<Object>("please set fpath", headers, HttpStatus.OK);
        }

        String filename="hdfs://"+"172.17.201.196"+":"+"9000"+fpath;
        //String filename="hdfs://"+"39.96.93.7"+":"+"9000"+fpath;
        Configuration config=new Configuration();
        FileSystem fs = null;
        FSDataInputStream in=null;
        try {
            fs = FileSystem.get(URI.create(filename),config);
            in=fs.open(new Path(filename));
            //return new ResponseEntity<Object>("please set fpath", headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final long fileLen = fs.getFileStatus(new Path(filename)).getLen();
        headers.add("Content-type", "video/mp4");

        //resp.setHeader("Content-type","video/mp4");
        //OutputStream out=resp.getOutputStream();
        //直接访问这个链接就是下载，stream通常用来文件传输的
        if(range==null)
        {
            filename=fpath.substring(fpath.lastIndexOf("/")+1);
            InputStreamResource inputStreamResource = new InputStreamResource(in);
            headers.setContentLength((int)fileLen);
            headers.add("Content-Disposition", "attachment; filename="+filename);
            headers.setContentType(MediaType.valueOf("application/octet-stream"));
            //in.close();
            //in = null;

            return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
        }
        else
        {
            long start=Integer.valueOf(range.substring(range.indexOf("=")+1, range.indexOf("-")));
            long count=fileLen-start;
            long end;
            if(range.endsWith("-")) {
                end=fileLen-1;
            } else {
                end=Integer.valueOf(range.substring(range.indexOf("-")+1));
            }
            String ContentRange="bytes "+String.valueOf(start)+"-"+end+"/"+String.valueOf(fileLen);
            //resp.setStatus(206);
            //headers.setContentType(MediaType.valueOf("video/mpeg4"));
            headers.add("Content-type", "video/mp4");

            headers.add("Content-Range",ContentRange);
            in.seek(start);
            try{
                InputStreamResource inputStreamResource = new InputStreamResource(in);
                headers.setContentLength((int)count);
                //in.close();
                //in = null;
                return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.PARTIAL_CONTENT);
                //IOUtils.copyBytes(in, out, count, false);
            }
            catch(Exception e)
            {
                throw e;
            }
        }
    }
}
