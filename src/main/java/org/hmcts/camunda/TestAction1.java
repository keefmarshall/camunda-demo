package org.hmcts.camunda;

import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Service
public class TestAction1 {
    private final static Logger LOGGER = Logger.getLogger(TestAction1.class.getName());


    @PostConstruct
    public void doTestAction1() {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe("test-action-1")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    // Get a process variable
                    String item = (String) externalTask.getVariable("item");
                    Long amount = (Long) externalTask.getVariable("amount");

                    if (item == null) {
                        //throw new IllegalStateException("Value required for variable 'item'");
                        externalTaskService.handleFailure(
                                externalTask,
                                externalTask.getWorkerId(),
                                "Value required for variable 'item'",
                                0,
                                0L);
                    } else { // success
                        LOGGER.info("Charging credit card with an amount of '" + amount + "'€ for the item '" + item + "'...");

                        // Complete the task
                        externalTaskService.complete(externalTask);
                    }
                })
                .open();
    }
}
