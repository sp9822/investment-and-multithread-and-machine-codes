package org.example.invest.client;

import lombok.extern.slf4j.Slf4j;
import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class BseClient {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Get market cap board data for a specific category with custom type
     *
     * @param category Category number (1-5)
     * @param type     Type parameter
     * @return BSE market cap board response
     */
    public BseMktCapBoardResponse getMktCapBoardData(int category, int type) {
        try {
            String url = "https://api.bseindia.com/BseIndiaAPI/api/MktCapBoard_indstream/w?cat=" + category + "&type=" + type;

            HttpHeaders headers = createBseHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<BseMktCapBoardResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    BseMktCapBoardResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching BSE market cap board data for category " + category + " and type " + type + ": " + e.getMessage(), e);
        }
    }

    /**
     * Create headers for BSE API requests
     *
     * @return HttpHeaders with BSE-specific headers
     */
    private HttpHeaders createBseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        headers.set("Accept", "*/*");
        headers.set("Referer", "https://www.bseindia.com/");
        return headers;
    }
}
