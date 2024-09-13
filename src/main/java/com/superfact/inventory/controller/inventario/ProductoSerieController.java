package com.superfact.inventory.controller.inventario;

import com.superfact.inventory.model.dto.inventario.ProductoResponse;
import com.superfact.inventory.model.dto.inventario.ProductoSerieResponse;
import com.superfact.inventory.model.dto.inventario.ProductoSeriesRequest;
import org.springframework.web.bind.annotation.*;

import com.superfact.inventory.service.inventario.ProductoSerieService;

import lombok.RequiredArgsConstructor;

import java.util.List;


@RestController
@RequestMapping("/inventory/productoserie")
@RequiredArgsConstructor
public class ProductoSerieController {
    
    private final ProductoSerieService productoSerieService;


    @GetMapping
    public List<ProductoSerieResponse> getAllSeries() {
        return productoSerieService.getAll();
    }

    @GetMapping("/stock/{id_producto}")
    public List<ProductoSerieResponse> getAllSeries(@PathVariable("id_producto") String idProducto) {
        return productoSerieService.getAllStock(idProducto);
    }

    @GetMapping("/belong/{sn}")
    public ProductoResponse getSeries(@PathVariable("sn") String serie) {
        return productoSerieService.getProductbelong(serie);
    }

    @PostMapping
    public void save(@RequestBody ProductoSeriesRequest productoSerieRequest){
        productoSerieService.save(productoSerieRequest);
    }


    
}
