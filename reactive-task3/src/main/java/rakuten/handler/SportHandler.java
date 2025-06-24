package rakuten.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import rakuten.model.Sport;
import rakuten.repo.RakutenRepository;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class SportHandler {

    private final RakutenRepository repository;

    public Mono<ServerResponse> createSport(ServerRequest request) {
        String sportName = request.pathVariable("sportname");
        
        return repository.findByName(sportName)
                .flatMap(existingSport -> 
                    ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue("{\"error\":\"Sport with name '" + sportName + "' already exists\"}")
                )
                .switchIfEmpty(
                    repository.save(new Sport(null, sportName))
                            .flatMap(savedSport -> 
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(savedSport)
                            )
                            .onErrorResume(DataIntegrityViolationException.class, ex ->
                                ServerResponse.badRequest()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue("{\"error\":\"Sport with name '" + sportName + "' already exists\"}")
                            )
                )
                .doOnSuccess(response -> log.info("Created sport: {}", sportName))
                .doOnError(error -> log.error("Error creating sport {}: ", sportName, error));
    }

    public Mono<ServerResponse> searchSports(ServerRequest request) {
        String query = request.queryParam("q").orElse("");
        
        if (query.isEmpty()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"error\":\"Query parameter 'q' is required\"}");
        }

        return repository.findByNameContainingIgnoreCase(query)
                .collectList()
                .flatMap(sports -> 
                    ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(sports)
                )
                .doOnSuccess(response -> log.info("Searched sports with query: {}", query))
                .doOnError(error -> log.error("Error searching sports with query {}: ", query, error));
    }
}