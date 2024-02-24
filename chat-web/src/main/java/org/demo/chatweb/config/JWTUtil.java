package org.demo.chatweb.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.time.ZonedDateTime;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;
    private final UserDetailService userDetailService;

    @Autowired
    public JWTUtil(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }


    public String generateToken(String username, String role, String email)
    {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(300).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withClaim("role", role)
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("Spring API")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException
    {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("Spring API")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }


    public String filterAndRetrieveClaimSpecificToken(String authHeader) throws ServletException {

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                throw new ServletException("Invalid JWT Token: Token is empty");
            } else {
                try {
                    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                            .withSubject("User details")
                            .withIssuer("Spring API")
                            .build();

                    DecodedJWT jwToken = verifier.verify(jwt);

                    String username =jwToken.getClaim("username").asString();
                    return username;

                } catch (JWTVerificationException exc) {
                    throw new ServletException("Invalid JWT Token: Verification failed", exc);
                }
            }
        } else {
            throw new ServletException("Invalid JWT Token: Token format is not valid");
        }
    }





}
