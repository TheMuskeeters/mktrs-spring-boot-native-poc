/*----------------------------------------------------------------------------*/
/* Source File:   MARKETINGCARDLOCALEDATA.JAVA                                */
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
 * Contains the information for Marketing Card Call to Action definition.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"title", "description", "ctaLabel", "media", "callToAction"})
public class MarketingCardLocaleData {
    private String title;
    private String description;

    private MarketingCardMedia media;
    private MarketingCardCallToAction callToAction;

    public MarketingCardLocaleData() {
    }

    public MarketingCardLocaleData(
        String title,
        String description,
        MarketingCardMedia media,
        MarketingCardCallToAction callToAction) {
        this.title = title;
        this.description = description;
        this.media = media;
        this.callToAction = callToAction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MarketingCardMedia getMedia() {
        return media;
    }

    public void setMedia(MarketingCardMedia media) {
        this.media = media;
    }

    public MarketingCardCallToAction getCallToAction() {
        return callToAction;
    }

    public void setCallToAction(MarketingCardCallToAction callToAction) {
        this.callToAction = callToAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketingCardLocaleData that = (MarketingCardLocaleData) o;
        return Objects.equals(title, that.title)
            && Objects.equals(description, that.description)
            && Objects.equals(media, that.media)
            && Objects.equals(callToAction, that.callToAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, media, callToAction);
    }

    @Override
    public String toString() {
        return "MarketingCardLocaleData{"
            + "title='"
            + title
            + '\''
            + ", description='"
            + description
            + '\''
            + ", media="
            + media
            + ", callToAction="
            + callToAction
            + '}';
    }
}
