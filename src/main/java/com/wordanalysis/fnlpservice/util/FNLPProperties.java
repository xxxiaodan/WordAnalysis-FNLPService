package com.wordanalysis.fnlpservice.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
@Configuration
@PropertySource("classpath:fnlp.properties")
@Data
@Component
@ConfigurationProperties(ignoreUnknownFields = true)
public class FNLPProperties {
    private String root;
    private String SegPath;
    private String PosPath;
    private String TempFilePath;

    public String getRoot() {
        return root;
    }

    public String getSegPath() {
        return root+SegPath;
    }

    public String getPosPath() {
        return root+PosPath;
    }

    public String getTempFilePath() { return root+TempFilePath; }
}
