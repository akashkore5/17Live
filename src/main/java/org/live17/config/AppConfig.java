package org.live17.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.live17.io.JsonFileReader;
import org.live17.io.JsonFileWriter;
import org.live17.service.StreamDeduplicationService;
import org.live17.service.StreamDeduplicationServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private final Properties properties;
    private final StreamDeduplicationService deduplicationService;
    private final JsonFileReader jsonFileReader;
    private final JsonFileWriter jsonFileWriter;

    public AppConfig() {
        this.properties = loadProperties();
        ObjectMapper objectMapper = new ObjectMapper();
        this.deduplicationService = new StreamDeduplicationServiceImpl();
        this.jsonFileReader = new JsonFileReader(objectMapper);
        this.jsonFileWriter = new JsonFileWriter(objectMapper);
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("application.properties not found");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
        return props;
    }

    public String getInputFilePath() {
        return properties.getProperty("input.file.path");
    }

    public String getOutputFilePath() {
        return properties.getProperty("output.file.path");
    }

    public StreamDeduplicationService getDeduplicationService() {
        return deduplicationService;
    }

    public JsonFileReader getJsonFileReader() {
        return jsonFileReader;
    }

    public JsonFileWriter getJsonFileWriter() {
        return jsonFileWriter;
    }
}