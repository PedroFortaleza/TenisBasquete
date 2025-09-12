package org.acme.dto;

import java.util.List;

public class CriarPedidoDTO {
    private Long usuarioId;
    private List<ItemPedidoDTO> itens;

    // Getters and Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public List<ItemPedidoDTO> getItens() { return itens; }
    public void setItens(List<ItemPedidoDTO> itens) { this.itens = itens; }
}