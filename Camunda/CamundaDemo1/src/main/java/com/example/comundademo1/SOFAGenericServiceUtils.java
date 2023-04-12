package com.example.comundademo1;

import com.alipay.sofa.rpc.api.GenericService;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;

import java.util.concurrent.TimeUnit;

public class SOFAGenericServiceUtils {

    private static ConsumerConfig<GenericService> consumerConfig;

    public static ConsumerConfig getConsumerConfig(){
        if(consumerConfig == null) {
            synchronized (SOFAGenericServiceUtils.class) {
                if(consumerConfig == null) {
                    consumerConfig = initConsumerConfig();
                }
            }
        }
        return consumerConfig;
    }

    /**
     * 初始化对象ConsumerConfig
     * @return
     */
    private static ConsumerConfig initConsumerConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper")
                     .setAddress("127.0.0.1:2181");
        ConsumerConfig<GenericService>  consumerConfig = new ConsumerConfig<GenericService>()
                                                        .setGeneric(true)
                                                        .setTimeout(5000)
                                                        .setProtocol("bolt")
                                                        .setRegistry(registryConfig);
        return consumerConfig;
    }
}
