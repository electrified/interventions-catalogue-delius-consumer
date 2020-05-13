package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class LocalstackConfiguration {
    @Bean
    @ConditionalOnProperty(name = "sqs.provider", havingValue = "localstack", matchIfMissing = true)
    @Primary
    public AmazonSQSAsync awsSqsClient(@Value("${sqs.endpoint.url}") String serviceEndpoint, @Value("${cloud.aws.region.static}") String region) {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, region))
                .build();
    }
}
