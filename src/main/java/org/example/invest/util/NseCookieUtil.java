package org.example.invest.util;

/**
 * Utility class for NSE API cookie management
 * This class provides methods to handle NSE API cookies
 */
public class NseCookieUtil {
    
    /**
     * Default cookie value for NSE API
     * Note: In production, you should obtain this cookie dynamically from NSE website
     */
    private static final String DEFAULT_COOKIE = "_abck=8897CF1F52971CB1FF4DDB1EE57E686E~-1~YAAQL63OF84AK02ZAQAAx/qijw5jYn2Cc7FeMQDiX3s7PueSn1qDm+MY96hXiAAyGsnUaLgE7f4KyEF0Uc8kV0MXgxi7fimyvKixgFuKziQqu1ewlTu6Zne4oyRwMQUhvPAgUgHhJ7Y1R7u05kwGrB4MJUhIjuneEOx1l7TFYV5TT9QdDiy4Qh7YIOhwliJYY/xQHvmp04EGOT0wuIO4KlYZzC2wbPsJEyerfEFO4nuVPwk3SHSv4BKh3rYpXorJO17Xs8L1XGBR33tHdFt9N9daY8n3j/v6SabzBNuYblrDBEJE2NrwNJLC2H6dDRdtVAMGJHcgNqOkmqF+3eewmqSeYrfhNV6ofCk+5JQSIl62BcZn5BZzysdYiw0dUKR76oJHwGBuKx6wleOHT3PKvxCQid3fibauDpmFMmUgao+dZMUIBpkE5uYnw1kvRRN2aD7bMZa2Ig==~-1~-1~-1~-1~-1; bm_sz=703D13E585B14DDE29B7F72ECA8FA2C5~YAAQL63OF88AK02ZAQAAx/qijx3mvfyjigQ4jqUf7+K8hL6UKCxbnsbUtujm9Hgh5CfCPSKxqV5HKLTg+6btUA/9sDFneVieqd1eEH/+84zhJUwkbK+GoJPOZla1tzQUerKdNLv1oEVBfOIhjTnznChhhPObox+lwrKvuTqc8FeeT7pRYunjixzL0Rk6ogOWZvlo+MKw4Q983obiJxX054of3JcxbbRDRCHbgyEL6Hr0QVexCu5gq12tM8dxJl2K5dH8o5HXur/SL0Uekjs47jMDqIGRAYhb4HkHYbAYleVKHbLGQCHEDlf2eKXnj6MR5YODr/ECgEI5Qj/+CUk4Tp6QTHT/nEEg64E4cVhu~4539700~3158324";
    
    /**
     * Get the default cookie for NSE API
     * @return Default cookie string
     */
    public static String getDefaultCookie() {
        return DEFAULT_COOKIE;
    }
    
    /**
     * Validate if a cookie string is not null or empty
     * @param cookie Cookie string to validate
     * @return true if cookie is valid, false otherwise
     */
    public static boolean isValidCookie(String cookie) {
        return cookie != null && !cookie.trim().isEmpty();
    }
    
    /**
     * Get cookie or return default if provided cookie is invalid
     * @param cookie Cookie string to check
     * @return Valid cookie string
     */
    public static String getCookieOrDefault(String cookie) {
        return isValidCookie(cookie) ? cookie : getDefaultCookie();
    }
}

