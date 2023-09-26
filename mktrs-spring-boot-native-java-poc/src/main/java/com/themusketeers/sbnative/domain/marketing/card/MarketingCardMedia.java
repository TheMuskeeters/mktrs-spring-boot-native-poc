/*----------------------------------------------------------------------------*/
/* Source File:   MARKETINGCARDMEDIA.JAVA                                     */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.marketing.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Objects;

/**
 * Contains the information for Marketing Card Media definition.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "uri"})
public class MarketingCardMedia {
    private String type;
    private String uri;

    public MarketingCardMedia() {
    }

    public MarketingCardMedia(String type, String uri) {
        this.type = type;
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketingCardMedia that = (MarketingCardMedia) o;
        return Objects.equals(type, that.type) && Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, uri);
    }

    @Override
    public String toString() {
        return "MarketingCardMedia{" + "type='" + type + '\'' + ", uri='" + uri + '\'' + '}';
    }
}
