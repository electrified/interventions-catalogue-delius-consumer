package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity.Organisation;

public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    Organisation getOrganisationByCode(final String code);
}
