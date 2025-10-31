package org.acme.service;

import org.acme.dto.EsporteDTO;
import org.acme.dto.CriarEsporteDTO;
import org.acme.model.Esporte;
import org.acme.repository.EsporteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EsporteService {
    
    @Inject
    EsporteRepository esporteRepository;
    
    public List<EsporteDTO> listAll() {
        return esporteRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<EsporteDTO> listAtivos() {
        return esporteRepository.findAtivos().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public EsporteDTO findById(Long id) {
        Esporte esporte = esporteRepository.findById(id);
        return esporte != null ? toDTO(esporte) : null;
    }
    
    public Esporte buscarEsporteEntity(Long id) {
        Esporte esporte = esporteRepository.findById(id);
        if (esporte == null) {
            throw new RuntimeException("Esporte não encontrado");
        }
        return esporte;
    }
    
    @Transactional
    public EsporteDTO create(CriarEsporteDTO dto) {
        if (esporteRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Esporte com nome '" + dto.getNome() + "' já existe");
        }
        
        Esporte esporte = new Esporte();
        esporte.setNome(dto.getNome());
        esporte.setDescricao(dto.getDescricao());
        esporte.setAtivo(true);
        
        esporteRepository.persist(esporte);
        return toDTO(esporte);
    }
    
    @Transactional
    public EsporteDTO update(Long id, CriarEsporteDTO dto) {
        Esporte esporte = esporteRepository.findById(id);
        if (esporte == null) {
            throw new RuntimeException("Esporte não encontrado");
        }
        
        if (!esporte.getNome().equals(dto.getNome()) && 
            esporteRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Esporte com nome '" + dto.getNome() + "' já existe");
        }
        
        esporte.setNome(dto.getNome());
        esporte.setDescricao(dto.getDescricao());
        
        esporteRepository.persist(esporte);
        return toDTO(esporte);
    }
    
    @Transactional
    public boolean delete(Long id) {
        Esporte esporte = esporteRepository.findById(id);
        if (esporte != null) {
            esporte.setAtivo(false);
            esporteRepository.persist(esporte);
            return true;
        }
        return false;
    }
    
    @Transactional
    public boolean ativar(Long id) {
        Esporte esporte = esporteRepository.findById(id);
        if (esporte != null) {
            esporte.setAtivo(true);
            esporteRepository.persist(esporte);
            return true;
        }
        return false;
    }
    
    public List<EsporteDTO> searchByNome(String nome) {
        return esporteRepository.findByNomeContaining(nome).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private EsporteDTO toDTO(Esporte esporte) {
        return new EsporteDTO(
            esporte.getId(),
            esporte.getNome(),
            esporte.getDescricao(),
            esporte.getAtivo()
        );
    }
}