package com.sasindu.springsecurity.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sasindu.springsecurity.helpers.HelperUtilMethods;
import com.sasindu.springsecurity.security.services.AppUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AppUserDetailsService userDetailsService;

    // Use this approach if you want to change header based or cookie based JWT
    //    @Value("${jwt.type.cookie.based}")
    //    private String isCookieBased;


    /**
     * Filter to authenticate requests - overridden method from OncePerRequestFilter
     *
     * @param request The request object
     * @param response The response object
     * @param filterChain The filter chain object
     * @throws ServletException If an error occurs
     * @throws IOException If an error occurs
     */
    @Override
    protected void doFilterInternal(
            @Nullable HttpServletRequest request,
            @Nullable HttpServletResponse response,
            @Nullable FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (request == null || response == null || filterChain == null) {
                throw new RuntimeException("Request, response or filter chain is null");
            }

            String token = parseJwt(request);

            if(!StringUtils.hasText(token) || !jwtUtils.isAccessTokenValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtUtils.getEmailFromAccessToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            assert response != null;
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Construct JSON response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", null);
            errorResponse.put("error", "Access Denied");
            errorResponse.put("data", null);

            // Convert to JSON and write response
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Get JWT from the request
     *
     * @param request the request object
     * @return JWT token
     */
    private String parseJwt(HttpServletRequest request) {
    //        boolean isCookieBased = this.isCookieBased.equals("true");
    //        if (isCookieBased) {
    //            return getAccessTokenFromCookie(request);
    //        } else{
    //            return getAccessTokenFromHeader(request);
    //        }
        return getAccessTokenFromCookie(request);
    }


    // FOR HEADER BASED JWT
    //    /**
    //     * Get JWT from the header
    //     *
    //     * @param request the request object
    //     * @return JWT token
    //     */
    //    private String getAccessTokenFromHeader(HttpServletRequest request) {
    //        boolean isCookieBased = this.isCookieBased.equals("true");
    //        String headerAuth = request.getHeader("Authorization");
    //
    //        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
    //            return headerAuth.substring(7);
    //        }
    //
    //        return null;
    //    }


    /**
     * Get JWT from the cookie
     *
     * @param request the request object
     * @return JWT token
     */
    private String getAccessTokenFromCookie(HttpServletRequest request) {
        return HelperUtilMethods.getCookieFromRequest(request, "access");
    }
}
