package com.sasindu.springsecurity.services;


import com.sasindu.springsecurity.abstractions.dto.request.auth.LoginRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.RegisterUserRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.ResetPasswordRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.VerifyEmailRequestDto;
import com.sasindu.springsecurity.abstractions.enums.AppUserRoles;
import com.sasindu.springsecurity.abstractions.interfaces.IAuthService;
import com.sasindu.springsecurity.entities.AppUser;
import com.sasindu.springsecurity.exceptions.BadRequestException;
import com.sasindu.springsecurity.exceptions.ForbiddenException;
import com.sasindu.springsecurity.exceptions.UnAuthorizedException;
import com.sasindu.springsecurity.helpers.HelperUtilMethods;
import com.sasindu.springsecurity.repository.IUserRepository;
import com.sasindu.springsecurity.security.jwt.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * The Auth service - handles all authentication related operations
 */
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final IUserRepository _userRepository;
    private final JWTUtils _jwtUtils;
    private final PasswordEncoder _passwordEncoder;
    private final AuthenticationManager _authenticationManager;

    @Value("${application.environment}")
    String environment;

    @Value("${jwt.access.expiration.minutes}")
    String jwtAccessExpireStringMinutes;

    @Value("${jwt.refresh.expiration.days}")
    String refreshTokenExpireStringDays;


    /**
     * Set the cookies - re-usable private method
     *
     * @param access The access token
     * @param refresh The refresh token
     * @param response The response object
     * @param accessMaxAge The access token max age
     * @param refreshMaxAge The refresh token max age
     */
    private void setCookies(String access, String refresh, HttpServletResponse response, int accessMaxAge, int refreshMaxAge) {
        try {
            Cookie accessCookie = new Cookie("access", access);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(environment.equals("production"));
            accessCookie.setPath("/");
            accessCookie.setMaxAge(accessMaxAge);

            Cookie refreshCookie = new Cookie("refresh", refresh);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(environment.equals("production"));
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(refreshMaxAge);

            // Add the cookies to the response
            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Register a new user
     *
     * @param request The request object
     * @return The registered user
     */
    @Override
    public AppUser register(RegisterUserRequestDto request) {
        try{
            AppUser user = new AppUser();

            if(_userRepository.existsByEmail(request.getEmail())){
                throw new BadRequestException("Email is already taken");
            }

            if(!Objects.equals(request.getPassword(), request.getConfirmPassword())){
                throw new BadRequestException("Passwords do not match");
            }

            if(!HelperUtilMethods.isPasswordStrong(request.getPassword())){
                throw new BadRequestException("Password is not strong enough");
            }

            user.setPassword(_passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setRole(AppUserRoles.ROLE_USER.toString());
            return _userRepository.save(user);
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Login a user - set Cookies in the response
     *
     * @param request The request object
     * @throws UnAuthorizedException If the credentials are invalid
     * @throws RuntimeException If an error occurs
     */
    @Override
    public void login(LoginRequestDto request, HttpServletResponse response) {
        try{
            Authentication auth = _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Set the authentication object to the security context
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Get authenticated user directly from auth object
            AppUser user = (AppUser) auth.getPrincipal();

            String access = _jwtUtils.generateAccessToken(user);
            String refresh = _jwtUtils.generateRefreshToken(user);

            setCookies(
                    access,
                    refresh,
                    response,
                    Integer.parseInt(jwtAccessExpireStringMinutes) * 60,
                    Integer.parseInt(refreshTokenExpireStringDays) * 60 * 60 * 24
            );
        }
        catch(InternalAuthenticationServiceException | BadCredentialsException e){
            throw new UnAuthorizedException("Invalid credentials");
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Get the authenticated user
     *
     * @return The authenticated user
     */
    @Override
    public AppUser getAuthenticatedUser() {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return (AppUser) auth.getPrincipal();
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Refresh the access token
     *
     * @param request The request object
     * @param response The response object
     */
    @Override
    public void refreshToken(HttpServletRequest request,  HttpServletResponse response) {
        try{
            String refresh = HelperUtilMethods.getCookieFromRequest(request, "refresh");

            if(!_jwtUtils.isRefreshTokenValid(refresh)){
                throw new ForbiddenException("Invalid refresh token");
            }

            Long userId = _jwtUtils.getUserIdFromRefreshToken(refresh);
            AppUser user = _userRepository.findById(userId)
                    .orElseThrow(() -> new ForbiddenException("Invalid refresh token"));
            String access = _jwtUtils.generateAccessToken(user);
            String newRefresh = _jwtUtils.generateRefreshToken(user);

            setCookies(
                    access,
                    newRefresh,
                    response,
                    Integer.parseInt(jwtAccessExpireStringMinutes) * 60,
                    Integer.parseInt(refreshTokenExpireStringDays) * 60 * 60 * 24
            );
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Logout - Clear the cookies in the response
     *
     * @param response The response object
     */
    @Override
    public void logout(HttpServletResponse response) {
        try{
            setCookies(
                    null,
                    null,
                    response,
                    0,
                    0
            );
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailVerificationOtpEmail(String email) {

    }

    @Override
    public void sendPasswordResetOtpEmail(String email) {

    }

    @Override
    public void verifyEmail(VerifyEmailRequestDto request) {

    }

    @Override
    public void resetPassword(ResetPasswordRequestDto request) {

    }
}
