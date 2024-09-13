package com.superfact.inventory.service.inventario;
import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.model.dto.inventario.LoteResponse;
import com.superfact.inventory.model.entity.inventario.Lote;
import com.superfact.inventory.model.entity.inventario.Producto;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import com.superfact.inventory.model.dto.inventario.LoteRequest;
import com.superfact.inventory.repository.inventario.LoteRepository;
import com.superfact.inventory.repository.inventario.ProductoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoteService {
    
    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;

    public List<LoteResponse> getAll(){
        List<Lote> lotes = loteRepository.findAll();

        return lotes.stream().map(this::mapToLoteResponse).toList();
    }
    private LoteResponse mapToLoteResponse(Lote lote){
        return new LoteResponse(lote.getId(),lote.getNombre(),lote.getFecha());
    }
    public void save(LoteRequest loteRequest){
        Optional<Producto> optionalproducto = productoRepository.findById(loteRequest.id_producto());

        if(optionalproducto.isEmpty())throw new EntityNotFoundException("Lote no encontrado con el id: " + loteRequest.id_producto());

        Producto producto = optionalproducto.get();
        if(producto.getLote() == null){
            producto.setLote(new ArrayList<>());
        }

        Lote nuevolote = new Lote().builder()
                .nombre(loteRequest.nombre())
                .fecha(loteRequest.fecha())
                .producto(producto)
                .tenantId(TenantContext.getCurrentTenant())
                .build();

        if(loteRequest.nombre().isEmpty()) {
            nuevolote.setNombre("Lote"+producto.getPn()+"-nro"+loteRepository.count());
        }
        producto.getLote().add(nuevolote);
        loteRepository.save(nuevolote);
        productoRepository.save(producto);

    }

    public LoteResponse getById(Long id) {
        Optional<Lote> lote = loteRepository.findById(id);

        if(lote.isEmpty()) throw new EntityNotFoundException("Lote no encontrado con el id: " + id);

        return mapToLoteResponse(lote.get());
    }

    public void update(Long id, LoteRequest loteRequest) {
        Optional<Lote> loteOptional = loteRepository.findById(id);

        if(loteOptional.isEmpty())throw new EntityNotFoundException("Categoría no encontrada");
        if(!loteOptional.get().getProducto().getId().equals(loteRequest.id_producto())) throw new EntityNotFoundException("Un Lote es intransferible a otro producto");

        Lote loteActual = loteOptional.get();

        loteActual.setNombre(loteRequest.nombre());
        loteActual.setFecha(loteRequest.fecha());

        loteRepository.save(loteActual);
    }

    public void delete(Long id) {
        Optional<Lote> lote = loteRepository.findById(id);
        if(lote.isEmpty()) throw new EntityNotFoundException("Lote no encontrado con el id: " + id);
        if(lote.get().getProductoserie() == null || lote.get().getProductoserie().isEmpty()){
            loteRepository.delete(lote.get());
        }else{
            throw new EntityNotFoundException("No se puede eliminar un Lote con series asociadas: " + id);
        }
    }
}
