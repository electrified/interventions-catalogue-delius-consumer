package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class NsiTypeProbationAreaId implements Serializable {
    @Column(name = "PROBATION_AREA_ID")
    private long standardReferenceId;

    @Column(name = "NSI_TYPE_ID")
    private long nsiTypeId;
}
