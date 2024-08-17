/*----------------------------------------------------------------------------*/
/* Source File:   COUNTRIES.JAVA                                              */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.15/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.countries;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Represents Countries information.
 *
 * @param countryId    Unique identifier for the Country.
 * @param countryCode  Unique assigned code for the Country (Alternate).
 * @param name         Description about the Country.
 * @param officialName Gives a official name for the Country.
 * @param population   Indicates the number of inhabitants for the Country.
 * @param areaSqKm     Indicates the Country Area in Kilometers.
 * @param latitude     Indicates the Country latitude.
 * @param longitude    Indicates the Country longitude.
 * @param timeZone     Indicates the Country TimeZone.
 * @param regionId     Unique identifier for the Region
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record Countries(
    @Id @Column("COUNTRY_ID") String countryId,
    @Column("COUNTRY_CODE") String countryCode,
    @Column("NAME") String name,
    @Column("OFFICIAL_NAME") String officialName,
    @Column("POPULATION") Long population,
    @Column("AREA_SQ_KM") Long areaSqKm,
    @Column("LATITUDE") Long latitude,
    @Column("LONGITUDE") Long longitude,
    @Column("TIME_ZONE") String timeZone,
    @Column("REGION_ID") String regionId) {
}
