package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.Address;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.Organisation;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.PartitionArea;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.ReferenceDataMaster;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.StandardReference;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.OrganisationRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.PartitionAreaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.StandardReferenceRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;

/**
 * To be run one-off to generate demo data, referenced by the types we insert
 */
@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class PlaceholderDataGenerator {
    public static final String SPG_OFF = "SPG_OFF";
    public static final String NPS = "NPS";
    public static final String TEST_AREA = "TEST_AREA";
    public static final String GENERIC_NSI = "NSI";

    private final StandardReferenceRepository standardReferenceRepository;
    private final OrganisationRepository organisationRepository;
    private final PartitionAreaRepository partitionAreaRepository;

    @PostConstruct
    public void generateTestData() {
        //SPG Not active
        var existingSpg = standardReferenceRepository.getStandardReferenceByCodeValue(SPG_OFF);

        if(existingSpg == null) {
            standardReferenceRepository.save(StandardReference.builder()
                    .codeValue(SPG_OFF)
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
                    .build());
        }

        // NSI Type Purpose
        var genericNsiPurpose = standardReferenceRepository.getStandardReferenceByCodeValue(GENERIC_NSI);

        if(genericNsiPurpose == null) {
            standardReferenceRepository.save(StandardReference.builder()
                    .codeValue(GENERIC_NSI)
                    .codeDescription("Generic NSI")
                    .referenceDataMaster(ReferenceDataMaster.builder()
                            .codeSetName("NSI")
                            .description("Generic NSI")
                            .listSequence('0').build())
                    .selectable('Y')
                    .createdByUserId(0)
                    .createdDateTime(new Date())
                    .lastUpdatedDateTime(new Date())
                    .lastUpdatedUserId(0)
                    .build());
        }

        // NSI Type Purpose
        var existingNpsOrganisation = organisationRepository.getOrganisationByCode(NPS);

        if (existingNpsOrganisation == null) {
            organisationRepository.save(Organisation.builder()
                    .code(NPS)
                    .description("NPS Organisation")
                    .activeFlag(1)
                    .privateFlag(0)
                    .startDate(new Date())
                    .address(Address.builder()
                            .softDeleted(0)
                            .partitionArea(PartitionArea.builder()
                                    .area("Test Partition Area")
                                    .build())
                            .createdByUserId(0)
                            .createdDateTime(new Date())
                            .lastUpdatedDateTime(new Date())
                            .lastUpdatedUserId(0)
                            .build())
                    .createdDateTime(new Date())
                    .createdByUserId(0)
                    .lastUpdatedDateTime(new Date())
                    .lastUpdatedUserId(0)
                    .build());
        }

        var existingPartitionArea = partitionAreaRepository.getPartitionAreaByArea(TEST_AREA);

        if (existingPartitionArea == null) {
            partitionAreaRepository.save(PartitionArea.builder()
                    .area(TEST_AREA)
                    .build());
        }
    }
}
