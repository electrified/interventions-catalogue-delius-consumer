package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "R_STANDARD_REFERENCE_LIST")
public class StandardReference {
    @Id
    @GeneratedValue(generator = "referenceListIdGenerator")
    @SequenceGenerator(name = "referenceListIdGenerator", sequenceName = "STANDARD_REFERENCE_LIST_ID_SEQ", allocationSize = 1)
    @Column(name = "STANDARD_REFERENCE_LIST_ID")
    private Long standardReferenceListId;

    @Column(name = "CODE_VALUE")
    private String codeValue;

    @Column(name = "CODE_DESCRIPTION")
    private String codeDescription;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "REFERENCE_DATA_MASTER_ID")
    private ReferenceDataMaster referenceDataMaster;

    @Column(name = "SELECTABLE")
    private char selectable;

    @Column(name = "CREATED_BY_USER_ID")
    private long createdByUserId;

    @Column(name = "CREATED_DATETIME")
    private Date createdDateTime;

    @Column(name = "LAST_UPDATED_USER_ID")
    private long lastUpdatedUserId;

    @Column(name = "LAST_UPDATED_DATETIME")
    private Date lastUpdatedDateTime;
}
