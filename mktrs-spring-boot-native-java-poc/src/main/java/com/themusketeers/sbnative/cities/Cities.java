/*----------------------------------------------------------------------------*/
/* Source File:   CITIES.JAVA                                                 */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.15/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.cities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

/**
 * Represents Cities information.
 *
 * @param cityId       Unique identifier for the City
 * @param name         Description about the City.
 * @param officialName Gives a official name for the Country.
 * @param population   Indicates the number of inhabitants for the City.
 * @param IsCapital    Specifies the city is a capital.
 * @param latitude     Indicates the Country latitude.
 * @param longitude    Indicates the Country longitude.
 * @param timeZone     Indicates the Country TimeZone.
 * @param countryId    Unique identifier for the Country
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record Cities(
    @Id @Column("CITY_ID") String cityId,
    @Column("NAME") String name,
    @Column("OFFICIAL_NAME") String officialName,
    @Column("POPULATION") Long population,
    @Column("IS_CAPITAL") Character IsCapital,
    @Column("LATITUDE") Long latitude,
    @Column("LONGITUDE") Long longitude,
    @Column("TIME_ZONE") String timeZone,
    @Column("COUNTRY_ID") String countryId) {
}
