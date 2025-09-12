package org.acme.service;

import org.acme.dto.CriarEstoqueDTO;
import org.acme.dto.EstoqueDTO;
import org.acme.model.Estoque;
import org.acme.model.Modelo;
import org.acme.repository.EstoqueRepository;




import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EstoqueService {

    @Inject
    EstoqueRepository estoqueRepository;

    @Inject
    ModeloService modeloService;

    public List<EstoqueDTO> listarTodos() {
        return estoqueRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EstoqueDTO buscarPorId(Long id) {
        Estoque estoque = estoqueRepository.findById(id);
        if (estoque == null) {
            throw new RuntimeException("Estoque não encontrado");
        }
        return toDTO(estoque);
    }

    public Estoque buscarEstoqueEntity(Long id) {
        Estoque estoque = estoqueRepository.findById(id);
        if (estoque == null) {
            throw new RuntimeException("Estoque não encontrado");
        }
        return estoque;
    }

    public List<EstoqueDTO> buscarPorModelo(Long modeloId) {
        return estoqueRepository.findByModeloId(modeloId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EstoqueDTO> buscarDisponiveis() {
        return estoqueRepository.findDisponiveis().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EstoqueDTO criarEstoque(CriarEstoqueDTO dto) {
        Modelo modelo = modeloService.buscarModeloEntity(dto.getModeloId());

        Estoque estoque = new Estoque();
        estoque.setModelo(modelo);
        estoque.setQuantidade(dto.getQuantidade());
        estoque.setPrecoUnitario(dto.getPrecoUnitario());
        estoque.setLocalizacao(dto.getLocalizacao());

        estoqueRepository.persist(estoque);
        return toDTO(estoque);
    }

    @Transactional
    public EstoqueDTO atualizarEstoque(Long id, CriarEstoqueDTO dto) {
        Estoque estoque = estoqueRepository.findById(id);
        if (estoque == null) {
            throw new RuntimeException("Estoque não encontrado");
        }

        if (dto.getModeloId() != null) {
            Modelo modelo = modeloService.buscarModeloEntity(dto.getModeloId());
            estoque.setModelo(modelo);
        }

        if (dto.getQuantidade() != null) {
            estoque.setQuantidade(dto.getQuantidade());
        }

        if (dto.getPrecoUnitario() != null) {
            estoque.setPrecoUnitario(dto.getPrecoUnitario());
        }

        if (dto.getLocalizacao() != null) {
            estoque.setLocalizacao(dto.getLocalizacao());
        }

        estoqueRepository.persist(estoque);
        return toDTO(estoque);
    }

    @Transactional
    public EstoqueDTO atualizarQuantidade(Long id, Integer quantidade) {
        Estoque estoque = estoqueRepository.findById(id);
        if (estoque == null) {
            throw new RuntimeException("Estoque não encontrado");
        }

        estoque.setQuantidade(quantidade);
        estoqueRepository.persist(estoque);
        return toDTO(estoque);
    }

    @Transactional
    public void deletarEstoque(Long id) {
        Estoque estoque = estoqueRepository.findById(id);
        if (estoque == null) {
            throw new RuntimeException("Estoque não encontrado");
        }
        estoqueRepository.delete(estoque);
    }

    private EstoqueDTO toDTO(Estoque estoque) {
        EstoqueDTO dto = new EstoqueDTO();
        dto.setId(estoque.getId());
        dto.setModeloId(estoque.getModelo().getId());
        dto.setModeloNome(estoque.getModelo().getNome());
        dto.setQuantidade(estoque.getQuantidade());
        dto.setPrecoUnitario(estoque.getPrecoUnitario());
        dto.setLocalizacao(estoque.getLocalizacao());
        return dto;
    }
}