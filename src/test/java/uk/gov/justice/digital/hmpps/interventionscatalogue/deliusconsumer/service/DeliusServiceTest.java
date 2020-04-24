package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiSubTypeId;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.ReferenceDataMaster;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.StandardReference;

import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest
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
}
