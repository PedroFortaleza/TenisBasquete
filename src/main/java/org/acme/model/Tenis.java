package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tenis")
public class Tenis extends PanacheEntity {
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(length = 500)
    private String descricao;
    
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;
    
    @Column(length = 20)
    private String genero;
    
    @Column(length = 100)
    private String material;
    
    @ElementCollection
    @CollectionTable(name = "tenis_tamanhos", joinColumns = @JoinColumn(name = "tenis_id"))
    @Column(name = "tamanho")
    private List<String> tamanhos;
    
    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;

    // RELAÇÕES CORRETAS - VERIFIQUE SE OS NOMES DAS COLUNAS ESTÃO CORRETOS NO BANCO
    @ManyToOne
    @JoinColumn(name = "cor_id", nullable = false)
    private Cor cor;

    @ManyToOne
    @JoinColumn(name = "esporte_id", nullable = false)
    private Esporte esporte;

    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;

    public Tenis() {
        this.ativo = true;
    }
    
    public Tenis(String nome, String descricao, BigDecimal preco, String genero, 
                String material, List<String> tamanhos, String imagemUrl, 
                Cor cor, Esporte esporte, Marca marca, Modelo modelo) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.genero = genero;
        this.material = material;
        this.tamanhos = tamanhos;
        this.imagemUrl = imagemUrl;
        this.cor = cor;
        this.esporte = esporte;
        this.marca = marca;
        this.modelo = modelo;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    
    public List<String> getTamanhos() { return tamanhos; }
    public void setTamanhos(List<String> tamanhos) { this.tamanhos = tamanhos; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public Cor getCor() { return cor; }
    public void setCor(Cor cor) { this.cor = cor; }

    public Esporte getEsporte() { return esporte; }
    public void setEsporte(Esporte esporte) { this.esporte = esporte; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    public Modelo getModelo() { return modelo; }
    public void setModelo(Modelo modelo) { this.modelo = modelo; }
}