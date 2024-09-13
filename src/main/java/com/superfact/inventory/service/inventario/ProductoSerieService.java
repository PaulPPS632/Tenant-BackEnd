package com.superfact.inventory.service.inventario;

import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.model.dto.inventario.ProductoResponse;
import com.superfact.inventory.model.dto.inventario.ProductoSerieResponse;
import com.superfact.inventory.model.dto.inventario.ProductoSeriesRequest;
import com.superfact.inventory.model.entity.inventario.Lote;
import com.superfact.inventory.model.entity.inventario.Producto;

import com.superfact.inventory.model.entity.inventario.ProductoSerie;
import com.superfact.inventory.repository.inventario.LoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import com.superfact.inventory.repository.inventario.ProductoRepository;
import com.superfact.inventory.repository.inventario.ProductoSerieRepository;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoSerieService {

    private final ProductoSerieRepository productoSerieRepository;
    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;

    public void save(ProductoSeriesRequest productoSeriesRequest){
        Optional<Lote> optionallote = loteRepository.findById(productoSeriesRequest.id_Lote());
        Optional<Producto> optionalProducto = productoRepository.findById(productoSeriesRequest.id_producto());
        if(optionallote.isEmpty() && optionalProducto.isEmpty())throw new EntityNotFoundException("Se necesita almenos 1 id de referencia (Producto o Lote)");

        Producto producto = optionalProducto.get();
        Lote lote = optionallote.orElseGet(() -> {
            List<Lote> lotes = producto.getLote();
            if (lotes.isEmpty()) {
                throw new EntityNotFoundException("No hay lotes disponibles para el producto");
            }
            return lotes.get(lotes.size() - 1);  // Obtiene el último elemento de la lista de lotes
        });

        if(lote.getProductoserie().isEmpty()) lote.setProductoserie(new ArrayList<>());

        List<ProductoSerie> nuevasSeries = Arrays.stream(productoSeriesRequest.sn()).map(
                request -> ProductoSerie.builder()
                        .sn(request.toString())
                        .lote(lote)
                        .build()
        ).collect(Collectors.toList());

        productoSerieRepository.saveAll(nuevasSeries);

        lote.getProductoserie().addAll(nuevasSeries);
        loteRepository.save(lote);
    }

    public List<ProductoSerieResponse> getAllStock(String id){
        Optional<Producto> product = productoRepository.findByIdAndTenantId(id, TenantContext.getCurrentTenant());
        if(product.isPresent()){
            return product.get().getLote().stream().flatMap(res -> res.getProductoserie().stream()).filter(serie -> serie.getEstadoproducto().getId() == 1).map(this::mapToProductoSerieResponse).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    public List<ProductoSerieResponse> getAll(){
        return productoSerieRepository.findAll().stream().map(this::mapToProductoSerieResponse).collect(Collectors.toList());
    }
    public List<ProductoSerieResponse> getAll(String id){
        Optional<Producto> product = productoRepository.findByIdAndTenantId(id, TenantContext.getCurrentTenant());
        if(product.isPresent()){
            return product.get().getLote().stream().flatMap(res -> res.getProductoserie().stream()).map(this::mapToProductoSerieResponse).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    public ProductoResponse getProductbelong(String sn){
        Optional<ProductoSerie> serie = productoSerieRepository.findBySnAndTenantId(sn, TenantContext.getCurrentTenant());
        if(serie.isEmpty()) throw new EntityNotFoundException("No se Encontro la serie: " + sn);
        if(serie.get().getEstadoproducto().getId() == 1){
            Producto producto = serie.get().getLote().getProducto();
            return mapToProductoResponse(producto);
        }
        return null;
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
    public ProductoSerieResponse mapToProductoSerieResponse(ProductoSerie productoSerie){
        return new ProductoSerieResponse(productoSerie.getSn());
    }

}
