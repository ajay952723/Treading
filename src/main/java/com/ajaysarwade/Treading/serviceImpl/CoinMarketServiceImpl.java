package com.ajaysarwade.Treading.serviceImpl;

import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.repository.CoinMarketServiceRepository;
import com.ajaysarwade.Treading.service.CoinMarketService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class CoinMarketServiceImpl implements CoinMarketService {

    @Autowired
    private CoinMarketServiceRepository coinMarketServiceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<CoinMarketData> getCoinList(int page) {
        String url = "https://api.coingecko.com/api/v3/coins/markets"
                   + "?vs_currency=usd&order=market_cap_desc&per_page=10&page=" + page;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String responseBody = responseEntity.getBody();

            List<CoinMarketData> coins = objectMapper.readValue(responseBody, new TypeReference<List<CoinMarketData>>() {});

            // Save new coins to DB only if not already present
            for (CoinMarketData coin : coins) {
                if (!coinMarketServiceRepository.existsById(coin.getId())) {
                    coinMarketServiceRepository.save(coin);
                }
            }

            return coins;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error fetching coin data: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }


    @Override
    public String getMarketChart(String coinId, int days) {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error fetching market chart: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public String getCoinsDetails(String coinId) {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            CoinMarketData marketData = new CoinMarketData();
            marketData.setId(jsonNode.get("id").asText());
            marketData.setName(jsonNode.get("name").asText());
            marketData.setSymbol(jsonNode.get("symbol").asText());
            marketData.setImage(jsonNode.get("image").get("large").asText());

            JsonNode marketData2 = jsonNode.get("market_data");

            marketData.setCurrentPrice(marketData2.get("current_price").get("usd").asDouble());
            marketData.setMarketCap(marketData2.get("market_cap").get("usd").asLong());
            marketData.setMarketCapRank(marketData2.get("market_cap_rank").asInt());
            marketData.setTotalVolume(marketData2.get("total_volume").get("usd").asLong());
            marketData.setHigh24h(marketData2.get("high_24h").get("usd").asDouble());
            marketData.setLow24h(marketData2.get("low_24h").get("usd").asDouble());
            marketData.setPriceChange24h(marketData2.get("price_change_24h").asDouble());
            marketData.setPriceChangePercentage24h(marketData2.get("price_change_percentage_24h").asDouble());
            marketData.setMarketCapChange24h(marketData2.get("market_cap_change_24h").asLong());
            marketData.setMarketCapChangePercentage24h(marketData2.get("market_cap_change_percentage_24h").asDouble());
            marketData.setTotalSupply(marketData2.get("total_supply").asDouble());
            marketData.setCirculatingSupply(marketData2.get("circulating_supply").asDouble());
            marketData.setMaxSupply(marketData2.get("max_supply").asDouble());

            coinMarketServiceRepository.save(marketData);

            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error fetching coin details: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public CoinMarketData findById(String coinId) throws Exception {
        Optional<CoinMarketData> optional = coinMarketServiceRepository.findById(coinId);
        if (optional.isEmpty()) {
            throw new Exception("Coin Not found");
        }
        return optional.get();
    }

    @Override
    public String searchCoin(String keyword) {
        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error searching for coin: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public String getTop50CoinsByMarketCapRank() {
        String url = "https://api.coingecko.com/api/v3/coins/markets"
                   + "?vs_currency=usd&order=market_cap_desc&per_page=50&page=1";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error fetching top 50 coins: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public String getTrendingCoins() {
        String url = "https://api.coingecko.com/api/v3/search/trending";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Error fetching trending coins: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }
}
