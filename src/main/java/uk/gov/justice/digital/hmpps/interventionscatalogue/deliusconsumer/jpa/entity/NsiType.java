package uk.gov.justice.digital.hmpps.interventionscatalogue.deliusconsumer.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

/*
"NSI_TYPE_ID" NUMBER NOT NULL ENABLE,
	"CODE" VARCHAR2(20) NOT NULL ENABLE,
	"DESCRIPTION" VARCHAR2(200) NOT NULL ENABLE,
	"OFFENDER_LEVEL" NUMBER NOT NULL ENABLE,
	"EVENT_LEVEL" NUMBER NOT NULL ENABLE,
	"ALLOW_ACTIVE_DUPLICATES" NUMBER NOT NULL ENABLE,
	"ALLOW_INACTIVE_DUPLICATES" NUMBER NOT NULL ENABLE,
	"MINIMUM_LENGTH" NUMBER,
	"MAXIMUM_LENGTH" NUMBER,
	"UNITS_ID" NUMBER,
	"ENFORCEMENT_TYPE" NUMBER DEFAULT 0 NOT NULL ENABLE,
	"SELECTABLE" CHAR(1) NOT NULL ENABLE,
	"CREATED_BY_USER_ID" NUMBER NOT NULL ENABLE,
	"CREATED_DATETIME" DATE NOT NULL ENABLE,
	"LAST_UPDATED_USER_ID" NUMBER NOT NULL ENABLE,
	"LAST_UPDATED_DATETIME" DATE NOT NULL ENABLE,
	"ROW_VERSION" NUMBER DEFAULT 0 NOT NULL ENABLE,
	"TRAINING_SESSION_ID" NUMBER,
	"TERM_COUNT" CHAR(1),
	"RATE_CARD_FLAG" CHAR(1) DEFAULT 'N' NOT NULL ENABLE,
	"CRC_WORK_REQUESTS_DIARY_FLAG" CHAR(1) DEFAULT 'N' NOT NULL ENABLE,
	"NSI_PURPOSE_ID" NUMBER DEFAULT NULL NOT NULL ENABLE,
	"NSI_TRANSFER_TYPE_ID" NUMBER,
	"ADDITIONAL_IDENTIFIER_TYPE1_ID" NUMBER,
	"ADDITIONAL_IDENTIFIER_TYPE2_ID" NUMBER,
 */
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
}
