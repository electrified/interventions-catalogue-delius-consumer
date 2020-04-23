package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
public class NsiSubTypeId implements Serializable {
    @Column(name = "NSI_SUB_TYPE_ID")
    private long nsiSubTypeId;

    @Id
    @Column(name = "NSI_TYPE_ID")
    private long nsiTypeId;
}
