package com.superfact.inventory.service.Auth;

import com.superfact.inventory.model.dto.globales.UserInfo;
import com.superfact.inventory.model.dto.users.UserAuthDto;
import com.superfact.inventory.model.entity.Tenant.Tenant;
import com.superfact.inventory.model.entity.users.User;
import com.superfact.inventory.repository.tenant.TenantRepository;
import com.superfact.inventory.repository.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient userInfoClient;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final JdbcTemplate jdbcTemplate;

    public User Login(String token){
        Mono<UserInfo> userInfoMono = userInfoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(UserInfo.class);
        Optional<User> usuarioOptional = userRepository.findByEmail(userInfoMono.block().email());
        User usuario ;
        if(usuarioOptional.isEmpty()){
            usuario = Save(userInfoMono.block(),"","",false);

        }else{
            usuario = usuarioOptional.get();
        }
        return usuario;
    }
    private User Save(UserInfo userinfo, String tenantName,String tenantId,boolean isRegist) {
        User nuevo = User.builder()
                .sub(userinfo.sub())
                .name(userinfo.name())
                .given_name(userinfo.given_name())
                .family_name(userinfo.family_name())
                .picture(userinfo.picture())
                .email(userinfo.email())
                .email_verified(userinfo.email_verified())
                .locale(userinfo.locale())
                .tenantName(tenantName)
                .tenantId(tenantId)
                .Regist(isRegist)
                .build();
        User regist = userRepository.save(nuevo);
        return regist;
    }
    public boolean IsExistUserByToken(String token){
        Mono<UserInfo> userInfoMono = userInfoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(UserInfo.class);
        Optional<User> usuarioOptional = userRepository.findByEmail(userInfoMono.block().email());
        return usuarioOptional.isPresent();
    }
    public boolean IsExistUserByEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }


    public User Regist(User usuario) {
        Tenant TenantNuevo = tenantRepository.save(Tenant.builder().name(usuario.getTenantName()).build());
        usuario.setTenantId(TenantNuevo.getId());
        usuario.setRegist(true);

        switch (usuario.getTiponegocio()){
            case "electronica": executeSqlWithTenantId(TenantNuevo.getId(),"electronica.sql"); break;
            case "textil": executeSqlWithTenantId(TenantNuevo.getId(),"textil.sql"); break;
        }

        return userRepository.save(usuario);
    }
    private void executeSqlWithTenantId(String tenantId, String pathSql) {
        try {
            // Leer el archivo SQL
            ClassPathResource resource = new ClassPathResource(pathSql);
            String sql = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Reemplazar la variable tenantId en el SQL
            sql = sql.replace("[tenantId]", tenantId);

            // Ejecutar el SQL
            for (String query : sql.split(";")) {
                if (!query.trim().isEmpty()) {
                    if (query.contains("[uuid]")) {
                        String uuid = UUID.randomUUID().toString();
                        query = query.replace("[uuid]", uuid);
                    }
                    jdbcTemplate.execute(query.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing SQL script", e);
        }
    }
}
