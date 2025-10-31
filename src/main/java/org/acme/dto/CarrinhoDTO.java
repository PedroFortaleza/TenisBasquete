package org.acme.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CarrinhoDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<ItemCarrinhoDTO> itens;
    private Double total;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public List<ItemCarrinhoDTO> getItens() { return itens; }
    public void setItens(List<ItemCarrinhoDTO> itens) { this.itens = itens; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}