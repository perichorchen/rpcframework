package com.petrichor.rpc;

import java.util.List;
import java.util.Random;

/**
 * @author petrichor
 * @date 2020/8/4 16:38
 */
public class LoadBalance {
    public String selectServiceAddress(List<String> serviceAddresses) {
        if (serviceAddresses == null || serviceAddresses.size() == 0) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses);
    }

    public static String doSelect(List<String> serviceAddresses){
        Random random = new Random();
        String selectedAddress = serviceAddresses.get(random.nextInt(serviceAddresses.size()));
        return selectedAddress;
    }


}
