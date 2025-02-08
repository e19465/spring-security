package com.sasindu.springsecurity.constants;

public class ApplicationConstants {

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

    // Combine multiple arrays into one
    public static final String[] PUBLIC_URLS = combineArrays(PUBLIC_APPLICATION_URLS, PUBLIC_API_SHARED_URLS);

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

}
