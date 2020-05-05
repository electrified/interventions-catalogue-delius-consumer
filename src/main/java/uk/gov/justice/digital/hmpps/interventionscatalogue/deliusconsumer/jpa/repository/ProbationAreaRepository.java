package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.ProbationArea;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.StandardReference;

public interface ProbationAreaRepository extends JpaRepository<ProbationArea, Long> {
    public ProbationArea getByCode(final String code);
}
