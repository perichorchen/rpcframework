package com.petrichor.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author petrichor
 * @date 2020/8/3 21:16
 */
public class SocketRpcClient implements ClientType {
    @Override
    public Object sendRequest(Request request) {
        try {
            Socket socket = new Socket("127.0.0.1",8888);
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
