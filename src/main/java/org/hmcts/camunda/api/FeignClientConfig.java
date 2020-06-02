package org.hmcts.camunda.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import feign.Logger;
import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableFeignClients
public class FeignClientConfig {

    public final static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /**
     * Custom decoder using the ObjectMapper (from below)
     */
    @Bean
    public Decoder feignDecoder() {
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper());
        return new ResponseEntityDecoder(new SpringDecoder(() ->
            new HttpMessageConverters(jacksonConverter)
        ));
    }

    /**
     * Object mapper with Java Time support
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // For some reason the ISO format used by Camunda is not translated automatically - not sure why
        // as it looks like valid ISO8601 to me. However, we can fix it using a custom Deserializer - just
        // be aware this will apply to all Feign clients in this application unless more work is done.
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer());
        timeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer());

        mapper.registerModule(timeModule);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

    private LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    private LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    /**
     * Attempting to get dates formatted correctly in GET parameters attached to Feign requests
     * see: https://github.com/spring-cloud/spring-cloud-openfeign/issues/104#issuecomment-232330995
     *
     * NB Feign request URLs use this for formatting, NOT the above ones which are used for JSON ser/deser
     * (e.g. creating an application/json POST body of a request, or parsing a JSON response)
     *
     * @return
     */
    @Bean
    public FeignFormatterRegistrar localDateFeignFormatterRegistrar() {
        // NB Java 8 lambda syntactic sugar for anonymous classes with only one method
        return formatterRegistry -> {
            DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DATE_PATTERN));
            registrar.registerFormatters(formatterRegistry);
        };
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
