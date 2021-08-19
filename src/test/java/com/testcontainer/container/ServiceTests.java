package com.testcontainer.container;

import com.testcontainer.api.entity.Customer;
import com.testcontainer.api.service.CustomerService;
import com.testcontainer.api.repository.IRepository;
import com.testcontainer.api.service.ICustomerService;
import com.testcontainer.container.config.ModelConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.testcontainer.databuilder.CustomerBuilder.customerWithName;
import static org.junit.jupiter.api.Assertions.assertTrue;

// https://www.baeldung.com/spring-data-mongodb-reactive
public class ServiceTests extends ModelConfig {

  final private String enabledTest = "true";
  private List<Customer> customerList;
  private Flux<Customer> customerFlux;

  @Lazy
  @Autowired
  private IRepository repo;

  private ICustomerService service;


  @BeforeAll
  public static void beforeAll() {
    ModelConfig.beforeAll();
  }


  @AfterAll
  public static void afterAll() {
    ModelConfig.afterAll();
  }


  @BeforeEach
  public void setUp() {
    //------------------------------------------//
    //VERY IMPORTANT!!!!
    //DEPENDENCY INJECTION MUST BE DONE MANUALLY
    service = new CustomerService(repo);
    //------------------------------------------//

    Customer customer1 = customerWithName().create();
    Customer customer2 = customerWithName().create();
    customerList = Arrays.asList(customer1,customer2);
    customerFlux = service.saveAll(customerList);
  }


  @Test
  @DisplayName("Save")
  @EnabledIf(expression = enabledTest, loadContext = true)
  public void save() {
    StepVerifier.create(customerFlux)
                .expectNextSequence(customerList)
                .verifyComplete();
  }


  @Test
  @DisplayName("Find: Content")
  @EnabledIf(expression = enabledTest, loadContext = true)
  public void find_count() {
    StepVerifier
         .create(customerFlux)
         .expectSubscription()
         .expectNextMatches(customer -> customerList.get(0)
                                                    .getEmail()
                                                    .equals(customer.getEmail()))
         .expectNextMatches(customer -> customerList.get(1)
                                                    .getEmail()
                                                    .equals(customer.getEmail()))
         .verifyComplete();
  }


  @Test
  @DisplayName("Find: Objects")
  @EnabledIf(expression = enabledTest, loadContext = true)
  public void find_object() {
    StepVerifier
         .create(customerFlux)
         .expectNext(customerList.get(0))
         .expectNext(customerList.get(1))
         .verifyComplete();
  }


  @Test
  @DisplayName("DeleteById")
  @EnabledIf(expression = enabledTest, loadContext = true)
  public void deleteById() {
    StepVerifier.create(customerFlux)
                .expectNextSequence(customerList)
                .verifyComplete();

    StepVerifier
         .create(service.deleteById(customerList.get(0)
                                                .getId()))
         .expectSubscription()
         .verifyComplete();

    Mono<Customer> monoTest = service.findById(customerList.get(0)
                                                           .getId());

    StepVerifier
         .create(monoTest)
         .expectSubscription()
         .expectNextCount(0L)
         .verifyComplete();
  }


  @Test
  @DisplayName("Container")
  @EnabledIf(expression = enabledTest, loadContext = true)
  public void checkContainer() {
    assertTrue(sharedContainer.isRunning());
  }


  @Test
  @DisplayName("BHWorks")
  @EnabledIf(expression = enabledTest, loadContext = true)
  public void bHWorks() {
    try {
      FutureTask<?> task = new FutureTask<>(() -> {
        Thread.sleep(0);
        return "";
      });

      Schedulers.parallel()
                .schedule(task);

      task.get(10,TimeUnit.SECONDS);
      Assertions.fail("should fail");
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
      Assertions.assertTrue(e.getCause() instanceof BlockingOperationError,"detected");
    }
  }
}