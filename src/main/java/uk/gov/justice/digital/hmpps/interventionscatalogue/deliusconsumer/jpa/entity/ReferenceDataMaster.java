package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "R_REFERENCE_DATA_MASTER")
public class ReferenceDataMaster {
    @Id
    @GeneratedValue(generator = "referenceDataMasterIdGenerator")
    @SequenceGenerator(name = "referenceDataMasterIdGenerator", sequenceName = "REFERENCE_DATA_MASTER_ID_SEQ", allocationSize = 1)
    @Column(name = "REFERENCE_DATA_MASTER_ID")
    private Long ReferenceDataMasterId;

    @Column(name = "CODE_SET_NAME")
    private String codeSetName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ROW_VERSION")
    private long rowVersion;

    @Column(name = "LIST_SEQUENCE")
    private char listSequence;
}
