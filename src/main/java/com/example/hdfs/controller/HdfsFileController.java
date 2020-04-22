package com.example.hdfs.controller;

import com.example.hdfs.config.HdfsConfig;
import com.example.hdfs.domain.HdfsFile;
import com.example.hdfs.repository.HdfsFileRepository;
import com.example.hdfs.util.HdfsUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zly
 */

@RestController
//@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class HdfsFileController {
    @Autowired
    private HdfsFileRepository hdfsFileRepository;

    /**
     * 查询数据库中的所有
     * @return
     */
    @PostMapping(value = "/filelist")
    public List<HdfsFile> getHdfsFileList(HttpServletRequest request){
        System.out.println(request.getParameter("count"));
        List<HdfsFile> hdfsFiles = hdfsFileRepository.findAll();
        for(int i=0; i<hdfsFiles.size(); i++){
            hdfsFiles.get(i).setKey( String.valueOf(i+1));
        }
        return hdfsFiles;
    }

    @GetMapping(value = "/videofilelist")
    public List<HdfsFile> getHdfsVideoFileList(HttpServletRequest request){
        System.out.println(request.getParameter("count"));
        List<HdfsFile> hdfsFiles = hdfsFileRepository.findByFiletype(request.getParameter("filetype"));
        for(int i=0; i<hdfsFiles.size(); i++){
            hdfsFiles.get(i).setKey( String.valueOf(i+1));
        }
        return hdfsFiles;
    }

    @GetMapping(value = "/dockerfilelist")
    public List<HdfsFile> getHdfsDockerFileList(HttpServletRequest request){
        System.out.println(request.getParameter("count"));
        List<HdfsFile> hdfsFiles = hdfsFileRepository.findByType("docker");
        for(int i=0; i<hdfsFiles.size(); i++){
            hdfsFiles.get(i).setKey( String.valueOf(i+1));
        }
        return hdfsFiles;
    }

    @GetMapping(value = "/textfilelist")
    public List<HdfsFile> getHdfsTextFileList(HttpServletRequest request){
        System.out.println(request.getParameter("count"));
        List<HdfsFile> hdfsFiles = hdfsFileRepository.findByFiletype(request.getParameter("filetype"));
        for(int i=0; i<hdfsFiles.size(); i++){
            hdfsFiles.get(i).setKey( String.valueOf(i+1));
        }
        return hdfsFiles;
    }

    @PostMapping(value = "/uploadtohdfs")
    //上传视频到hdfs的接口，不修改文件名称，以便存入数据库的接口找到文件存储
    //在hdfs中的目录
    //@CrossOrigin(origins = "*",allowCredentials="true",allowedHeaders = "",methods = {})
    public boolean uploadHdfsFile(@RequestParam(value = "files") MultipartFile files) {
        Map<String,Object> map = new HashMap<>();
        String dateName=null;
        String destination = null;
        String fileName = null;
        if (files.isEmpty()) {
            System.out.println("文件传输失败\n");
            return false;
        }
        else {
            fileName = files.getOriginalFilename();
//            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//            Calendar calendar = Calendar.getInstance();
//            //存储文件的名字
//            System.out.println("输出上传文件的名称");
//            System.out.println(fileName);
//            dateName = df.format(calendar.getTime())+files.getOriginalFilename();
        }
        String[] funcName = fileName.split("\\.");
        String fileType = "mp4";
        if(funcName[1].equals(fileType)){
            destination = "/user/data/video/"+fileName;
        }
        else{
            destination = "/user/data/text/"+fileName;
        }
        System.out.println(destination);
        System.out.println("没有收到前端转发请求");
        InputStream in = null;
        try {
            in = files.getInputStream();
        } catch (IOException e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        HdfsConfig config = new HdfsConfig("172.17.201.196", "9000","hadoop");
        HdfsUtil.upload(config,in, destination);
        return true;
    }

    /**
     上传文件，不过这个并没有真正的上传文件
     */
    //@PostMapping(value = "/uploadfile")
    @RequestMapping(value="/uploadfile", method=RequestMethod.POST)
    //{title: "test1", filename: "test.mp4", subDescription: "abc", cover: "https://gw.alipayobjects.com/zos/rmsportal/uMfMFlvUuceEyPpotzlq.png"}
    //这个地方需要改一下，前端传来的参数必须是带后缀的，但是后端要处理一下，把后缀去掉
    public HdfsFile addHdfsFile(@RequestBody Map<String,String> maplist){
        String title = maplist.get("title");
        String filename = maplist.get("filename");
        String subDescription = maplist.get("subDescription");
        String cover = maplist.get("cover");
        String type =maplist.get("type");
        String data = maplist.get("data");
        HdfsFile file=new HdfsFile();
        String[] fileNames = filename.split("\\.");
        String type1 = "mp4";
        if(type1.equals(fileNames[1])) {
           file.setHref("/user/data/video/"+filename);
           file.setFiletype("video");
        }
        else {
            file.setHref("/user/data/text/"+filename);
            file.setFiletype("text");
        }
        //上传文件到hadoop
        //String destination = file.getFilename()+filename;
        //HdfsUtil.upload(config,source,destination);
        System.out.println("输出type");
        System.out.println(type);
        file.setType(type);
        file.setTitle(title);
        file.setCover(cover);
        file.setFilename(filename);
        file.setSubDescription(subDescription);
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        Date date1 = null;
        file.setCreatedAt(dateFormat.format(date));
        try {
            date1 = dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date1.getTime();
        file.setUpdatedAt(ts);

        //存储进hdfs
        if(data!=null){
            System.out.println("保存富文本文件");
            InputStream inputStrem = new ByteArrayInputStream(data.getBytes());
            String destination = file.getHref();
            HdfsConfig config = new HdfsConfig("172.17.201.196", "9000","hadoop");
            HdfsUtil.upload(config,inputStrem, destination);
        }
        return hdfsFileRepository.save(file);
    }

    //这个地方如果不清楚id,那得通过其他的param进行查询后获取id，这个是前端搭建需要注意的
    @GetMapping(value = "/deletefile")
    //在数据库中设置文件名不重复
    //alter table hdfs_file add constraint unique_filename unique(filename);
    @Transactional
    public ResponseEntity<Object> deleteFile(@RequestParam(value = "title") String title){
                //hdfsFileRepository.deleteById(id);
        //删除hdfs中文件的代码，还没有测试
        List<HdfsFile> hdfsFiles = hdfsFileRepository.findByTitle(title);
        String filePath = hdfsFiles.get(0).getHref();
        HdfsConfig config = new HdfsConfig("172.17.201.196", "9000","hadoop");
        HdfsUtil.delete(config,filePath);
        hdfsFileRepository.deleteByTitle(title);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<Object>("ok", headers, HttpStatus.OK);
    }

    @GetMapping(value = "/textreader")
    public String textReader(HttpServletRequest request)  {
        String href = request.getParameter("href");
        HdfsConfig config = new HdfsConfig("172.17.201.196", "9000","hadoop");
        String data=HdfsUtil.downloadString(config,href);
        return data;
    }

    @GetMapping(value = "/play")
    public ResponseEntity<Object> playVideo(@RequestHeader(value = "Range",required = false) String range, @RequestParam(value = "fpath",required = false) String fpath) throws IOException {
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

    @GetMapping(value = "/downloadfile")
    public ResponseEntity<Object> downloadFiles(@RequestHeader(value = "Range",required = false) String range, @RequestParam(value = "title",required = false) String title) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        //String fpath = null;
        List<HdfsFile> hdfsFiles = hdfsFileRepository.findByTitle(title);
        String fpath = hdfsFiles.get(0).getHref();
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

            return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Object>("download fail", headers, HttpStatus.OK);
        }

    }

}
