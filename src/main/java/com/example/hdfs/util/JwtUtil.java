package com.example.hdfs.util;

import com.example.hdfs.domain.JwtBody;
import com.google.protobuf.ByteString;
import com.ibm.etcd.api.RangeResponse;
import com.ibm.etcd.client.EtcdClient;
import com.ibm.etcd.client.KvStoreClient;
import com.ibm.etcd.client.kv.KvClient;
import net.sf.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.*;


/**
 * @author zly
 * https://blog.csdn.net/qzcsu/article/details/103940111
 */

//从etcd获取注册信息
public class JwtUtil {

//https://github.com/IBM/etcd-java/blob/master/src/test/java/com/ibm/etcd/client/KvTest.java
    public Map<String, List<String>> getEtcd(){
        Map<String, List<String>> keyValue = new HashMap<String, List<String>>();
        String[] jsonString = {"jsonString"};
        KvStoreClient client = EtcdClient.forEndpoint("172.17.201.199", 2379).withPlainText().build();
        KvClient kvClient = client.getKvClient();
        RangeResponse result = kvClient.get(ByteString.copyFromUtf8("jwt/")).asPrefix().sync();
        int keyNum = result.getKvsCount();
        for(int i=0; i<keyNum; i++)
        {
            //需要去掉前缀
            String ip = result.getKvs(i).getKey().toStringUtf8();
            String funcValue = result.getKvs(i).getValue().toStringUtf8();

            ip = ip.replace("jwt/","");
            //像这种格式是list，就把它转化成数组处理吧
            funcValue=funcValue.replace("\"","");
            funcValue=funcValue.substring(1,funcValue.length()-1);
            String[] funcName = funcValue.split(",");
            for (String temp : funcName) {
              if(!keyValue.containsKey(temp)){
                  List<String> list=new ArrayList<String>();
                  keyValue.put(temp,list);
              }
              List<String> listIP =keyValue.get(temp);
              listIP .add(ip);
              keyValue.put(temp,listIP);
          }
          //格式：keyValue['test_1']=list('ip1','ip2')
      }
      return keyValue;
    }

    //随机获取调用的ip，在客户端实现负载均衡
    public String loadCall(String funcName,Map<String, List<String>> keyValue) {
        List<String> ips = keyValue.get(funcName);
        int size = ips.size();
        Random rand = new Random();
        int index = rand.nextInt(size);
        String ip = ips.get(index);
        return ip;
    }

    public String callJwt(String ip){
        String url = "http://"+ip;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        JwtBody jwtBody = new JwtBody();
        jwtBody.setMethod("validateToken");
        List<String> args =new ArrayList<String>();
        args.add("eyJUeXAiOiJKV1QiLCJBbGciOiJIUzI1NiIsIkN0eSI6IiJ9.eyJSb2xlIjoiQWRtaW4iLCJVc2VybmFtZSI6ImpvaG4iLCJleHAiOjE1ODU0OTE1MTMsImlhdCI6MTU4NTQ5MTUxMn0.02tsHatS8O36OxIGtOwvy0_CRlGkv95gOVgOuZQeC5w");
        //Map<String,List<String>> argsMap= new HashMap<String,List<String>>();
        //argsMap.put("args",args);
        jwtBody.setArgs(args);

        String body = JSONObject.fromObject(jwtBody).toString();

        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        String strbody=restTemplate.exchange(url, HttpMethod.POST, entity,String.class).getBody();
        return strbody;
        //https://blog.csdn.net/qq_39727896/article/details/83508718
    }

    public static ByteString bs(String str) {
        return ByteString.copyFromUtf8(str);
    }
}

