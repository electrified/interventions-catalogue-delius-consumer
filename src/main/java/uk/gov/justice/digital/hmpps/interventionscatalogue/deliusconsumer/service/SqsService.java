package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiType;

import java.util.Date;

@Service
@Slf4j
public class SqsService {
    private final AmazonSQSAsync amazonSqsClient;
    private final DeliusService deliusService;

    public SqsService(final AmazonSQSAsync amazonSqsClient, final DeliusService deliusService) {
        this.amazonSqsClient = amazonSqsClient;
        this.deliusService = deliusService;
    }

    @SqsListener("delius_event_queue")
    public void receiveMessage(String message, @Header("SenderId") String senderId) {

        System.out.println(message);


    }
}
