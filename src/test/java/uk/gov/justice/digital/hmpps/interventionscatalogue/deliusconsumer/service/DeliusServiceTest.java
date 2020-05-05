package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroDataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroInterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.EventType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.Address;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiSubTypeId;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.Organisation;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.PartitionArea;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.ProbationArea;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.ReferenceDataMaster;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.StandardReference;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class DeliusServiceTest {
    @Autowired
    DeliusService deliusService;

    @Test
    public void testNsiTypeSave() {
        var standardReference = StandardReference.builder()
                .codeValue("test")
                .codeDescription("test")
                .referenceDataMaster(ReferenceDataMaster.builder()
                        .codeSetName("test")
                        .description("test")
                        .listSequence('0').build())
                .selectable('Y')
                .createdByUserId(0)
                .createdDateTime(new Date())
                .lastUpdatedDateTime(new Date())
                .lastUpdatedUserId(0)
                .build();

        deliusService.saveStandardReference(standardReference);

        var nsiType = NsiType.builder()
                .code("test")
                .description("test")
                .offenderLevel(0)
                .eventLevel(0)
                .allowActiveDuplicates(1)
                .allowInactiveDuplicates(1)
                .selectable('Y')
                .createdByUserId(0)
                .createdDateTime(new Date())
                .lastUpdatedDateTime(new Date())
                .lastUpdatedUserId(0)
                .nsiPurposeId(standardReference)
                .build();

        deliusService.saveNsiType(nsiType);
    }

    @Test
    public void saveNsiSubTypeTest() {
        var nsiType = deliusService.getNsiTypes().stream().findFirst().get();

        var standardReference = StandardReference.builder()
                .codeValue("test")
                .codeDescription("test")
                .referenceDataMaster(ReferenceDataMaster.builder()
                        .codeSetName("test")
                        .description("test")
                        .listSequence('0').build())
                .selectable('Y')
                .createdByUserId(0)
                .createdDateTime(new Date())
                .lastUpdatedDateTime(new Date())
                .lastUpdatedUserId(0)
                .build();

        deliusService.saveStandardReference(standardReference);

        var nsiSubType = NsiSubType.builder()
                .id(new NsiSubTypeId(standardReference.getStandardReferenceListId(), nsiType.getNsiTypeId()))
                .nsiType(nsiType)
                .standardReference(standardReference)
                .build();

        deliusService.saveNsiSubType(nsiSubType);
    }

    @Test
    public void saveProbationArea() {
        var standardReference = StandardReference.builder()
                .codeValue("test")
                .codeDescription("test")
                .referenceDataMaster(ReferenceDataMaster.builder()
                        .codeSetName("test")
                        .description("test")
                        .listSequence('0').build())
                .selectable('Y')
                .createdByUserId(0)
                .createdDateTime(new Date())
                .lastUpdatedDateTime(new Date())
                .lastUpdatedUserId(0)
                .build();

        var probationArea = ProbationArea.builder()
                .code("TST")
                .description("test")
                .address(Address.builder()
                        .softDeleted(0)
                        .partitionArea(PartitionArea.builder()
                                .area("test")
                                .build())
                    .createdByUserId(0)
                    .createdDateTime(new Date())
                    .lastUpdatedDateTime(new Date())
                    .lastUpdatedUserId(0)
                .build())
                .organisation(Organisation.builder()
                        .code("TST")
                        .description("test")
                        .activeFlag(1)
                        .privateFlag(1)
                        .startDate(new Date())
                        .address(Address.builder()
                                .softDeleted(0)
                                .partitionArea(PartitionArea.builder()
                                        .area("test")
                                        .build())
                                .createdByUserId(0)
                                .createdDateTime(new Date())
                                .lastUpdatedDateTime(new Date())
                                .lastUpdatedUserId(0)
                                .build())
                        .createdByUserId(0)
                        .createdDateTime(new Date())
                        .lastUpdatedDateTime(new Date())
                        .lastUpdatedUserId(0)
                .build())
                .privateFlag(0)
                .probationAreaId(0)
                .spgActiveId(standardReference)
                .selectable('Y')
                .startDate(new Date())
                .createdByUserId(0)
                .createdDateTime(new Date())
                .lastUpdatedDateTime(new Date())
                .lastUpdatedUserId(0)
                .build();

        deliusService.saveProbationArea(probationArea);
    }

    @Test
    void createProvider() {
        var avroDataMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroProvider.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusCode("TP1")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test provider")
                        .build())
                .build();

        deliusService.updateDelius(avroDataMessage);

        var resultingProbationArea = deliusService.getProbationArea("TP1");
        assertThat(resultingProbationArea).isNotNull();
        assertThat(resultingProbationArea.getDescription()).isEqualTo("Test provider");
        assertThat(resultingProbationArea.getSelectable()).isEqualTo('Y');
    }

    @Test
    void updateProvider() {
        var uuid = UUID.randomUUID().toString();
        var createMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroProvider.newBuilder()
                        .setId(uuid)
                        .setDeliusCode("TP2")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test provider")
                        .build())
                .build();

        deliusService.updateDelius(createMessage);

        var updateMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.UPDATED)
                .setEntity(AvroProvider.newBuilder()
                        .setId(uuid)
                        .setDeliusCode("TP2")
                        .setActive(false)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Updated test provider")
                        .build())
                .build();

        deliusService.updateDelius(updateMessage);

        var resultingProbationArea = deliusService.getProbationArea("TP2");
        assertThat(resultingProbationArea).isNotNull();
        assertThat(resultingProbationArea.getDescription()).isEqualTo("Updated test provider");
        assertThat(resultingProbationArea.getSelectable()).isEqualTo('Y');
    }

    @Test
    void updatingWhenNonExistentResultsInException() {
        var updateMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.UPDATED)
                .setEntity(AvroProvider.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusCode("TP3")
                        .setActive(false)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test provider")
                        .build())
                .build();

        Exception exception = assertThrows(DeliusDataException.class, () -> {
            deliusService.updateDelius(updateMessage);
        });
    }

    @Test
    void deleteProvider() {
        var uuid = UUID.randomUUID().toString();
        var createMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroProvider.newBuilder()
                        .setId(uuid)
                        .setDeliusCode("TP4")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test provider")
                        .build())
                .build();

        deliusService.updateDelius(createMessage);

        var deleteMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.DELETED)
                .setEntity(AvroProvider.newBuilder()
                        .setId(uuid)
                        .setDeliusCode("TP4")
                        .setVersion(123)
                        .build())
                .build();

        deliusService.updateDelius(deleteMessage);

        var resultingProbationArea = deliusService.getProbationArea("TP4");
        assertThat(resultingProbationArea).isNotNull();
        assertThat(resultingProbationArea.getSelectable()).isEqualTo('N');
    }

    @Test
    void createNsiType() {
        var avroDataMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroInterventionType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusCode("TP1")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test Intervention")
                        .build())
                .build();

        deliusService.updateDelius(avroDataMessage);

        var resultingInterventionType = deliusService.getNsiType("TP1");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getDescription()).isEqualTo("Test Intervention");
        assertThat(resultingInterventionType.getSelectable()).isEqualTo('Y');
    }

    @Test
    void updateNsiType() {
        var createMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroInterventionType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusCode("TP1")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test Intervention")
                        .build())
                .build();

        deliusService.updateDelius(createMessage);

        var updateMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.UPDATED)
                .setEntity(AvroInterventionType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusCode("TP1")
                        .setActive(false)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Got another name now")
                        .build())
                .build();

        deliusService.updateDelius(updateMessage);

        var resultingInterventionType = deliusService.getNsiType("TP1");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getDescription()).isEqualTo("Got another name now");
        assertThat(resultingInterventionType.getSelectable()).isEqualTo('N');
    }

    @Test
    void deleteNsiType() {
        var uuid = UUID.randomUUID().toString();
        var createMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroInterventionType.newBuilder()
                        .setId(uuid)
                        .setDeliusCode("TP1")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test Intervention")
                        .build())
                .build();

        deliusService.updateDelius(createMessage);

        var updateMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.DELETED)
                .setEntity(AvroInterventionType.newBuilder()
                        .setId(uuid)
                        .setDeliusCode("TP1")
                        .setVersion(123)
                        .build())
                .build();

        deliusService.updateDelius(updateMessage);

        var resultingInterventionType = deliusService.getNsiType("TP1");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getSelectable()).isEqualTo('N');
    }

    @Test
    void createNsiSubType() {
        var createTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroInterventionType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusCode("TP2")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test Intervention")
                        .build())
                .build();

        deliusService.updateDelius(createTypeMessage);

        var createSubTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroInterventionSubType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusParentNsiCode("TP2")
                        .setDeliusCode("ST1")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test Intervention")
                        .build())
                .build();

        deliusService.updateDelius(createSubTypeMessage);

        var resultingInterventionType = deliusService.getNsiType("TP2");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getNsiSubTypes()).hasSize(1);
    }

    @Test
    void updateNsiSubType() {
        var createTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroInterventionType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusCode("TP2")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test Intervention")
                        .build())
                .build();

        deliusService.updateDelius(createTypeMessage);

        var createSubTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroInterventionSubType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusParentNsiCode("TP2")
                        .setDeliusCode("ST1")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test Intervention")
                        .build())
                .build();

        deliusService.updateDelius(createSubTypeMessage);

        var updateSubTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.UPDATED)
                .setEntity(AvroInterventionSubType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusParentNsiCode("TP2")
                        .setDeliusCode("ST1")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Modified Intervention Subtype")
                        .build())
                .build();

        deliusService.updateDelius(updateSubTypeMessage);

        var resultingInterventionType = deliusService.getNsiType("TP2");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getNsiSubTypes()).hasSize(1);
        assertThat(resultingInterventionType.getNsiSubTypes().get(0).getStandardReference().getCodeDescription())
                .isEqualTo("Modified Intervention Subtype");
    }

    @Test
    void deleteNsiSubType() {
        var createTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroInterventionType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusCode("TP2")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test Intervention")
                        .build())
                .build();

        deliusService.updateDelius(createTypeMessage);

        var createSubTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.CREATED)
                .setEntity(AvroInterventionSubType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusParentNsiCode("TP2")
                        .setDeliusCode("ST1")
                        .setActive(true)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName("Test Intervention")
                        .build())
                .build();

        deliusService.updateDelius(createSubTypeMessage);

        var updateSubTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(EventType.DELETED)
                .setEntity(AvroInterventionSubType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusParentNsiCode("TP2")
                        .setDeliusCode("ST1")
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .build())
                .build();

        deliusService.updateDelius(updateSubTypeMessage);

        var resultingInterventionType = deliusService.getNsiType("TP2");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getNsiSubTypes()).hasSize(1);
        assertThat(resultingInterventionType.getNsiSubTypes().get(0).getStandardReference().getSelectable())
                .isEqualTo('N');
    }

    @Test
    void linkProviderToNsiType() {

    }

    @Test
    void unlinkProviderFromNsiType() {

    }
}
