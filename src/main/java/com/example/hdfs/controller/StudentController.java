package com.example.hdfs.controller;

import com.example.hdfs.domain.JwtReponse;
import com.example.hdfs.domain.Student;
import com.example.hdfs.domain.Userinfo;
import com.example.hdfs.repository.StudentRepository;
import com.example.hdfs.util.JwtUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//import com.google.protobuf.ByteString;

@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 查询数据库中的所有
     * @return
     */
    @GetMapping(value = "/hello")
    public List<Student> getStuList(){
        return studentRepository.findAll();
    }

    @GetMapping(value = "/worldjwt")
    public ResponseEntity<Object> etcdJwt()  {
        JwtUtil jwt = new JwtUtil();
        HttpHeaders headers = new HttpHeaders();
        Map<String, List<String>> results=jwt.getEtcd();
        String ip = jwt.loadCall("validateToken",results);
        //{test_1=[172.17.201.199:9092], validateToken=[172.17.201.199:9092]}

        String strBody=jwt.callJwt(ip);
        JwtReponse a = new JwtReponse();
        Gson gson=new Gson();
        JwtReponse jwtResponse =  gson.fromJson(strBody,a.getClass());

        int code = jwtResponse.getCode();
        //返回都是：Admin,john,1585491513，之后split一下就好了
        //接下来就是设置middlewire之类的
        if(code == 0){
            String info = jwtResponse.getData().get(0);
            Userinfo userinfo = new Userinfo();
            userinfo = gson.fromJson(info,userinfo.getClass());
            return new ResponseEntity<Object>(userinfo.getUserinfo(), headers, HttpStatus.OK);
        }
        else{
            String msg = jwtResponse.getMsg();
            return new ResponseEntity<Object>(msg, headers, HttpStatus.OK);
        }
    }

    /**
     * 添加一个学生记录
     */
    @PostMapping(value = "/hello")
    public Student addStu(@RequestParam("name") String name, @RequestParam("age") Integer age){
        Student stu=new Student();
        stu.setName(name);
        stu.setAge(age);
        return studentRepository.save(stu);
    }

    @DeleteMapping(value = "/hello/{id}")
     public void deleteStu(@PathVariable("id") Integer id){
                 studentRepository.deleteById(id);
             }
}