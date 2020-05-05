package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroDataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.EventType;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class SqsService {
    private final AmazonSQSAsync amazonSqsClient;
    private final DeliusService deliusService;
    private final AvroDeserialiser avroDeserialiser;

    @SqsListener("delius_event_queue")
    public void receiveMessage(final String message, @Header("SenderId") final String senderId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String unwrappedMessage = mapper.readTree(message).get("Message").asText();

        deliusService.updateDelius(avroDeserialiser.deserialise(unwrappedMessage));
    }
}
