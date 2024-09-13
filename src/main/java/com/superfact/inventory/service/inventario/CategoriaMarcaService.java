package com.superfact.inventory.service.inventario;

import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.model.dto.inventario.CategoriaMarcaRequest;
import com.superfact.inventory.model.dto.inventario.CategoriaMarcaResponse;
import com.superfact.inventory.model.dto.inventario.CategoriasMarcaRequest;
import com.superfact.inventory.model.entity.inventario.CategoriaMarca;
import com.superfact.inventory.model.entity.inventario.Marca;
import com.superfact.inventory.repository.inventario.CategoriaMarcaRepository;
import com.superfact.inventory.repository.inventario.MarcaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaMarcaService {
    private final CategoriaMarcaRepository categoriaMarcaRepository;
    private final MarcaRepository marcaRepository;
    public List<CategoriaMarcaResponse> getAll() {
        List<CategoriaMarca> categoriaMarcas = categoriaMarcaRepository.findAll();
        return categoriaMarcas.stream().map(this::mapToCategoriaMarcaResponse).toList();
    }
    private CategoriaMarcaResponse mapToCategoriaMarcaResponse(CategoriaMarca categoriaMarca){
        return new CategoriaMarcaResponse(categoriaMarca.getId(), categoriaMarca.getNombre(), categoriaMarca.getMarca().getNombre());
    }
    public CategoriaMarcaResponse getById(Long id) {
        Optional<CategoriaMarca> categoriaMarca = categoriaMarcaRepository.findById(id);
        CategoriaMarcaResponse result = null;
        if(categoriaMarca.isPresent()){
            result = mapToCategoriaMarcaResponse(categoriaMarca.get());
        }else{
            result = mapToCategoriaMarcaResponse(null);
        }
        return result;
    }

    public void save(CategoriaMarcaRequest categoriaMarcaRequest){
        Optional<Marca> marca = marcaRepository.findById(categoriaMarcaRequest.id_marca());
        Marca result = null;
        if(marca.isPresent()){
            result = marca.get();
        }
        CategoriaMarca categoria = new CategoriaMarca().builder().nombre(categoriaMarcaRequest.nombre()).marca(result).tenantId(TenantContext.getCurrentTenant()).build();
        if(result.getCategoriamarcas().isEmpty()){
            result.setCategoriamarcas(new ArrayList<>());
        }else{
            result.getCategoriamarcas().add(categoria);
        }
        categoriaMarcaRepository.save(categoria);
        marcaRepository.save(result);


    }
    public void savesAll(CategoriasMarcaRequest categoriasMarcaRequest){
        Optional<Marca> optionalMarca = marcaRepository.findById(categoriasMarcaRequest.id_marca());
        if (optionalMarca.isPresent()) {
            Marca marca = optionalMarca.get();

            if (marca.getCategoriamarcas() == null) {
                marca.setCategoriamarcas(new ArrayList<>());
            }

            List<CategoriaMarca> nuevasCategorias = categoriasMarcaRequest.categorias().stream()
                    .map(request -> CategoriaMarca.builder()
                                                  .nombre(request.nombre())
                                                  .marca(marca)
                                                  .build())
                    .collect(Collectors.toList());


            categoriaMarcaRepository.saveAll(nuevasCategorias);
            marca.getCategoriamarcas().addAll(nuevasCategorias);
            marcaRepository.save(marca);

        }
    }
    private CategoriaMarca mapToCategoriaMarca(CategoriaMarcaRequest categoriaMarcaRequest){
        return new CategoriaMarca().builder().nombre(categoriaMarcaRequest.nombre()).build();
    }

    public void delete(Long id) {
        categoriaMarcaRepository.delete(categoriaMarcaRepository.findById(id).get());
    }

    public void update(Long id, CategoriaMarcaRequest categoria) {
        Optional<CategoriaMarca> actualcategoria = categoriaMarcaRepository.findById(id);
        if(actualcategoria.isPresent()){
            CategoriaMarca actual = actualcategoria.get();
            Marca marcaactual = actual.getMarca();
            if(marcaactual.getId().equals(categoria.id_marca())){
                actual.setNombre(categoria.nombre());
                categoriaMarcaRepository.save(actual);
            }else {
                marcaactual.getCategoriamarcas().remove(actual);
                marcaRepository.save(marcaactual);
                Optional<Marca> nuevamarcaoptional = marcaRepository.findById(categoria.id_marca());
                if(nuevamarcaoptional.isPresent()){
                    Marca nuevamarca = nuevamarcaoptional.get();
                    actual.setNombre(categoria.nombre());
                    actual.setMarca(nuevamarca);

                    if(nuevamarca.getCategoriamarcas() == null){
                        nuevamarca.setCategoriamarcas(new ArrayList<>());
                    }
                    nuevamarca.getCategoriamarcas().add(actual);
                    categoriaMarcaRepository.save(actual);
                    marcaRepository.save(nuevamarca);
                }else {
                    throw new EntityNotFoundException("Nueva marca no encontrada");
                }
            }
        }else {
            throw new EntityNotFoundException("Categoría no encontrada");
        }
    }

    public List<CategoriaMarcaResponse> getAllPaged(Pageable pageable) {
        Page<CategoriaMarca> categoriaMarcas = categoriaMarcaRepository.findAll(pageable);
        return categoriaMarcas.stream().map(this::mapToCategoriaMarcaResponse).toList();
    }
}
