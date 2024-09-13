package com.superfact.inventory.service.globales;

import com.superfact.inventory.model.dto.documentos.CorrelativoResponse;
import com.superfact.inventory.model.dto.documentos.DetalleVentaResponse;
import com.superfact.inventory.model.dto.documentos.VentaResponse;
import com.superfact.inventory.model.dto.users.PrivilegioResponse;
import com.superfact.inventory.model.dto.users.RolResponse;
import com.superfact.inventory.model.dto.users.UserResponse;
import com.superfact.inventory.model.entity.correlativos.Correlativo;
import com.superfact.inventory.model.entity.documentos.DetalleVenta;
import com.superfact.inventory.model.entity.documentos.Venta;
import com.superfact.inventory.model.entity.users.Privilegio;
import com.superfact.inventory.model.entity.users.Rol;
import com.superfact.inventory.model.entity.users.User;
import com.superfact.inventory.repository.documentos.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;

    public List<VentaResponse> findAll() {
        List<Venta> ventas = ventaRepository.findAll();
        return ventas.stream().map(this::mapToVentaResponse).toList();
    }
    private VentaResponse mapToVentaResponse(Venta venta){
        return new VentaResponse(
                venta.getId(),
                mapToCorrelativoResponse(venta.getCorrelativo()),
                venta.getEntidad_cliente(),
                maptoUserResponse(venta.getUsuario()),
                venta.getTipocondicion(),
                venta.getTipopago(),
                venta.getTipomoneda(),
                venta.getTipo_cambio(),
                venta.getFecha_emision(),
                venta.getFecha_vencimiento(),
                venta.getNota(),
                venta.getGravada(),
                venta.getImpuesto(),
                venta.getTotal(),
                venta.getFechapago(),
                venta.getFormapago()
        );
    }
    private CorrelativoResponse mapToCorrelativoResponse(Correlativo correlativo){
        String prefijo = correlativo.getNumeracioncomprobante().getTipocomprobante().getPrefijo();
        Long numeracion = correlativo.getNumeracioncomprobante().getNumeracion();
        Long corre = correlativo.getNumero();
        String documento = prefijo + String.format("%04d", numeracion) + "-" + corre;
        return new CorrelativoResponse(prefijo,numeracion,corre,documento);
    }
    private DetalleVentaResponse mapToDetalleVentaResponse(DetalleVenta detalleVenta){
        return new DetalleVentaResponse(detalleVenta.getProductoserie().getSn(),detalleVenta.getPrecio_neto());
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
