package com.superfact.inventory.model.dto.inventario;

public record ProductoRequest(
    String nombre, 
    String pn,
    String descripcion,
    Double stock,
    Double precio,
    Long id_categoriamarca,
    Long id_subcategoria,
    Double garantia_cliente,
    Double garantia_total
    ) {
    
}
