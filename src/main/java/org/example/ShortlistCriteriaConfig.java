package org.example;

public interface ShortlistCriteriaConfig {
    Long LEAST_ETF_VOLUME = 20000L;//ideally 20,000.. Greater is better means more liquidity
    Long LEAST_BSE_ETF_VOLUME = 20000L;//ideally 20,000.. Greater is better means more liquidity
    Double MAX_LTP_TO_YRLOW_DIFF_PER_D = 12D;//Lesser is better(means LTP is closer to yr low)...Ideally LTP should not be more than 12% Yearly low value. i.e. LTP should be close to Yearly low value
    Double LEAST_YearHighToYearLowDiffPer_D = 10D;//Greater is better means more expected variation... Ideally At least 10% Yearly change in yr high to yr lowld be close to Yearly low value
    Double LEAST_YearHighToLatestDiffPer_D = 4D;//Greater is better means not buying closer to year high...Ideally At least yr high should be 4% more than cur value
    Double LEAST_NavToMarketLtPDeltaPercent_D = 0D;//Greater is better(means NAV > LTP)... Ideally NAV should not be less than LTP.. Ideal value 0D..
}
