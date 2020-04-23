package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.NsiType;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.StandardReference;

public interface StandardReferenceRepository extends JpaRepository<StandardReference, Long> {
}
