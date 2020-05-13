package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "R_NSI_TYPE_SUB_TYPE")
public class NsiSubType {
    @EmbeddedId
    private NsiSubTypeId id;

    @Column(name = "ROW_VERSION")
    private long rowVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("nsiTypeId")
    @JoinColumn(name = "NSI_TYPE_ID")
    private NsiType nsiType;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("standardReferenceId")
    @JoinColumn(name = "NSI_SUB_TYPE_ID")
    private StandardReference standardReference;
}
