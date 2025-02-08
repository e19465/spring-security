package com.sasindu.springsecurity.services;


import com.sasindu.springsecurity.abstractions.dto.request.user.AddUserRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.user.LoginRequestDto;
import com.sasindu.springsecurity.abstractions.enums.AppUserRoles;
import com.sasindu.springsecurity.abstractions.interfaces.IAuthService;
import com.sasindu.springsecurity.entities.AppUser;
import com.sasindu.springsecurity.exceptions.BadRequestException;
import com.sasindu.springsecurity.exceptions.UnAuthorizedException;
import com.sasindu.springsecurity.helpers.HelperUtilMethods;
import com.sasindu.springsecurity.repository.IUserRepository;
import com.sasindu.springsecurity.security.jwt.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final IUserRepository _userRepository;
    private final JWTUtils _jwtUtils;
    private final PasswordEncoder _passwordEncoder;
    private final AuthenticationManager _authenticationManager;


    /**
     * Register a new user
     *
     * @param request The request object
     * @return The registered user
     */
    @Override
    public AppUser register(AddUserRequestDto request) {
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
     * Login a user
     *
     * @param request The request object
     * @return The login response
     * @throws UnAuthorizedException If the credentials are invalid
     * @throws RuntimeException If an error occurs
     */
    @Override
    public Map<String, String> login(LoginRequestDto request) {
        try{
            Authentication auth = _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Set the authentication object to the security context
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Get authenticated user directly from auth object
            AppUser user = (AppUser) auth.getPrincipal();

            String access = _jwtUtils.generateAccessToken(user);
            String refresh = _jwtUtils.generateRefreshToken(user);

            return Map.of("access", access, "refresh", refresh);
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
}
