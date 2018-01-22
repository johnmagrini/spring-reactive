package com.talks.springstuff.server;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class InstrumentRepo {

    private static final Map<Integer, Instrument> instruments = new HashMap<>();
    private static int counter = 1;

    public void addInstrument(Instrument inst) {
        instruments.put(inst.getSecurityId(), inst);
    }

    public Mono<Instrument> getById(Integer id) {
        return Mono.just(instruments.get(id));
    }

    public Flux<Instrument> getAll() {
        return Flux.fromIterable(instruments.values());
    }
}
