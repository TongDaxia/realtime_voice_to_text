package com.tyg.speech.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class AppConfig {
    private final Properties props;

    public AppConfig() {
        props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                log.info("无法找到 application.properties 文件");
                initializeDefaults();
                return;
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件失败", e);
        }
    }

    private void initializeDefaults() {
        props.setProperty("server.port", "8080");
        props.setProperty("auth.token", "11223333");
        props.setProperty("python.rpc.host", "[::1]");
        props.setProperty("python.rpc.port", "50051");
        props.setProperty("log.path", "logs/speech_server.log");
    }


    public int getServerPort() {
        return Integer.parseInt(props.getProperty("server.port", "8080"));
    }

    public String getAuthToken() {
        return props.getProperty("auth.token", "11223333");
    }

    public String getPythonRpcHost() {
        return props.getProperty("python.rpc.host", "[::1]");
    }

    public int getPythonRpcPort() {
        return Integer.parseInt(props.getProperty("python.rpc.port", "50051"));
    }

    public String getLogPath() {
        return props.getProperty("log.path", "logs/speech_server.log");
    }


}