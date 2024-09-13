package com.superfact.inventory.model.dto.inventario;

import java.util.List;

public record CategoriasRequest(
        List<CategoriaRequest> categorias
) {
}
