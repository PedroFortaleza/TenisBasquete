package org.acme.service;

import org.acme.dto.TenisDTO;
import org.acme.dto.CriarTenisDTO;
import org.acme.dto.UploadImagemDTO;
import org.acme.model.Tenis;
import org.acme.model.Cor;
import org.acme.model.Esporte;
import org.acme.model.Marca;
import org.acme.model.Modelo;
import org.acme.repository.TenisRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class TenisService {
    
    @Inject
    TenisRepository tenisRepository;
    
    @Inject
    CorService corService;
    
    @Inject
    EsporteService esporteService;
    
    @Inject
    MarcaService marcaService;
    
    @Inject
    ModeloService modeloService;
    
    @Inject
    MinioService minioService;
    
    public List<TenisDTO> listAll() {
        return tenisRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> listAtivos() {
        return tenisRepository.find("ativo", true).stream()
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
            throw new RuntimeException("Tênis não encontrado com ID: " + id);
        }
        return tenis;
    }
    
    @Transactional
    public TenisDTO create(CriarTenisDTO dto) {
        // CORREÇÃO: findByNome retorna Optional, então usamos isPresent() em vez de size()
        if (tenisRepository.findByNome(dto.getNome()).isPresent()) {
            throw new RuntimeException("Tênis com nome '" + dto.getNome() + "' já existe");
        }
        
        // BUSCAR TODAS AS ENTIDADES RELACIONADAS
        Cor cor = corService.buscarCorEntity(dto.getCorId());
        Esporte esporte = esporteService.buscarEsporteEntity(dto.getEsporteId());
        Marca marca = marcaService.buscarMarcaEntity(dto.getMarcaId());
        Modelo modelo = modeloService.buscarModeloEntity(dto.getModeloId());
        
        Tenis tenis = new Tenis();
        tenis.setNome(dto.getNome());
        tenis.setDescricao(dto.getDescricao());
        tenis.setPreco(dto.getPreco());
        tenis.setGenero(dto.getGenero());
        tenis.setMaterial(dto.getMaterial());
        tenis.setTamanhos(dto.getTamanhos());
        tenis.setImagemUrl(dto.getImagemUrl());
        tenis.setCor(cor);
        tenis.setEsporte(esporte);
        tenis.setMarca(marca);
        tenis.setModelo(modelo);
        tenis.setAtivo(true);
        
        tenisRepository.persist(tenis);
        return toDTO(tenis);
    }
    
    @Transactional
    public TenisDTO update(Long id, CriarTenisDTO dto) {
        Tenis tenis = tenisRepository.findById(id);
        if (tenis == null) {
            throw new RuntimeException("Tênis não encontrado com ID: " + id);
        }
        
        // CORREÇÃO: findByNome retorna Optional, então verificamos se existe outro tênis com o mesmo nome
        if (!tenis.getNome().equals(dto.getNome())) {
            Optional<Tenis> tenisComMesmoNome = tenisRepository.findByNome(dto.getNome());
            if (tenisComMesmoNome.isPresent() && !tenisComMesmoNome.get().getId().equals(id)) {
                throw new RuntimeException("Tênis com nome '" + dto.getNome() + "' já existe");
            }
        }
        
        // BUSCAR TODAS AS ENTIDADES RELACIONADAS
        Cor cor = corService.buscarCorEntity(dto.getCorId());
        Esporte esporte = esporteService.buscarEsporteEntity(dto.getEsporteId());
        Marca marca = marcaService.buscarMarcaEntity(dto.getMarcaId());
        Modelo modelo = modeloService.buscarModeloEntity(dto.getModeloId());
        
        tenis.setNome(dto.getNome());
        tenis.setDescricao(dto.getDescricao());
        tenis.setPreco(dto.getPreco());
        tenis.setGenero(dto.getGenero());
        tenis.setMaterial(dto.getMaterial());
        tenis.setTamanhos(dto.getTamanhos());
        tenis.setImagemUrl(dto.getImagemUrl());
        tenis.setCor(cor);
        tenis.setEsporte(esporte);
        tenis.setMarca(marca);
        tenis.setModelo(modelo);
        
        tenisRepository.persist(tenis);
        return toDTO(tenis);
    }
    
    @Transactional
    public TenisDTO atualizarPreco(Long id, BigDecimal novoPreco) {
        Tenis tenis = tenisRepository.findById(id);
        if (tenis == null) {
            throw new RuntimeException("Tênis não encontrado com ID: " + id);
        }
        
        tenis.setPreco(novoPreco);
        tenisRepository.persist(tenis);
        return toDTO(tenis);
    }
    
    @Transactional
    public TenisDTO processarUploadImagem(Long tenisId, MultipartFormDataInput input) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            List<InputPart> inputParts = uploadForm.get("file");
            
            if (inputParts == null || inputParts.isEmpty()) {
                throw new RuntimeException("Arquivo não enviado");
            }
            
            InputPart inputPart = inputParts.get(0);
            
            // Extrair informações do arquivo
            String fileName = this.extractFileName(inputPart);
            String contentType = inputPart.getMediaType().toString();
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] fileData = inputStream.readAllBytes();
            
            // Validar tamanho do arquivo (máximo 10MB)
            if (fileData.length > 10 * 1024 * 1024) {
                throw new RuntimeException("Arquivo muito grande. Tamanho máximo: 10MB");
            }
            
            // Criar DTO de upload
            UploadImagemDTO uploadDTO = new UploadImagemDTO(fileName, contentType, fileData);
            
            return this.uploadImagem(tenisId, uploadDTO);
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo: " + e.getMessage());
        }
    }
    
    @Transactional
    public TenisDTO uploadImagem(Long tenisId, UploadImagemDTO uploadImagemDTO) {
        Tenis tenis = tenisRepository.findById(tenisId);
        if (tenis == null) {
            throw new RuntimeException("Tênis não encontrado com ID: " + tenisId);
        }
        
        // Se existir uma imagem anterior, remover do MinIO
        if (tenis.getImagemUrl() != null && !tenis.getImagemUrl().isEmpty()) {
            try {
                this.removerImagemDoStorage(tenis.getImagemUrl());
            } catch (Exception e) {
                System.err.println("Aviso: Não foi possível remover imagem anterior: " + e.getMessage());
            }
        }
        
        // Gerar nome único para o arquivo
        String fileExtension = this.getFileExtension(uploadImagemDTO.getFileName());
        String objectName = "tenis-" + tenisId + "-" + System.currentTimeMillis() + fileExtension;
        
        // Fazer upload para o MinIO
        String storedObjectName = minioService.uploadFile(
            objectName, 
            uploadImagemDTO.getFileData(), 
            uploadImagemDTO.getContentType()
        );
        
        // Gerar URL pública para a imagem
        String imageUrl = minioService.getFileUrl(storedObjectName);
        
        // Atualizar a URL da imagem no tênis
        tenis.setImagemUrl(imageUrl);
        tenisRepository.persist(tenis);
        
        return toDTO(tenis);
    }
    
    @Transactional
    public TenisDTO removerImagem(Long tenisId) {
        Tenis tenis = tenisRepository.findById(tenisId);
        if (tenis == null) {
            throw new RuntimeException("Tênis não encontrado com ID: " + tenisId);
        }
        
        // Remover imagem do storage
        if (tenis.getImagemUrl() != null && !tenis.getImagemUrl().isEmpty()) {
            this.removerImagemDoStorage(tenis.getImagemUrl());
        }
        
        // Remover a referência da imagem
        tenis.setImagemUrl(null);
        tenisRepository.persist(tenis);
        
        return toDTO(tenis);
    }
    
    private void removerImagemDoStorage(String imageUrl) {
        try {
            // Extrair objectName da URL
            String objectName = this.extractObjectNameFromUrl(imageUrl);
            if (objectName != null) {
                minioService.deleteFile(objectName);
            }
        } catch (Exception e) {
            System.err.println("Erro ao remover imagem do storage: " + e.getMessage());
            // Não lançar exceção para não interromper o fluxo principal
        }
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
    
    @Transactional
    public boolean desativar(Long id) {
        return this.delete(id);
    }
    
    // MÉTODOS DE BUSCA ADICIONADOS
    public List<TenisDTO> findByGenero(String genero) {
        return tenisRepository.find("genero", genero).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByCor(Long corId) {
        return tenisRepository.find("cor.id", corId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByEsporte(Long esporteId) {
        return tenisRepository.find("esporte.id", esporteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByTamanho(String tamanho) {
        return tenisRepository.listAll().stream()
                .filter(tenis -> tenis.getTamanhos() != null && tenis.getTamanhos().contains(tamanho))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByMaterial(String material) {
        return tenisRepository.find("material", material).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> searchByNome(String nome) {
        return tenisRepository.find("nome like ?1", "%" + nome + "%").stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByPrecoBetween(Double precoMin, Double precoMax) {
        BigDecimal min = precoMin != null ? BigDecimal.valueOf(precoMin) : BigDecimal.ZERO;
        BigDecimal max = precoMax != null ? BigDecimal.valueOf(precoMax) : BigDecimal.valueOf(10000);
        
        return tenisRepository.find("preco between ?1 and ?2", min, max).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByCorAndEsporte(Long corId, Long esporteId) {
        return tenisRepository.find("cor.id = ?1 and esporte.id = ?2", corId, esporteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findByGeneroAndEsporte(String genero, Long esporteId) {
        return tenisRepository.find("genero = ?1 and esporte.id = ?2", genero, esporteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findMaisCaros(Integer limit) {
        return tenisRepository.find("ativo = true order by preco desc").page(0, limit).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findMaisBaratos(Integer limit) {
        return tenisRepository.find("ativo = true order by preco asc").page(0, limit).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> findRecentes(Integer limit) {
        return tenisRepository.find("ativo = true order by id desc").page(0, limit).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<TenisDTO> buscarComFiltros(String nome, String genero, Long corId, Long esporteId, 
                                         String tamanho, Boolean ativo) {
        // Implementação simplificada - filtra em memória
        return tenisRepository.listAll().stream()
                .filter(tenis -> nome == null || tenis.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(tenis -> genero == null || tenis.getGenero().equals(genero))
                .filter(tenis -> corId == null || tenis.getCor().getId().equals(corId))
                .filter(tenis -> esporteId == null || tenis.getEsporte().getId().equals(esporteId))
                .filter(tenis -> tamanho == null || (tenis.getTamanhos() != null && tenis.getTamanhos().contains(tamanho)))
                .filter(tenis -> ativo == null || tenis.getAtivo().equals(ativo))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<String> findGenerosDistintos() {
        return tenisRepository.listAll().stream()
                .map(Tenis::getGenero)
                .distinct()
                .collect(Collectors.toList());
    }
    
    public Long countTenisAtivos() {
        return tenisRepository.find("ativo", true).count();
    }
    
    public Long countTenisInativos() {
        return tenisRepository.find("ativo", false).count();
    }
    
    public Long countTotalTenis() {
        return tenisRepository.count();
    }
    
    public BigDecimal findPrecoMedio() {
        List<Tenis> tenisAtivos = tenisRepository.find("ativo", true).list();
        if (tenisAtivos.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal soma = tenisAtivos.stream()
                .map(Tenis::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return soma.divide(BigDecimal.valueOf(tenisAtivos.size()), 2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal findPrecoMaximo() {
        // CORREÇÃO: firstResult() retorna Tenis diretamente, não Optional
        Tenis tenisMaisCaro = tenisRepository.find("ativo = true order by preco desc").firstResult();
        return tenisMaisCaro != null ? tenisMaisCaro.getPreco() : BigDecimal.ZERO;
    }
    
    public BigDecimal findPrecoMinimo() {
        // CORREÇÃO: firstResult() retorna Tenis diretamente, não Optional
        Tenis tenisMaisBarato = tenisRepository.find("ativo = true order by preco asc").firstResult();
        return tenisMaisBarato != null ? tenisMaisBarato.getPreco() : BigDecimal.ZERO;
    }
    
    public EstatisticasTenis getEstatisticas() {
        Long total = this.countTotalTenis();
        Long ativos = this.countTenisAtivos();
        Long inativos = this.countTenisInativos();
        BigDecimal precoMedio = this.findPrecoMedio();
        BigDecimal precoMaximo = this.findPrecoMaximo();
        BigDecimal precoMinimo = this.findPrecoMinimo();
        
        return new EstatisticasTenis(total, ativos, inativos, precoMedio, precoMaximo, precoMinimo);
    }
    
    public EstatisticasCompletas getEstatisticasCompletas() {
        EstatisticasTenis estatisticas = this.getEstatisticas();
        // Implementações simplificadas para estatísticas por categoria
        List<Object[]> estatisticasPorEsporte = List.of();
        List<Object[]> estatisticasPorCor = List.of();
        List<Object[]> estatisticasPorGenero = List.of();
        
        return new EstatisticasCompletas(estatisticas, estatisticasPorEsporte, 
                                       estatisticasPorCor, estatisticasPorGenero);
    }
    
    // MÉTODOS AUXILIARES
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
            tenis.getImagemUrl(),
            tenis.getCor().getId(),
            tenis.getCor().getNome(),
            tenis.getCor().getCodigoHex(),
            tenis.getEsporte().getId(),
            tenis.getEsporte().getNome(),
            tenis.getMarca().getId(),
            tenis.getMarca().getNome(),
            tenis.getModelo().getId(),
            tenis.getModelo().getNome()
        );
    }
    
    private String getFileExtension(String fileName) {
        if (fileName == null) return ".jpg";
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex).toLowerCase() : ".jpg";
    }
    
    private String extractObjectNameFromUrl(String url) {
        if (url == null || url.isEmpty()) return null;
        // Lógica simplificada para extrair objectName da URL
        String[] parts = url.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : null;
    }
    
    private String extractFileName(InputPart inputPart) {
        try {
            Map<String, List<String>> headers = inputPart.getHeaders();
            List<String> contentDispositionHeaders = headers.get("Content-Disposition");
            if (contentDispositionHeaders != null && !contentDispositionHeaders.isEmpty()) {
                String contentDisposition = contentDispositionHeaders.get(0);
                String[] parts = contentDisposition.split(";");
                for (String part : parts) {
                    if (part.trim().startsWith("filename")) {
                        String[] keyValue = part.split("=");
                        if (keyValue.length > 1) {
                            return keyValue[1].trim().replaceAll("\"", "");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao extrair nome do arquivo: " + e.getMessage());
        }
        return "unknown";
    }
    
    // CLASSES INTERNAS PARA ESTATÍSTICAS
    public static class EstatisticasTenis {
        public Long total;
        public Long ativos;
        public Long inativos;
        public BigDecimal precoMedio;
        public BigDecimal precoMaximo;
        public BigDecimal precoMinimo;
        
        public EstatisticasTenis(Long total, Long ativos, Long inativos, 
                               BigDecimal precoMedio, 
                               BigDecimal precoMaximo, 
                               BigDecimal precoMinimo) {
            this.total = total;
            this.ativos = ativos;
            this.inativos = inativos;
            this.precoMedio = precoMedio;
            this.precoMaximo = precoMaximo;
            this.precoMinimo = precoMinimo;
        }
    }
    
    public static class EstatisticasCompletas {
        public EstatisticasTenis gerais;
        public List<Object[]> porEsporte;
        public List<Object[]> porCor;
        public List<Object[]> porGenero;
        
        public EstatisticasCompletas(EstatisticasTenis gerais, List<Object[]> porEsporte,
                                   List<Object[]> porCor, List<Object[]> porGenero) {
            this.gerais = gerais;
            this.porEsporte = porEsporte;
            this.porCor = porCor;
            this.porGenero = porGenero;
        }
    }
}