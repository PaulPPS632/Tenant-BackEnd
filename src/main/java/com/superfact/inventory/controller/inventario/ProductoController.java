package com.superfact.inventory.controller.inventario;

import com.superfact.inventory.service.inventario.ProductoService;
import com.superfact.inventory.model.dto.inventario.ProductoRequest;
import com.superfact.inventory.model.dto.inventario.ProductoResponse;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory/producto")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponse> getAll(){
        return productoService.getAll();
    }

    @GetMapping("/paged")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponse> getAllMarca(Pageable pageable) {
        return productoService.getAllPaged(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductoResponse getById(@PathVariable("id") String id){
        return productoService.getById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponse> getByKey(@RequestParam("keyboard") String keyboard){return productoService.Busqueda(keyboard);}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody ProductoRequest producto){
        productoService.save(producto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") String id, @RequestBody ProductoRequest producto){
        productoService.update(id, producto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") String id){
        productoService.delete(id);
    }
}
