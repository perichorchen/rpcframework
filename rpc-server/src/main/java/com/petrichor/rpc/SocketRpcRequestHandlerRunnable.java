package com.petrichor.rpc;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author petrichor
 * @date 2020/8/5 16:18
 */
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable{
    private Socket socket;
    private RpcResquestHandler rpcRequestHandler;   //用单例避免频繁创建与销毁

    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcResquestHandler.class);
    }

    //服务端起处理线程
    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            Request rpcRequest = (Request) objectInputStream.readObject();
            Object result = rpcRequestHandler.handle(rpcRequest);      //调用Handler处理
            objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception:", e);
        }

    }
}
