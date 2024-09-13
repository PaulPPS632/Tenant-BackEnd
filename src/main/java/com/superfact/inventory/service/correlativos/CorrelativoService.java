package com.superfact.inventory.service.correlativos;

import com.superfact.inventory.Tenant.TenantContext;
import com.superfact.inventory.model.dto.documentos.CorrelativoResponse;
import com.superfact.inventory.model.entity.correlativos.Correlativo;
import com.superfact.inventory.model.entity.correlativos.NumeracionComprobante;
import com.superfact.inventory.model.entity.correlativos.TipoComprobante;
import com.superfact.inventory.repository.correlativos.CorrelativoRepository;
import com.superfact.inventory.repository.correlativos.NumeracionComprobanteRepository;
import com.superfact.inventory.repository.correlativos.TipoComprobanteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CorrelativoService {

    private final TipoComprobanteRepository tipoComprobanteRepository;
    private final NumeracionComprobanteRepository numeracionComprobanteRepository;
    private final CorrelativoRepository correlativoRepository;
/*
    public CorrelativoResponse generateCorrelativo(String prefijo, Long numeracion){

        Optional<TipoComprobante> tipoComprobanteOptional = tipoComprobanteRepository.findByPrefijo(prefijo);
        if(tipoComprobanteOptional.isEmpty()) throw new EntityNotFoundException("No se encontro el Tipo de Comprobante");
        Optional<NumeracionComprobante> numeracionComprobanteOptional = numeracionComprobanteRepository.buscar(tipoComprobanteOptional.get().getId(),numeracion);

        NumeracionComprobante numeracionComprobante = numeracionComprobanteOptional.get();
        if(numeracionComprobanteOptional.isEmpty()){
            throw new EntityNotFoundException("Error con la numeracion de comprobante");
        }else{
            numeracionComprobante = new NumeracionComprobante().builder()
                    .numeracion(numeracion)
                    .descripcion("numeracion-"+numeracion)
                    .tipocomprobante(tipoComprobanteOptional.get())
                    .build();
            numeracionComprobanteRepository.save(numeracionComprobante);
            tipoComprobanteOptional.get().getNumeraciones().add(numeracionComprobante);
            tipoComprobanteRepository.save(tipoComprobanteOptional.get());
        }
        Long correlativoActual = correlativoRepository.countByNumeracioncomprobante(numeracionComprobante);

        Correlativo correlativo = new Correlativo().builder()
                .numero(correlativoActual+1)
                .numeracioncomprobante(numeracionComprobante)
        .build();
        correlativoRepository.save(correlativo);
        numeracionComprobante.setCorrelativos(new ArrayList<>());
        numeracionComprobante.getCorrelativos().add(correlativo);
        numeracionComprobanteRepository.save(numeracionComprobante);
        return new CorrelativoResponse(prefijo,numeracion.toString(),(correlativoActual+1),prefijo + String.format("%04d", numeracion.toString()) + "-" + (correlativoActual+1));
    }
*/
public CorrelativoResponse generateCorrelativoResponse(String prefijo, Long numeracion) {

    Optional<TipoComprobante> tipoComprobanteOptional = tipoComprobanteRepository.findByPrefijo(prefijo);
    if (tipoComprobanteOptional.isEmpty()) {
        throw new EntityNotFoundException("No se encontró el Tipo de Comprobante");
    }

    TipoComprobante tipoComprobante = tipoComprobanteOptional.get();

    Optional<NumeracionComprobante> numeracionComprobanteOptional = numeracionComprobanteRepository.findByTipocomprobanteAndNumeracionAndTenantId(tipoComprobante, numeracion, TenantContext.getCurrentTenant());

    NumeracionComprobante numeracionComprobante;
    if (numeracionComprobanteOptional.isEmpty()) {
        numeracionComprobante = NumeracionComprobante.builder()
                .numeracion(numeracion)
                .descripcion("numeracion-" + numeracion)
                .tipocomprobante(tipoComprobante)
                .correlativos(new ArrayList<>())
                .build();
        numeracionComprobanteRepository.save(numeracionComprobante);
        tipoComprobante.getNumeraciones().add(numeracionComprobante);
        tipoComprobanteRepository.save(tipoComprobante);
    } else {
        numeracionComprobante = numeracionComprobanteOptional.get();
    }

    Long correlativoActual = correlativoRepository.countByNumeracioncomprobante(numeracionComprobante);

    Correlativo correlativo = Correlativo.builder()
            .numero(correlativoActual + 1)
            .numeracioncomprobante(numeracionComprobante)
            .build();
    correlativoRepository.save(correlativo);

    numeracionComprobante.getCorrelativos().add(correlativo);
    numeracionComprobanteRepository.save(numeracionComprobante);

    return new CorrelativoResponse(
            prefijo,
            numeracion,
            correlativoActual + 1,
            prefijo + String.format("%04d", numeracion) + "-" + (correlativoActual + 1)
    );
}
    public Correlativo generateCorrelativoEntity(String prefijo, Long numeracion) {

        Optional<TipoComprobante> tipoComprobanteOptional = tipoComprobanteRepository.findByPrefijo(prefijo);
        if (tipoComprobanteOptional.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el Tipo de Comprobante");
        }

        TipoComprobante tipoComprobante = tipoComprobanteOptional.get();

        Optional<NumeracionComprobante> numeracionComprobanteOptional = numeracionComprobanteRepository.findByTipocomprobanteAndNumeracionAndTenantId(tipoComprobante, numeracion, TenantContext.getCurrentTenant());

        NumeracionComprobante numeracionComprobante;
        if (numeracionComprobanteOptional.isEmpty()) {
            numeracionComprobante = NumeracionComprobante.builder()
                    .numeracion(numeracion)
                    .descripcion("numeracion-" + numeracion)
                    .tipocomprobante(tipoComprobante)
                    .tenantId(TenantContext.getCurrentTenant())
                    .correlativos(new ArrayList<>())
                    .build();
            numeracionComprobanteRepository.save(numeracionComprobante);
            tipoComprobante.getNumeraciones().add(numeracionComprobante);
            tipoComprobanteRepository.save(tipoComprobante);
        } else {
            numeracionComprobante = numeracionComprobanteOptional.get();
        }

        Long correlativoActual = correlativoRepository.countByNumeracioncomprobante(numeracionComprobante);

        Correlativo correlativo = Correlativo.builder()
                .numero(correlativoActual + 1)
                .numeracioncomprobante(numeracionComprobante)
                .tenantId(TenantContext.getCurrentTenant())
                .build();
        correlativoRepository.save(correlativo);

        numeracionComprobante.getCorrelativos().add(correlativo);
        numeracionComprobanteRepository.save(numeracionComprobante);

        return correlativo;
    }
    public Long correlativoSiguiente(String prefijo, Long numeracion){
        Optional<TipoComprobante> tipoComprobanteOptional = tipoComprobanteRepository.findByPrefijo(prefijo);
        if (tipoComprobanteOptional.isEmpty()) {
            throw new EntityNotFoundException("No se encontró el Tipo de Comprobante");
        }

        TipoComprobante tipoComprobante = tipoComprobanteOptional.get();

        Optional<NumeracionComprobante> numeracionComprobanteOptional = numeracionComprobanteRepository.findByTipocomprobanteAndNumeracionAndTenantId(tipoComprobante, numeracion, TenantContext.getCurrentTenant());

        NumeracionComprobante numeracionComprobante;
        if (numeracionComprobanteOptional.isEmpty()) {
            numeracionComprobante = NumeracionComprobante.builder()
                    .numeracion(numeracion)
                    .descripcion("numeracion-" + numeracion)
                    .tipocomprobante(tipoComprobante)
                    .tenantId(TenantContext.getCurrentTenant())
                    .correlativos(new ArrayList<>())
                    .build();
            numeracionComprobanteRepository.save(numeracionComprobante);
            tipoComprobante.getNumeraciones().add(numeracionComprobante);
            tipoComprobanteRepository.save(tipoComprobante);
        } else {
            numeracionComprobante = numeracionComprobanteOptional.get();
        }

        Long correlativoActual = correlativoRepository.countByNumeracioncomprobante(numeracionComprobante);
        return correlativoActual+1;
    }

    public void delete(Correlativo corr) {
        NumeracionComprobante num = corr.getNumeracioncomprobante();
        num.getCorrelativos().remove(corr);
        numeracionComprobanteRepository.save(num);
        correlativoRepository.delete(corr);

    }
}
