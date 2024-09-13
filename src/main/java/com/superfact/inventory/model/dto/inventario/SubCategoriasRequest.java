package com.superfact.inventory.model.dto.inventario;

import java.util.List;

public record SubCategoriasRequest(
        Long id_categoria,
        List<SubCategoriaResponse> subCategorias
) {
}
