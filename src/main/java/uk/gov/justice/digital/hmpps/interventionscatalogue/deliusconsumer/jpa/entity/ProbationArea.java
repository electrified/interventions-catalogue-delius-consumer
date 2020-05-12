package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "PROBATION_AREA")
public class ProbationArea {
    @Id
    @GeneratedValue(generator = "probationAreaIdGenerator")
    @SequenceGenerator(name= "probationAreaIdGenerator", sequenceName = "PROBATION_AREA_ID_SEQ", allocationSize = 1)
    @Column(name = "PROBATION_AREA_ID")
    private long probationAreaId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SELECTABLE")
    private char selectable;

    @Column(name = "ROW_VERSION")
    private long rowVersion;

    @Column(name = "CREATED_BY_USER_ID")
    private long createdByUserId;

    @Column(name = "CREATED_DATETIME")
    private Date createdDateTime;

    @Column(name = "LAST_UPDATED_USER_ID")
    private long lastUpdatedUserId;

    @Column(name = "LAST_UPDATED_DATETIME")
    private Date lastUpdatedDateTime;

    @Column(name = "PRIVATE")
    private long privateFlag;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ORGANISATION_ID")
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

    @Column(name = "START_DATE")
    private Date startDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "SPG_ACTIVE_ID")
    private StandardReference spgActiveId;

    @OneToMany(mappedBy = "probationArea", cascade = CascadeType.ALL)
    private List<NsiTypeProbationArea> nsiTypeProbationAreas;
}
