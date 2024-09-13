package com.superfact.inventory.model.dto.inventario;

import java.util.List;

public record CategoriasMarcaRequest(Long id_marca,List<ListaCategoriasMarcaRequest> categorias) {
    
}