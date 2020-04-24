package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "R_NSI_TYPE")
public class NsiType {
    @Id
    @GeneratedValue(generator = "nsiTypeIdGenerator")
    @SequenceGenerator(name= "nsiTypeIdGenerator", sequenceName = "NSI_TYPE_ID_SEQ", allocationSize = 1)
    @Column(name = "NSI_TYPE_ID")
    private long nsiTypeId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OFFENDER_LEVEL")
    private long offenderLevel;

    @Column(name = "EVENT_LEVEL")
    private long eventLevel;

    @Column(name = "ALLOW_ACTIVE_DUPLICATES")
    private long allowActiveDuplicates;

    @Column(name = "ALLOW_INACTIVE_DUPLICATES")
    private long allowInactiveDuplicates;

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

    @Column(name = "ROW_VERSION")
    private long rowVersion;

    @ManyToOne
    @JoinColumn(name = "NSI_PURPOSE_ID")
    private StandardReference nsiPurposeId;

    @OneToMany(mappedBy = "nsiType",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<NsiSubType> nsiSubTypes;
}
