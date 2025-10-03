package org.example.invest.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Test class to verify BSE API access using RestTemplate
 */
@Component
public class BseApiTest {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Test BSE API access using RestTemplate
     */
    public void testBseApiAccess() {
        try {
            String url = "https://api.bseindia.com/BseIndiaAPI/api/MktCapBoard_indstream/w?cat=2&type=2";

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            headers.set("Accept", "*/*");
            headers.set("Referer", "https://www.bseindia.com/");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            System.out.println("BSE API Test - Status: " + response.getStatusCode());
            System.out.println("BSE API Test - Response: " + response.getBody().substring(0, Math.min(200, response.getBody().length())) + "...");

        } catch (Exception e) {
            System.err.println("BSE API Test - Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
