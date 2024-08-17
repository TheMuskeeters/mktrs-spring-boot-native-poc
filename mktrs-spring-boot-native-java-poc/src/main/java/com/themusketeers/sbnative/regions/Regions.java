/*----------------------------------------------------------------------------*/
/* Source File:   REGIONS.JAVA                                                */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.14/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.regions;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents Regions information.
 *
 * @param regionId Unique identifier for the Region.
 * @param name     Description about the Region.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Table("REGIONS")
public record Regions(
    @Id @Column("REGION_ID") String regionId,
    @Column("NAME") String name) {
}
