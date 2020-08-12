package com.petrichor.rpc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author petrichor
 * @date 2020/8/12 11:52
 */
public class KryoSerialization implements Serialization {

    @Override
    public byte[] serialize(Object obj) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeObject(output, obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        input.close();
        return (T) kryo.readObject(input, clz);
    }

    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>(){
        protected Kryo initialValue() {
            //这里初始化的就是每一个线程私有的threadlocal变量值
            Kryo kryo = new Kryo();
            kryo.setReferences(true);
            kryo.setRegistrationRequired(false);
            return kryo;
        }
    };


}