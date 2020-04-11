package com.example.hdfs.controller;
import com.example.hdfs.domain.HdfsFile;
import com.example.hdfs.repository.HdfsFileRepository;
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

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * @author zly
 */

@RestController
public class HdfsFileController {
    @Autowired
    private HdfsFileRepository hdfsFileRepository;

    /**
     * 查询数据库中的所有
     * @return
     */
    @GetMapping(value = "/filelist")
    public List<HdfsFile> getHdfsFileList(){
        return hdfsFileRepository.findAll();
    }

    /**
     上传文件，不过这个并没有真正的上传文件
     */
    @PostMapping(value = "/uploadfile")
    public HdfsFile addHdfsFile(@RequestParam("filename") String filename, @RequestParam("type") String type,@RequestParam("subDescription") String subDescription,@RequestParam("title") String title,@RequestParam("cover") String cover){
        //HdfsConfig config = new HdfsConfig("172.17.201.196", "9000","hadoop");
        //String source = filename; //这个需要绝对路径
        //之后的filename值需要最后的文件名，要改！！！
        HdfsFile file=new HdfsFile();
        String type1 = "video";
        String type2 = "text";
        if(type1.equals(type)) {
           file.setHref("/user/data/video/"+filename);
        }
        else if(type2.equals(type)){
            file.setHref("/user/data/text/"+filename);
        }
        else {
            file.setHref("/user/data/files/"+filename);
        }
        //上传文件到hadoop
        //String destination = file.getFilename()+filename;
        //HdfsUtil.upload(config,source,destination);
        file.setType(type);
        file.setTitle(title);
        file.setCover(cover);
        file.setSubDescription(subDescription);
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        Date date1 = null;
        try {
            date1 = dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date1.getTime();
        file.setUpdatedAt(ts);
        file.setCreatedAt(ts);
        return hdfsFileRepository.save(file);
    }

    //这个地方如果不清楚id,那得通过其他的param进行查询后获取id，这个是前端搭建需要注意的
        @DeleteMapping(value = "/deletefile")
        public void deleteFile(@RequestParam(value = "id") Integer id){
                hdfsFileRepository.deleteById(id);
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
}
