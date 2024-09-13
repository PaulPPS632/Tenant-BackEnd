package com.superfact.inventory.controller.inventario;


import com.superfact.inventory.model.dto.inventario.*;
import com.superfact.inventory.service.inventario.CategoriaMarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inventory/categoriamarca")
@RequiredArgsConstructor
public class CategoriaMarcaController {
    private final CategoriaMarcaService categoriaMarcaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaMarcaResponse> getAll(){
        return  categoriaMarcaService.getAll();
    }
    @GetMapping("/paged")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaMarcaResponse> getAllMarca(Pageable pageable) {
        return categoriaMarcaService.getAllPaged(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoriaMarcaResponse getById(@PathVariable("id") Long id){
        return categoriaMarcaService.getById(id);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody CategoriaMarcaRequest categoria) {
        categoriaMarcaService.save(categoria);
    }

    @PostMapping("/saves")
    @ResponseStatus(HttpStatus.CREATED)
    public void savesAll(@RequestBody CategoriasMarcaRequest categorias) {
        categoriaMarcaService.savesAll(categorias);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody CategoriaMarcaRequest categoria) {
        categoriaMarcaService.update(id, categoria);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") Long id){
        categoriaMarcaService.delete(id);

    }
}
