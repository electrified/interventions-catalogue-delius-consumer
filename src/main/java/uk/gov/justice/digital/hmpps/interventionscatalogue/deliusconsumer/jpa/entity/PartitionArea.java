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
@Table(name = "PARTITION_AREA")
public class PartitionArea {
    @Id
    @GeneratedValue(generator = "partitionAreaIdGenerator")
    @SequenceGenerator(name= "partitionAreaIdGenerator", sequenceName = "PARTITION_ID_SEQ", allocationSize = 1)
    @Column(name = "PARTITION_AREA_ID")
    private long partitionAreaId;

    @Column(name = "AREA")
    private String area;

    @Column(name = "ROW_VERSION")
    private long rowVersion;
}
