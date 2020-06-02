package org.hmcts.camunda.api;

import org.hmcts.camunda.api.model.Activity;
import org.hmcts.camunda.api.model.Process;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZonedDateTime;
import java.util.List;

@FeignClient("camunda")
public interface HistoryService {

    @RequestMapping("/engine-rest/history/process-instance")
    List<Process> getProcessHistory();

    @RequestMapping("/engine-rest/history/process-instance")
    List<Process> getProcessHistoryByKey(
            @RequestParam String processDefinitionKey
    );

    @RequestMapping("/engine-rest/history/process-instance")
    List<Process> getProcessHistoryByKeySinceDate(
            @RequestParam String processDefinitionKey,
            @RequestParam ZonedDateTime startedAfter
    );

    /**
     * Get all activities associated with a single process instance
     * @param processInstanceId
     * @return
     */
    @RequestMapping("/engine-rest/history/activity-instance")
    List<Activity> getProcessInstanceActivities(
            @RequestParam String processInstanceId
    );

    /**
     * Get all finished activities associated with a single process instance
     * @param processInstanceId
     * @return
     */
    @RequestMapping("/engine-rest/history/activity-instance?finished=true")
    List<Activity> getProcessInstanceFinishedActivities(
            @RequestParam String processInstanceId
    );

    /**
     * Get finished, completeScope activity/activities associated with a single process instance
     * Should ideally return just a single Activity representing the end state but there might
     * be edge cases with more than one completeScope activity depending on the flow definition
     *
     * @param processInstanceId
     * @return
     */
    @RequestMapping("/engine-rest/history/activity-instance?finished=true&completeScope=true")
    List<Activity> getProcessInstanceCompleteActivities(
            @RequestParam String processInstanceId
    );

}
