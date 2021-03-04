package de.neuefische.githubbingomaster.security;

import de.neuefische.githubbingomaster.service.TimeUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilsTest {

    private TimeUtils timeUtils = mock(TimeUtils.class);
    private JwtConfig jwtConfig = mock(JwtConfig.class);
    private JwtUtils jwtUtils = new JwtUtils(timeUtils, jwtConfig);

    @Test
    public void generateJwtToken(){
        //GIVEN
        Instant now = Instant.ofEpochSecond(Instant.now().getEpochSecond());
        when(timeUtils.now()).thenReturn(now);
        String secret = "super-secret";
        when(jwtConfig.getJwtSecret()).thenReturn(secret);

        //WHEN
        String token = jwtUtils.createToken("jan", new HashMap<>(Map.of("data", "value")));

        //THEN
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        assertThat(claims.getSubject(), Matchers.is("jan"));
        assertThat(claims.getIssuedAt(), Matchers.is(Date.from(now)));
        assertThat(claims.getExpiration(), Matchers.is(Date.from(now.plus(Duration.ofHours(4)))));
        assertThat(claims.get("data"), Matchers.is("value"));
    }

}
