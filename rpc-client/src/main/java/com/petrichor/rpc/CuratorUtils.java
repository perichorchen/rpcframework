package com.petrichor.rpc;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author petrichor
 * @date 2020/8/4 16:57
 */

@Slf4j
public class CuratorUtils {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 5;
    private static final String CONNECT_STRING = "127.0.0.1:2181";
    public static final String ZK_REISTER_ROOT_PATH = "/my-rpc/";
    public static Map<String, List<String>> serviceAddressMap = new ConcurrentHashMap<>();
    private static Set<String> registeredPathSet = ConcurrentHashMap.newKeySet();  //线程安全的set，用concurrentHashMap实现
    private static CuratorFramework zkclient;

    static{
        zkclient = getZkClient();
    }

    private CuratorUtils(){};

    /**
     * 创建持久化节点，持久化节点不会因为客户端连接断开而被删除
     * @param path 节点路径
     */
    public static void createPersistentNode(String path){
        try {
            if(registeredPathSet.contains(path)||zkclient.checkExists().forPath(path)!=null){
                log.info("节点已经存在，节点为:[{}]", path);
            }else{
                //eg: /my-rpc/com.petrichor.HelloService/127.0.0.1:9999
                zkclient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("节点创建成功，节点为:[{}]", path);
            }
            registeredPathSet.add(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public static CuratorFramework getZkClient(){
        //重试策略。重试三次，并且会增加重试之间的睡眠时间
         RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME,MAX_RETRIES);
         CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                 .connectString(CONNECT_STRING)
                 .retryPolicy(retryPolicy)
                 .build();
         curatorFramework.start();
         return curatorFramework;
     }

    /**
     * 获取某个字节下的子节点,也就是获取所有提供服务的生产者的地址
     *
     * @param serviceName 服务对象接口名 eg:petrichor.rpc.HelloService
     * @return 指定子节下的所有子节点,serviceUrlList(ip:port)
     * serviceAddressMap(K:petrichor.rpc.HelloService , V:{{127.0.0.1:8080},{197.1.1.2:8021},...})
     */
    public static List<String> getChildNodes(String serviceName){
        if(serviceAddressMap.containsKey(serviceName)){
            return serviceAddressMap.get(serviceName);
        }
        List<String> result = null;
        // myrpc/petrichor.rpc.HelloService
        String servicePath = ZK_REISTER_ROOT_PATH + serviceName;
        try {
            //如果没有，zkclient找到放进去
            result = zkclient.getChildren().forPath(servicePath);
            serviceAddressMap.put(serviceName,result);
            registerWatcher(zkclient, serviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 清空注册中心数据
     */
    public static void clearRegistry(){
        registeredPathSet.stream().parallel().forEach(p->{
            try {
                zkclient.delete().forPath(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        log.info("服务端（Provider）所有注册的服务都被清空:[{}]", registeredPathSet.toString());
    }

    /**
     * 注册监听指定节点
     *
     * @param serviceName eg：petrichor.rpc.helloService
     */

    private static void registerWatcher(CuratorFramework zkclient, String serviceName){

    }



}
