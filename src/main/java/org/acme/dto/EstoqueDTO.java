package org.acme.dto;

public class EstoqueDTO {
    private Long id;
    private Long modeloId;
    private String modeloNome;
    private Integer quantidade;
    private Double precoUnitario;
    private String localizacao;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getModeloId() { return modeloId; }
    public void setModeloId(Long modeloId) { this.modeloId = modeloId; }

    public String getModeloNome() { return modeloNome; }
    public void setModeloNome(String modeloNome) { this.modeloNome = modeloNome; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(Double precoUnitario) { this.precoUnitario = precoUnitario; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
}