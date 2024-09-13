package com.superfact.inventory.controller.reportes;

import com.superfact.inventory.model.dto.reportes.VentaReporteDto;
import com.superfact.inventory.service.reportes.ReporteVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/inventory/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final ReporteVentaService reporteVentaService;

    @GetMapping
    public List<VentaReporteDto> reporteVenta(){
        return reporteVentaService.reporte();
    }

}
