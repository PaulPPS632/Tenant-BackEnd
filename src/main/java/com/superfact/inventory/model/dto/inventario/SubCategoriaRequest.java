package com.superfact.inventory.model.dto.inventario;

public record SubCategoriaRequest(
        String nombre,
        String descripcion,
        Long id_categoria
) {
}
