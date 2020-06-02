package org.hmcts.camunda.api.model;

import java.time.LocalDateTime;

/**
 * Data object, deliberately haven't implemented setters/getters - use Lombok
 * (or better, Kotlin!) if required.
 */
public class Activity {
    public String id;
    public String activityId;
    public String activityName;
    public String activityType;
    public boolean canceled;
    public boolean completeScope;
    public String processInstanceId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public int duration;

    @Override
    public String toString() {
        return String.format("%s : %s - %s %s", activityType, startTime, endTime, completeScope);
    }
}
