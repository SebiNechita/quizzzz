/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.questions.Activity;
import commons.utils.LoggerUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import server.api.game.ActivityService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackages = { "commons", "server" })
@EnableConfigurationProperties
public class Main {

    /**
     * runs the server
     * @param args - arguments passed by command line while starting a program
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * parses a json file when the application starts
     * @param activityService - the activity service
     * @return a lambda expression which parses a json file
     */
    @Bean
    CommandLineRunner runner(ActivityService activityService) {
        return args -> {
            // read json and write to db
            ObjectMapper mapper = new ObjectMapper();

            TypeReference<List<Activity>> typeReference = new TypeReference<List<Activity>>() {};

            InputStream inputStream = TypeReference.class.getResourceAsStream("/activity-bank/activities.json");

            if (inputStream == null) {
                LoggerUtil.warnInline("The file '/activity-bank/activities.json' does not exist in the resources directory!" +
                        "\nThe reason could be that it is included in .gitignore and hence not available in the remote repository");
                return;
            }

            try {
                List<Activity> activities = mapper.readValue(inputStream, typeReference);
                activityService.save(activities);
                LoggerUtil.infoInline("Activities Saved!");
            } catch (IOException e) {
                LoggerUtil.warnInline("Unable to save activities: " + e.getMessage());
            }
        };
    }
}
