package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Marca extends PanacheEntity {
    
    @Column(nullable = false, unique = true, length = 50)
    private String nome;
    
    @Column(length = 50)
    private String paisOrigem;
    
    @Column(name = "ano_fundacao")
    private Integer anoFundacao;
    
    @Column(name = "site_oficial", length = 200)
    private String siteOficial;
    
    @Column(length = 500)
    private String descricao;
    
    @Column(name = "logo_url", length = 300)
    private String logoUrl;
    
    private Boolean ativa;
    
    // REMOVER esta relação ou corrigir
    // @OneToMany(mappedBy = "marca")
    // private List<Modelo> modelos;

    
    public Marca() {
        this.ativa = true;
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

    // REMOVER ou comentar os getters/setters da relação
    /*
    public List<Modelo> getModelos() {
        return modelos;
    }

    public void setModelos(List<Modelo> modelos) {
        this.modelos = modelos;
    }
    */
}