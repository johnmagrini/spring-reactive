package com.talks.springstuff.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Slf4j
@Component
public class InstrumentGenerator {

    private InstrumentRepo instrumentRepo;

    InstrumentGenerator(InstrumentRepo instrumentRepo) {
        this.instrumentRepo = instrumentRepo;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void generateData(ApplicationReadyEvent event) {
        IntStream.range(0,100)
                .forEach(x -> createInstrument(x));
    }

    public void createInstrument(int count) {
        Instrument i = new Instrument();
        i.setSecurityId(count);
        i.setCusip(RandomHelper.randomAlphaNumeric(9));
        i.setDescription(RandomHelper.randomString(30));
        i.setSymbol(RandomHelper.randomString(3).toUpperCase());
        instrumentRepo.addInstrument(i);
        log.info(i.toString());
    }
}
