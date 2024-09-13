//package com.example.xxjob.config;
//
//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.profile.DefaultProfile;
//import com.aliyuncs.profile.IClientProfile;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/12/8
 */
//@Configuration
//@Slf4j
//public class IAcsClientConfig {
//
//
//    @Autowired
//    private EmailConfig emailConfig;
//
//    @Bean
//    public IAcsClient schedulerFactoryBean(){
//        IClientProfile profile = DefaultProfile.getProfile(emailConfig.getRegionId(), emailConfig.getAccessKeyId(), emailConfig.getSecret());
//        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
//        //try {
//        //DefaultProfile.addEndpoint("dm.ap-southeast-1.aliyuncs.com", "ap-southeast-1", "Dm",  "dm.ap-southeast-1.aliyuncs.com");
//        //} catch (ClientException e) {
//        //e.printStackTrace();
//        //}
//        //建议初始化一次就可以，初始化多次也不影响功能，但是性能有损失
//        return new DefaultAcsClient(profile);
//    }
//}
