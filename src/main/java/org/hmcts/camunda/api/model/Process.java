package org.hmcts.camunda.api.model;

import java.time.LocalDateTime;

public class Process {
    public String id;
    public String processDefinitionKey;
    public String processDefinitionName;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public Long durationInMillis;
    public String state;

    @Override
    public String toString() {
        return String.format("%s : %s - %s %s", processDefinitionKey, startTime, endTime, state);
    }

}
