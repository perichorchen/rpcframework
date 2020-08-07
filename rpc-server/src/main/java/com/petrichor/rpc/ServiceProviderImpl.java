package com.petrichor.rpc;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现了ServiceProvider接口，可以将其看成是一个保存和提供服务实例对象的示例
 *
 * @author petrichor
 * @date 2020/8/5 22:31
 */
public class ServiceProviderImpl implements ServiceProvider {

    /**
     * 接口名和服务的对应关系
     * note:处理一个接口被两个实现类实现的情况如何处理？（通过 group 分组）
     * key:service/interface name
     * value:service
     */
     private Map<String,Object> serviceMap = new ConcurrentHashMap<>();
     private Set<String> registeredService = ConcurrentHashMap.newKeySet();


    /**
     * 服务注册，服务名和服务类的对应关系
     * note:可以修改为扫描注解注册
     *
     * @param service      服务实体
     * @param serviceClass 服务实例对象类（类在内存中的信息）
     * @param <T>
     */
    @Override
    public <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if(registeredService.contains(serviceName)){
            return;
        }

        registeredService.add(serviceName);
        serviceMap.put(serviceName,service);
    }

    //服务端通过通过请求体中的服务名字，获取那个类，然后去调用
    @Override
    public Object getServiceProvider(String serviceName) {
        if(serviceName.contains(serviceName)){
            return serviceMap.get(serviceName);
        }

        return null;
    }
}
