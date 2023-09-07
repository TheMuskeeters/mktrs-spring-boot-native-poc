/*----------------------------------------------------------------------------*/
/* Source File:   MARKETINGCARDCALLTOACTION.JAVA                              */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Objects;

/**
 * Contains the information for Marketing Card Call to Action definition.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"label", "type", "uri"})
public class MarketingCardCallToAction {
    private String label;
    private String type;
    private String uri;

    public MarketingCardCallToAction() {
    }

    public MarketingCardCallToAction(String label, String type, String uri) {
        this.label = label;
        this.type = type;
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        MarketingCardCallToAction that = (MarketingCardCallToAction) o;
        return Objects.equals(label, that.label)
            && Objects.equals(type, that.type)
            && Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, type, uri);
    }

    @Override
    public String toString() {
        return "MarketingCardCallToAction{"
            + "label='"
            + label
            + '\''
            + ", type='"
            + type
            + '\''
            + ", uri='"
            + uri
            + '\''
            + '}';
    }
}
