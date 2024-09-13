package com.superfact.inventory.controller.inventario;

import com.superfact.inventory.model.dto.inventario.*;
import com.superfact.inventory.service.inventario.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/categoria")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaResponse> getAllMarca() {
        return categoriaService.getAll();
    }

    @GetMapping("/paged")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaResponse> getAllMarca(Pageable pageable) {
        return categoriaService.getAllPaged(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoriaResponse getMarcaById(@PathVariable("id") Long id) {
        return categoriaService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody CategoriaRequest Marca) {
        categoriaService.save(Marca);
    }

    @PostMapping("/saves")
    @ResponseStatus(HttpStatus.CREATED)
    public void savesAll(@RequestBody CategoriasRequest categoriasRequest) {
        categoriaService.savesAll(categoriasRequest);
    }
    @GetMapping("/subs/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<SubCategoriaResponse> getSubs(@PathVariable("id") Long id){
        return categoriaService.SubCategoriaBelogns(id);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") Long id, @RequestBody CategoriaRequest marca) {
        categoriaService.update(id, marca);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") Long id) {
        categoriaService.delete(id);}
}
