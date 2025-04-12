package org.live17;

import org.live17.config.AppConfig;
import org.live17.exception.DeduplicationException;
import org.live17.model.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            AppConfig config = new AppConfig();

            // Read input
            List<Section> sections = config.getJsonFileReader().readSections(config.getInputFilePath());

            // Process deduplication
            Map<String, List<String>> output = config.getDeduplicationService().deduplicateTop3(sections);

            // Write output
            config.getJsonFileWriter().writeOutput(config.getOutputFilePath(), output);

            logger.info("Stream deduplication completed successfully");

        } catch (DeduplicationException e) {
            logger.error("Deduplication failed: {}", e.getMessage(), e);
            System.exit(1);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}