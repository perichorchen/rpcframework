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
public class HessianSerializerUtil implements Serialization{

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

//    public  <T> byte[] serialize(T obj) {
//        byte[] bytes = null;
//        // 1、创建字节输出流
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//        // 2、对字节数组流进行再次封装
//
//        // step 1. 定义外部序列化工厂
//        //ExtSerializerFactory extSerializerFactory = new ExtSerializerFactory();
//        //extSerializerFactory.addSerializer(java.time.OffsetDateTime.class, new OffsetDateTimeRedisSerializer());
//        //extSerializerFactory.addDeserializer(java.time.OffsetDateTime.class, new OffsetDateTimeRedisDeserializer());
//        // step 2. 序列化工厂
//        //SerializerFactory serializerFactory = new SerializerFactory();
//        //serializerFactory.addFactory(extSerializerFactory);
//
//        HessianOutput hessianOutput = new HessianOutput(bos);
//        //hessianOutput.setSerializerFactory(serializerFactory);
//
//        try {
//            // 注意，obj 必须实现Serializable接口
//            hessianOutput.writeObject(obj);
//            bytes = bos.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return bytes;

//    public <T> T deserialize(byte[] data,Class<T> clz) {
//            if (data == null) {
//                return null;
//            }
//            // 1、将字节数组转换成字节输入流
//            ByteArrayInputStream bis = new ByteArrayInputStream(data);
//
//            // step 1. 定义外部序列化工厂
//            //ExtSerializerFactory extSerializerFactory = new ExtSerializerFactory();
//            //extSerializerFactory.addSerializer(java.time.OffsetDateTime.class, new OffsetDateTimeRedisSerializer());
//            //extSerializerFactory.addDeserializer(java.time.OffsetDateTime.class, new OffsetDateTimeRedisDeserializer());
//            // step 2. 序列化工厂
//            //SerializerFactory serializerFactory = new SerializerFactory();
//            //serializerFactory.addFactory(extSerializerFactory);
//            HessianInput hessianInput = new HessianInput(bis);
//            //hessianInput.setSerializerFactory(serializerFactory);
//            Object object = null;
//
//            try {
//                object = hessianInput.readObject();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return (T) object;
//    }
}
