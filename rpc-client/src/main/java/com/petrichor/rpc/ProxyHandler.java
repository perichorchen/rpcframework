package com.petrichor.rpc;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@Slf4j
public class ProxyHandler implements InvocationHandler {
//    private Object targetObject ; //传入的代理对象
    private ClientType clientType;
    public ProxyHandler(ClientType clientType){
        this.clientType=clientType;
    }

    //返回一个可以代理原对象的代理对象
    public <T> T getProxy(Class<T> clazz){
        ClassLoader classLoader=clazz.getClassLoader();
        //this 当前对象，该对象实现了InvocationHandler接口所以有invoke方法，通过invoke方法可以调用被代理对象的方法
        //调用Proxy的newProxyInstance方法可以生成代理对象
        return (T)Proxy.newProxyInstance(classLoader,new Class<?>[]{clazz},this);
    }

    @Override
    //你给我的对象希望怎么代理
    //该方法在代理对象调用方法时调用,希望怎么改造某个方法
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = Request.builder()
                .menthodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .build();

        RpcResponse rpcResponse = null;
        if(clientType instanceof SocketRpcClient){
            rpcResponse = (RpcResponse) clientType.sendRequest(request);
        }

        return rpcResponse;
    }


}