package com.testcontainer.compose.config;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

/*
SPEED-UP TESTCONTAINERS
https://callistaenterprise.se/blogg/teknik/2020/10/09/speed-up-your-testcontainers-tests/
https://medium.com/pictet-technologies-blog/speeding-up-your-integration-tests-with
-testcontainers-e54ab655c03d
 */
@Testcontainers
public class ComposeConfig {

    final private String COMPOSE_PATH = "src/test/resources/compose-testcontainers.yml";
    final static public int SERVICE_PORT = 27017;
    final static public String SERVICE = "db";


    //@Container //Nao anotar aqui. Annotacao deve ficar na classe receptora
    public DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(
                    new File(COMPOSE_PATH))
                    .withExposedService(SERVICE,SERVICE_PORT,Wait.forListeningPort());

}




