package com.superfact.inventory.service.inventario;
import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.exception.ResourceNotFoundException;
import com.superfact.inventory.model.dto.inventario.MarcaRequest;
import com.superfact.inventory.repository.inventario.MarcaRepository;
import com.superfact.inventory.model.dto.inventario.MarcaResponse;
import com.superfact.inventory.model.dto.inventario.MarcasRequest;
import com.superfact.inventory.model.dto.inventario.CategoriaMarcaResponse;
import com.superfact.inventory.model.entity.inventario.Marca;
import com.superfact.inventory.model.entity.inventario.CategoriaMarca;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarcaService {
    
    private final MarcaRepository marcaRepository;

    public List<MarcaResponse> getAllPaged(Pageable pageable){
        Page<Marca> marca = marcaRepository.findAll(pageable);
        return marca.stream().map(this::mapToMarcaResponse).toList();
    }
    public List<MarcaResponse> getAll(){
        List<Marca> marcas = marcaRepository.findByTenantId(TenantContext.getCurrentTenant());
        //marcas.forEach(marca -> Hibernate.initialize(marca.getCategoriamarcas()));
        return marcas.stream().map(this::mapToMarcaResponse).toList();
    }
    public MarcaResponse getById(Long id){
        Optional<Marca> marca = marcaRepository.findById(id);
        MarcaResponse result = null;
        if(marca.isPresent()){
            result = mapToMarcaResponse(marca.get());
        }else{
            result = mapToMarcaResponse(null);
        }
        return result;
    }
    private MarcaResponse mapToMarcaResponse(Marca marca){
        return new MarcaResponse(marca.getId(), marca.getNombre(), marca.getCategoriamarcas().stream().map(this::mapCategoriaMarcaResponse).toList());
    }
    private CategoriaMarcaResponse mapCategoriaMarcaResponse(CategoriaMarca categoriaMarca){
        return new CategoriaMarcaResponse(categoriaMarca.getId(), categoriaMarca.getNombre(), categoriaMarca.getMarca().getNombre());
    }

    public void save(MarcaRequest marcaRequest){
        marcaRepository.save(new Marca().builder().nombre(marcaRequest.nombre()).build());
    }
    public void savesAll(MarcasRequest marcasRequest){
        marcaRepository.saveAll(marcasRequest.marcas().stream().map(this::mapToMarca).toList());
    }
    private Marca mapToMarca(MarcaRequest marcaRequest){
        return new Marca().builder().nombre(marcaRequest.nombre()).build();
    }

    public void update(Long id, MarcaRequest marca) {
        Optional<Marca> m = marcaRepository.findById(id);
        if(m.isPresent()){
            Marca actual = m.get();
            actual.setNombre(marca.nombre());
            marcaRepository.save(actual);
        }
    }
    public void delete(Long id){
        Optional<Marca> marca = marcaRepository.findById(id);
        if(marca.isEmpty()) throw new ResourceNotFoundException("Marca no encontrada con id: " + marca.get().getId());
        if(marca.get().getCategoriamarcas() != null) throw new ResourceNotFoundException("No se puede eliminar una marca con categorias asociadas: ");
        marcaRepository.deleteById(id);
    }

    public List<CategoriaMarcaResponse> categoriamarcaBelogns(Long id) {
        Optional<Marca> marca = marcaRepository.findById(id);
        if(marca.isEmpty()) throw new ResourceNotFoundException("Marca no encontrada con id: " + marca.get().getId());

        return marca.get().getCategoriamarcas().stream().map(this::mapCategoriaMarcaResponse).toList();
    }
}
