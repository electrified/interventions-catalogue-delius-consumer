package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroDataEvent;

import java.io.IOException;

@Service
public class AvroDeserialiser {
    public AvroDataEvent deserialise(final String avroMessage) throws IOException {
        DatumReader<AvroDataEvent> reader = new SpecificDatumReader<>(AvroDataEvent.class);
        Decoder decoder = DecoderFactory.get().jsonDecoder(AvroDataEvent.getClassSchema(), avroMessage);
        return reader.read(null, decoder);
    }
}
