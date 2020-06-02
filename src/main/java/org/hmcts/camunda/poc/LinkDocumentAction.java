package org.hmcts.camunda.poc;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class LinkDocumentAction extends AbstractBaseTaskHandler {

    private final static Logger LOGGER = Logger.getLogger(LinkDocumentAction.class.getName());

    @PostConstruct
    public void init() {
        LOGGER.info("Starting LinkDocumentAction...");
        super.init("link-doc-to-case");
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        // Get a process variable
        String ccdid = externalTask.getVariable("ccdid");
        String documentLink = externalTask.getVariable("documentLink");

        if (documentLink == null || ccdid == null) {
            handleError(externalTask, externalTaskService, "Value required for variables 'documentLink' and 'ccdid'");
        }
        else { // success
            LOGGER.info("Received ccdid: " + ccdid + ", documentLink:  " + documentLink);

            // Would link document to case here, instead we just set a success variable

            Map<String, Object> vars = new HashMap<>();
            vars.put("documentLinked", true);

            // Complete the task
            externalTaskService.complete(externalTask, vars);
        }

    }
}
