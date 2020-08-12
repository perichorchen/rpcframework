package com.petrichor.rpc;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author petrichor
 * @date 2020/8/11 22:46
 */
public class HessianSerialization implements Serialization{

    @Override
    public byte[] serialize(Object data) throws IOException {
        // 1、创建字节输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // 2、对字节数组流进行再次封装
        Hessian2Output out = new Hessian2Output(bos);
        out.writeObject(data);
        out.flush();
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
        return (T) input.readObject(clz);
    }


}
