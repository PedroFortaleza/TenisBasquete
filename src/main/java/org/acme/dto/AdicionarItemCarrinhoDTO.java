package org.acme.dto;

public class AdicionarItemCarrinhoDTO {
    private Long tenisId;
    private Integer quantidade;

    // Getters e Setters
    public Long getTenisId() { return tenisId; }
    public void setTenisId(Long tenisId) { this.tenisId = tenisId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}