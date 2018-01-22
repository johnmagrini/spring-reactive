package com.talks.springstuff.server;

import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Indexed
@Service
public class InstrumentService {

    private InstrumentRepo instrumentRepo;

    InstrumentService (InstrumentRepo repo) {
        this.instrumentRepo = repo;
    }

    public Mono<Instrument> getById(Integer id) {
        return this.instrumentRepo.getById(id);
    }

    public Mono<Instrument> getById(String id) {
        return getById(Integer.parseInt(id));
    }

    public Flux<Instrument> getAll() {
        return this.instrumentRepo.getAll();
    }
}
