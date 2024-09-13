package com.superfact.inventory.controller.inventario;

import com.superfact.inventory.model.dto.inventario.*;
import com.superfact.inventory.service.inventario.SubCategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inventory/subcategoria")
@RequiredArgsConstructor
public class SubCategoriaController {
    private final SubCategoriaService subCategoriaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SubCategoriaResponse> getAll(){
        return  subCategoriaService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubCategoriaResponse getById(@PathVariable("id") Long id){
        return subCategoriaService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody SubCategoriaRequest subCategoriasRequest) {
        subCategoriaService.save(subCategoriasRequest);
    }

    @PostMapping("/saves")
    @ResponseStatus(HttpStatus.CREATED)
    public void savesAll(@RequestBody SubCategoriasRequest subCategoriasRequest) {
        subCategoriaService.savesAll(subCategoriasRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody SubCategoriaRequest subCategoriaRequest) {
        subCategoriaService.update(id, subCategoriaRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") Long id){
        subCategoriaService.delete(id);

    }
}
