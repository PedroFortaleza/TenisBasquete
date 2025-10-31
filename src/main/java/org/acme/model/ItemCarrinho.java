package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "itens_carrinho")
public class ItemCarrinho extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    @ManyToOne
    @JoinColumn(name = "tenis_id", nullable = false)
    private Tenis tenis;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "data_adicao", nullable = false)
    private LocalDateTime dataAdicao;

    public ItemCarrinho() {
        this.dataAdicao = LocalDateTime.now();
    }

    public ItemCarrinho(Carrinho carrinho, Tenis tenis, Integer quantidade) {
        this();
        this.carrinho = carrinho;
        this.tenis = tenis;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Carrinho getCarrinho() { return carrinho; }
    public void setCarrinho(Carrinho carrinho) { this.carrinho = carrinho; }

    public Tenis getTenis() { return tenis; }
    public void setTenis(Tenis tenis) { this.tenis = tenis; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public LocalDateTime getDataAdicao() { return dataAdicao; }
    public void setDataAdicao(LocalDateTime dataAdicao) { this.dataAdicao = dataAdicao; }
}