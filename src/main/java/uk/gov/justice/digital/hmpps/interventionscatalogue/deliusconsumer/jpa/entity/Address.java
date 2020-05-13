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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ADDRESS")
public class Address {
    @Id
    @GeneratedValue(generator = "addressIdGenerator")
    @SequenceGenerator(name= "addressIdGenerator", sequenceName = "ADDRESS_ID_SEQ", allocationSize = 1)
    @Column(name = "ADDRESS_ID")
    private long addressId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PARTITION_AREA_ID")
    private PartitionArea partitionArea;

    @Column(name = "SOFT_DELETED")
    private long softDeleted;

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
}
