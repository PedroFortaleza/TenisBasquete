package org.acme.dto;

public class ItemPedidoDTO {
    private Long estoqueId;
    private Integer quantidade;
    private Double precoUnitario;

    // Getters and Setters
    public Long getEstoqueId() { return estoqueId; }
    public void setEstoqueId(Long estoqueId) { this.estoqueId = estoqueId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(Double precoUnitario) { this.precoUnitario = precoUnitario; }
}