package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroDataEvent;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroInterventionSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroInterventionType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.AvroProvider;
import uk.gov.justice.digital.hmpps.interventionscatalogue.avro.EventType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.Address;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiSubTypeId;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiTypeProbationArea;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.ProbationArea;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.ReferenceDataMaster;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.StandardReference;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.NsiSubTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.NsiTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.OrganisationRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.PartitionAreaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.ProbationAreaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.StandardReferenceRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service.PlaceholderDataGenerator.GENERIC_NSI;
import static uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service.PlaceholderDataGenerator.NPS;
import static uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service.PlaceholderDataGenerator.SPG_OFF;
import static uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service.PlaceholderDataGenerator.TEST_AREA;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class DeliusService {
    private final NsiTypeRepository nsiTypeRepository;
    private final NsiSubTypeRepository nsiSubTypeRepository;
    private final StandardReferenceRepository standardReferenceRepository;
    private final ProbationAreaRepository probationAreaRepository;
    private final OrganisationRepository organisationRepository;
    private final PartitionAreaRepository partitionAreaRepository;

    public ProbationArea getProbationArea(final String code) {
        return probationAreaRepository.getByCode(code);
    }

    public NsiType getNsiType(final String code) {
        return nsiTypeRepository.getByCode(code);
    }

    public void updateDelius(final AvroDataEvent event) {
        if (event.getEntity() instanceof AvroProvider) {
            generateProviderUpdate((AvroProvider)event.getEntity(), event.getEventType());
        } else if (event.getEntity() instanceof AvroInterventionType) {
            generateInterventionTypeUpdate((AvroInterventionType)event.getEntity(), event.getEventType());
        } else if (event.getEntity() instanceof AvroInterventionSubType) {
            generateInterventionSubTypeUpdate((AvroInterventionSubType)event.getEntity(), event.getEventType());
        }
    }

    private void generateProviderUpdate(final AvroProvider entity, final EventType eventType) {
        log.info(entity.toString());

        var existingEntity = probationAreaRepository.getByCode(entity.getDeliusCode());

        if (eventType == EventType.CREATED) {
            if (existingEntity != null) {
                throw new DeliusDataException("Create message received but Provider already exists with that code. Doing nothing for safety");
            } else {
                probationAreaRepository.save(mapProviderToDelius(entity));
            }
        }
        else if (eventType == EventType.UPDATED) {
            if (existingEntity != null) {
                probationAreaRepository.save(updateProbationArea(existingEntity, entity));
            } else {
                throw new DeliusDataException("Update message received but Provider does not exist");
            }

        } else if (eventType == EventType.DELETED) {
            if (existingEntity != null) {
                existingEntity.setSelectable('N');
                probationAreaRepository.save(existingEntity);
            } else {
                throw new DeliusDataException("Delete message received but Provider does not exist");
            }
        }
    }

    private void generateInterventionTypeUpdate(final AvroInterventionType entity, final EventType eventType) {
        log.info(entity.toString());

        var existingEntity = nsiTypeRepository.getByCode(entity.getDeliusCode());

        if (eventType == EventType.CREATED) {
            if (existingEntity != null) {
                throw new DeliusDataException("Create message received but NsiType already exists with that code. Doing nothing for safety");
            } else {
                nsiTypeRepository.save(mapInterventionTypeToDelius(entity));
            }
        }
        else if (eventType == EventType.UPDATED) {
            if (existingEntity != null) {
                nsiTypeRepository.save(updateInterventionType(existingEntity, entity));
            } else {
                throw new DeliusDataException("Update message received but NsiType does not exist");
            }

        } else if (eventType == EventType.DELETED) {
            if (existingEntity != null) {
                existingEntity.setSelectable('N');
                nsiTypeRepository.save(existingEntity);
            } else {
                throw new DeliusDataException("Delete message received but NsiType does not exist");
            }
        }
    }

    private void generateInterventionSubTypeUpdate(final AvroInterventionSubType entity, final EventType eventType) {
        log.info(entity.toString());

        var existingParentEntity = nsiTypeRepository.getByCode(entity.getDeliusParentNsiCode());

        var existingEntity = existingParentEntity != null &&
                             existingParentEntity.getNsiSubTypes() != null
                ? existingParentEntity.getNsiSubTypes().stream()
                .filter(nst -> nst.getStandardReference().getCodeValue().equals(entity.getDeliusCode()))
                .findFirst().orElse(null) : null;

        if (eventType == EventType.CREATED) {
            if (existingEntity != null) {
                throw new DeliusDataException("Create message received but NsiSubType already exists with that code. Doing nothing for safety");
            } else {
                var subtypeStandardReference = mapInterventionSubTypeToDelius(entity);
                standardReferenceRepository.save(subtypeStandardReference);
                var nsiSubType = NsiSubType.builder()
                        .id(new NsiSubTypeId(subtypeStandardReference.getStandardReferenceListId(), existingParentEntity.getNsiTypeId()))
                        .nsiType(existingParentEntity)
                        .standardReference(subtypeStandardReference)
                        .build();

                nsiSubTypeRepository.save(nsiSubType);

                existingParentEntity.getNsiSubTypes().add(nsiSubType);
                nsiTypeRepository.save(existingParentEntity);
            }
        }
        else if (eventType == EventType.UPDATED) {
            if (existingEntity != null) {
                standardReferenceRepository.save(updateSubType(existingEntity.getStandardReference(), entity));
            } else {
                throw new DeliusDataException("Update message received but NsiSubType does not exist");
            }

        } else if (eventType == EventType.DELETED) {
            if (existingEntity != null) {
                existingEntity.getStandardReference().setSelectable('N');
                standardReferenceRepository.save(existingEntity.getStandardReference());
            } else {
                throw new DeliusDataException("Delete message received but NsiSubType does not exist");
            }
        }
    }

    public StandardReference saveStandardReference(final StandardReference standardReference) {
        return standardReferenceRepository.save(standardReference);
    }

    public NsiType saveNsiType(final NsiType nsiType) {
        return nsiTypeRepository.save(nsiType);
    }

    public List<NsiType> getNsiTypes() {
        return nsiTypeRepository.findAll();
    }

    public NsiSubType saveNsiSubType(final NsiSubType nsiSubType) {
        return nsiSubTypeRepository.save(nsiSubType);
    }

    public ProbationArea saveProbationArea(final ProbationArea probationArea) {
        return probationAreaRepository.save(probationArea);
    }

    private NsiType updateInterventionType(NsiType nsiType, final AvroInterventionType interventionType) {
        nsiType.setDescription(interventionType.getName());
        nsiType.setSelectable(interventionType.getActive() ? 'Y' : 'N');
        nsiType.setLastUpdatedDateTime(new Date());
        return nsiType;
    }

    private NsiType mapInterventionTypeToDelius(final AvroInterventionType interventionType) {
        var genericNsiPurpose = standardReferenceRepository.getStandardReferenceByCodeValue(GENERIC_NSI);
        return updateInterventionType(NsiType.builder()
                .code(interventionType.getDeliusCode())
                .offenderLevel(0)
                .eventLevel(0)
                .allowActiveDuplicates(1)
                .allowInactiveDuplicates(1)
                .createdByUserId(0)
                .createdDateTime(Date.from(Instant.ofEpochSecond(interventionType.getCreatedTimestamp())))
                .lastUpdatedUserId(0)
                .nsiPurposeId(genericNsiPurpose)
                .build(), interventionType);
    }

    private StandardReference updateSubType(final StandardReference standardReference, final AvroInterventionSubType avroInterventionSubType) {
        standardReference.setCodeDescription(avroInterventionSubType.getName());
        standardReference.setLastUpdatedDateTime(new Date());
        standardReference.setSelectable(avroInterventionSubType.getActive() ? 'Y' : 'N');
        return standardReference;
    }

    private StandardReference mapInterventionSubTypeToDelius(final AvroInterventionSubType interventionSubType) {
        var creationDate = Date.from(Instant.ofEpochSecond(interventionSubType.getCreatedTimestamp()));

        return updateSubType(StandardReference.builder()
                .codeValue(interventionSubType.getDeliusCode())
                .codeDescription(interventionSubType.getName())
                .referenceDataMaster(ReferenceDataMaster.builder()
                        .codeSetName(interventionSubType.getDeliusCode())
                        .description(interventionSubType.getName())
                        .listSequence('0').build())
                .createdByUserId(0)
                .createdDateTime(creationDate)
                .lastUpdatedUserId(0)
                .build(), interventionSubType);
    }

    /**
     * Updates the provided probation area with details from the incoming catalogue message
     * @param probationArea
     * @param catalogueProvider
     * @return
     */
    public ProbationArea updateProbationArea(ProbationArea probationArea, final AvroProvider catalogueProvider) {
        // currently the description and whether they are active are the only fields mastered within the catalogue
        probationArea.setDescription(catalogueProvider.getName());
        probationArea.setSelectable(catalogueProvider.getActive() ? 'Y' : 'N');
        probationArea.setLastUpdatedDateTime(new Date());
        return probationArea;
    }

    public ProbationArea mapProviderToDelius(final AvroProvider catalogueProvider) {
        var npsOrganisation = organisationRepository.getOrganisationByCode(NPS);
        var spgNonActive = standardReferenceRepository.getStandardReferenceByCodeValue(SPG_OFF);
        var partitionArea = partitionAreaRepository.getPartitionAreaByArea(TEST_AREA);

        var creationDate = Date.from(Instant.ofEpochSecond(catalogueProvider.getCreatedTimestamp()));

        return updateProbationArea(ProbationArea.builder()
                .code(catalogueProvider.getDeliusCode())
                .privateFlag(0)
                .organisation(npsOrganisation)
                .address(Address.builder()
                        .softDeleted(0)
                        .partitionArea(partitionArea)
                        .createdByUserId(0)
                        .createdDateTime(creationDate)
                        .lastUpdatedDateTime(creationDate)
                        .lastUpdatedUserId(0)
                        .build())
                .spgActiveId(spgNonActive)
                .startDate(creationDate)
                .createdByUserId(0)
                .createdDateTime(creationDate)
                .lastUpdatedUserId(0)
                .build(), catalogueProvider);
    }

    private NsiType linkProviderToNsiType(final String providerCode, final String nsiCode) {
        NsiType nsiType = nsiTypeRepository.getByCode(nsiCode);
        ProbationArea provider = probationAreaRepository.getByCode(providerCode);

        NsiTypeProbationArea nsiTypeProbationArea = new NsiTypeProbationArea();
        nsiTypeProbationArea.setNsiType(nsiType);
        nsiTypeProbationArea.setProbationArea(provider);

        nsiType.getProbationAreas()
                .add(nsiTypeProbationArea);

        return nsiTypeRepository.save(nsiType);
    }

    private NsiType unlinkProviderFromNsiType(final String providerCode, final String nsiCode) {
        NsiType nsiType = nsiTypeRepository.getByCode(nsiCode);
        Optional<NsiTypeProbationArea> probationAreaToRemove =
                nsiType
                .getProbationAreas().stream()
                .filter(provider -> provider.getProbationArea().getCode().equals(providerCode))
                .findFirst();

        if(probationAreaToRemove.isPresent()) {
            nsiType.getProbationAreas().remove(probationAreaToRemove);
        }
        return nsiTypeRepository.save(nsiType);
    }

    private NsiType linkNsiTypeToNsiSubType(final String nsiCode, final Long nsiSubTypeId) {
        NsiType nsiType = nsiTypeRepository.getByCode(nsiCode);
        NsiSubType nsiSubType = nsiSubTypeRepository.getOne(new NsiSubTypeId(nsiSubTypeId, nsiType.getNsiTypeId()));
        nsiType.getNsiSubTypes().add(nsiSubType);
        return nsiTypeRepository.save(nsiType);
    }

    private NsiType unlinkNsiTypeFromNsiSubType(final String nsiCode, final Long nsiSubTypeId) {
        NsiType nsiType = nsiTypeRepository.getByCode(nsiCode);
        Optional<NsiSubType> subTypeToRemove = nsiType.getNsiSubTypes().stream()
                .filter(subtype -> subtype.getId().equals(nsiSubTypeId))
                .findFirst();

        if(subTypeToRemove.isPresent()) {
            nsiType.getNsiSubTypes().remove(subTypeToRemove);
        }
        return nsiTypeRepository.save(nsiType);
    }
}
