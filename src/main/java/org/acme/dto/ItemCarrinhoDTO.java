package org.acme.dto;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class ItemCarrinhoDTO {
    private Long id;
    private Long carrinhoId;
    private Long tenisId;
    private String tenisNome;
    private BigDecimal tenisPreco;
    private String corNome;
    private String esporteNome;
    private Integer quantidade;
    private LocalDateTime dataAdicao;
    private Double subtotal;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCarrinhoId() { return carrinhoId; }
    public void setCarrinhoId(Long carrinhoId) { this.carrinhoId = carrinhoId; }

    public Long getTenisId() { return tenisId; }
    public void setTenisId(Long tenisId) { this.tenisId = tenisId; }

    public String getTenisNome() { return tenisNome; }
    public void setTenisNome(String tenisNome) { this.tenisNome = tenisNome; }

    public BigDecimal getTenisPreco() { return tenisPreco; }
    public void setTenisPreco(BigDecimal tenisPreco) { this.tenisPreco = tenisPreco; }

    public String getCorNome() { return corNome; }
    public void setCorNome(String corNome) { this.corNome = corNome; }

    public String getEsporteNome() { return esporteNome; }
    public void setEsporteNome(String esporteNome) { this.esporteNome = esporteNome; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public LocalDateTime getDataAdicao() { return dataAdicao; }
    public void setDataAdicao(LocalDateTime dataAdicao) { this.dataAdicao = dataAdicao; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}