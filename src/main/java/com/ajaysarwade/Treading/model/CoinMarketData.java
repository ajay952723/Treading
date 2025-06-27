package com.ajaysarwade.Treading.model;

import com.ajaysarwade.Treading.Other.UtcToLocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import lombok.Data;

@Data
@Entity
public class CoinMarketData {

    @Id
    private String id;

    private String symbol;

    private String name;

    private String image;

    @JsonProperty("current_price")
    private double currentPrice;

    @JsonProperty("market_cap")
    private long marketCap;

    @JsonProperty("market_cap_rank")
    private int marketCapRank;

    @JsonProperty("fully_diluted_valuation")
    private long fullyDilutedValuation;

    @JsonProperty("total_volume")
    private long totalVolume;

    @JsonProperty("high_24h")
    private double high24h;

    @JsonProperty("low_24h")
    private double low24h;

    @JsonProperty("price_change_24h")
    private double priceChange24h;

    @JsonProperty("price_change_percentage_24h")
    private double priceChangePercentage24h;

    @JsonProperty("market_cap_change_24h")
    private long marketCapChange24h;

    @JsonProperty("market_cap_change_percentage_24h")
    private double marketCapChangePercentage24h;

    @JsonProperty("circulating_supply")
    private double circulatingSupply;

    @JsonProperty("total_supply")
    private double totalSupply;

    @JsonProperty("max_supply")
    private double maxSupply;

    private double ath;

    @JsonProperty("ath_change_percentage")
    private double athChangePercentage;

    @JsonProperty("ath_date")
    @JsonDeserialize(using = UtcToLocalDateTimeDeserializer.class)
    private LocalDateTime athDate;

    private double atl;

    @JsonProperty("atl_change_percentage")
    private double atlChangePercentage;

    @JsonProperty("atl_date")
    @JsonDeserialize(using = UtcToLocalDateTimeDeserializer.class)
    private LocalDateTime atlDate;

    @Transient
    @JsonProperty("roi")
    private Object roi;

    @JsonProperty("last_updated")
    @JsonDeserialize(using = UtcToLocalDateTimeDeserializer.class)
    private LocalDateTime lastUpdated;
}
