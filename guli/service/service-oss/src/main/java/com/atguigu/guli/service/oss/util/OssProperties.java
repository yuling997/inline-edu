package com.atguigu.guli.service.oss.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component  //自动初始化对象创建到容器中
@ConfigurationProperties(prefix = "aliyun.oss.file")  //注意prefix属性要写到变量定义到的最后一个层次之前
public class OssProperties {

    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;
}
