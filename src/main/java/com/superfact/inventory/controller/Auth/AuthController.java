package com.superfact.inventory.controller.Auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.superfact.inventory.model.dto.globales.MessageDto;
import com.superfact.inventory.model.dto.globales.TokenDto;
import com.superfact.inventory.model.dto.globales.UrlDto;
import com.superfact.inventory.model.dto.globales.UserInfo;
import com.superfact.inventory.model.entity.users.User;
import com.superfact.inventory.service.Auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class AuthController {
    @Value("${spring.security.oauth2.resourceserver.opaque-token.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.clientSecret}")
    private String clientSecret;

    private final WebClient userInfoClient;
    private final AuthService authService;
    @GetMapping("/auth/url")
    public ResponseEntity<UrlDto> auth(){
        String url = new GoogleAuthorizationCodeRequestUrl(
                clientId, "http://paulyeffertperezsanjinez.link",
                Arrays.asList("email","profile", "openid")).build();
        return ResponseEntity.ok(new UrlDto(url));
    }

    @GetMapping("/auth/callback")
    public ResponseEntity<TokenDto> callback(@RequestParam("code") String code) throws URISyntaxException {
        String token;
        try {
            token = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(), new GsonFactory(), clientId, clientSecret, code, "http://paulyeffertperezsanjinez.link"
            ).execute().getAccessToken();
        }catch (IOException e){
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new TokenDto(token));
    }

//    @GetMapping("/messages")
//    public ResponseEntity<MessageDto> privateMessages(@AuthenticationPrincipal(expression = "name") String name) {
//
//        return ResponseEntity.ok(new MessageDto("private content " + name));
//
//    }
    @GetMapping("/messages")
    public ResponseEntity<MessageDto> privateMessages(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer " prefix

        Mono<UserInfo> user = userInfoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(UserInfo.class);

        return ResponseEntity.ok(new MessageDto("El email autenticado es: " + user.block().email_verified()));

    }
    @GetMapping("/userinfo")
    public Mono<UserInfo> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        // Extract the token from the Authorization header
        String token = authHeader.substring(7); // Remove "Bearer " prefix

        return userInfoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(UserInfo.class);
    }

    @GetMapping("/login")
    public User getLogin(@RequestHeader("Authorization") String authHeader) {
        // Extract the token from the Authorization header
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        return authService.Login(token);
    }

    @GetMapping("/regist")
    public User getRegist(@RequestBody User usuario) {
        // Extract the token from the Authorization header// Remove "Bearer " prefix
        return authService.Regist(usuario);
    }
}
