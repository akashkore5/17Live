package org.live17.service;

import org.junit.Before;
import org.junit.Test;
import org.live17.exception.DeduplicationException;
import org.live17.model.Section;
import org.live17.model.Stream;

import java.util.*;

import static org.junit.Assert.*;

public class StreamDeduplicationServiceTest {
    private StreamDeduplicationService service;

    @Before
    public void setUp() {
        service = new StreamDeduplicationServiceImpl();
    }

    @Test
    public void testDeduplicateTop3_NoDuplicates() {
        List<Section> sections = Arrays.asList(
                new Section("s1", Arrays.asList(
                        new Stream("id1"),
                        new Stream("id2"),
                        new Stream("id3")
                )),
                new Section("s2", Arrays.asList(
                        new Stream("id4"),
                        new Stream("id5"),
                        new Stream("id6")
                ))
        );

        Map<String, List<String>> result = service.deduplicateTop3(sections);

        assertEquals(Arrays.asList("id1", "id2", "id3"), result.get("s1"));
        assertEquals(Arrays.asList("id4", "id5", "id6"), result.get("s2"));
    }

    @Test
    public void testDeduplicateTop3_WithDuplicates() {
        List<Section> sections = Arrays.asList(
                new Section("s1", Arrays.asList(
                        new Stream("id1"),
                        new Stream("id2"),
                        new Stream("id2"), // Duplicate
                        new Stream("id3"),
                        new Stream("id4")
                ))
        );
        Map<String, List<String>> result = service.deduplicateTop3(sections);
        List<String> s1Top3 = result.get("s1").subList(0, 3);
        assertEquals(Arrays.asList("id1", "id2", "id3"), s1Top3); // Duplicate replaced
    }

    @Test
    public void testDeduplicateTop3_CrossSectionDuplicates() {
        List<Section> sections = Arrays.asList(
                new Section("s1", Arrays.asList(
                        new Stream("id1"),
                        new Stream("id2"),
                        new Stream("id3")
                )),
                new Section("s2", Arrays.asList(
                        new Stream("id1"), // Already used in s1
                        new Stream("id4"),
                        new Stream("id5"),
                        new Stream("id6")
                ))
        );

        Map<String, List<String>> result = service.deduplicateTop3(sections);
        List<String> s1Top3 = result.get("s1").subList(0, 3);
        List<String> s2Top3 = result.get("s2").subList(0, 3);

        assertTrue(s1Top3.contains("id1"));
        assertFalse(s2Top3.contains("id1")); // id1 must not be in s2 top 3
    }

    @Test
    public void testDeduplicateTop3_InsufficientUniqueIds() {
        List<Section> sections = Arrays.asList(
                new Section("s1", Arrays.asList(
                        new Stream("id1"),
                        new Stream("id1"),
                        new Stream("id1")
                )),
                new Section("s2", Arrays.asList(
                        new Stream("id1"),
                        new Stream("id1"),
                        new Stream("id1")
                ))
        );

        Map<String, List<String>> result = service.deduplicateTop3(sections);
        // Should still return 3 items per section but may repeat since no alternatives
        assertEquals(3, result.get("s1").size());
        assertEquals(3, result.get("s2").size());
    }

    @Test
    public void testDeduplicateTop3_PartialSection() {
        List<Section> sections = Collections.singletonList(
                new Section("s1", Arrays.asList(
                        new Stream("id1"),
                        new Stream("id1")
                ))
        );

        Map<String, List<String>> result = service.deduplicateTop3(sections);
        // Only 2 elements, ensure no crash and returns as is
        assertEquals(2, result.get("s1").size());
    }

    @Test
    public void testDeduplicateTop3_SameStreamAcrossAllSections() {
        List<Section> sections = Arrays.asList(
                new Section("s1", Arrays.asList(
                        new Stream("id1"),
                        new Stream("id1"),
                        new Stream("id1")
                )),
                new Section("s2", Arrays.asList(
                        new Stream("id1"),
                        new Stream("id1"),
                        new Stream("id1")
                )),
                new Section("s3", Arrays.asList(
                        new Stream("id1"),
                        new Stream("id1"),
                        new Stream("id1")
                ))
        );

        Map<String, List<String>> result = service.deduplicateTop3(sections);
        // Only the first section should have id1 in top3; others will struggle to find unique top 3
        assertTrue(result.get("s1").contains("id1"));
        assertFalse(result.get("s2").subList(0, 1).contains("id1") &&
                result.get("s3").subList(0, 1).contains("id1")); // At most 1 top section can use id1
    }


    @Test(expected = DeduplicationException.class)
    public void testDeduplicateTop3_EmptySections() {
        List<Section> sections = new ArrayList<Section>();
        service.deduplicateTop3(sections);
    }

    @Test(expected = DeduplicationException.class)
    public void testDeduplicateTop3_NullInput() {
        service.deduplicateTop3(null);
    }
}