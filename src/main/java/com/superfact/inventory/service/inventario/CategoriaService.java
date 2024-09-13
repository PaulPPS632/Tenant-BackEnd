package com.superfact.inventory.service.inventario;

import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.exception.ResourceNotFoundException;
import com.superfact.inventory.model.dto.inventario.*;
import com.superfact.inventory.model.entity.inventario.Categoria;
import com.superfact.inventory.model.entity.inventario.SubCategoria;
import com.superfact.inventory.repository.inventario.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> getAllPaged(Pageable pageable){
        Page<Categoria> categoria = categoriaRepository.findAll(pageable);
        return categoria.stream().map(this::mapToCategoriaResponse).toList();
    }
    public List<CategoriaResponse> getAll(){
        List<Categoria> categoria = categoriaRepository.findByTenantId(TenantContext.getCurrentTenant());
        return categoria.stream().map(this::mapToCategoriaResponse).toList();
    }
    public CategoriaResponse getById(Long id){
        Optional<Categoria> categoria = categoriaRepository.findById(id);

        if(categoria.isEmpty()) throw new EntityNotFoundException("Categoria no encontrada con el id: " + id);

        return mapToCategoriaResponse(categoria.get());
    }


    public void save(CategoriaRequest categoriaRequest){
        Categoria categoria = mapToCategoria(categoriaRequest);
        categoria.setTenantId(TenantContext.getCurrentTenant());
        categoriaRepository.save(categoria);
    }
    public void savesAll(CategoriasRequest marcasRequest){
        categoriaRepository.saveAll(marcasRequest.categorias().stream().map(this::mapToCategoria).toList());
    }
    public void update(Long id, CategoriaRequest categoriaRequest) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if(categoria.isEmpty()) throw new EntityNotFoundException("Categoria no encontrada con el id: " + id);
        Categoria actual = categoria.get();
        actual.setNombre(categoriaRequest.nombre());
        actual.setDescripcion(categoriaRequest.descripcion());
        categoriaRepository.save(actual);
    }
    public void delete(Long id){
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if(categoria.isEmpty()) throw new EntityNotFoundException("Categoria no encontrada con el id: " + id);
        categoriaRepository.delete(categoria.get());
    }

    private CategoriaResponse mapToCategoriaResponse(Categoria categoria){
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion(),categoria.getSubacategorias().stream().map(this::mapCategoriaMarcaResponse).toList());
    }
    private SubCategoriaResponse mapCategoriaMarcaResponse(SubCategoria subCategoria){
        return new SubCategoriaResponse(subCategoria.getId(), subCategoria.getNombre(), subCategoria.getDescripcion(),subCategoria.getCategoria().getNombre());
    }
    private Categoria mapToCategoria(CategoriaRequest categoriaRequest){
        return new Categoria().builder().nombre(categoriaRequest.nombre()).Descripcion(categoriaRequest.descripcion()).tenantId(TenantContext.getCurrentTenant()).build();
    }
    private SubCategoriaResponse mapSubCategoriaResponse(SubCategoria subCategoria){
        return new SubCategoriaResponse(subCategoria.getId(), subCategoria.getNombre(), subCategoria.getDescripcion(),subCategoria.getCategoria().getNombre());
    }
    public List<SubCategoriaResponse> SubCategoriaBelogns(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if(categoria.isEmpty()) throw new ResourceNotFoundException("Categoria no encontrada con id: " + categoria.get().getId());

        return categoria.get().getSubacategorias().stream().map(this::mapSubCategoriaResponse).toList();
    }
}
