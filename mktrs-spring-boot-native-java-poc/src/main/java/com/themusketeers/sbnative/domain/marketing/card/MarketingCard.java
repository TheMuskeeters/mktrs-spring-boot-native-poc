/*----------------------------------------------------------------------------*/
/* Source File:   MARKETINGCARD.JAVA                                          */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.26/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.marketing.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Contains the information for Marketing Card.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "title",
    "description",
    "actionType",
    "timeZone",
    "sortOrder",
    "publish",
    "sites",
    "media",
    "callToAction",
    "localeData",
    "updated"
})
public class MarketingCard {
    private String id;
    private String title;
    private String description;
    private String actionType;
    private String timeZone;
    private String updated;

    private Integer sortOrder;
    private List<String> sites;
    private MarketingCardMedia media;
    private MarketingCardCallToAction callToAction;
    private Map<String, MarketingCardLocaleData> localeData;
    private MarketingCardPublishTime publish;

    public MarketingCard() {
    }

    public MarketingCard(
        String id,
        String title,
        String description,
        String actionType,
        String timeZone,
        String updated,
        Integer sortOrder,
        List<String> sites,
        MarketingCardMedia media,
        MarketingCardCallToAction callToAction,
        Map<String, MarketingCardLocaleData> localeData,
        MarketingCardPublishTime publish) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.actionType = actionType;
        this.timeZone = timeZone;
        this.updated = updated;
        this.sortOrder = sortOrder;
        this.sites = sites;
        this.media = media;
        this.callToAction = callToAction;
        this.localeData = localeData;
        this.publish = publish;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        this.sites = sites;
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

    public Map<String, MarketingCardLocaleData> getLocaleData() {
        return localeData;
    }

    public void setLocaleData(Map<String, MarketingCardLocaleData> localeData) {
        this.localeData = localeData;
    }

    public MarketingCardPublishTime getPublish() {
        return publish;
    }

    public void setPublish(MarketingCardPublishTime publish) {
        this.publish = publish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketingCard that = (MarketingCard) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(actionType, that.actionType) &&
            Objects.equals(timeZone, that.timeZone) &&
            Objects.equals(updated, that.updated) &&
            Objects.equals(sortOrder, that.sortOrder)
            && Objects.equals(sites, that.sites) &&
            Objects.equals(media, that.media) &&
            Objects.equals(callToAction, that.callToAction) &&
            Objects.equals(localeData, that.localeData) &&
            Objects.equals(publish, that.publish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, actionType, timeZone, updated, sortOrder, sites, media, callToAction, localeData, publish);
    }

    @Override
    public String toString() {
        return "MarketingCardDocument{"
            + "id='"
            + id
            + '\''
            + ", title='"
            + title
            + '\''
            + ", description='"
            + description
            + '\''
            + ", actionType='"
            + actionType
            + '\''
            + ", timeZone='"
            + timeZone
            + '\''
            + ", updated='"
            + updated
            + '\''
            + ", sortOrder="
            + sortOrder
            + ", sites="
            + sites
            + ", media="
            + media
            + ", callToAction="
            + callToAction
            + ", localeData="
            + localeData
            + ", publish="
            + publish
            + '}';
    }
}
