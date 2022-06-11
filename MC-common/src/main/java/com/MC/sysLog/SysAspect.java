package com.MC.sysLog;


import com.MC.dto.RegisterDto;
import com.MC.entity.Msg;
import com.MC.entity.SysLogger;
import com.MC.entity.User;
import com.MC.mapper.LogMapper;
import com.MC.utils.IpUtil;
import com.MC.utils.JWTUtil;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 日志
 * 在controller中，添加了 @LogWeb的注解，都会走这里
 */
@Aspect 
@Component 
public class SysAspect 
{ 
//    private static final Logger logger = LoggerFactory.getLogger(SysAspect.class);
    @Autowired
    LogMapper logMapper;

    ThreadLocal<Long> startTime = new ThreadLocal<>(); 

    private SysLogger sysLogger;

    @Pointcut("@annotation(com.MC.sysLog.Logweb)")
    public void LogPointcut(){}

    @Before("LogPointcut()") 
    public void doBefore(JoinPoint joinPoint) throws Exception
    {

        sysLogger=new SysLogger();
        startTime.set(System.currentTimeMillis()); 

        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attribute.getRequest();
        //Log注解
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();
        String descr = method.getAnnotation(Logweb.class).value();
        sysLogger.setDescr(descr);
        sysLogger.setStart_time(new Date());
        sysLogger.setIp(IpUtil.getIpAddr(request));

        sysLogger.setUrl(request.getRequestURL().toString());
        sysLogger.setClass_method(signature.getDeclaringTypeName() + "." + signature.getName());
        sysLogger.setRequest_type(request.getMethod());

        String methodName=signature.getName();
//        System.out.println(methodName);
        if(methodName.equals("register"))
        {
            Object[] args = joinPoint.getArgs();
            RegisterDto dto= (RegisterDto) args[0];
            sysLogger.setParams("email:"+dto.getEmail()+",userName:"+dto.getUsername());
        }
        else if(methodName.equals("login")||methodName.equals("updatePassword")){
            sysLogger.setParams("");
        }
        else if(joinPoint.getArgs()!=null){
            Object[] args = joinPoint.getArgs();
            Object[] arguments = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = args[i];
            }
            if (arguments != null){
                sysLogger.setParams(JSON.toJSONString(arguments));
            }

        }

        else
            sysLogger.setParams("");

        sysLogger.setLog_type(1);


    }
    @AfterThrowing(throwing="ex" ,pointcut = "LogPointcut()")
    void ExceptionPrintLog(Throwable ex){
        String token=(String) SecurityUtils.getSubject().getPrincipal();
        if(token!=null&&!token.equals(""))
        {
            Integer userId = JWTUtil.getUserId(token);
            if(userId!=null)
                sysLogger.setUid(userId);
        }
        sysLogger.setLog_type(2);
        sysLogger.setExce_time((int) (System.currentTimeMillis()-startTime.get()));
        sysLogger.setResponse(JSON.toJSONString(ex));
        logMapper.insert(sysLogger);
    }

    @AfterReturning(returning = "ret", pointcut = "LogPointcut()") 
    public void doAfterReturning(Object ret)
    {
        String token=(String) SecurityUtils.getSubject().getPrincipal();
        if(token!=null&&!token.equals(""))
        {
            Integer userId = JWTUtil.getUserId(token);
            if(userId!=null)
                sysLogger.setUid(userId);
        }

        if(ret instanceof Msg){
            Map<String, Object> data = ((Msg) ret).getData();
            if(data.containsKey("token")){
                data.remove(token);
                User user= (User) data.get("user");
                sysLogger.setUid(user.getId());
            }
            ((Msg) ret).setData(data);
        }
        sysLogger.setResponse(JSON.toJSONString(ret)+"");
        sysLogger.setExce_time((int) (System.currentTimeMillis()-startTime.get()));
        logMapper.insert(sysLogger);
    } 
} 
