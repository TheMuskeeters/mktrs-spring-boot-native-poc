package com.themusketeers.sbnative.service;

import com.themusketeers.sbnative.domain.marketing.card.MarketingCard;
import com.themusketeers.sbnative.domain.marketing.card.document.MarketingCardDocument;
import com.themusketeers.sbnative.repository.MarketingCardRepository;
import com.themusketeers.sbnative.service.intr.MarketingCardService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MarketingCardServiceImpl implements MarketingCardService {
    private final MarketingCardRepository marketingCardRepository;

    public MarketingCardServiceImpl(MarketingCardRepository marketingCardRepository) {
        this.marketingCardRepository = marketingCardRepository;
    }

    @Override
    public Mono<MarketingCard> save(MarketingCard marketingCard) {
        var marketingCardDocument = new MarketingCardDocument(marketingCard.getId(), marketingCard.getTitle(), marketingCard.getDescription(), marketingCard.getActionType(), marketingCard.getTimeZone(), marketingCard.getUpdated(), marketingCard.getSortOrder(), marketingCard.getSites(), marketingCard.getMedia(), marketingCard.getCallToAction(), marketingCard.getLocaleData(), marketingCard.getPublish());

        return marketingCardRepository
            .save(marketingCardDocument)
            .map(msd -> new MarketingCard(msd.getId(), msd.getTitle(), msd.getDescription(), msd.getActionType(), msd.getTimeZone(), msd.getUpdated(), msd.getSortOrder(), msd.getSites(), msd.getMedia(), msd.getCallToAction(), msd.getLocaleData(), msd.getPublish()));
    }

    @Override
    public Mono<MarketingCard> delete(String id) {
        return marketingCardRepository
            .findById(id)
            .flatMap(p ->
                marketingCardRepository
                    .deleteById(p.getId())
                    .thenReturn(p)
            )
            .map(msd -> new MarketingCard(msd.getId(), msd.getTitle(), msd.getDescription(), msd.getActionType(), msd.getTimeZone(), msd.getUpdated(), msd.getSortOrder(), msd.getSites(), msd.getMedia(), msd.getCallToAction(), msd.getLocaleData(), msd.getPublish()));
    }

    @Override
    public Mono<MarketingCard> update(String id, MarketingCard marketingCard) {
        return marketingCardRepository.findById(id)
            .flatMap(u -> {
                var marketingCardToUpdate = new MarketingCard(u.getId(), marketingCard.getTitle(), marketingCard.getDescription(), marketingCard.getActionType(), marketingCard.getTimeZone(), marketingCard.getUpdated(), marketingCard.getSortOrder(), marketingCard.getSites(), marketingCard.getMedia(), marketingCard.getCallToAction(), marketingCard.getLocaleData(), marketingCard.getPublish());

                return save(marketingCardToUpdate);
            }).switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<MarketingCard> retrieveAll() {
        return marketingCardRepository
            .findAll()
            .map(msd -> new MarketingCard(msd.getId(), msd.getTitle(), msd.getDescription(), msd.getActionType(), msd.getTimeZone(), msd.getUpdated(), msd.getSortOrder(), msd.getSites(), msd.getMedia(), msd.getCallToAction(), msd.getLocaleData(), msd.getPublish()));
    }
}
