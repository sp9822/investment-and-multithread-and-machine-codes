package org.example.invest.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.invest.dto.bse.index.BseIndexFundamentals;
import org.example.invest.dto.bse.index.BseMktCapBoardResponse;
import org.example.invest.dto.bse.etf.BseEtfResponse;
import org.example.invest.dto.bse.etf.BseEtfData;
import org.example.invest.dto.bse.index.BseRealTimeData;
import org.example.invest.util.GeneralUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BseClient {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GeneralUtil generalUtil;

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
     * Get ETF data from BSE ETF market watch page
     * This method scrapes the BSE ETF market watch page to retrieve ETF data
     *
     * @return BSE ETF response containing all ETF data
     */
    public BseEtfResponse getEtfData() {
        try {
            String url = "https://www.bseindia.com/markets/etf/ETF_MktWatch.aspx";

            HttpHeaders headers = createBseHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // Parse the HTML response to extract ETF data
            return parseEtfDataFromHtml(response.getBody());
        } catch (Exception e) {
            log.error("Error fetching BSE ETF data: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching BSE ETF data: " + e.getMessage(), e);
        }
    }

    /**
     * Parse ETF data from HTML response
     * This method extracts ETF data from the BSE ETF market watch page HTML
     *
     * @param htmlResponse HTML response from BSE ETF page
     * @return Parsed BSE ETF response
     */
    private BseEtfResponse parseEtfDataFromHtml(String htmlResponse) {
        try {
            Document doc = Jsoup.parse(htmlResponse);
            List<BseEtfData> etfDataList = new ArrayList<>();

            // Look for ETF data table - BSE typically uses specific table structures
            Elements tables = doc.select("table");

            for (Element table : tables) {
                Elements rows = table.select("tr");

                // Skip header row
                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cells = row.select("td");

                    if (cells.size() >= 10) { // Ensure we have enough columns
                        BseEtfData etfData = parseEtfRow(cells);
                        if (etfData != null && !etfDataList.contains(etfData)) {
                            etfDataList.add(etfData);
                        }
                    }
                }
            }

            // If no data found in tables, try alternative selectors
            if (etfDataList.isEmpty()) {
                etfDataList = parseAlternativeFormat(doc);
            }

            BseEtfResponse response = new BseEtfResponse(java.time.Instant.now().toString()
                    , etfDataList
                    , etfDataList == null ? 0 : etfDataList.size()
                    , ((int) (etfDataList != null ? etfDataList.stream().filter(etf -> etf.getChange() != null && etf.getChange() > 0).count() : 0))
                    , ((int) (etfDataList != null ? etfDataList.stream().filter(etf -> etf.getChange() != null && etf.getChange() < 0).count() : 0))
                    , ((int) (etfDataList != null ? etfDataList.stream().filter(etf -> etf.getChange() != null && etf.getChange() == 0).count() : 0))
                    , (etfDataList != null ? etfDataList.stream().mapToDouble(etf -> etf.getTurnover() != null ? etf.getTurnover() : 0.0).sum() : 0.0)
                    , (etfDataList != null ? etfDataList.stream().mapToLong(etf -> etf.getVolume() != null ? etf.getVolume() : 0L).sum() : 0L)
                    , (etfDataList != null ? etfDataList.stream().mapToDouble(etf -> etf.getMarketCap() != null ? etf.getMarketCap() : 0.0).sum() : 0.0)
                    , "Don't Know"
                    , java.time.Instant.now().toString()
                    , "BSE"
                    , true
                    , "ETF data retrieved successfully"
                    , "NA");

            return response;
        } catch (Exception e) {
            log.error("Error parsing ETF data from HTML: {}", e.getMessage(), e);
            throw new RuntimeException("Error parsing ETF data from HTML: " + e.getMessage(), e);
        }
    }

    /**
     * Parse a single ETF row from table cells
     *
     * @param cells Table cells containing ETF data
     * @return Parsed ETF data or null if parsing fails
     */
    private BseEtfData parseEtfRow(Elements cells) {
        /*
                                                                    <tr class="tdcolumn">
                                                                        <td class="TTRow_left">
                                                                            <a class="tablebluelink10" href="https://www.bseindia.com/stock-share-price/nippon-india-etf-nifty-1d-rate-liquid-bees/liquidbees/590096/" target="_blank">LIQUIDBEES</a>
                                                                        </td>
                                                                        <td class="TTRow_right">1000.01</td>
                                                                        <td class="TTRow_right">0.00</td>
                                                                        <td class="TTRow_right">1000.01/999.99</td>
                                                                        <td class="TTRow_right">1004.99/994.00</td>
                                                                        <td class="TTRow_right">1000.01/999.99</td>
                                                                        <td class="TTRow_right">1000.01</td>
                                                                        <td class="TTRow_right">752623</td>
                                                                        <td class="TTRow_right">7526.27</td>
                                                                        <td class="TTRow_right">1100.01/900.01</td>
                                                                    </tr>
         */

        try {
            BseEtfData etfData = new BseEtfData();

            // Map cells to ETF data fields based on BSE ETF table structure
            // This is a generic mapping - you may need to adjust based on actual BSE page structure

            if (cells.size() >= 1) {
                Element scriptElement = cells.get(0);
                etfData.setScripCode(scriptElement.text().trim());
                etfData.setProcessedScripCode(generalUtil.removeHypnUnderScorSpcSecIndAndGetLowerCase(etfData.getScripCode()));

                etfData.setScripName(getScripName(scriptElement));
                etfData.setProcessedScripName(generalUtil.removeHypnUnderScorSpcSecIndAndGetLowerCase(etfData.getScripName()));
            }
            if (cells.size() >= 2) {
                etfData.setLtp(parseDouble(cells.get(1).text()));
            }
            if (cells.size() >= 3) {
                etfData.setChange(parseDouble(cells.get(2).text()));
            }
            if (cells.size() >= 4) {
                String[] dayHighLowArr = cells.get(3).text().split("/");
                if (dayHighLowArr.length >= 2) {
                    etfData.setHigh(parseDouble(dayHighLowArr[0]));
                    etfData.setLow(parseDouble(dayHighLowArr[1]));
                }
            }
            if (cells.size() >= 5) {
                String[] yearHighLowArr = cells.get(4).text().split("/");
                if (yearHighLowArr.length >= 2) {
                    etfData.setWeek52High(parseDouble(yearHighLowArr[0]));
                    etfData.setWeek52Low(parseDouble(yearHighLowArr[1]));
                }
            }
            if (cells.size() >= 6) {
                String[] prevCloseOpenArr = cells.get(5).text().split("/");
                if (prevCloseOpenArr.length >= 2) {
                    etfData.setPrevClose(parseDouble(prevCloseOpenArr[0]));
                    etfData.setOpen(parseDouble(prevCloseOpenArr[1]));
                }
            }
            if (cells.size() >= 7) {
                etfData.setWtAvgPrice(parseDouble(cells.get(6).text()));
            }
            if (cells.size() >= 8) {
                etfData.setVolume(parseLong(cells.get(7).text()));
            }
            if (cells.size() >= 9) {
                Double turnover = parseDouble(cells.get(8).text());//inLacs
                if (turnover != null) {
                    turnover *= 100000D;
                    etfData.setTurnover(turnover);
                }
            }
            if (cells.size() >= 10) {
                String[] circuitLimitsArr = cells.get(9).text().split("/");
                if (circuitLimitsArr.length >= 2) {
                    etfData.setCircuit(parseDouble(circuitLimitsArr[0]));
                    etfData.setLimits(parseDouble(circuitLimitsArr[1]));
                }
            }

            // Set default values for required fields
            etfData.setIsActive(true);
            etfData.setIsSuspended(false);
            etfData.setIsDelisted(false);

            return etfData;
        } catch (Exception e) {
            log.warn("Failed to parse ETF row: {}", e.getMessage());
            return null;
        }
    }

    private String getScripName(Element scriptElement) {
        /*
        <td class="TTRow_left">
            <a class="tablebluelink10" href="https://www.bseindia.com/stock-share-price/nippon-india-etf-nifty-1d-rate-liquid-bees/liquidbees/590096/" target="_blank">LIQUIDBEES</a>
        </td>
         */
        Elements scripLink = scriptElement.select("a");
        if (scripLink != null && !scripLink.isEmpty()) {
            String scripLinkText = scripLink.get(0).attr("href");
            if (StringUtils.isNotEmpty(scripLinkText)) {
                scripLinkText = scripLinkText.replaceAll("https://www.bseindia.com/stock-share-price/", "");
                int firstSlashInd = scripLinkText.indexOf("/");
                if (firstSlashInd <= 0 || firstSlashInd > scripLinkText.length()) {
                    firstSlashInd = scripLinkText.length();
                }
                scripLinkText = scripLinkText.substring(0, firstSlashInd);
                scripLinkText = scripLinkText.trim();
                return scripLinkText;
            }
        }
        return null;
    }

    /**
     * Parse ETF data using alternative format selectors
     *
     * @param doc Parsed HTML document
     * @return List of parsed ETF data
     */
    private List<BseEtfData> parseAlternativeFormat(Document doc) {
        List<BseEtfData> etfDataList = new ArrayList<>();

        // Try to find ETF data in different formats
        // This is a fallback method for when the main table parsing doesn't work

        // Look for div elements with ETF data
        Elements etfDivs = doc.select("div[class*='etf'], div[class*='ETF']");

        for (Element div : etfDivs) {
            BseEtfData etfData = new BseEtfData();
            // Parse div content - this would need to be customized based on actual BSE page structure
            etfData.setIsActive(true);
            etfData.setIsSuspended(false);
            etfData.setIsDelisted(false);
            if (etfData != null && !etfDataList.contains(etfData)) {
                etfDataList.add(etfData);
            }
        }

        return etfDataList;
    }

    /**
     * Safely parse double value from string
     *
     * @param value String value to parse
     * @return Parsed double or null if parsing fails
     */
    private Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            // Remove common formatting characters
            String cleanValue = value.replaceAll("[,\\s]", "").replaceAll("[^\\d.-]", "");
            return Double.parseDouble(cleanValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Safely parse long value from string
     *
     * @param value String value to parse
     * @return Parsed long or null if parsing fails
     */
    private Long parseLong(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            // Remove common formatting characters
            String cleanValue = value.replaceAll("[,\\s]", "").replaceAll("[^\\d]", "");
            return Long.parseLong(cleanValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public BseRealTimeData setFundamentals(BseRealTimeData bseRealTimeData) {
        if (bseRealTimeData == null || bseRealTimeData.getScripFlagCode() == null) {
            return bseRealTimeData;
        }
        try {
            String url = "https://api.bseindia.com/BseIndiaAPI/api/MarketCap/w?code="
                    .concat(bseRealTimeData.getScripFlagCode().toString());

            HttpHeaders headers = createBseHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<BseIndexFundamentals>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new org.springframework.core.ParameterizedTypeReference<List<BseIndexFundamentals>>() {
                    }
            );

            if (response != null && response.getBody() != null && CollectionUtils.isNotEmpty(response.getBody())) {
                bseRealTimeData.setFundamentals(response.getBody().get(0));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching BSE index fundamentals for ScripFlagCode: " + bseRealTimeData.getScripFlagCode()
                    + ": " + e.getMessage(), e);
        }
        return bseRealTimeData;
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
