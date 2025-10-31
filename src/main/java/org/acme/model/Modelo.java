package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Modelo extends PanacheEntity {
    
    @Column(nullable = false, unique = true, length = 100)
    private String nome;
    
    // Se quiser manter a relação com Marca, adicione este campo:
    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;
    
    // Construtor
    public Modelo() {}
    
    public Modelo(String nome) {
        this.nome = nome;
    }
    
    public Modelo(String nome, Marca marca) {
        this.nome = nome;
        this.marca = marca;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }
}