package com.superfact.inventory.service.inventario;

import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.model.dto.inventario.PedidosReStockRequest;
import com.superfact.inventory.model.dto.inventario.PedidosReStockResponse;
import com.superfact.inventory.model.dto.inventario.ProductoResponse;
import com.superfact.inventory.model.dto.users.PrivilegioResponse;
import com.superfact.inventory.model.dto.users.RolResponse;
import com.superfact.inventory.model.dto.users.UserResponse;
import com.superfact.inventory.model.entity.inventario.PedidosReStock;
import com.superfact.inventory.model.entity.inventario.Producto;
import com.superfact.inventory.model.entity.users.Privilegio;
import com.superfact.inventory.model.entity.users.Rol;
import com.superfact.inventory.model.entity.users.User;
import com.superfact.inventory.repository.inventario.PedidosReStockRepository;
import com.superfact.inventory.repository.inventario.ProductoRepository;
import com.superfact.inventory.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidosReStockService {

    private final PedidosReStockRepository pedidosReStockRepository;
    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;

    public void registrar(PedidosReStockRequest pedido){
        pedidosReStockRepository.save(PedidosReStock.builder()
                        .usuario(userRepository.findById(pedido.id_usuario()).orElseThrow())
                        .fecha(pedido.fecha())
                        .producto(productoRepository.findById(pedido.id_producto()).orElseThrow())
                        .estado("pendiente")
                        .cantidad(pedido.cantidad())
                        .nota(pedido.nota())
                        .tenantId(TenantContext.getCurrentTenant())
                .build());
    }
    public List<PedidosReStockResponse> Lista(){
        List<PedidosReStock> pedidos = pedidosReStockRepository.findByTenantId(TenantContext.getCurrentTenant());
        if(pedidos.isEmpty()){
            return new ArrayList<>();
        }
        return pedidos.stream().map(this::mapToPedidosReStockResponse).toList();
    }
    public void editar(PedidosReStockRequest pedido){
        PedidosReStock actual = pedidosReStockRepository.findById(pedido.id()).orElseThrow();
        pedidosReStockRepository.save(PedidosReStock.builder()
                .id(actual.getId())
                .usuario(userRepository.findById(pedido.id_usuario()).orElseThrow())
                .fecha(pedido.fecha())
                .producto(productoRepository.findById(pedido.id_producto()).orElseThrow())
                .estado(pedido.estado())
                .cantidad(pedido.cantidad())
                .nota(pedido.nota())
                .tenantId(TenantContext.getCurrentTenant())
                .build());
    }
    private PedidosReStockResponse mapToPedidosReStockResponse(PedidosReStock pedido){
        return PedidosReStockResponse.builder()
                .id(pedido.getId())
                .usuario(maptoUserResponse(pedido.getUsuario()))
                .fecha(pedido.getFecha())
                .producto(mapToProductoResponse(pedido.getProducto()))
                .estado(pedido.getEstado())
                .cantidad(pedido.getCantidad())
                .nota(pedido.getNota())
                .tenantId(pedido.getTenantId())
                .build();
    }
    public ProductoResponse mapToProductoResponse(Producto producto){
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPn(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getPrecio(),
                producto.getCategoriamarca().getMarca().getNombre(),
                producto.getCategoriamarca().getNombre(),
                producto.getSubcategoria().getCategoria().getNombre(),
                producto.getSubcategoria().getNombre(),
                producto.getGarantia_cliente(),
                producto.getGarantia_total()
        );
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
}
