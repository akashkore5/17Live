package org.live17.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.live17.exception.DeduplicationException;
import org.live17.model.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonFileReader {
    private static final Logger logger = LoggerFactory.getLogger(JsonFileReader.class);
    private final ObjectMapper objectMapper;

    public JsonFileReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Section> readSections(String filePath) {
        logger.info("Reading input file: {}", filePath);
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                logger.error("Input file does not exist: {}", filePath);
                throw new DeduplicationException("Input file does not exist: " + filePath);
            }
            Section[] sections = objectMapper.readValue(file, Section[].class);
            return Arrays.asList(sections);
        } catch (IOException e) {
            logger.error("Failed to read input file: {}", filePath, e);
            throw new DeduplicationException("Error reading input file: " + filePath, e);
        }
    }
}