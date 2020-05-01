package com.example.hdfs.interceptor;


//https://blog.csdn.net/weixin_42863267/article/details/102976901
//https://blog.csdn.net/weixin_43820012/article/details/90608536

//import com.example.hdfs.util.JwtUtil;

import com.example.hdfs.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import javax.xml.transform.Result;


@Component
public class TokenInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    //private Logger logger = (Logger) LoggerFactory.getLogger(TokenInterceptor.class);
    //可能这个地方包导入有问题
    /**
     * 忽略拦截的url
     */
    private String urls[] = {
    };

    private JwtUtil jwtUtil;


    /**
     * 进入controller层之前拦截请求
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        // System.out.println("处理请求完成后视图渲染之前的处理操作");
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String url = httpServletRequest.getRequestURI();
        String token = httpServletRequest.getHeader("token");
        System.out.println(token);
        //这个之后在某个API中试一下，看返回的是什么
        //获得其中的
        String method = httpServletRequest.getMethod();

//        if (!method.equals("OPTIONS")){
//            logger.info(token);
//            logger.info(url);
//            logger.info(method);
//            // 遍历需要忽略拦截的路径
//            for (String item : this.urls){
//                if (item.equals(url)){
//                    return true;
//                }
//            }
//            // 查询验证token
//            JwtUtil jwt = new JwtUtil();
//            Userinfo userinfo = new Userinfo();
//            userinfo = jwt.JwtValidate(token);
//            if (userinfo == null){
//                httpServletResponse.setCharacterEncoding("UTF-8");
//                httpServletResponse.setContentType("application/json; charset=utf-8");
//                PrintWriter out = null ;
//                ResultMsg resultMsg = new ResultMsg();
//                try{
//                    //Result res = new Result(10001,"登录失效重新登录");
//                    //String json = JSON.toJSONString(res);
//                    resultMsg.setLogin(false);
//                    resultMsg.setMsg("请登录");
//                    String body = JSONObject.fromObject(resultMsg).toString();
//
//                    //JSONObject body = JSONObject.fromObject(resultMsg.toString());
//                    httpServletResponse.setContentType("application/json");
//                    out = httpServletResponse.getWriter();
//                    // 返回json信息给前端
//                    out.append(body);
//                    out.flush();
//                    return false;
//                } catch (Exception e){
//                    e.printStackTrace();
//                    httpServletResponse.sendError(500);
//                    return false;
//                }
//            }
//            else {
//                return true;
//                //时间戳，验证时间戳
//            }
//            //return true;
//        }
        return false;
    }



    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        // System.out.println("视图渲染之后的操作");
    }

}
