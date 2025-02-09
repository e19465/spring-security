package com.sasindu.springsecurity.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ApplicationConstants {

    //! EMAIL SENDING CONFIGURATION
    public static final String EMAIL_VERIFY_TEMPLATE = "emailVerifyTemplate";
    public static final String PASSWORD_RESET_TEMPLATE = "passwordResetTemplate";
    public static final int EMAIL_OTP_EXPIRATION_MINUTES = 30;
    public static final int PASSWORD_OTP_EXPIRATION_MINUTES = 30;

    //! Configure Public URLs
    private static final String[] PUBLIC_APPLICATION_URLS = new String[]{
            "/static/**",
            "/favicon.ico",
            "/error",
            "/webjars/**",
    };
    private static final String[] PUBLIC_API_SHARED_URLS = new String[]{
            "/api/v1/auth/**",
            "/api/v1/public/**",
    };
    private static final String[] PUBLIC_API_CATEGORY_URLS = new String[]{
            "/api/v1/category/get-by-id/**",
            "/api/v1/category/get-all/**",
    };
    private static final String[] PUBLIC_API_PRODUCT_URLS = new String[]{
            "/api/v1/product/get-by-id/**",
            "/api/v1/product/get-all/**",
    };
    // Combine multiple arrays into one
    public static final String[] PUBLIC_URLS = combineArrays(
            PUBLIC_APPLICATION_URLS,
            PUBLIC_API_SHARED_URLS,
            PUBLIC_API_CATEGORY_URLS,
            PUBLIC_API_PRODUCT_URLS
    );


    //!  CORS Configuration
    private static String[] CORS_ALLOWED_ORIGINS;
    private static String[] CORS_ALLOWED_METHODS;
    private static String[] CORS_ALLOWED_HEADERS;
    private static boolean CORS_ALLOW_CREDENTIALS;

    // Method to combine multiple arrays into one
    private static String[] combineArrays(String[]... arrays) {
        // Calculate the total length of all arrays
        int totalLength = 0;
        for (String[] array : arrays) {
            totalLength += array.length;
        }

        // Create a new array to hold all elements
        String[] combined = new String[totalLength];
        int currentIndex = 0;

        // Copy each array into the combined array
        for (String[] array : arrays) {
            System.arraycopy(array, 0, combined, currentIndex, array.length);
            currentIndex += array.length;
        }

        return combined;
    }

    public static String[] getCorsAllowedOrigins() {
        return CORS_ALLOWED_ORIGINS;
    }

    @Value("${cors.allowed.origins}")
    public void setCorsAllowedOrigins(String corsAllowedOriginsString) {
        CORS_ALLOWED_ORIGINS = corsAllowedOriginsString.split(",");
    }

    public static String[] getCorsAllowedMethods() {
        return CORS_ALLOWED_METHODS;
    }

    @Value("${cors.allowed.methods}")
    public void setCorsAllowedMethods(String corsAllowedMethodsString) {
        CORS_ALLOWED_METHODS = corsAllowedMethodsString.split(",");
    }

    public static String[] getCorsAllowedHeaders() {
        return CORS_ALLOWED_HEADERS;
    }

    @Value("${cors.allowed.headers}")
    public void setCorsAllowedHeaders(String corsAllowedHeadersString) {
        CORS_ALLOWED_HEADERS = corsAllowedHeadersString.split(",");
    }

    public static boolean isCorsAllowCredentials() {
        return CORS_ALLOW_CREDENTIALS;
    }

    @Value("${cors.allow.credentials}")
    public void setCorsAllowCredentials(String corsAllowCredentialsString) {
        CORS_ALLOW_CREDENTIALS = corsAllowCredentialsString.equals("true");
    }
}
