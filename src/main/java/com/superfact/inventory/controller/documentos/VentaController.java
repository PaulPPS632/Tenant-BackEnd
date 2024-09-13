package com.superfact.inventory.controller.documentos;

import com.superfact.inventory.model.dto.documentos.VentaResponse;
import com.superfact.inventory.service.globales.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory/venta")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VentaResponse> getAll(){
        return ventaService.findAll();
    }
}
