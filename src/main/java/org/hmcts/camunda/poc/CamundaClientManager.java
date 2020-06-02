package org.hmcts.camunda.poc;

import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The documentation is not very forthcoming about the re-usability or thread-safety
 * of the ExternalTaskClient instance - but it seems to work fine here in the PoC
 *
 */
@Component
public class CamundaClientManager {

    private ExternalTaskClient externalTaskClient;

    @Value("${camunda.baseurl}")
    protected String camundaBaseUrl;


    @PostConstruct
    public void init() {
        this.externalTaskClient = ExternalTaskClient.create()
                .baseUrl(camundaBaseUrl)
                .asyncResponseTimeout(10000) // long polling timeout
                .build();
    }

    public ExternalTaskClient getExternalTaskClient() {
        return this.externalTaskClient;
    }
}
