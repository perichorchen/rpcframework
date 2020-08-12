package com.petrichor.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author petrichor
 * @date 2020/8/3 21:16
 */
public class SocketRpcClient implements ClientType {
    private final ServiceDiscovery serviceDiscovery=null;

    // java.io.ObjectOutputStream代表对象输出流，它的writeObject(Object obj)方法可对参数指定的obj对象进行序列化，
    // 把得到的字节序列写到一个目标输出流中。
    //java.io.ObjectInputStream代表对象输入流，它的readObject()方法从一个源输入流中读取字节序列，再把它们反序列化为一个对象
    //，并将其返回。
    @Override
    public Object sendRequest(Request request) {
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.discoveryService(request.getInterfaceName());
            Socket socket = new Socket(inetSocketAddress.getHostName(),inetSocketAddress.getPort());
            //发送
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(request);

            //返回response
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (IOException |ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return null;
    }
}
