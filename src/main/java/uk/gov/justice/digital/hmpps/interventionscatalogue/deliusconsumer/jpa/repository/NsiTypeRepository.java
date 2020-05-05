package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiType;

public interface NsiTypeRepository extends JpaRepository<NsiType, Long> {
    NsiType getByCode(String nsiCode);
}
