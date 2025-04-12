package org.live17.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.live17.exception.DeduplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonFileWriter {
    private static final Logger logger = LoggerFactory.getLogger(JsonFileWriter.class);
    private final ObjectMapper objectMapper;

    public JsonFileWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void writeOutput(String filePath, Map<String, List<String>> output) {
        logger.info("Writing output to file: {}", filePath);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), output);
            logger.info("Successfully wrote output to {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to write output file: {}", filePath, e);
            throw new DeduplicationException("Error writing output file: " + filePath, e);
        }
    }
}