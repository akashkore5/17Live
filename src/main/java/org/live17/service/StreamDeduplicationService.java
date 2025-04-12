package org.live17.service;

import org.live17.model.Section;

import java.util.List;
import java.util.Map;

public interface StreamDeduplicationService {
    Map<String, List<String>> deduplicateTop3(List<Section> sections);
}