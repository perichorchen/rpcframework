package com.petrichor.rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * Class对象的生成方式如下：
 *
 * 1.类名.class
 * 2.Class.forName(“类名字符串”)
 * 3.实例对象.getClass()
 * +newInstance
 *
 * @author petrichor
 * @date 2020/8/5 20:59
 * 获取单例对象的工厂类
 */
public class SingletonFactory {
    private static Map<String, Object> objectMap = new HashMap<>();
    private SingletonFactory(){};

    public static <T> T getInstance(Class<T> tClass){
        String key = tClass.toString();
        Object instance = objectMap.get(key);

        //synchronized放在静态方法上，是class锁，可以对类的所有实例起作用
        if(instance==null){
            synchronized (tClass){
                if(instance==null){
                    try {
                        instance = tClass.newInstance();
                        objectMap.put(key,instance);
                    } catch (InstantiationException|IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return tClass.cast(instance);
      }
}
