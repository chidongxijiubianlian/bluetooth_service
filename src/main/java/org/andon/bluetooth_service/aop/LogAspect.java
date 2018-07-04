package org.andon.bluetooth_service.aop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.andon.bluetooth_service.common.DateUtils;
import org.andon.bluetooth_service.controller.BluetoothController;
import org.andon.bluetooth_service.dto.DTOLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class LogAspect {
    /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    private final static Logger logger = LoggerFactory.getLogger(BluetoothController.class);

    @Pointcut("execution(* org.andon.bluetooth_service.controller.BluetoothController.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        /*System.out.println("目标方法名为:" + joinPoint.getSignature().getName());
        System.out.println("目标方法所属类的简单类名:" +        joinPoint.getSignature().getDeclaringType().getSimpleName());
        System.out.println("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));
        System.out.println("被代理的对象:" + joinPoint.getTarget());
        System.out.println("代理对象自己:" + joinPoint.getThis());*/
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println("请求参数为:" + JSONArray.toJSONString(args[i]));
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容  
        System.out.println("方法的返回值 : " + JSONArray.toJSONString(ret));
    }

    //后置异常通知  
    @AfterThrowing("webLog()")
    public void throwss(JoinPoint jp) {
        System.out.println("方法异常时执行.....");
    }

    //后置最终通知,final增强，不管是抛出异常或者正常退出都会执行  
    @After("webLog()")
    public void after(JoinPoint jp) {
        System.out.println("方法最后执行.....");
    }

    //环绕通知,环绕增强，相当于MethodInterceptor  
    @Around("webLog()")
    public Object arround(ProceedingJoinPoint pjp) {
        /*--------------log------------------*/
        DTOLogger logDto = new DTOLogger();
        logDto.setRequestTime(DateUtils.getDateTimeStr(new Date()));
        logDto.setAPIName(pjp.getSignature().getName());
        logDto.setRequestUrl("api/" + pjp.getSignature().getName());
        //获取传入目标方法的参数
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            logDto.setPostData(JSONArray.toJSONString(args[i]));
        }
        /*--------------log------------------*/
        //System.out.println("方法环绕start.....");
        try {
            Object o = pjp.proceed();
            //System.out.println("方法环绕proceed，结果是 :" + o);
             /*--------------log------------------*/
            logDto.setResponseData(JSONArray.toJSONString(o));
            logDto.setResponseTime(DateUtils.getDateTimeStr(new Date()));
            logger.info(JSONObject.toJSONString(logDto));
        /*--------------log------------------*/
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}  