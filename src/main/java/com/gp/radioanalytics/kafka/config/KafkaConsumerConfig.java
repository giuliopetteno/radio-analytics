package com.gp.radioanalytics.kafka.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.common.errors.DisconnectException;
import org.apache.kafka.common.errors.NetworkException;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;
import tools.jackson.core.JacksonException;

@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.consumer.retry.generic-error.interval}")
    private long genericErrorInterval;

    @Value("${kafka.consumer.retry.generic-error.max-retries}")
    private long genericErrorMaxRetries;

    @Value("${kafka.consumer.retry.infrastructure-error.initial-interval}")
    private long infrastructureErrorInitialInterval;

    @Value("${kafka.consumer.retry.infrastructure-error.multiplier}")
    private double infrastructureErrorMultiplier;

    @Value("${kafka.consumer.retry.infrastructure-error.max-interval}")
    private long infrastructureErrorMaxInterval;

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        DeadLetterPublishingRecoverer dlpRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

        FixedBackOff genericErrorBackOff = new FixedBackOff(genericErrorInterval, genericErrorMaxRetries);

        ExponentialBackOff infrastructureErrorBackOff = new ExponentialBackOff();
        infrastructureErrorBackOff.setInitialInterval(infrastructureErrorInitialInterval);
        infrastructureErrorBackOff.setMultiplier(infrastructureErrorMultiplier);
        infrastructureErrorBackOff.setMaxInterval(infrastructureErrorMaxInterval);
        infrastructureErrorBackOff.setMaxElapsedTime(Long.MAX_VALUE);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(dlpRecoverer, genericErrorBackOff);

        errorHandler.setBackOffFunction((_, exception) -> {
            if (isInfrastructureFailure(exception)) {
                return infrastructureErrorBackOff;
            }
            return genericErrorBackOff;
        });

        errorHandler.addNotRetryableExceptions(JacksonException.class);

        return errorHandler;
    }

    private boolean isInfrastructureFailure(Exception exception) {
        return ExceptionUtils.getThrowableList(exception).stream()
                .anyMatch(cause -> cause instanceof TransientDataAccessException
                        || cause instanceof DataAccessResourceFailureException
                        || cause instanceof TimeoutException
                        || cause instanceof DisconnectException
                        || cause instanceof NetworkException);
    }
}