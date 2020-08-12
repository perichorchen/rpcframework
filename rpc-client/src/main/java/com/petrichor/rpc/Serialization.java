package com.petrichor.rpc;

import java.io.IOException;

/**
 * @author petrichor
 * @date 2020/8/12 10:45
 */
public interface Serialization {
    byte[] serialize(Object obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException;
}
