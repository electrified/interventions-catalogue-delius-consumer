package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiSubType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.ProbationArea;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.StandardReference;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.NsiSubTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.NsiTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.ProbationAreaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.StandardReferenceRepository;

import java.util.List;

@Service
@Slf4j
public class DeliusService {
    private final NsiTypeRepository nsiTypeRepository;
    private final NsiSubTypeRepository nsiSubTypeRepository;
    private final StandardReferenceRepository standardReferenceRepository;
    private final ProbationAreaRepository probationAreaRepository;

    public DeliusService(NsiTypeRepository nsiTypeRepository,
                         NsiSubTypeRepository nsiSubTypeRepository,
                         StandardReferenceRepository standardReferenceRepository,
                         ProbationAreaRepository probationAreaRepository) {
        this.nsiTypeRepository = nsiTypeRepository;
        this.standardReferenceRepository = standardReferenceRepository;
        this.nsiSubTypeRepository = nsiSubTypeRepository;
        this.probationAreaRepository = probationAreaRepository;
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
}
