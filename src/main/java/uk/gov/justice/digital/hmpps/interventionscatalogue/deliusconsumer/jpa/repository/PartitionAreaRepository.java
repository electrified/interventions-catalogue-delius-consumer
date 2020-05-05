package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.PartitionArea;

public interface PartitionAreaRepository extends JpaRepository<PartitionArea, Long> {
    public PartitionArea getPartitionAreaByArea(final String area);
}
