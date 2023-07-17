package com.example.bootIML.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ConfigurationProperties("service")
@Component
public class StorageProperties {

    private String location = System.getProperty("java.io.tmpdir");
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
