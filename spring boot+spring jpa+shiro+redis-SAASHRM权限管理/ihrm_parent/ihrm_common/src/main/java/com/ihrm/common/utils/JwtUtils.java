package com.ihrm.common.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt.config")
public class JwtUtils {


    private  String key;


    private  Long ttl;

    public  String createJwt(String id, String name, Map<String,Object> map)
    {

        long now=System.currentTimeMillis();
        long exp=ttl+now;
        JwtBuilder jwtBuilder = Jwts.builder().setId(id).setSubject(name).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key);
            for(Map.Entry<String,Object> entry:map.entrySet())
                jwtBuilder.claim(entry.getKey(),entry.getValue());
            jwtBuilder.setExpiration(new Date(exp));
        return jwtBuilder.compact();
    }


    public Claims parseJwt(String token)
    {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }
}
