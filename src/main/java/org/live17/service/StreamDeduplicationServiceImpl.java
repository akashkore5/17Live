package org.live17.service;

import org.live17.exception.DeduplicationException;
import org.live17.model.Section;
import org.live17.model.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class StreamDeduplicationServiceImpl implements StreamDeduplicationService {
    private static final Logger logger = LoggerFactory.getLogger(StreamDeduplicationServiceImpl.class);
    private static final int TOP_N = 3;

    @Override
    public Map<String, List<String>> deduplicateTop3(List<Section> sections) {
        if (sections == null || sections.isEmpty()) {
            logger.warn("Received null or empty sections list");
            throw new DeduplicationException("Sections list cannot be null or empty");
        }
        logger.info("Starting deduplication for {} sections", sections.size());
        List<Section> mutableSections = createMutableSections(sections);
        deduplicate(mutableSections);
        return buildOutputMap(mutableSections);
    }

    private List<Section> createMutableSections(List<Section> sections) {
        List<Section> mutableSections = new ArrayList<>();
        for (Section section : sections) {
            List<Stream> mutableStreams = new ArrayList<>(section.getSectionData());
            Section mutableSection = new Section(section.getSectionID(), mutableStreams);
            mutableSection.setLokalisedKey(section.getLokalisedKey());
            mutableSection.setMlDynamicLabel(section.isMlDynamicLabel());
            mutableSection.setLabelID(section.getLabelID());
            mutableSections.add(mutableSection);
        }
        return mutableSections;
    }

    private void deduplicate(List<Section> sections) {
        Set<String> globalUsedTop3Ids = new HashSet<>();

        for (Section section : sections) {
            List<Stream> originalStreams = section.getSectionData();
            if (originalStreams == null || originalStreams.size() <= 1) continue;

            List<Stream> newTop3 = new ArrayList<>();
            Set<String> localTop3Ids = new HashSet<>();
            List<Stream> fallbackPool = new ArrayList<>();

            // Step 1: Build newTop3 and fallback pool
            for (int i = 0; i < originalStreams.size(); i++) {
                Stream stream = originalStreams.get(i);
                String id = stream.getStreamerID();

                if (i < TOP_N) {
                    if (!globalUsedTop3Ids.contains(id) && !localTop3Ids.contains(id)) {
                        newTop3.add(stream);
                        globalUsedTop3Ids.add(id);
                        localTop3Ids.add(id);
                    } else {
                        fallbackPool.add(stream);
                    }
                } else {
                    fallbackPool.add(stream); // preserve order for non-top3
                }
            }

            // Step 2: Fill missing top 3 slots from fallback pool
            Iterator<Stream> fallbackIt = fallbackPool.iterator();
            while (newTop3.size() < TOP_N && fallbackIt.hasNext()) {
                Stream candidate = fallbackIt.next();
                String id = candidate.getStreamerID();
                if (!globalUsedTop3Ids.contains(id) && !localTop3Ids.contains(id)) {
                    newTop3.add(candidate);
                    globalUsedTop3Ids.add(id);
                    localTop3Ids.add(id);
                    fallbackIt.remove();
                }
            }

            // Step 3: Rebuild section list
            List<Stream> updatedSectionData = new ArrayList<>();
            updatedSectionData.addAll(newTop3);
            for (int i = TOP_N; i < originalStreams.size(); i++) {
                updatedSectionData.add(originalStreams.get(i)); // keep original order after top 3
            }

            section.setSectionData(updatedSectionData);
        }
    }

    private Map<String, List<String>> buildOutputMap(List<Section> sections) {
        Map<String, List<String>> output = new LinkedHashMap<>();
        for (Section section : sections) {
            List<String> streamerIDs = new ArrayList<>();
            for (Stream stream : section.getSectionData()) {
                streamerIDs.add(stream.getStreamerID());
            }
            output.put(section.getSectionID(), streamerIDs);
        }
        logger.info("Deduplication complete. Output map created with {} sections", output.size());
        return output;
    }
}