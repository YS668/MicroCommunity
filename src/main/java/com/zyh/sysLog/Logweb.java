package com.zyh.sysLog;

import java.lang.annotation.*;

/**
 * 自定义注解，进行AOP操作
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logweb 
{ 
    String value() default ""; 
} 