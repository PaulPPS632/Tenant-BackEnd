package com.superfact.inventory.service.reportes;

import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.model.dto.reportes.VentaReporteDto;
import com.superfact.inventory.model.entity.documentos.Venta;
import com.superfact.inventory.repository.documentos.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteVentaService {
    private final VentaRepository ventaRepository;

    public List<VentaReporteDto> reporte(){
        List<Venta> ventas = ventaRepository.findByTenantId(TenantContext.getCurrentTenant());
        return ventas.stream().map(this::mapToVentaReporteDto).toList();
    }
    private VentaReporteDto mapToVentaReporteDto(Venta venta){
        return VentaReporteDto.builder()
                .fecha(venta.getFecha_emision())
                .usuario_id(venta.getUsuario().getId())
                .usuario_name(venta.getUsuario().getName())
                .monto(venta.getTotal())
                .build();
    }
}
