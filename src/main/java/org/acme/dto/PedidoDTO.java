package org.acme.dto;

import org.acme.enums.StatusPedido;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDTO {
    private Long id;
    private Long usuarioId;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private Double total;
    private List<ItemPedidoDTO> itens;

    // Constructors
    public PedidoDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public List<ItemPedidoDTO> getItens() { return itens; }
    public void setItens(List<ItemPedidoDTO> itens) { this.itens = itens; }
}