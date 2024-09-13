package com.superfact.inventory.controller.globales;


import com.superfact.inventory.model.dto.EntidadRequest;
import com.superfact.inventory.model.entity.globales.Entidad;
import com.superfact.inventory.service.globales.EntidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inventory/entidad")
@RequiredArgsConstructor
public class EntidadController {

    private final EntidadService entidadService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Entidad> getAllMarca() {
        return entidadService.getAll();
    }

    @GetMapping("/paged")
    @ResponseStatus(HttpStatus.OK)
    public List<Entidad> getAllMarca(Pageable pageable) {
        return entidadService.getAllPaged(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Entidad getMarcaById(@PathVariable("id") String  id) {
        return entidadService.getById(id);
    }

    @GetMapping("/documento/{documento}")
    @ResponseStatus(HttpStatus.OK)
    public List<Entidad> getxDocumento(@PathVariable("documento") String documento) {
        return entidadService.getByIdDocumento(documento);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody EntidadRequest entidadRequest) {
        entidadService.save(entidadRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") String id, @RequestBody EntidadRequest entidadRequest) {
        entidadService.update(id, entidadRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") String id) {entidadService.delete(id);}

}
