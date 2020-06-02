package org.hmcts.camunda.api;

import org.hmcts.camunda.api.FeignClientConfig;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FeignClientConfigTest {

    @Test
    public void testDateFormatter() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(FeignClientConfig.DATE_PATTERN);
        System.out.println(df.format(OffsetDateTime.now()));
        System.out.println(df.format(ZonedDateTime.now()));
    }
}