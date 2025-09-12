package org.acme.service;

import org.acme.dto.ModeloDTO;
import org.acme.model.Modelo;
import org.acme.repository.ModeloRepository;



import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;



@ApplicationScoped
public class ModeloService {
    
    @Inject
    ModeloRepository modeloRepository;
    
    public List<ModeloDTO> listAll() {
        return modeloRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public ModeloDTO findById(Long id) {
        Modelo modelo = modeloRepository.findById(id);
        return modelo != null ? toDTO(modelo) : null;
    }
    
    @Transactional
    public ModeloDTO create(ModeloDTO modeloDTO) {
        Modelo modelo = toEntity(modeloDTO);
        modeloRepository.persist(modelo);
        return toDTO(modelo);
    }
    
    @Transactional
    public ModeloDTO update(Long id, ModeloDTO modeloDTO) {
        Modelo modelo = modeloRepository.findById(id);
        if (modelo != null) {
            // Usando setters para atualizar os valores
            modelo.setNome(modeloDTO.getNome());
            modelo.setMarca(modeloDTO.getMarca());
            modelo.setTamanho(modeloDTO.getTamanho());
            modelo.setPreco(modeloDTO.getPreco());
            modelo.setCor(modeloDTO.getCor());
            modelo.setEmEstoque(modeloDTO.getEmEstoque());
            modelo.setDescricao(modeloDTO.getDescricao());
            
            modeloRepository.persist(modelo);
            return toDTO(modelo);
        }
        return null;
    }
    
    @Transactional
    public boolean delete(Long id) {
        return modeloRepository.deleteById(id);
    }
    
    public List<ModeloDTO> findByMarca(String marca) {
        return modeloRepository.findByMarca(marca).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ModeloDTO> findEmEstoque() {
        return modeloRepository.findEmEstoque().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    // Métodos de conversão entre Entity e DTO
    private ModeloDTO toDTO(Modelo modelo) {
        ModeloDTO dto = new ModeloDTO();
        dto.setId(modelo.getId());
        dto.setNome(modelo.getNome());
        dto.setMarca(modelo.getMarca());
        dto.setTamanho(modelo.getTamanho());
        dto.setPreco(modelo.getPreco());
        dto.setCor(modelo.getCor());
        dto.setEmEstoque(modelo.getEmEstoque());
        dto.setDescricao(modelo.getDescricao());
        return dto;
    }
    
    private Modelo toEntity(ModeloDTO dto) {
        Modelo modelo = new Modelo();
        modelo.setNome(dto.getNome());
        modelo.setMarca(dto.getMarca());
        modelo.setTamanho(dto.getTamanho());
        modelo.setPreco(dto.getPreco());
        modelo.setCor(dto.getCor());
        modelo.setEmEstoque(dto.getEmEstoque());
        modelo.setDescricao(dto.getDescricao());
        return modelo;
    }
    public Modelo buscarModeloEntity(Long id) {
        Modelo modelo = modeloRepository.findById(id);
        if (modelo == null) {
            throw new RuntimeException("Modelo não encontrado");
        }
        return modelo;
    }
}