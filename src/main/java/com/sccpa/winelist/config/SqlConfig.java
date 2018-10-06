package com.sccpa.winelist.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sql")
@Getter
@Setter
public class SqlConfig {
    private String fetchEntireList;
    private String insertEntry;
}
