package org.hmcts.camunda.poc;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Logger;

public abstract class AbstractBaseTaskHandler implements ExternalTaskHandler {

    @Autowired protected CamundaClientManager clientManager;

    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    protected void init(String topic) {
        // subscribe to an external task topic as specified in the process
        clientManager.getExternalTaskClient().subscribe(topic)
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler(this)
                .open();
    }

    protected void handleError(ExternalTask externalTask, ExternalTaskService externalTaskService, String message) {
        externalTaskService.handleFailure(
                externalTask,
                externalTask.getWorkerId(),
                message,
                0,
                0L);
    }

}
