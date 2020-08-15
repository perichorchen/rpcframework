package com.petrichor.rpc;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author petrichor
 * @date 2020/8/13 19:04
 */

@Slf4j
public abstract class AbstractRpcServer implements RpcServer {
    public void scanServices() {
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass = null;

        try {
            startClass = Class.forName(mainClassName);
            if(startClass.isAnnotationPresent(ServiceScan.class)){
                log.error("启动类缺少 @ServiceScan 注解");
            }

        } catch (ClassNotFoundException e) {
            log.error("没有发现入口类");
            e.printStackTrace();
        }

        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }

        //拿到这个包下的所欲类
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);

        for (Class clazz:classSet)
        {
            //有service注解的就实例化
            if(clazz.isAnnotationPresent(Service.class)){
                Object object;
                try {
                    object = clazz.newInstance();
                } catch (InstantiationException|IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
