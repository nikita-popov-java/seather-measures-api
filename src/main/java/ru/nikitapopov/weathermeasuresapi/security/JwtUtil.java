package ru.nikitapopov.weathermeasuresapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nikitapopov.weathermeasuresapi.utils.Authority;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt-subject}")
    private String JWT_SUBJECT;
    @Value("${jwt-claim.username}")
    private String JWT_CLAIM_USERNAME;
    @Value("${jwt-issuer}")
    private String JWT_ISSUER;
    @Value("${jwt-secret}")
    private String JWT_SECRET;
    @Value("${jwt-living-time}")
    private int JWT_LIVING_TIME;

    public String createToken(String username) {
        Date expiredDate = Date.from(ZonedDateTime.now().toInstant().plusSeconds(JWT_LIVING_TIME));

        return JWT.create()
                .withSubject(JWT_SUBJECT)
                .withClaim(JWT_CLAIM_USERNAME, username)
                .withIssuer(JWT_ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(expiredDate)
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }

    public String retrieveUsernameClaimFromToken(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = decodeJwtToken(token);
        return decodedJWT.getClaim(JWT_CLAIM_USERNAME).asString();
    }

    public DecodedJWT decodeJwtToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(JWT_SECRET))
                .withSubject(JWT_SUBJECT)
                .withIssuer(JWT_ISSUER)
                .build()
                .verify(token);
    }
}
