package org.acme.dto;

public class CorDTO {
    private Long id;
    private String nome;
    private String codigoHex;
    private Boolean ativo;

    // Construtores
    public CorDTO() {}

    public CorDTO(Long id, String nome, String codigoHex, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.codigoHex = codigoHex;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCodigoHex() { return codigoHex; }
    public void setCodigoHex(String codigoHex) { this.codigoHex = codigoHex; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}