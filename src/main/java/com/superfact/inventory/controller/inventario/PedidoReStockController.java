package com.superfact.inventory.controller.inventario;

import com.superfact.inventory.model.dto.inventario.PedidosReStockRequest;
import com.superfact.inventory.model.dto.inventario.PedidosReStockResponse;
import com.superfact.inventory.service.inventario.PedidosReStockService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/pedidos")
@RequiredArgsConstructor
public class PedidoReStockController {
    private final PedidosReStockService pedidosReStockService;

    @PostMapping
    public void registrar(@RequestBody PedidosReStockRequest pedidosReStockRequest){
        pedidosReStockService.registrar(pedidosReStockRequest);
    }

    @GetMapping
    public List<PedidosReStockResponse> listar(){
        return pedidosReStockService.Lista();
    }
}
