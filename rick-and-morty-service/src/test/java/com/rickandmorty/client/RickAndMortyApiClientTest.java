package com.rickandmorty.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class RickAndMortyApiClientTest {

    private RestClient restClient;
    private MockRestServiceServer server;
    private RickAndMortyApiClient client;

    @BeforeEach
    void setUp() {
        var builder = RestClient.builder().baseUrl("https://example.com/api");
        server = MockRestServiceServer.bindTo(builder).build();
        restClient = builder.build();
        client = new RickAndMortyApiClient(
                restClient,
                "/character",
                "page"
        );
    }

    @Test
    void getCharactersPage_should_call_rest_client_and_return_response() {
        var json = """
                {
                  "info": null,
                  "results": []
                }
                """;

        server.expect(requestTo("https://example.com/api/character?page=1"))
              .andExpect(method(org.springframework.http.HttpMethod.GET))
              .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        var result = client.getCharactersPage(1);

        assertEquals(0, result.getResults().size());

        server.verify();
    }
}
