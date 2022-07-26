package me.dev.solution.util;


import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import me.dev.solution.model.UserData;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class CsvDataLoader {

    /**
     * header format
     * name,email,subject,eventName,eventStartTime,eventLink,sign
     */
    public static List<UserData> loadUserData(Path path) throws IOException {
        List<UserData> userDataList = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path)) {
            int lineNumber = 0;
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    if (lineNumber == 0) {
                        lineNumber++;
                        continue;
                    }

                    Map<String, Object> attributes = new HashMap<>();

                    attributes.put("name", line[0]);
                    attributes.put("eventName", line[3]);
                    attributes.put("eventStartTime", line[4]);
                    attributes.put("eventLink", line[5]);
                    attributes.put("sign", line[6]);
                    UserData userData = UserData.builder()
                            .email(line[1])
                            .subject(line[2])
                            .attributes(attributes)
                            .build();
                    log.info("{} at line no : {}", Arrays.toString(line), lineNumber);
                    userDataList.add(userData);
                    lineNumber++;
                }
            }
        }
        return userDataList;
    }

    // Overloaded method for default path
    public static List<UserData> loadUserData() throws URISyntaxException, IOException {
        Path path = Paths.get(
                ClassLoader.getSystemResource("data.csv").toURI());
        return loadUserData(path);
    }


}
