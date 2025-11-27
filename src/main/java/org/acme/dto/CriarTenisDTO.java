package org.acme.dto;

import java.math.BigDecimal;
import java.util.List;

public class CriarTenisDTO {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String genero;
    private String material;
    private List<String> tamanhos;
    private String imagemUrl;
    private Long corId;
    private Long esporteId;
    private Long marcaId;    // NOVO CAMPO
    private Long modeloId;   // NOVO CAMPO

    // Getters e Setters
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

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public Long getCorId() { return corId; }
    public void setCorId(Long corId) { this.corId = corId; }

    public Long getEsporteId() { return esporteId; }
    public void setEsporteId(Long esporteId) { this.esporteId = esporteId; }

    public Long getMarcaId() { return marcaId; }
    public void setMarcaId(Long marcaId) { this.marcaId = marcaId; }

    public Long getModeloId() { return modeloId; }
    public void setModeloId(Long modeloId) { this.modeloId = modeloId; }
}