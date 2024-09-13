package com.superfact.inventory.controller.inventario;


import com.superfact.inventory.model.dto.inventario.*;
import com.superfact.inventory.service.inventario.MarcaService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/inventory/marca")
@RequiredArgsConstructor
public class MarcaController {
    
    private final MarcaService marcaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MarcaResponse> getAllMarca() {
        return marcaService.getAll();
    }

    @GetMapping("/paged")
    @ResponseStatus(HttpStatus.OK)
    public List<MarcaResponse> getAllMarca(Pageable pageable) {
        return marcaService.getAllPaged(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MarcaResponse getMarcaById(@PathVariable("id") Long id) {
        return marcaService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody MarcaRequest Marca) {
        marcaService.save(Marca);
    }

    @PostMapping("/saves")
    @ResponseStatus(HttpStatus.CREATED)
    public void savesAll(@RequestBody MarcasRequest marcas) {
        marcaService.savesAll(marcas);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") Long id, @RequestBody MarcaRequest marca) {
        marcaService.update(id, marca);
    }

    @GetMapping("/subs/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaMarcaResponse> getSubs(@PathVariable("id") Long id){
        return marcaService.categoriamarcaBelogns(id);
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") Long id) {marcaService.delete(id);}
}
