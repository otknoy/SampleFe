package com.example.SampleFE;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ItemService {

    @Value("${item.api.host}")
    private String host;

    @Value("${item.api.port}")
    private int port;

    @HystrixCommand(fallbackMethod = "empty")
    public List<Item> getItems() {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http").host(host).port(port)
                .path("item")
                .build().encode().toUri();

        RestTemplate rt = new RestTemplate();
        ResponseEntity<Item[]> response = rt.getForEntity(uri, Item[].class);

        return Arrays.asList(response.getBody());
    }

    public List<Item> empty() {
        return Collections.emptyList();
    }
}
