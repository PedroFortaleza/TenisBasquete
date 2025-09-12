package org.acme.dto;

public class MarcaDTO {
    
    private Long id;
    private String nome;
    private String paisOrigem;
    private Integer anoFundacao;
    private String siteOficial;
    private String descricao;
    private String logoUrl;
    private Boolean ativa;

    // Construtores
    public MarcaDTO() {}

    public MarcaDTO(Long id, String nome, String paisOrigem, Integer anoFundacao, 
                   String siteOficial, String descricao, String logoUrl, Boolean ativa) {
        this.id = id;
        this.nome = nome;
        this.paisOrigem = paisOrigem;
        this.anoFundacao = anoFundacao;
        this.siteOficial = siteOficial;
        this.descricao = descricao;
        this.logoUrl = logoUrl;
        this.ativa = ativa;
    }

    // Getters e Setters
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

    public String getPaisOrigem() {
        return paisOrigem;
    }

    public void setPaisOrigem(String paisOrigem) {
        this.paisOrigem = paisOrigem;
    }

    public Integer getAnoFundacao() {
        return anoFundacao;
    }

    public void setAnoFundacao(Integer anoFundacao) {
        this.anoFundacao = anoFundacao;
    }

    public String getSiteOficial() {
        return siteOficial;
    }

    public void setSiteOficial(String siteOficial) {
        this.siteOficial = siteOficial;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
}