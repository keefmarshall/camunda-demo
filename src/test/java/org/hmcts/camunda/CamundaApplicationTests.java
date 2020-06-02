package org.hmcts.camunda;

import org.hmcts.camunda.api.HistoryService;
import org.hmcts.camunda.api.model.Activity;
import org.hmcts.camunda.api.model.Process;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CamundaApplicationTests {

    @Autowired
    HistoryService historyService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testHistoryService_getProcessHistory() {
        List<Process> processes = historyService.getProcessHistory();
        assertTrue(processes.size() > 0);
        System.out.println(processes);
    }

    @Test
    public void testHistoryService_getProcessHistoryByKey() {
        List<Process> processes = historyService.getProcessHistoryByKey("test-action");
        assertTrue(processes.size() > 0);
        System.out.println(processes);
    }

    @Test
    public void testHistoryService_getProcessHistoryByKeySinceDate() {
        List<Process> processes =
                historyService.getProcessHistoryByKeySinceDate(
                        "test-action",
                        ZonedDateTime.now().minusMonths(1));
        assertTrue(processes.size() > 0);
        System.out.println(processes);
    }


    @Test
    public void testHistoryService_getProcessInstanceActivities() {
        List<Activity> activities = historyService.getProcessInstanceActivities("53df0d33-5c70-11e9-9a09-0242ac110002");
        System.out.println(activities);
    }

    @Test
    public void testHistoryService_getProcessInstanceCompleteActivities() {
        List<Activity> activities = historyService.getProcessInstanceCompleteActivities("53df0d33-5c70-11e9-9a09-0242ac110002");
        System.out.println(activities);
    }
}
