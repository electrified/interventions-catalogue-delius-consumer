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
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroProviderInterventionLink;
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
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.PartitionAreaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.ProbationAreaRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service.PlaceholderDataGenerator.TEST_AREA;

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
        updateProvider(UUID.randomUUID(), EventType.CREATED, "TP1", true, "Test provider");

        var resultingProbationArea = deliusService.getProbationArea("TP1");
        assertThat(resultingProbationArea).isNotNull();
        assertThat(resultingProbationArea.getDescription()).isEqualTo("Test provider");
        assertThat(resultingProbationArea.getSelectable()).isEqualTo('Y');
    }

    @Test
    void updateProvider() {
        var uuid = UUID.randomUUID();
        updateProvider(uuid, EventType.CREATED, "TP2", true, "Test provider");

        updateProvider(uuid, EventType.UPDATED, "TP2", false, "Updated test provider");

        var resultingProbationArea = deliusService.getProbationArea("TP2");
        assertThat(resultingProbationArea).isNotNull();
        assertThat(resultingProbationArea.getDescription()).isEqualTo("Updated test provider");
        assertThat(resultingProbationArea.getSelectable()).isEqualTo('N');
    }

    @Test
    void updatingWhenNonExistentProviderResultsInException() {
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

        assertThrows(DeliusDataException.class, () -> {
            deliusService.updateDelius(updateMessage);
        });
    }

    @Test
    void deleteProvider() {
        var uuid = UUID.randomUUID();
        updateProvider(uuid, EventType.CREATED, "TP4", true, "Test provider");

        updateProvider(uuid, EventType.DELETED, "TP4", true, "Test provider");

        var resultingProbationArea = deliusService.getProbationArea("TP4");
        assertThat(resultingProbationArea).isNotNull();
        assertThat(resultingProbationArea.getSelectable()).isEqualTo('N');
    }

    @Test
    void createNsiType() {
        updateType(EventType.CREATED, UUID.randomUUID(), "TP1", true, "Test Intervention");

        var resultingInterventionType = deliusService.getNsiType("TP1");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getDescription()).isEqualTo("Test Intervention");
        assertThat(resultingInterventionType.getSelectable()).isEqualTo('Y');
    }

    @Test
    void updateNsiType() {
        var uuid = UUID.randomUUID();
        updateType(EventType.CREATED, uuid, "TP1", true, "Test Intervention");

        updateType(EventType.UPDATED, uuid, "TP1", false, "Got another name now");

        var resultingInterventionType = deliusService.getNsiType("TP1");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getDescription()).isEqualTo("Got another name now");
        assertThat(resultingInterventionType.getSelectable()).isEqualTo('N');
    }

    @Test
    void deleteNsiType() {
        var uuid = UUID.randomUUID();
        updateType(EventType.CREATED, uuid, "TP1", true, "Test Intervention");

        updateType(EventType.DELETED, uuid, "TP1", true, "Test Intervention");

        var resultingInterventionType = deliusService.getNsiType("TP1");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getSelectable()).isEqualTo('N');
    }

    @Test
    void createNsiSubType() {
        updateType(EventType.CREATED, UUID.randomUUID(), "TP2", true, "Test Intervention");

        createAndUpdateSubType(EventType.CREATED, "Test Intervention", true);

        var resultingInterventionType = deliusService.getNsiType("TP2");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getNsiSubTypes()).hasSize(1);
    }

    @Test
    void updateNsiSubType() {
        updateType(EventType.CREATED, UUID.randomUUID(), "TP2", true, "Test Intervention");

        createAndUpdateSubType(EventType.CREATED, "Test Intervention", true);

        createAndUpdateSubType(EventType.UPDATED, "Modified Intervention Subtype", true);

        var resultingInterventionType = deliusService.getNsiType("TP2");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getNsiSubTypes()).hasSize(1);
        assertThat(resultingInterventionType.getNsiSubTypes().get(0).getStandardReference().getCodeDescription())
                .isEqualTo("Modified Intervention Subtype");
    }

    @Test
    void deleteNsiSubType() {
        updateType(EventType.CREATED, UUID.randomUUID(), "TP2", true, "Test Intervention");

        createAndUpdateSubType(EventType.CREATED, "Test Intervention", true);

        createAndUpdateSubType(EventType.DELETED, "Test Intervention", false);

        var resultingInterventionType = deliusService.getNsiType("TP2");
        assertThat(resultingInterventionType).isNotNull();
        assertThat(resultingInterventionType.getNsiSubTypes()).hasSize(1);
        assertThat(resultingInterventionType.getNsiSubTypes().get(0).getStandardReference().getSelectable())
                .isEqualTo('N');
    }

    @Test
    public void createProviderNsiTypeLink() {
        var providerId = UUID.randomUUID();
        updateProvider(providerId, EventType.CREATED, "P10", true, "Test provider");

        var NsiTypeId = UUID.randomUUID();
        updateType(EventType.CREATED, UUID.randomUUID(), "IV1", true, "Test Intervention");

        UpdateProviderNsiType(providerId, NsiTypeId, EventType.CREATED, "P10", "IV1");

        var probationArea = deliusService.getProbationArea("P10");

        assertThat(probationArea).isNotNull();
        assertThat(probationArea.getNsiTypeProbationAreas()).hasSize(1);
        assertThat(probationArea.getNsiTypeProbationAreas().get(0).getNsiType().getCode()).isEqualTo("IV1");
        assertThat(probationArea.getNsiTypeProbationAreas().get(0).getProbationArea().getCode()).isEqualTo("P10");
    }

    @Test
    public void deleteProviderNsiTypeLink() {
        var providerId = UUID.randomUUID();
        updateProvider(providerId, EventType.CREATED, "P11", true, "Test provider");

        var NsiTypeId = UUID.randomUUID();
        updateType(EventType.CREATED, UUID.randomUUID(), "IV2", true, "Test Intervention");

        UpdateProviderNsiType(providerId, NsiTypeId, EventType.CREATED, "P11", "IV2");

        UpdateProviderNsiType(providerId, NsiTypeId, EventType.DELETED, "P11", "IV2");

        var probationArea = deliusService.getProbationArea("P11");

        assertThat(probationArea).isNotNull();
        assertThat(probationArea.getNsiTypeProbationAreas()).hasSize(0);
    }

    private void UpdateProviderNsiType(UUID providerId, UUID nsiTypeId, EventType created, String p11, String iv2) {
        var createProviderNsiTypeLinkMessage = AvroDataEvent.newBuilder()
                .setEventType(created)
                .setEntity(AvroProviderInterventionLink.newBuilder()
                        .setProviderId(providerId.toString())
                        .setDeliusProviderCode(p11)
                        .setInterventionTypeId(nsiTypeId.toString())
                        .setDeliusInterventionCode(iv2)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .build())
                .build();

        deliusService.updateDelius(createProviderNsiTypeLinkMessage);
    }

    private void createAndUpdateSubType(EventType created, String s, boolean active) {
        var createSubTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(created)
                .setEntity(AvroInterventionSubType.newBuilder()
                        .setId(UUID.randomUUID().toString())
                        .setDeliusParentNsiCode("TP2")
                        .setDeliusCode("ST1")
                        .setActive(active)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName(s)
                        .build())
                .build();

        deliusService.updateDelius(createSubTypeMessage);
    }

    private void updateType(EventType created, UUID id, String deliusCode, boolean active, String name) {
        var createTypeMessage = AvroDataEvent.newBuilder()
                .setEventType(created)
                .setEntity(AvroInterventionType.newBuilder()
                        .setId(id.toString())
                        .setDeliusCode(deliusCode)
                        .setActive(active)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName(name)
                        .build())
                .build();

        deliusService.updateDelius(createTypeMessage);
    }

    private AvroDataEvent updateProvider(UUID uuid, EventType created, String tp2, boolean b, String s) {
        var createMessage = AvroDataEvent.newBuilder()
                .setEventType(created)
                .setEntity(AvroProvider.newBuilder()
                        .setId(uuid.toString())
                        .setDeliusCode(tp2)
                        .setActive(b)
                        .setVersion(123)
                        .setCreatedTimestamp(1588607648L)
                        .setName(s)
                        .build())
                .build();

        deliusService.updateDelius(createMessage);
        return createMessage;
    }
}
