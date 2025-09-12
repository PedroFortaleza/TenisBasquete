package org.acme.service;

import org.acme.dto.CriarPedidoDTO;
import org.acme.dto.PedidoDTO;
import org.acme.enums.StatusPedido;
import org.acme.model.*;
import org.acme.repository.PedidoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PedidoService {

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    UsuarioService usuarioService;

    @Inject
    EstoqueService estoqueService;

    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }
        return toDTO(pedido);
    }

    public List<PedidoDTO> buscarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO criarPedido(CriarPedidoDTO dto) {
        Usuario usuario = usuarioService.buscarUsuarioEntity(dto.getUsuarioId());
        
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setStatus(StatusPedido.PENDENTE);
        
        // Processar itens do pedido
        double total = 0;
        for (var itemDTO : dto.getItens()) {
            Estoque estoque = estoqueService.buscarEstoqueEntity(itemDTO.getEstoqueId());
            
            // Verificar disponibilidade
            if (estoque.getQuantidade() < itemDTO.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o item: " + estoque.getId());
            }
            
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setEstoque(estoque);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(itemDTO.getPrecoUnitario());
            item.setSubtotal(itemDTO.getQuantidade() * itemDTO.getPrecoUnitario());
            
            total += item.getSubtotal();
            
            // Atualizar estoque
            estoque.setQuantidade(estoque.getQuantidade() - itemDTO.getQuantidade());
            estoque.persist();
        }
        
        pedido.setTotal(total);
        pedidoRepository.persist(pedido);
        
        return toDTO(pedido);
    }

    @Transactional
    public PedidoDTO atualizarStatus(Long id, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }
        
        pedido.setStatus(status);
        pedidoRepository.persist(pedido);
        
        return toDTO(pedido);
    }

    @Transactional
    public void deletarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }
        
        // Restaurar estoque se o pedido for cancelado
        if (pedido.getStatus() != StatusPedido.CANCELADO) {
            for (ItemPedido item : pedido.getItens()) {
                Estoque estoque = item.getEstoque();
                estoque.setQuantidade(estoque.getQuantidade() + item.getQuantidade());
                estoque.persist();
            }
        }
        
        pedidoRepository.delete(pedido);
    }

    private PedidoDTO toDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setUsuarioId(pedido.getUsuario().getId());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setStatus(pedido.getStatus());
        dto.setTotal(pedido.getTotal());
        // Implementar conversão de itens se necessário
        return dto;
    }
}