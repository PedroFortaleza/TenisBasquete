package org.acme.service;

import org.acme.dto.CarrinhoDTO;
import org.acme.dto.ItemCarrinhoDTO;
import org.acme.dto.AdicionarItemCarrinhoDTO;
import org.acme.model.Carrinho;
import org.acme.model.ItemCarrinho;
import org.acme.model.Usuario;
import org.acme.model.Tenis;
import org.acme.repository.CarrinhoRepository;
import org.acme.repository.ItemCarrinhoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CarrinhoService {

    @Inject
    CarrinhoRepository carrinhoRepository;

    @Inject
    ItemCarrinhoRepository itemCarrinhoRepository;

    @Inject
    UsuarioService usuarioService;

    @Inject
    TenisService tenisService; // Corrigido: TenisService ao invés de TenisResource

    public CarrinhoDTO buscarCarrinhoPorUsuario(Long usuarioId) {
        Optional<Carrinho> carrinhoOpt = carrinhoRepository.findByUsuarioId(usuarioId);
        
        if (carrinhoOpt.isPresent()) {
            return toDTO(carrinhoOpt.get());
        } else {
            // Criar carrinho se não existir
            Usuario usuario = usuarioService.buscarUsuarioEntity(usuarioId);
            Carrinho carrinho = new Carrinho(usuario);
            carrinhoRepository.persist(carrinho);
            return toDTO(carrinho);
        }
    }

    @Transactional
    public CarrinhoDTO adicionarItem(Long usuarioId, AdicionarItemCarrinhoDTO dto) {
        Carrinho carrinho = obterOuCriarCarrinho(usuarioId);
        Tenis tenis = tenisService.buscarTenisEntity(dto.getTenisId());

        // Verificar se o tênis está ativo
        if (!tenis.getAtivo()) {
            throw new RuntimeException("Tênis não está disponível para compra");
        }

        Optional<ItemCarrinho> itemExistente = itemCarrinhoRepository.findByCarrinhoAndTenis(carrinho.getId(), tenis.getId());

        if (itemExistente.isPresent()) {
            // Atualizar quantidade se item já existir
            ItemCarrinho item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + dto.getQuantidade());
            itemCarrinhoRepository.persist(item);
        } else {
            // Adicionar novo item
            ItemCarrinho item = new ItemCarrinho(carrinho, tenis, dto.getQuantidade());
            itemCarrinhoRepository.persist(item);
        }

        carrinho.setDataAtualizacao(LocalDateTime.now());
        carrinhoRepository.persist(carrinho);

        return toDTO(carrinho);
    }

    @Transactional
    public CarrinhoDTO atualizarQuantidadeItem(Long usuarioId, Long tenisId, Integer quantidade) {
        Carrinho carrinho = obterOuCriarCarrinho(usuarioId);
        Optional<ItemCarrinho> itemOpt = itemCarrinhoRepository.findByCarrinhoAndTenis(carrinho.getId(), tenisId);

        if (itemOpt.isPresent()) {
            ItemCarrinho item = itemOpt.get();
            if (quantidade <= 0) {
                itemCarrinhoRepository.delete(item);
            } else {
                item.setQuantidade(quantidade);
                itemCarrinhoRepository.persist(item);
            }

            carrinho.setDataAtualizacao(LocalDateTime.now());
            carrinhoRepository.persist(carrinho);
        }

        return toDTO(carrinho);
    }

    @Transactional
    public CarrinhoDTO removerItem(Long usuarioId, Long tenisId) {
        Carrinho carrinho = obterOuCriarCarrinho(usuarioId);
        itemCarrinhoRepository.deleteByCarrinhoAndTenis(carrinho.getId(), tenisId);

        carrinho.setDataAtualizacao(LocalDateTime.now());
        carrinhoRepository.persist(carrinho);

        return toDTO(carrinho);
    }

    @Transactional
    public CarrinhoDTO limparCarrinho(Long usuarioId) {
        Carrinho carrinho = obterOuCriarCarrinho(usuarioId);
        itemCarrinhoRepository.deleteByCarrinhoId(carrinho.getId());

        carrinho.setDataAtualizacao(LocalDateTime.now());
        carrinhoRepository.persist(carrinho);

        return toDTO(carrinho);
    }

    @Transactional
    public CarrinhoDTO transferirCarrinhoParaPedido(Long usuarioId) {
        Carrinho carrinho = obterOuCriarCarrinho(usuarioId);
        List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinhoId(carrinho.getId());
        
        if (itens.isEmpty()) {
            throw new RuntimeException("Carrinho está vazio");
        }

        // Aqui você implementaria a lógica para transferir os itens do carrinho para um pedido
        // Por enquanto, apenas limpa o carrinho após a "compra"
        itemCarrinhoRepository.deleteByCarrinhoId(carrinho.getId());
        
        carrinho.setDataAtualizacao(LocalDateTime.now());
        carrinhoRepository.persist(carrinho);

        return toDTO(carrinho);
    }

    public Integer contarItensNoCarrinho(Long usuarioId) {
        Optional<Carrinho> carrinhoOpt = carrinhoRepository.findByUsuarioId(usuarioId);
        
        if (carrinhoOpt.isPresent()) {
            List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinhoId(carrinhoOpt.get().getId());
            return itens.stream()
                    .mapToInt(ItemCarrinho::getQuantidade)
                    .sum();
        }
        
        return 0;
    }

    public Double calcularTotalCarrinho(Long usuarioId) {
        Optional<Carrinho> carrinhoOpt = carrinhoRepository.findByUsuarioId(usuarioId);
        
        if (carrinhoOpt.isPresent()) {
            List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinhoId(carrinhoOpt.get().getId());
            return itens.stream()
                    .mapToDouble(item -> item.getTenis().getPreco().doubleValue() * item.getQuantidade())
                    .sum();
        }
        
        return 0.0;
    }

    private Carrinho obterOuCriarCarrinho(Long usuarioId) {
        Optional<Carrinho> carrinhoOpt = carrinhoRepository.findByUsuarioId(usuarioId);
        
        if (carrinhoOpt.isPresent()) {
            return carrinhoOpt.get();
        } else {
            Usuario usuario = usuarioService.buscarUsuarioEntity(usuarioId);
            Carrinho carrinho = new Carrinho(usuario);
            carrinhoRepository.persist(carrinho);
            return carrinho;
        }
    }

    private CarrinhoDTO toDTO(Carrinho carrinho) {
        CarrinhoDTO dto = new CarrinhoDTO();
        dto.setId(carrinho.getId());
        dto.setUsuarioId(carrinho.getUsuario().getId());
        dto.setUsuarioNome(carrinho.getUsuario().getNome());
        dto.setDataCriacao(carrinho.getDataCriacao());
        dto.setDataAtualizacao(carrinho.getDataAtualizacao());

        // Calcular itens e total
        List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinhoId(carrinho.getId());
        List<ItemCarrinhoDTO> itensDTO = itens.stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());

        dto.setItens(itensDTO);
        
        // Calcular total
        Double total = itens.stream()
                .mapToDouble(item -> item.getTenis().getPreco().doubleValue() * item.getQuantidade())
                .sum();
        dto.setTotal(total);

        return dto;
    }

    private ItemCarrinhoDTO toItemDTO(ItemCarrinho item) {
        ItemCarrinhoDTO dto = new ItemCarrinhoDTO();
        dto.setId(item.getId());
        dto.setCarrinhoId(item.getCarrinho().getId());
        dto.setTenisId(item.getTenis().getId());
        dto.setTenisNome(item.getTenis().getNome());
        dto.setTenisPreco(item.getTenis().getPreco());
        dto.setCorNome(item.getTenis().getCor().getNome());
        dto.setEsporteNome(item.getTenis().getEsporte().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setDataAdicao(item.getDataAdicao());
        dto.setSubtotal(item.getTenis().getPreco().doubleValue() * item.getQuantidade());

        return dto;
    }

    // Método para verificar se um tênis específico está no carrinho do usuário
    public boolean isTenisNoCarrinho(Long usuarioId, Long tenisId) {
        Optional<Carrinho> carrinhoOpt = carrinhoRepository.findByUsuarioId(usuarioId);
        
        if (carrinhoOpt.isPresent()) {
            Optional<ItemCarrinho> itemOpt = itemCarrinhoRepository.findByCarrinhoAndTenis(
                carrinhoOpt.get().getId(), tenisId);
            return itemOpt.isPresent();
        }
        
        return false;
    }

    // Método para obter a quantidade de um tênis específico no carrinho
    public Integer getQuantidadeTenisNoCarrinho(Long usuarioId, Long tenisId) {
        Optional<Carrinho> carrinhoOpt = carrinhoRepository.findByUsuarioId(usuarioId);
        
        if (carrinhoOpt.isPresent()) {
            Optional<ItemCarrinho> itemOpt = itemCarrinhoRepository.findByCarrinhoAndTenis(
                carrinhoOpt.get().getId(), tenisId);
            return itemOpt.map(ItemCarrinho::getQuantidade).orElse(0);
        }
        
        return 0;
    }

    // Método para validar se o carrinho pode ser convertido em pedido
    public boolean validarCarrinhoParaPedido(Long usuarioId) {
        Optional<Carrinho> carrinhoOpt = carrinhoRepository.findByUsuarioId(usuarioId);
        
        if (carrinhoOpt.isPresent()) {
            List<ItemCarrinho> itens = itemCarrinhoRepository.findByCarrinhoId(carrinhoOpt.get().getId());
            
            // Verificar se há itens no carrinho
            if (itens.isEmpty()) {
                return false;
            }
            
            // Verificar se todos os tênis estão ativos
            boolean todosAtivos = itens.stream()
                    .allMatch(item -> item.getTenis().getAtivo());
            
            return todosAtivos;
        }
        
        return false;
    }
}