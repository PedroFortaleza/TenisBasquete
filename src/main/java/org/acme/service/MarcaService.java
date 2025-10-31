package org.acme.service;

import org.acme.dto.MarcaDTO;
import org.acme.model.Marca;
import org.acme.repository.MarcaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class MarcaService {
    
    @Inject
    MarcaRepository marcaRepository;
    
    public List<MarcaDTO> listAll() {
        return marcaRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<MarcaDTO> listAtivas() {
        return marcaRepository.findAtivas().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public MarcaDTO findById(Long id) {
        Optional<Marca> marca = marcaRepository.findByIdOptional(id);
        return marca.map(this::toDTO).orElse(null);
    }
    
    public MarcaDTO findByNome(String nome) {
        Optional<Marca> marca = marcaRepository.findByNome(nome);
        return marca.map(this::toDTO).orElse(null);
    }
    
    @Transactional
    public MarcaDTO create(MarcaDTO marcaDTO) {
        if (marcaRepository.existsByNome(marcaDTO.getNome())) {
            throw new RuntimeException("Marca com nome '" + marcaDTO.getNome() + "' já existe");
        }
        
        Marca marca = toEntity(marcaDTO);
        marcaRepository.persist(marca);
        return toDTO(marca);
    }
    
    @Transactional
    public MarcaDTO update(Long id, MarcaDTO marcaDTO) {
        Optional<Marca> marcaOpt = marcaRepository.findByIdOptional(id);
        
        if (marcaOpt.isPresent()) {
            Marca marca = marcaOpt.get();
            
            // Verifica se outro registro já tem esse nome
            if (!marca.getNome().equals(marcaDTO.getNome()) && 
                marcaRepository.existsByNome(marcaDTO.getNome())) {
                throw new RuntimeException("Marca com nome '" + marcaDTO.getNome() + "' já existe");
            }
            
            marca.setNome(marcaDTO.getNome());
            marca.setPaisOrigem(marcaDTO.getPaisOrigem());
            marca.setAnoFundacao(marcaDTO.getAnoFundacao());
            marca.setSiteOficial(marcaDTO.getSiteOficial());
            marca.setDescricao(marcaDTO.getDescricao());
            marca.setLogoUrl(marcaDTO.getLogoUrl());
            marca.setAtiva(marcaDTO.getAtiva());
            
            marcaRepository.persist(marca);
            return toDTO(marca);
        }
        return null;
    }
    
    @Transactional
    public boolean delete(Long id) {
        return marcaRepository.deleteById(id);
    }
    
    @Transactional
    public boolean desativar(Long id) {
        Optional<Marca> marcaOpt = marcaRepository.findByIdOptional(id);
        if (marcaOpt.isPresent()) {
            Marca marca = marcaOpt.get();
            marca.setAtiva(false);
            marcaRepository.persist(marca);
            return true;
        }
        return false;
    }
    
    public List<MarcaDTO> findByPais(String pais) {
        return marcaRepository.findByPais(pais).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<MarcaDTO> searchByNome(String nome) {
        return marcaRepository.findByNomeContaining(nome).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    // Métodos de conversão
    private MarcaDTO toDTO(Marca marca) {
        return new MarcaDTO(
            marca.getId(),
            marca.getNome(),
            marca.getPaisOrigem(),
            marca.getAnoFundacao(),
            marca.getSiteOficial(),
            marca.getDescricao(),
            marca.getLogoUrl(),
            marca.getAtiva()
        );
    }
    
    private Marca toEntity(MarcaDTO dto) {
        Marca marca = new Marca();
        marca.setNome(dto.getNome());
        marca.setPaisOrigem(dto.getPaisOrigem());
        marca.setAnoFundacao(dto.getAnoFundacao());
        marca.setSiteOficial(dto.getSiteOficial());
        marca.setDescricao(dto.getDescricao());
        marca.setLogoUrl(dto.getLogoUrl());
        marca.setAtiva(dto.getAtiva() != null ? dto.getAtiva() : true);
        return marca;
    }
}