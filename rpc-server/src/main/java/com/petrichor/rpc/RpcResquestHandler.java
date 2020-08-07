package com.petrichor.rpc;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author petrichor
 * @date 2020/8/5 21:50
 */

@Slf4j
public class RpcResquestHandler {
    private static ServiceProvider serviceProvider = new ServiceProviderImpl();
    Object handle(Request rpcRequest){
        String serviceName = rpcRequest.getInterfaceName();
        //获取到请求的service实体
        Object service = serviceProvider.getServiceProvider(serviceName);
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * handle 调用这个方法对客户端请求的某个方法，执行后返回
     * @param rpcRequest 请求体
     * @param service   具体哪个服务
     * @return 方法执行结果
     */
    private Object invokeTargetMethod(Request rpcRequest, Object service) {
        Object result = null;

        try {
            //从service根据方法名和参数列表获取到这个方法
            Method method = service.getClass().getMethod(rpcRequest.getMenthodName(),rpcRequest.getParamTypes());
            //方法被代理后，进行执行，返回结果
            result = method.invoke(service,rpcRequest.getParameters());
        } catch (NoSuchMethodException |IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

}
