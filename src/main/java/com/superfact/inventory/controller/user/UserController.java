package com.superfact.inventory.controller.user;

import com.superfact.inventory.model.dto.globales.UserInfo;
import com.superfact.inventory.model.dto.users.RolResponse;
import com.superfact.inventory.model.dto.users.UserAuthDto;
import com.superfact.inventory.model.dto.users.UserResponse;
import com.superfact.inventory.model.entity.users.LogicaNegocioUser;
import com.superfact.inventory.model.entity.users.User;
import com.superfact.inventory.repository.users.UserRepository;
import com.superfact.inventory.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WebClient userInfoClient;
    private final UserRepository userRepository;

    @GetMapping("/regist")
    public UserResponse Regist(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String id,
            @RequestParam String sub,
            @RequestParam String name,
            @RequestParam String given_name,
            @RequestParam String family_name,
            @RequestParam String picture,
            @RequestParam String email,
            @RequestParam boolean email_verified,
            @RequestParam String locale,
            @RequestParam String password,
            @RequestParam String tenantId,
            @RequestParam String tenantName,
            @RequestParam boolean isRegist,
            @RequestParam String tiponegocio
    ){
        User user = User.builder()
                .id(id)
                .sub(sub)
                .name(name)
                .given_name(given_name)
                .family_name(family_name)
                .picture(picture)
                .email(email)
                .email_verified(email_verified)
                .locale(locale)
                .password(password)
                .tenantId(tenantId)
                .tenantName(tenantName)
                .Regist(isRegist)
                .tiponegocio(tiponegocio)
                .build();
        String token = authHeader;
        if(token != ""){
            UserInfo nuevo = userInfoClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth2/v3/userinfo")
                            .queryParam("access_token", token)
                            .build())
                    .retrieve()
                    .bodyToMono(UserInfo.class).block();
            return userService.Regist(maptoUser(nuevo,user) , true);
        }
        return userService.Regist(user, false);
    }
    private User maptoUser(UserInfo userInfo, User user){
        return new User().builder()
                .sub(userInfo.sub())
                .name(userInfo.name())
                .given_name(userInfo.given_name())
                .family_name(userInfo.family_name())
                .picture(userInfo.picture())
                .email(userInfo.email())
                .email_verified(userInfo.email_verified())
                .locale(userInfo.locale())
                .tenantId(user.getTenantId())
                .tenantName(user.getTenantName())
                .Regist(false)
                .tiponegocio(user.getTiponegocio())
                .build();
    }
    @GetMapping("/login")
    public UserResponse Login(@RequestParam String email, @RequestParam String password){
        return userService.Login(email, password);
    }
    @GetMapping
    public List<UserResponse> GetAll(){

        return userService.getAll();
    }

    @GetMapping("/tenant/{id}")
    public List<UserResponse> GetAllTenant(@PathVariable("id") String id){
        return userService.getAllUserInTenant(id);
    }
    @GetMapping("/roles")
    public List<RolResponse> GetAllRoles(){
        return userService.getAllRoles();
    }
    @PutMapping("/asignarrol")
    public void putUsuario(@RequestBody UserResponse usuario){
        userService.AsignarRol(usuario);
    }
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable("id") String id){
        userService.delete(id);
    }
    @GetMapping("/logica/{id}")
    public LogicaNegocioUser logica(@PathVariable("id") String id){
        return userService.Logica(id);
    }
}
