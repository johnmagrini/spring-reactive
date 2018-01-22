package com.talks.springstuff.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest/instruments")
public class InstrumentController {

    private InstrumentService instrumentService;

    InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping("/{id}")
    Mono<Instrument> byId (@PathVariable("id") int id ) {
        return instrumentService.getById(id);
    }

    @GetMapping
    Flux<Instrument> getAll () {
        return instrumentService.getAll();
    }

}
