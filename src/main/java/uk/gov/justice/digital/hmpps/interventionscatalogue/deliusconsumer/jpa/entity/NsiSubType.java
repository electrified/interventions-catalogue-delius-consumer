package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(NsiSubTypeId.class)
@Table(name = "R_NSI_TYPE_SUB_TYPE")
public class NsiSubType {
    @Id
    private long nsiSubTypeId;

    @Id
    private long nsiTypeId;

    @Column(name = "ROW_VERSION")
    private long rowVersion;
}
