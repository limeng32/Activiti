package org.activiti.explorer.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Joram Barrez,李萌
 * @date 2017年11月6日 上午11:15:04
 * @Email limeng32@chinaunicom.cn
 * @version
 * @since JDK 1.8
 */
@Configuration
public class JacksonConfiguration {

    @Bean()
    public ObjectMapper objectMapper() {
        // To avoid instantiating and configuring the mapper everywhere
        return new ObjectMapper();
    }

}
