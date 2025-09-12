package org.acme.dto;

import java.math.BigDecimal;

public class ModeloDTO {
    
    private Long id;
    private String nome;
    private String marca;
    private String tamanho;
    private BigDecimal preco;
    private String cor;
    private Boolean emEstoque;
    private String descricao;

    
    public ModeloDTO() {}

    
    public ModeloDTO(Long id, String nome, String marca, String tamanho, 
                    BigDecimal preco, String cor, Boolean emEstoque, String descricao) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.tamanho = tamanho;
        this.preco = preco;
        this.cor = cor;
        this.emEstoque = emEstoque;
        this.descricao = descricao;
    }

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Boolean getEmEstoque() {
        return emEstoque;
    }

    public void setEmEstoque(Boolean emEstoque) {
        this.emEstoque = emEstoque;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
