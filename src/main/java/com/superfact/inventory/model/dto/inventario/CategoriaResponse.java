package com.superfact.inventory.model.dto.inventario;

import java.util.List;

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        List<SubCategoriaResponse> subcategorias

) {
}
