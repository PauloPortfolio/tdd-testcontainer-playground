package com.testcontainer.container.config;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/*
RESTARTED TESTCONTAINERS
https://www.testcontainers.org/test_framework_integration/junit_5/#restarted-containers
 */
@Testcontainers
public class ContainerConfig {

  @Container
  public static final MongoDBContainer sharedContainer =
       new MongoDBContainer(DockerImageName.parse("mongo:4.4.2"));


  @DynamicPropertySource
  static void mongoProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri",sharedContainer::getReplicaSetUrl);
  }

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





