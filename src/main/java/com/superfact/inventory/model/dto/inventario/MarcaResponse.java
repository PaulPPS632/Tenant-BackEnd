package com.superfact.inventory.model.dto.inventario;

import java.util.List;

public record MarcaResponse(
        Long id,
        String nombre,
        List<CategoriaMarcaResponse> categorias) {
    
}
