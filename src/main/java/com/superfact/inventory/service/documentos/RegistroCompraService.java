package com.superfact.inventory.service.documentos;

import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.model.dto.documentos.CompraResponse;
import com.superfact.inventory.model.dto.documentos.RegistrarCompraRequest;
import com.superfact.inventory.model.dto.users.PrivilegioResponse;
import com.superfact.inventory.model.dto.users.RolResponse;
import com.superfact.inventory.model.dto.users.UserResponse;
import com.superfact.inventory.model.entity.documentos.Compra;
import com.superfact.inventory.model.entity.documentos.DetalleCompra;
import com.superfact.inventory.model.entity.globales.Entidad;
import com.superfact.inventory.model.entity.globales.TipoCondicion;
import com.superfact.inventory.model.entity.globales.TipoMoneda;
import com.superfact.inventory.model.entity.globales.TipoPago;
import com.superfact.inventory.model.entity.historiales.HistorialCompra;
import com.superfact.inventory.model.entity.inventario.EstadoProducto;
import com.superfact.inventory.model.entity.inventario.Lote;
import com.superfact.inventory.model.entity.inventario.Producto;
import com.superfact.inventory.model.entity.inventario.ProductoSerie;
import com.superfact.inventory.model.entity.users.Privilegio;
import com.superfact.inventory.model.entity.users.Rol;
import com.superfact.inventory.model.entity.users.User;
import com.superfact.inventory.repository.correlativos.TipoCondicionRepository;
import com.superfact.inventory.repository.correlativos.TipoMonedaRepository;
import com.superfact.inventory.repository.correlativos.TipoPagoRepository;
import com.superfact.inventory.repository.documentos.CompraRepository;
import com.superfact.inventory.repository.documentos.DetalleCompraRepository;
import com.superfact.inventory.repository.historiales.HistorialCompraRepository;
import com.superfact.inventory.repository.inventario.EstadoProductoRepository;
import com.superfact.inventory.repository.inventario.LoteRepository;
import com.superfact.inventory.repository.inventario.ProductoRepository;
import com.superfact.inventory.repository.inventario.ProductoSerieRepository;
import com.superfact.inventory.repository.users.UserRepository;
import com.superfact.inventory.service.globales.EntidadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroCompraService {

    private final CompraRepository compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final ProductoRepository productoRepository;
    private final TipoCondicionRepository tipoCondicionRepository;
    private final TipoPagoRepository tipoPagoRepository;
    private final TipoMonedaRepository tipoMonedaRepository;
    private final UserRepository userRepository;
    private final EntidadService entidadService;
    private final LoteRepository loteRepository;
    private final ProductoSerieRepository productoSerieRepository;
    private final EstadoProductoRepository estadoProductoRepository;
    private final HistorialCompraRepository historialCompraRepository;

    public void registrar(RegistrarCompraRequest registrarCompraRequest) {
        Optional<TipoCondicion> tipoCondicionOptional = tipoCondicionRepository.findById(registrarCompraRequest.id_tipocondicion());
        Optional<TipoPago> tipoPagoOptional = tipoPagoRepository.findById(registrarCompraRequest.id_tipopago());
        Optional<TipoMoneda> tipoMonedaOptional = tipoMonedaRepository.findById(registrarCompraRequest.id_tipomoneda());
        Optional<User> usuarioOptional = userRepository.findById(registrarCompraRequest.usuario_id());
        if(tipoCondicionOptional.isEmpty() || tipoPagoOptional.isEmpty() || tipoMonedaOptional.isEmpty()) throw new EntityNotFoundException("Existe un error alguno de los tipos enviados");
        List<Entidad> entidades = entidadService.getByIdDocumento(registrarCompraRequest.documento_cliente());

        Compra compra = new Compra();
        compra.setDocumento(registrarCompraRequest.documento());
        compra.setEntidad_proveedor(entidades.get(0));
        compra.setUsuario(usuarioOptional.get());
        compra.setTipocondicion(tipoCondicionOptional.get());
        compra.setTipopago(tipoPagoOptional.get());
        compra.setTipomoneda(tipoMonedaOptional.get());
        compra.setTipo_cambio(registrarCompraRequest.tipo_cambio());
        compra.setFecha_emision(registrarCompraRequest.fecha_emision());
        compra.setFecha_vencimiento(registrarCompraRequest.fecha_vencimiento());
        compra.setNota(registrarCompraRequest.nota());
        compra.setGravada(registrarCompraRequest.gravada());
        compra.setImpuesto(registrarCompraRequest.impuesto());
        compra.setTotal(registrarCompraRequest.total());
        compra.setFechapago(registrarCompraRequest.fechapago());
        compra.setFormapago(registrarCompraRequest.formapago());
        compra.setTenantId(TenantContext.getCurrentTenant());
        EstadoProducto estado = estadoProductoRepository.findById(1L).orElseThrow(() -> {
            throw new RuntimeException("Error inesperado" );
        });

        List<DetalleCompra> detalleCompras = registrarCompraRequest.detalles().stream().flatMap(
                detalleCompraRequest -> {
                    Producto producto = productoRepository.findById(detalleCompraRequest.id_producto()).orElseThrow(() -> {
                        throw new RuntimeException("Producto no encontrado para ID: " + detalleCompraRequest.id_producto() + ", nombre: " + detalleCompraRequest.nombre());
                    });
                    Lote lote = crear_lote(producto);
                    Double cant = producto.getStock() + detalleCompraRequest.cantidad();
                    producto.setStock(cant);
                    productoRepository.save(producto);
                    return detalleCompraRequest.series().stream().map(serie ->{
                        ProductoSerie prodcSerie = productoSerieRepository.save(ProductoSerie.builder().sn(serie).lote(lote).estadoproducto(estado).tenantId(TenantContext.getCurrentTenant()).build());
                        return DetalleCompra.builder().compra(compra).sn(serie).productoserie(prodcSerie).precio_neto(detalleCompraRequest.precio_unitario()).tenantId(TenantContext.getCurrentTenant()).build();
                    });

                }
        ).collect(Collectors.toList());
        compra.setDetallecompra(new ArrayList<>());
        compraRepository.save(compra);
        detalleCompraRepository.saveAll(detalleCompras);
        compra.getDetallecompra().addAll(detalleCompras);
        Compra compra_Guardada = compraRepository.save(compra);
        historialCompraRepository.save(HistorialCompra.builder().fecha(LocalDateTime.now()).accion("Registro").detalles("").compra(compra_Guardada).usuario(usuarioOptional.orElseThrow()).build());
    }
    private Lote crear_lote(Producto producto){
        if(producto.getLote() == null){
            producto.setLote(new ArrayList<>());
        }

        Lote nuevolote = new Lote().builder()
                .nombre("Lote"+producto.getPn()+"-nro"+loteRepository.countByProductoAndTenantId(producto,TenantContext.getCurrentTenant()))
                .fecha(LocalDateTime.now())
                .producto(producto)
                .tenantId(TenantContext.getCurrentTenant())
                .build();


        producto.getLote().add(nuevolote);
        Lote creado = loteRepository.save(nuevolote);
        productoRepository.save(producto);
        return creado;
    }
    public List<CompraResponse> Lista(){
        List<Compra> compras = compraRepository.findByTenantId(TenantContext.getCurrentTenant());
        return compras.stream().map(this::maptoCompraResponse).toList();
    }
    private CompraResponse maptoCompraResponse(Compra compra){
        return CompraResponse.builder()
                .id(compra.getId())
                .documento(compra.getDocumento())
                .proveedor(compra.getEntidad_proveedor())
                .usuario(maptoUserResponse(compra.getUsuario()))
                .tipocondicion(compra.getTipocondicion())
                .tipopago(compra.getTipopago())
                .tipomoneda(compra.getTipomoneda())
                .tipo_cambio(compra.getTipo_cambio())
                .fecha_emision(compra.getFecha_emision())
                .fecha_vencimiento(compra.getFecha_vencimiento())
                .nota(compra.getNota())
                .gravada(compra.getGravada())
                .impuesto(compra.getImpuesto())
                .total(compra.getTotal())
                .fechapago(compra.getFechapago())
                .formapago(compra.getFormapago())
                .build();
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
