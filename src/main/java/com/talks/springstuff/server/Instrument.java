package com.talks.springstuff.server;

import lombok.Data;

@Data
public class Instrument {

    private Integer securityId;
    private String symbol;
    private String cusip;
    private String description;
}
