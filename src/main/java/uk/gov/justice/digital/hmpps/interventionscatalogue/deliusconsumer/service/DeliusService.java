package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.StandardReference;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.NsiTypeRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository.StandardReferenceRepository;

@Service
@Slf4j
public class DeliusService {
    private final NsiTypeRepository nsiTypeRepository;
    private final StandardReferenceRepository standardReferenceRepository;

    public DeliusService(NsiTypeRepository nsiTypeRepository,
                         StandardReferenceRepository standardReferenceRepository) {
        this.nsiTypeRepository = nsiTypeRepository;
        this.standardReferenceRepository = standardReferenceRepository;
    }

    public StandardReference saveStandardReference(final StandardReference standardReference) {
        return standardReferenceRepository.save(standardReference);
    }

    public NsiType saveNsiType(final NsiType nsiType) {
        return nsiTypeRepository.save(nsiType);
    }
}
