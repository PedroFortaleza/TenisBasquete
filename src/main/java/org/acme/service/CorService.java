package org.acme.service;

import org.acme.dto.CorDTO;
import org.acme.dto.CriarCorDTO;
import org.acme.model.Cor;
import org.acme.repository.CorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CorService {
    
    @Inject
    CorRepository corRepository;
    
    public List<CorDTO> listAll() {
        return corRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<CorDTO> listAtivas() {
        return corRepository.findAtivas().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public CorDTO findById(Long id) {
        Cor cor = corRepository.findById(id);
        return cor != null ? toDTO(cor) : null;
    }
    
    public Cor buscarCorEntity(Long id) {
        Cor cor = corRepository.findById(id);
        if (cor == null) {
            throw new RuntimeException("Cor não encontrada");
        }
        return cor;
    }
    
    @Transactional
    public CorDTO create(CriarCorDTO dto) {
        if (corRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Cor com nome '" + dto.getNome() + "' já existe");
        }
        
        Cor cor = new Cor();
        cor.setNome(dto.getNome());
        cor.setCodigoHex(dto.getCodigoHex());
        cor.setAtivo(true);
        
        corRepository.persist(cor);
        return toDTO(cor);
    }
    
    @Transactional
    public CorDTO update(Long id, CriarCorDTO dto) {
        Cor cor = corRepository.findById(id);
        if (cor == null) {
            throw new RuntimeException("Cor não encontrada");
        }
        
        if (!cor.getNome().equals(dto.getNome()) && 
            corRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Cor com nome '" + dto.getNome() + "' já existe");
        }
        
        cor.setNome(dto.getNome());
        cor.setCodigoHex(dto.getCodigoHex());
        
        corRepository.persist(cor);
        return toDTO(cor);
    }
    
    @Transactional
    public boolean delete(Long id) {
        Cor cor = corRepository.findById(id);
        if (cor != null) {
            cor.setAtivo(false);
            corRepository.persist(cor);
            return true;
        }
        return false;
    }
    
    @Transactional
    public boolean ativar(Long id) {
        Cor cor = corRepository.findById(id);
        if (cor != null) {
            cor.setAtivo(true);
            corRepository.persist(cor);
            return true;
        }
        return false;
    }
    
    public List<CorDTO> searchByNome(String nome) {
        return corRepository.findByNomeContaining(nome).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private CorDTO toDTO(Cor cor) {
        return new CorDTO(
            cor.getId(),
            cor.getNome(),
            cor.getCodigoHex(),
            cor.getAtivo()
        );
    }
}