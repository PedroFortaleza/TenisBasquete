package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "cores")
public class Cor extends PanacheEntity {
    
    @Column(nullable = false, unique = true, length = 50)
    private String nome;
    
    @Column(name = "codigo_hex", length = 7)
    private String codigoHex;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    // Construtores
    public Cor() {
        this.ativo = true;
    }
    
    public Cor(String nome, String codigoHex) {
        this();
        this.nome = nome;
        this.codigoHex = codigoHex;
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