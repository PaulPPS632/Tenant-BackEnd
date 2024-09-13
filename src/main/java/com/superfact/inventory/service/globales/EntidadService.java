package com.superfact.inventory.service.globales;

import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.model.dto.EntidadRequest;
import com.superfact.inventory.model.entity.globales.Entidad;
import com.superfact.inventory.model.entity.globales.TipoEntidad;
import com.superfact.inventory.repository.globales.EntidadRepository;
import com.superfact.inventory.repository.globales.TipoEntidadRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntidadService {
    private final EntidadRepository entidadRepository;
    private final TipoEntidadRepository tipoEntidadRepository;
    public List<Entidad> getAll(){
        return entidadRepository.findByTenantId(TenantContext.getCurrentTenant());
    }

    public List<Entidad> getAllPaged(Pageable pageable){
        Page<Entidad> entidades = entidadRepository.findAll(pageable);
        return entidades.getContent();
    }

    public Entidad getById(String id){
        Optional<Entidad> entidadOptional = entidadRepository.findById(id);
        if(entidadOptional.isEmpty()) throw new EntityNotFoundException("No se Encontro la entidad con id: " + id);
        return entidadOptional.get();
    }

    public void save(EntidadRequest entidad){
        entidadRepository.save(mapToEntidad(entidad));
    }
    public void update(String id, EntidadRequest entidad){
        Optional<Entidad> entidadOptional = entidadRepository.findById(id);
        if(entidadOptional.isEmpty()) throw new EntityNotFoundException("No se Encontro la entidad con id: " + id);
        if(!entidadOptional.get().getTipoEntidad().equals(entidad.id_tipoEntidad())){
            Optional<TipoEntidad> tipoEntidadOptional = tipoEntidadRepository.findById(entidad.id_tipoEntidad());
            if(tipoEntidadOptional.isEmpty()) throw new EntityNotFoundException("No se Encontro la entidad nueva con id: " + id);

            entidadOptional.get().setNombre(entidad.nombre());
            entidadOptional.get().setDocumento(entidad.documento());
            entidadOptional.get().setDireccion(entidad.direccion());
            entidadOptional.get().setEmail(entidad.email());
            entidadOptional.get().setTelefono(entidad.telefono());
            entidadOptional.get().setTipoEntidad(tipoEntidadOptional.get());

            entidadRepository.save(entidadOptional.get());
        }
    }
    public void delete(String id){
        Optional<Entidad> entidadOptional = entidadRepository.findById(id);
        if(entidadOptional.isEmpty()) throw new EntityNotFoundException("No se Encontro la entidad con id: " + id);
        entidadRepository.deleteById(id);
    }
    private Entidad mapToEntidad(EntidadRequest entidadResponse){
        Optional<TipoEntidad> tipoEntidad = tipoEntidadRepository.findById(entidadResponse.id_tipoEntidad());
        if(tipoEntidad.isEmpty())  throw new EntityNotFoundException("No se Encontro el Tipo entidad con id: " + entidadResponse.id_tipoEntidad());
        return new Entidad().builder()
                .nombre(entidadResponse.nombre())
                .documento(entidadResponse.documento())
                .direccion(entidadResponse.direccion())
                .telefono(entidadResponse.telefono())
                .email(entidadResponse.email())
                .tipoEntidad(tipoEntidad.get())
                .build();
    }

    public List<Entidad> getByIdDocumento(String documento) {
        List<Entidad> entidades = entidadRepository.findByDocumento(documento);
        if(entidades.isEmpty()) throw  new EntityNotFoundException("No se encontro la entidad con documento: " + documento);
        return entidades;
    }
}
