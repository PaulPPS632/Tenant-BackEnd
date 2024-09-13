package com.superfact.inventory.service.inventario;
import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.exception.ResourceNotFoundException;
import com.superfact.inventory.model.entity.inventario.SubCategoria;
import com.superfact.inventory.repository.inventario.CategoriaMarcaRepository;
import com.superfact.inventory.repository.inventario.ProductoRepository;
import com.superfact.inventory.model.dto.inventario.ProductoRequest;
import com.superfact.inventory.model.dto.inventario.ProductoResponse;
import com.superfact.inventory.model.entity.inventario.CategoriaMarca;
import com.superfact.inventory.model.entity.inventario.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.superfact.inventory.repository.inventario.SubCategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaMarcaRepository categoriaMarcaRepository;
    private final SubCategoriaRepository subCategoriaRepository;


    public List<ProductoResponse> getAll(){
        List<Producto> productos = productoRepository.findByTenantId(TenantContext.getCurrentTenant());
        return productos.stream().map(this::mapToProductoResponse).toList();
    }

    public ProductoResponse getById(String id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if(producto.isPresent()){
            return mapToProductoResponse(producto.get());
        }else {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
    }

    public void save(ProductoRequest producto){
        Optional<CategoriaMarca> categoriamarcaoptional = categoriaMarcaRepository.findById(producto.id_categoriamarca());
        if(categoriamarcaoptional.isEmpty())throw new ResourceNotFoundException("CategoriaMarca no encontrado con id: " + producto.id_categoriamarca());
        Optional<SubCategoria> subCategoriaOptional = subCategoriaRepository.findById(producto.id_subcategoria());
        if(subCategoriaOptional.isEmpty()) throw new ResourceNotFoundException("SubCategoria no encontrado con id: " + producto.id_subcategoria());

        CategoriaMarca categoriamarca = categoriamarcaoptional.get();
        SubCategoria subCategoria = subCategoriaOptional.get();

        Producto nuevo = new Producto().builder()
                .nombre(producto.nombre())
                .pn(producto.pn())
                .descripcion(producto.descripcion())
                .Stock(producto.stock())
                .Precio(producto.precio())
                .categoriamarca(categoriamarca)
                .subcategoria(subCategoria)
                .garantia_cliente(producto.garantia_cliente())
                .garantia_total(producto.garantia_total())
                .tenantId(TenantContext.getCurrentTenant())
                .build();

        if(categoriamarca.getProductos() == null){
            categoriamarca.setProductos(new ArrayList<>());
        }
        if(subCategoria.getProducto() == null){
            subCategoria.setProducto(new ArrayList<>());
        }
        subCategoria.getProducto().add(nuevo);
        categoriamarca.getProductos().add(nuevo);

        productoRepository.save(nuevo);
        subCategoriaRepository.save(subCategoria);
        categoriaMarcaRepository.save(categoriamarca);
    }

    public void update(String id, ProductoRequest productoRequest) {
        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isEmpty()) throw new ResourceNotFoundException("Producto no encontrado con id: " + id);

        Producto productoActual = productoOptional.get();
        CategoriaMarca categoriaMarcaActual = productoActual.getCategoriamarca();
        SubCategoria subCategoriaActual = productoActual.getSubcategoria();

        // Actualizamos los datos del producto existente
        productoActual.setNombre(productoRequest.nombre());
        productoActual.setPn(productoRequest.pn());
        productoActual.setDescripcion(productoRequest.descripcion());
        productoActual.setStock(productoRequest.stock());
        productoActual.setPrecio(productoRequest.precio());
        productoActual.setGarantia_cliente(productoRequest.garantia_cliente());
        productoActual.setGarantia_total(productoRequest.garantia_total());

        // Verificamos si la categoría ha cambiado
        if (!categoriaMarcaActual.getId().equals(productoRequest.id_categoriamarca())) {
            // Removemos el producto de la lista de la categoría actual
            categoriaMarcaActual.getProductos().remove(productoActual);

            // Buscamos la nueva categoría
            Optional<CategoriaMarca> nuevaCategoriaOptional = categoriaMarcaRepository.findById(productoRequest.id_categoriamarca());

            if (nuevaCategoriaOptional.isEmpty()) throw new ResourceNotFoundException("Categoría nueva no encontrada con id: " + productoRequest.id_categoriamarca());

            CategoriaMarca nuevaCategoria = nuevaCategoriaOptional.get();

            // Asignamos la nueva categoría al producto
            productoActual.setCategoriamarca(nuevaCategoria);
            nuevaCategoria.getProductos().add(productoActual);

            // Guardamos la nueva categoría
            categoriaMarcaRepository.save(nuevaCategoria);
        }
        // Verificamos si la subcategoría ha cambiado
        if (!subCategoriaActual.getId().equals(productoRequest.id_subcategoria())) {
            // Removemos el producto de la lista de la subcategoría actual
            subCategoriaActual.getProducto().remove(productoActual);

            // Buscamos la nueva subcategoría
            Optional<SubCategoria> nuevaSubCategoriaOptional = subCategoriaRepository.findById(productoRequest.id_categoriamarca());

            if (nuevaSubCategoriaOptional.isEmpty()) throw new ResourceNotFoundException("SubCategoria nueva no encontrada con id: " + productoRequest.id_subcategoria());

            SubCategoria nuevaSubCategoria = nuevaSubCategoriaOptional.get();

            // Asignamos la nueva subcategoría al producto
            productoActual.setSubcategoria(nuevaSubCategoria);
            nuevaSubCategoria.getProducto().add(productoActual);

            // Guardamos la nueva subcategoría
            subCategoriaRepository.save(nuevaSubCategoria);
        }
        // Guardamos el producto actualizado
        productoRepository.save(productoActual);

        // Guardamos la categoría actual si ha cambiado
        if (!categoriaMarcaActual.getId().equals(productoRequest.id_categoriamarca())) {
            categoriaMarcaRepository.save(categoriaMarcaActual);
        }
        // Guardamos la Subcategoría actual si ha cambiado
        if (!subCategoriaActual.getId().equals(productoRequest.id_subcategoria())) {
            subCategoriaRepository.save(subCategoriaActual);
        }
    }

    public void delete(String id) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isEmpty()) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        if(productoOptional.get().getLote() != null){
            throw new ResourceNotFoundException("No se puede eliminar un producto con lotes asociados: \n" +productoOptional.get().getLote().toString());
        }
        productoRepository.delete(productoOptional.get());
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
    public List<ProductoResponse> Busqueda(String keyboard){
        List<Producto> productos = productoRepository.findByNombreOrDescripcionContaining(keyboard);
        return productos.stream().map(this::mapToProductoResponse).toList();
    }

    public List<ProductoResponse> getAllPaged(Pageable pageable) {
        Page<Producto> productos = productoRepository.findAll(pageable);
        return productos.stream().map(this::mapToProductoResponse).toList();
    }
}
