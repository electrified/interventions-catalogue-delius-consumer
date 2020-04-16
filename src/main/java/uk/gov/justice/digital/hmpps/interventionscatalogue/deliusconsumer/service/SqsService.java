package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SqsService {
    private final AmazonSQSAsync amazonSqsClient;

    public SqsService(final AmazonSQSAsync amazonSqsClient) {
        this.amazonSqsClient = amazonSqsClient;
    }

    @SqsListener("delius_event_queue")
    public void receiveMessage(String message, @Header("SenderId") String senderId) {
        System.out.println(message);
    }
}
