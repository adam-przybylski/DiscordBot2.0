package com.bot;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static Config INSTANCE;
    private Map<String, Object> config;

    private Config() {
    }

    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }

        return INSTANCE;
    }

    public Map<String, Object> getConfig() {
        if(config == null){
            loadConfig();
        }
        return config;
    }

    public void loadConfig(){
        File jsonFile = new File("config.json");
        try {
            config = new ObjectMapper().readValue(jsonFile, HashMap.class);
        } catch (IOException e) {
            System.err.println("ERROR: Failed to read config");
        }
    }
}
