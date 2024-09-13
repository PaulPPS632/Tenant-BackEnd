package com.superfact.inventory.service.User;

import com.superfact.inventory.model.dto.users.PrivilegioResponse;
import com.superfact.inventory.model.dto.users.RolResponse;
import com.superfact.inventory.model.dto.users.UserAuthDto;
import com.superfact.inventory.model.dto.users.UserResponse;
import com.superfact.inventory.model.entity.Tenant.Tenant;
import com.superfact.inventory.model.entity.users.LogicaNegocioUser;
import com.superfact.inventory.model.entity.users.Privilegio;
import com.superfact.inventory.model.entity.users.Rol;
import com.superfact.inventory.model.entity.users.User;
import com.superfact.inventory.repository.tenant.TenantRepository;
import com.superfact.inventory.repository.users.LogicaNegocioUserRepository;
import com.superfact.inventory.repository.users.RolRepository;
import com.superfact.inventory.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final SQLService sqlService;
    private final JdbcTemplate jdbcTemplate;
    private final RolRepository rolRepository;
    private final com.superfact.inventory.repository.users.privilegioRepository privilegioRepository;
    private final LogicaNegocioUserRepository logicaNegocioUserRepository;
    //@Transactional
    public UserResponse Regist(User usuario, boolean isAuthenticated) {
        try {
            if(isAuthenticated) {
                Optional<User> optionalUser = userRepository.findByEmail(usuario.getEmail());
                if(optionalUser.isPresent()) {
                    optionalUser.get().setRegist(true);
                    return maptoUserResponse(optionalUser.get());
                }else{

                    if(usuario.getTenantName()==""){
                        if(usuario.getTenantId() == ""){
                            usuario.setRegist(false);
                            return maptoUserResponse(usuario);
                        }else{
                            usuario.setRegist(true);
                            return maptoUserResponse(userRepository.save(usuario));
                        }

                    }else {
                        return maptoUserResponse(crearTenant(usuario));
                    }
                }
            }else {
                if(usuario.getTenantId() != ""){
                    Tenant act = tenantRepository.findById(usuario.getTenantId()).orElse(null);
                    if(act != null) {
                        usuario.setRegist(true);
                        usuario.setTenantName(act.getName());
                    }else {
                        usuario.setRegist(false);
                    }
                    return maptoUserResponse(userRepository.save(usuario));
                }else{
                    return maptoUserResponse(crearTenant(usuario));
                }
            }
        }catch (Exception e) {
            // Manejar la excepción adecuadamente
            throw new RuntimeException("Error al registrar usuario", e);
        }

    }
    private User crearTenant(User usuario){
        Tenant TenantNuevo = new Tenant().builder().name(usuario.getTenantName()).build();
        TenantNuevo = tenantRepository.save(TenantNuevo);
        usuario.setTenantId(TenantNuevo.getId());
        usuario.setRegist(true);

        switch (usuario.getTiponegocio()) {
            case "electronica":
                //executeSqlWithTenantId(TenantNuevo.getId(), "electronica.sql");
                ejec(TenantNuevo.getId());
                break;
            case "textil":
                executeSqlWithTenantId(TenantNuevo.getId(), "textil.sql");
                break;
        }
        Rol rol = rolRepository.findById(1L).orElseThrow(() -> new RuntimeException("Rol not found"));
        List<Long> privilegioIds =new ArrayList<>();
        privilegioIds.add(1L);
        privilegioIds.add(2L);
        privilegioIds.add(3L);
        privilegioIds.add(4L);
        privilegioIds.add(5L);
        List<Privilegio> privilegios = privilegioRepository.findAllById(privilegioIds);
        rol.setPrivilegios(privilegios);
        rolRepository.save(rol);
        usuario.setRol(rol);

        User guardado = userRepository.save(usuario);
        //logicaNegocioUserRepository.save(LogicaNegocioUser.builder().usuario(guardado.getId()).metaventas(8000D).build());
        return guardado;
    }
    private UserAuthDto maptoUserAuthDTO(User user){
        return UserAuthDto.builder()
                .id(user.getId())
                .sub(user.getSub())
                .name(user.getName())
                .given_name(user.getGiven_name())
                .family_name(user.getFamily_name())
                .picture(user.getPicture())
                .email(user.getEmail())
                .email_verified(user.isEmail_verified())
                .locale(user.getLocale())
                .password(user.getPassword())
                .tenantId(user.getTenantId())
                .tenantName(user.getTenantName())
                .regist(user.isRegist())
                .tiponegocio(user.getTiponegocio())
                .build();
    }
    private void ejec(String tenantId){
        sqlService.execute(tenantId);
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
    public UserResponse Login(String email, String password) {
        return maptoUserResponse(userRepository.findByEmailAndPassword(email,password).orElseThrow());
    }

    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::maptoUserResponse).toList();
    }
    private UserResponse maptoUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .sub(user.getSub())
                .name(user.getName())
                .given_name(user.getGiven_name())
                .family_name(user.getFamily_name())
                .picture(user.getPicture())
                .email(user.getEmail())
                .email_verified(user.isEmail_verified())
                .locale(user.getLocale())
                .tenantId(user.getTenantId())
                .tenantName(user.getTenantName())
                .regist(user.isRegist())
                .tiponegocio(user.getTiponegocio())
                .rol(maptoRolResponse(user.getRol()))
                .build();
    }
    private RolResponse maptoRolResponse(Rol rol){
        if(rol != null){
            return RolResponse.builder()
                    .id(rol.getId())
                    .nombre(rol.getNombre())
                    .descripcion(rol.getDescripcion())
                    .privilegios(rol.getPrivilegios().stream().map(this::maptoPrivilegioResponse).toList())
                    .build();
        }
        return RolResponse.builder().build();
    }
    private PrivilegioResponse maptoPrivilegioResponse(Privilegio privilegio){
        if(privilegio != null){
            return PrivilegioResponse.builder()
                    .id(privilegio.getId())
                    .nombre(privilegio.getNombre())
                    .descripcion(privilegio.getDescripcion())
                    .build();
        }
        return PrivilegioResponse.builder().build();
    }

    public List<UserResponse> getAllUserInTenant(String tenantId) {
        List<User> usuariosEnTenant = userRepository.findByTenantId(tenantId);
        return usuariosEnTenant.stream().map(this::maptoUserResponse).toList();
    }

    public List<RolResponse> getAllRoles() {
        return rolRepository.findAll().stream().map(this::maptoRolResponse).toList();
    }

    public void AsignarRol(UserResponse usuario) {
        Optional<User> user = userRepository.findById(usuario.id());
        user.orElseThrow().setRol(rolRepository.findById(usuario.rol().id()).orElseThrow());
        userRepository.save(user.get());
    }
    private User maptoUser(UserResponse usuarioResponse){
        Optional<Rol> rol = rolRepository.findById(usuarioResponse.rol().id());
        return User.builder()
                .id(usuarioResponse.id())
                .sub(usuarioResponse.sub())
                .name(usuarioResponse.name())
                .given_name(usuarioResponse.given_name())
                .family_name(usuarioResponse.family_name())
                .picture(usuarioResponse.picture())
                .email(usuarioResponse.email())
                .email_verified(usuarioResponse.email_verified())
                .locale(usuarioResponse.locale())
                .tenantId(usuarioResponse.tenantId())
                .tenantName(usuarioResponse.tenantName())
                .Regist(usuarioResponse.regist())
                .tiponegocio(usuarioResponse.tiponegocio())
                .rol(rol.orElseThrow())
                .build();
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }

    public LogicaNegocioUser Logica(String id) {
        return logicaNegocioUserRepository.findByUsuario(id).orElseThrow();
    }
}
