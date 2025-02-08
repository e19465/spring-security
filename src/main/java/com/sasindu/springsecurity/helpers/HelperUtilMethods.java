package com.sasindu.springsecurity.helpers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class HelperUtilMethods {

    /**
     * This public static method is used to convert a hex string to a byte array
     *
     * @param hex The hex string to be converted
     * @return The byte array
     */
    public static byte[] hexStringToByteArray(String hex) {
        int length = hex.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }


    /**
     * This method is used to check if a password is strong
     * @param password The password to be checked
     * @return True if the password is strong, false otherwise
     */
    public static boolean isPasswordStrong(String password) {
        // At least 8 characters
        // At least 1 digit
        // At least 1 lower case letter
        // At least 1 upper case letter
        // At least 1 special character
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
    }



    /**
     * This method is used to get the cookies from a request
     * @param request The request object
     * @return The cookies as a map
     */
    public static String getCookieFromRequest(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }
}
