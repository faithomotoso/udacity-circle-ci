package com.udacity.timezones.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;

public class TimeZoneServiceTest {
    private static WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(9989));

    private static String serverPath = "http://localhost:9989";
    private static String timeZonePath = "/api/timezone/";

    private TimeZoneService timeZoneService;

    @BeforeAll
    static void initWireMock() {
        wireMockServer.start();
    }

    @AfterAll
    static void cleanup() {
        wireMockServer.stop();
    }

    @BeforeEach
    void beforeTest() {
        wireMockServer.resetAll();
        timeZoneService = new TimeZoneService(serverPath);
    }

    @Test
    void getAvailableTimezoneText_callsExternalApi_returnsTimeZones() {
        String area = "area51";

        String expected = "Canada, Nigeria, Austria, New Zealand";

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(timeZonePath + area)).willReturn(
                WireMock.aResponse().withStatus(200).withBody(
                        "[\"Canada\", \"Nigeria\", \"Austria\", \"New Zealand\"]"
                )
        ));

        String response = timeZoneService.getAvailableTimezoneText(area);
        Assertions.assertTrue(
                response.contains(expected)
        );
    }
}
