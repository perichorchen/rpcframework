package com.petrichor.rpc;

public class RpcFrameworkClient {
    public static void main(String[] args) {
            ProxyHandler proxyHandler = new ProxyHandler(new SocketRpcClient());
            //proxyHandler拿到这个对象，对每个方法调用invoke方法，增加日志打印。给它整个代理对象。
            TestService testService=(TestServiceImpl) proxyHandler.getProxy(new TestServiceImpl());
            testService.Test();
    }

}
