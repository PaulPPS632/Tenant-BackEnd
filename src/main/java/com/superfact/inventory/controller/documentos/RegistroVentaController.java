package com.superfact.inventory.controller.documentos;

import com.superfact.inventory.model.dto.documentos.RegistrarVentaRequest;
import com.superfact.inventory.model.dto.documentos.VentaResponse;
import com.superfact.inventory.service.documentos.RegistroVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/registroventa")
@RequiredArgsConstructor
public class RegistroVentaController {

    private final RegistroVentaService registroVentaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registrar(@RequestBody RegistrarVentaRequest registrarVentaRequest){
        registroVentaService.registrar(registrarVentaRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VentaResponse> Listar(){
        return registroVentaService.Lista();
    }
}
