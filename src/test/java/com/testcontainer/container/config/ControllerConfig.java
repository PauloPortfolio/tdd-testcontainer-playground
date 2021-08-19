package com.testcontainer.container.config;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/*
RESTARTED TESTCONTAINERS
https://www.testcontainers.org/test_framework_integration/junit_5/#restarted-containers
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class ControllerConfig extends ContainerConfig {

  public static void header(String title) {
    System.out.printf(
         "\n\n>=====================< %s >=====================<\n" +
              " --> Container-Name: %s\n" +
              " --> Container-Url: %s\n" +
              " --> Container-Running: %s\n" +
              ">=====================< %s >=====================<\n\n%n",
         title,
         sharedContainer.getContainerName(),
         sharedContainer.getReplicaSetUrl(),
         sharedContainer.isRunning(),
         title
                     );
  }
}





