package org.acme.dto;

import java.math.BigDecimal;
import java.util.List;

public class TenisDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String genero;
    private String material;
    private List<String> tamanhos;
    private Boolean ativo;
    private String imagemUrl;
    private Long corId;
    private String corNome;
    private String codigoHex;
    private Long esporteId;
    private String esporteNome;
    private Long marcaId;    // NOVO CAMPO
    private String marcaNome; // NOVO CAMPO
    private Long modeloId;   // NOVO CAMPO
    private String modeloNome; // NOVO CAMPO

    // Construtores
    public TenisDTO() {}

    public TenisDTO(Long id, String nome, String descricao, BigDecimal preco, 
                   String genero, String material, List<String> tamanhos, Boolean ativo,
                   String imagemUrl, Long corId, String corNome, String codigoHex,
                   Long esporteId, String esporteNome, Long marcaId, String marcaNome,
                   Long modeloId, String modeloNome) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.genero = genero;
        this.material = material;
        this.tamanhos = tamanhos;
        this.ativo = ativo;
        this.imagemUrl = imagemUrl;
        this.corId = corId;
        this.corNome = corNome;
        this.codigoHex = codigoHex;
        this.esporteId = esporteId;
        this.esporteNome = esporteNome;
        this.marcaId = marcaId;
        this.marcaNome = marcaNome;
        this.modeloId = modeloId;
        this.modeloNome = modeloNome;
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

    public Long getCorId() { return corId; }
    public void setCorId(Long corId) { this.corId = corId; }

    public String getCorNome() { return corNome; }
    public void setCorNome(String corNome) { this.corNome = corNome; }

    public String getCodigoHex() { return codigoHex; }
    public void setCodigoHex(String codigoHex) { this.codigoHex = codigoHex; }

    public Long getEsporteId() { return esporteId; }
    public void setEsporteId(Long esporteId) { this.esporteId = esporteId; }

    public String getEsporteNome() { return esporteNome; }
    public void setEsporteNome(String esporteNome) { this.esporteNome = esporteNome; }

    public Long getMarcaId() { return marcaId; }
    public void setMarcaId(Long marcaId) { this.marcaId = marcaId; }

    public String getMarcaNome() { return marcaNome; }
    public void setMarcaNome(String marcaNome) { this.marcaNome = marcaNome; }

    public Long getModeloId() { return modeloId; }
    public void setModeloId(Long modeloId) { this.modeloId = modeloId; }

    public String getModeloNome() { return modeloNome; }
    public void setModeloNome(String modeloNome) { this.modeloNome = modeloNome; }
}