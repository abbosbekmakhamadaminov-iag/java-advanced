package com.epam.javadvanced.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.epam.javadvanced.etl.service.RakutenETLService;

@Component
@Slf4j
@RequiredArgsConstructor
public class RakutenApiTest implements CommandLineRunner {

    private final RakutenETLService etlService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting Rakuten ETL process demonstration...");

        // Demonstrate backpressure with request(20) pattern
        etlService.processRakutenDataWithBackpressure()
                .take(20) // Limit to first 20 items for demonstration
                .doOnNext(sport -> log.info("ETL Result - Sport saved: {}", sport.getName()))
                .doOnError(error -> log.error("ETL process failed: ", error))
                .doOnComplete(() -> log.info("ETL demonstration completed"))
                .subscribe();

        // Keep the application running for a moment to see the logs
        Thread.sleep(5000);
    }
}
