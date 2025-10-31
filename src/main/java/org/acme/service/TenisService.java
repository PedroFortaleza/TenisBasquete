package org.acme.service;

import org.acme.dto.TenisDTO;
import org.acme.dto.CriarTenisDTO;
import org.acme.model.Tenis;
import org.acme.model.Cor;
import org.acme.model.Esporte;
import org.acme.repository.TenisRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TenisService {
    
    @Inject
    TenisRepository tenisRepository;
    
    @Inject
    CorService corService;
    
    @Inject
    EsporteService esporteService;
    
    public List<TenisDTO> listAll() {
        return tenisRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> listAtivos() {
        return tenisRepository.findAtivos().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public TenisDTO findById(Long id) {
        Tenis tenis = tenisRepository.findById(id);
        return tenis != null ? toDTO(tenis) : null;
    }
    
    public Tenis buscarTenisEntity(Long id) {
        Tenis tenis = tenisRepository.findById(id);
        if (tenis == null) {
            throw new RuntimeException("Tênis não encontrado");
        }
        return tenis;
    }
    
    @Transactional
    public TenisDTO create(CriarTenisDTO dto) {
        if (tenisRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Tênis com nome '" + dto.getNome() + "' já existe");
        }
        
        Cor cor = corService.buscarCorEntity(dto.getCorId());
        Esporte esporte = esporteService.buscarEsporteEntity(dto.getEsporteId());
        
        Tenis tenis = new Tenis();
        tenis.setNome(dto.getNome());
        tenis.setDescricao(dto.getDescricao());
        tenis.setPreco(dto.getPreco());
        tenis.setGenero(dto.getGenero());
        tenis.setMaterial(dto.getMaterial());
        tenis.setTamanhos(dto.getTamanhos());
        tenis.setCor(cor);
        tenis.setEsporte(esporte);
        tenis.setAtivo(true);
        
        tenisRepository.persist(tenis);
        return toDTO(tenis);
    }
    
    @Transactional
    public TenisDTO update(Long id, CriarTenisDTO dto) {
        Tenis tenis = tenisRepository.findById(id);
        if (tenis == null) {
            throw new RuntimeException("Tênis não encontrado");
        }
        
        if (!tenis.getNome().equals(dto.getNome()) && 
            tenisRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Tênis com nome '" + dto.getNome() + "' já existe");
        }
        
        Cor cor = corService.buscarCorEntity(dto.getCorId());
        Esporte esporte = esporteService.buscarEsporteEntity(dto.getEsporteId());
        
        tenis.setNome(dto.getNome());
        tenis.setDescricao(dto.getDescricao());
        tenis.setPreco(dto.getPreco());
        tenis.setGenero(dto.getGenero());
        tenis.setMaterial(dto.getMaterial());
        tenis.setTamanhos(dto.getTamanhos());
        tenis.setCor(cor);
        tenis.setEsporte(esporte);
        
        tenisRepository.persist(tenis);
        return toDTO(tenis);
    }
    
    @Transactional
    public TenisDTO atualizarPreco(Long id, java.math.BigDecimal novoPreco) {
        Tenis tenis = tenisRepository.findById(id);
        if (tenis == null) {
            throw new RuntimeException("Tênis não encontrado");
        }
        
        tenis.setPreco(novoPreco);
        tenisRepository.persist(tenis);
        return toDTO(tenis);
    }
    
    @Transactional
    public boolean delete(Long id) {
        Tenis tenis = tenisRepository.findById(id);
        if (tenis != null) {
            tenis.setAtivo(false);
            tenisRepository.persist(tenis);
            return true;
        }
        return false;
    }
    
    @Transactional
    public boolean ativar(Long id) {
        Tenis tenis = tenisRepository.findById(id);
        if (tenis != null) {
            tenis.setAtivo(true);
            tenisRepository.persist(tenis);
            return true;
        }
        return false;
    }
    
    public List<TenisDTO> findByGenero(String genero) {
        return tenisRepository.findByGenero(genero).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByCor(Long corId) {
        return tenisRepository.findByCor(corId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByEsporte(Long esporteId) {
        return tenisRepository.findByEsporte(esporteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByTamanho(String tamanho) {
        return tenisRepository.findByTamanho(tamanho).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> searchByNome(String nome) {
        return tenisRepository.findByNomeContaining(nome).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByPrecoBetween(Double precoMin, Double precoMax) {
        return tenisRepository.findByPrecoBetween(precoMin, precoMax).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByCorAndEsporte(Long corId, Long esporteId) {
        return tenisRepository.findByCorAndEsporte(corId, esporteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByGeneroAndEsporte(String genero, Long esporteId) {
        return tenisRepository.findByGeneroAndEsporte(genero, esporteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findMaisCaros(Integer limit) {
        return tenisRepository.findMaisCaros(limit).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findMaisBaratos(Integer limit) {
        return tenisRepository.findMaisBaratos(limit).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<String> findGenerosDistintos() {
        return tenisRepository.findGenerosDistintos();
    }
    
    public Long countTenisAtivos() {
        return tenisRepository.countAtivos();
    }
    
    public Long countTenisInativos() {
        return tenisRepository.countInativos();
    }
    
    public Long countTotalTenis() {
        return tenisRepository.count();
    }
    
    public java.math.BigDecimal findPrecoMedio() {
        return tenisRepository.findPrecoMedio();
    }
    
    public java.math.BigDecimal findPrecoMaximo() {
        return tenisRepository.findPrecoMaximo();
    }
    
    public java.math.BigDecimal findPrecoMinimo() {
        return tenisRepository.findPrecoMinimo();
    }
    
    public List<TenisDTO> findComDesconto(Double percentualDesconto) {
        return tenisRepository.findAtivos().stream()
                .map(tenis -> {
                    TenisDTO dto = toDTO(tenis);
                    java.math.BigDecimal precoOriginal = dto.getPreco();
                    java.math.BigDecimal precoComDesconto = precoOriginal.multiply(
                        java.math.BigDecimal.valueOf(1 - (percentualDesconto / 100))
                    );
                    dto.setPreco(precoComDesconto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findRecomendados(Long usuarioId) {
        return findMaisCaros(6);
    }
    
    private TenisDTO toDTO(Tenis tenis) {
        return new TenisDTO(
            tenis.getId(),
            tenis.getNome(),
            tenis.getDescricao(),
            tenis.getPreco(),
            tenis.getGenero(),
            tenis.getMaterial(),
            tenis.getTamanhos(),
            tenis.getAtivo(),
            tenis.getCor().getId(),
            tenis.getCor().getNome(),
            tenis.getCor().getCodigoHex(),
            tenis.getEsporte().getId(),
            tenis.getEsporte().getNome()
        );
    }
    
    public EstatisticasTenis getEstatisticas() {
        Long total = countTotalTenis();
        Long ativos = countTenisAtivos();
        Long inativos = countTenisInativos();
        java.math.BigDecimal precoMedio = findPrecoMedio();
        java.math.BigDecimal precoMaximo = findPrecoMaximo();
        java.math.BigDecimal precoMinimo = findPrecoMinimo();
        
        return new EstatisticasTenis(total, ativos, inativos, precoMedio, precoMaximo, precoMinimo);
    }
    
    public static class EstatisticasTenis {
        public Long total;
        public Long ativos;
        public Long inativos;
        public java.math.BigDecimal precoMedio;
        public java.math.BigDecimal precoMaximo;
        public java.math.BigDecimal precoMinimo;
        
        public EstatisticasTenis(Long total, Long ativos, Long inativos, 
                               java.math.BigDecimal precoMedio, 
                               java.math.BigDecimal precoMaximo, 
                               java.math.BigDecimal precoMinimo) {
            this.total = total;
            this.ativos = ativos;
            this.inativos = inativos;
            this.precoMedio = precoMedio;
            this.precoMaximo = precoMaximo;
            this.precoMinimo = precoMinimo;
        }
    }
}