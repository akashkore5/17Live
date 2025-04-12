
# Software Developer Assignment

This project addresses the task of removing duplicate streams from the top 3 positions in each section of the 17LIVE application, ensuring an improved user experience while preserving the original order of sections and streams (with only the top 3 adjustable).

## Steps to Run the Code

1. **Prerequisites**:
    - Ensure you have Java Development Kit (JDK) 8 or higher installed.
    - Install a build tool (e.g., Maven) or use the command-line compiler.
    - Configure a logging framework (e.g., SLF4J with Logback) for log output.
    - Navigate to the root folder and run:
   ```bash
     mvn clean install
     ```

2. **Project Structure**:
    - The project contains a `src` directory with all Java source files (`org.live17.*` packages).
    - A `resources` directory contains `application.properties` and root directory contains `input.json`, `output.json`.

3. **Compilation**:
    - Navigate to the project root directory.
    - Compile the code using:
      ```bash
      javac -d bin src/org/live17/*.java
      ```

4. **Execution**:
    - Run the main class:
      ```bash
      java -cp bin org.live17.Main
      ```
    - Ensure `application.properties` in the `resources` directory is configured as:
      ```
      input.file.path=input.json
      output.file.path=output.json
      ```
    - The program reads `input.json`, processes the data, and writes the result to `output.json`.


5. **Verification**:
    - Check the generated `output.json` file in the `resources` directory to confirm the deduplication.
   - Additionally, review the application logs to ensure the process executed without errors. The logs are stored in `resources/app.log` and can be configured using the following Logback configuration:
     ```xml
     <configuration>
        <appender name="FILE" class="ch.qos.logback.core.FileAppender">
           <file>resources/app.log</file>
           <encoder>
             <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
           </encoder>
        </appender>
        <root level="info">
           <appender-ref ref="FILE" />
        </root>
     </configuration>
     ```
   - Ensure the logs indicate successful processing and no warnings or errors related to deduplication.

## Algorithm Explanation

The algorithm aims to eliminate duplicate streams in the top 3 positions of each section while preserving the original order of sections and the relative order of streams (allowing adjustments only in the top 3). Here's a brief overview:

- **Input Processing**: The JSON dataset is read into a `List<Section>` using `JsonFileReader`, creating mutable copies of sections to avoid altering the original data.
- **Deduplication Logic**:
    - For each section, the top 3 streams are evaluated.
    - A `globalUsedTop3Ids` set tracks streamer IDs used in the top 3 across all sections to prevent global duplicates (though the requirement focuses on per-section duplicates, this adds robustness).
    - A `localTop3Ids` set tracks duplicates within the current section's top 3.
    - Streams are initially assigned to `newTop3` if unique; duplicates are moved to a `fallbackPool`.
    - The `fallbackPool` is iterated to fill any missing top 3 slots with unique streams, preserving the original order.
    - The section's stream list is rebuilt with the new top 3 followed by the original streams from position 3 onward.
- **Output Generation**: The deduplicated sections are converted into a `Map<String, List<String>>` and written to `output.json`.

This approach ensures no repetition in the top 3 slots per section while maintaining the required order constraints.

## Input and Output JSON

### Input (`input.json`)
```json
[
  {
    "sectionData": [
      {"streamerID": "db5309b4-053b-4013-9309-5e0e6a7d0a2a"},
      {"streamerID": "75d95031-72e7-4bee-be92-75494e1530ff"},
      {"streamerID": "75d95031-72e7-4bee-be92-75494e1530ff"},
      {"streamerID": "9b345f78-1973-498d-a06e-935d5c83aae5"},
      {"streamerID": "691f160b-131e-4572-b727-e7d88993428f"},
      {"streamerID": "a5aa8483-59d9-46ec-86a8-31ee05974c54"}
    ],
    "lokalisedKey": "streamers_daily_life_updates",
    "sectionID": "Streamers' Daily Life Updates"
  },
  {
    "mlDynamicLabel": true,
    "labelID": "",
    "sectionData": [
      {"streamerID": "4c2b41d1-f4ce-438e-a0f5-317b7cbf5c3a"},
      {"streamerID": "a352f686-ee93-430a-9e27-9fce686138f5"},
      {"streamerID": "75d95031-72e7-4bee-be92-75494e1530ff"},
      {"streamerID": "9b345f78-1973-498d-a06e-935d5c83aae5"},
      {"streamerID": "75d95031-72e7-4bee-be92-75494e1530ff"},
      {"streamerID": "b0a54dd8-4fa1-4ada-affa-267151093fff"}
    ],
    "sectionID": "Social Media Trends & Influencers"
  },
  {
    "mlDynamicLabel": true,
    "sectionData": [
      {"streamerID": "db53e9b4-053b-4013-9309-5e0e6a7d0a2a"},
      {"streamerID": "75d95031-72e7-4bee-be92-75494e1530ff"},
      {"streamerID": "75e95031-72e7-4bee-be92-75494e1530ff"},
      {"streamerID": "9b3465f78-1973-498d-a06e-935d5c83aae5"},
      {"streamerID": "691f162b-131e-4572-b727-e7d88993428f"},
      {"streamerID": "918d3470-23aa-414e-b81d-28bee67c0c27"},
      {"streamerID": "1a946d91-4855-4006-ba39-f8cf905a9ae2"},
      {"streamerID": "75abd13f-a74f-49ae-8ac3-ecb632c0622d"},
      {"streamerID": "63727510-7d63-4d54-6391-4b86c697819a"},
      {"streamerID": "9387f20d-cf69-4756-a9fc-d25bb6f673c8"},
      {"streamerID": "e6a3d4d9-b7c4-461b-8521-8ee975245855"},
      {"streamerID": "8a91fabf-34e9-43d8-9486-fb85fdb23919"},
      {"streamerID": "84484e4a-48b7-4665-a891-7bea8037613b"}
    ],
    "lokalisedKey": "music",
    "sectionID": "music"
  }
]
```

### Output (`output.json`)
```json
{
  "Streamers' Daily Life Updates": [
    "db5309b4-053b-4013-9309-5e0e6a7d0a2a",
    "75d95031-72e7-4bee-be92-75494e1530ff",
    "9b345f78-1973-498d-a06e-935d5c83aae5",
    "691f160b-131e-4572-b727-e7d88993428f",
    "a5aa8483-59d9-46ec-86a8-31ee05974c54"
  ],
  "Social Media Trends & Influencers": [
    "4c2b41d1-f4ce-438e-a0f5-317b7cbf5c3a",
    "a352f686-ee93-430a-9e27-9fce686138f5",
    "9b345f78-1973-498d-a06e-935d5c83aae5",
    "75d95031-72e7-4bee-be92-75494e1530ff",
    "b0a54dd8-4fa1-4ada-affa-267151093fff"
  ],
  "music": [
    "db53e9b4-053b-4013-9309-5e0e6a7d0a2a",
    "75d95031-72e7-4bee-be92-75494e1530ff",
    "75e95031-72e7-4bee-be92-75494e1530ff",
    "9b3465f78-1973-498d-a06e-935d5c83aae5",
    "691f162b-131e-4572-b727-e7d88993428f",
    "918d3470-23aa-414e-b81d-28bee67c0c27",
    "1a946d91-4855-4006-ba39-f8cf905a9ae2",
    "75abd13f-a74f-49ae-8ac3-ecb632c0622d",
    "63727510-7d63-4d54-6391-4b86c697819a",
    "9387f20d-cf69-4756-a9fc-d25bb6f673c8",
    "e6a3d4d9-b7c4-461b-8521-8ee975245855",
    "8a91fabf-34e9-43d8-9486-fb85fdb23919",
    "84484e4a-48b7-4665-a891-7bea8037613b"
  ]
}
```

## Test Cases

1. **Normal Case with Duplicates**:
    - **Input**: `["id1", "id2", "id2", "id3"]`
    - **Expected Output**: `["id1", "id2", "id3"]`
    - **Purpose**: Verifies duplicate removal in top 3.

2. **No Duplicates**:
    - **Input**: `["id1", "id2", "id3"]`
    - **Expected Output**: Unchanged
    - **Purpose**: Ensures non-duplicated sections remain intact.

3. **Fewer than 3 Streams**:
    - **Input**: `["id1", "id2"]`
    - **Expected Output**: Unchanged
    - **Purpose**: Handles sections with insufficient streams.

4. **All Duplicates**:
    - **Input**: `["id1", "id1", "id1"]`
    - **Expected Output**: `["id1", "id1", "id1"]` (with warning)
    - **Purpose**: Tests edge case with no unique replacements.

## Failure Scenario and Solution

- **Failure Scenario**: If a section contains fewer than 3 unique streams (e.g., `["id1", "id1", "id1"]`), the code cannot resolve duplicates, leaving the user with a suboptimal experience (repeated streams).
- **Possible Solution**: Log a warning and implement a fallback strategy, such as padding with a default stream or repeating the last unique stream (e.g., `["id1", "id1", "id1"]` could become `["id1", "id1", "default"]` if a default stream is available). This enhances user experience by avoiding empty slots or repetitive content.

---
