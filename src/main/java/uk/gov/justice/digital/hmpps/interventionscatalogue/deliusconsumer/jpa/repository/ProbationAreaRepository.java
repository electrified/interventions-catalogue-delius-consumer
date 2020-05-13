package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.ProbationArea;

import java.util.List;

public interface ProbationAreaRepository extends JpaRepository<ProbationArea, Long> {
    List<ProbationArea> getProbationAreaByCode(final String code);
}
