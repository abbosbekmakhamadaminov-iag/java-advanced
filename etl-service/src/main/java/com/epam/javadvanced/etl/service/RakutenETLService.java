package com.epam.javadvanced.etl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.epam.javadvanced.etl.model.RakutenResponse;
import com.epam.javadvanced.shared.model.Sport;
import com.epam.javadvanced.etl.repo.RakutenRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class RakutenETLService {

    private final RakutenRepository repository;
    private final WebClient webClient;

    private static final Set<String> SPORT_KEYWORDS = new HashSet<>(Arrays.asList(
            "football", "soccer", "basketball", "tennis", "baseball", "volleyball", 
            "swimming", "running", "cycling", "golf", "skiing", "surfing",
            "badminton", "table tennis", "hockey", "rugby", "cricket", "boxing"
    ));

    public RakutenETLService(RakutenRepository repository) {
        this.repository = repository;
        this.webClient = WebClient.builder()
                .baseUrl("https://app.rakuten.co.jp")
                .build();
    }

    public Flux<Sport> processRakutenData() {
        log.info("Starting ETL process with backpressure...");
        
        return fetchRakutenItems()
                .doOnRequest(n -> log.info("request({}) called - requesting {} items from upstream", n, n))
                .doOnNext(item -> log.info("onNext() called - processing item: {}", item.getItemName()))
                .flatMap(this::extractSportNames)
                .distinct()
                .filter(sportName -> !sportName.trim().isEmpty())
                .flatMap(this::saveSportIfNotExists)
                .doOnComplete(() -> log.info("ETL process completed"));
    }

    public Flux<Sport> processRakutenDataWithBackpressure() {
        log.info("Starting ETL process with controlled backpressure (request 20)...");
        
        return fetchRakutenItems()
                .limitRate(20)
                .doOnRequest(n -> log.info("request({}) called - requesting {} items from upstream", n, n))
                .doOnNext(item -> log.info("onNext() called - processing item: {}", item.getItemName()))
                .flatMap(this::extractSportNames)
                .distinct()
                .filter(sportName -> !sportName.trim().isEmpty())
                .flatMap(this::saveSportIfNotExists)
                .doOnComplete(() -> log.info("ETL process with backpressure completed"));
    }

    private Flux<com.epam.javadvanced.etl.model.RakutenItem> fetchRakutenItems() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/services/api/IchibaItem/Search/20220601")
                        .queryParam("format", "json")
                        .queryParam("keyword", "sport")
                        .queryParam("applicationId", "1057715132192408251")
                        .build())
                .retrieve()
                .bodyToMono(RakutenResponse.class)
                .doOnNext(response -> log.info("Received {} items from Rakuten API", 
                    response.getItems() != null ? response.getItems().size() : 0))
                .flatMapMany(response -> Flux.fromIterable(response.getItems()))
                .map(RakutenResponse.RakutenItemWrapper::getItem)
                .onErrorResume(error -> {
                    log.error("Error fetching from Rakuten API: ", error);
                    return Flux.empty();
                });
    }

    private Flux<String> extractSportNames(com.epam.javadvanced.etl.model.RakutenItem item) {
        String itemName = item.getItemName() != null ? item.getItemName().toLowerCase() : "";
        String itemCaption = item.getItemCaption() != null ? item.getItemCaption().toLowerCase() : "";
        
        return Flux.fromIterable(SPORT_KEYWORDS)
                .filter(sport -> itemName.contains(sport) || itemCaption.contains(sport))
                .map(String::toLowerCase);
    }

    private Mono<Sport> saveSportIfNotExists(String sportName) {
        return repository.findByName(sportName)
                .switchIfEmpty(
                    repository.save(new Sport(null, sportName))
                            .doOnNext(saved -> log.info("Saved new sport: {}", saved.getName()))
                )
                .doOnNext(sport -> log.debug("Sport exists or saved: {}", sport.getName()));
    }
}