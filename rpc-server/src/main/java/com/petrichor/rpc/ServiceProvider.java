package com.petrichor.rpc;

/**
 * @author petrichor
 * @date 2020/8/5 22:11
 */
public interface ServiceProvider {

    /**
     * 保存服务实例对象和服务实例对象实现的接口类的对应关系
     *
     * @param service      服务实例对象
     * @param serviceClass 服务实例对象的类信息
     * @param <T>          服务接口的类型
     */
    public <T> void addServiceProvider(T service, Class<T> serviceClass);

    /**
     * 获取服务实例对象
     *
     * @param serviceName 服务实例对象实现的接口类的类名
     * @return 服务实例对象
     */
    Object getServiceProvider(String serviceName);
}
