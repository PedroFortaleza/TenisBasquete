package org.acme.service;

import org.acme.dto.ModeloDTO;
import org.acme.model.Modelo;
import org.acme.model.Marca;
import org.acme.repository.ModeloRepository;
import org.acme.repository.MarcaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ModeloService {
    
    @Inject
    ModeloRepository modeloRepository;
    
    @Inject
    MarcaRepository marcaRepository;
    
    @Inject
    MarcaService marcaService;
    
    public List<ModeloDTO> listAll() {
        try {
            System.out.println("Listando todos os modelos...");
            List<ModeloDTO> modelos = modeloRepository.listAll().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            System.out.println("Total de modelos encontrados: " + modelos.size());
            return modelos;
        } catch (Exception e) {
            System.err.println("Erro ao listar modelos: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public List<ModeloDTO> listAtivos() {
        try {
            System.out.println("Listando modelos ativos...");
            // Buscar todos e filtrar por marca ativa
            List<ModeloDTO> modelos = modeloRepository.listAll().stream()
                    .filter(modelo -> modelo.getMarca() != null && 
                                     Boolean.TRUE.equals(modelo.getMarca().getAtiva()))
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            System.out.println("Total de modelos ativos encontrados: " + modelos.size());
            return modelos;
        } catch (Exception e) {
            System.err.println("Erro ao listar modelos ativos: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public ModeloDTO findById(Long id) {
        try {
            System.out.println("Buscando modelo por ID: " + id);
            Modelo modelo = modeloRepository.findById(id);
            if (modelo != null) {
                System.out.println("Modelo encontrado: " + modelo.getNome());
                return toDTO(modelo);
            }
            System.out.println("Modelo não encontrado com ID: " + id);
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao buscar modelo por ID: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Transactional
    public ModeloDTO create(ModeloDTO modeloDTO) {
        try {
            System.out.println("Tentando criar modelo: " + modeloDTO.getNome());
            
            // Validações
            if (modeloDTO.getNome() == null || modeloDTO.getNome().trim().isEmpty()) {
                throw new RuntimeException("Nome do modelo é obrigatório");
            }
            
            // Verificar se já existe modelo com mesmo nome
            if (modeloRepository.findByNome(modeloDTO.getNome()).size() > 0) {
                throw new RuntimeException("Já existe um modelo com o nome: " + modeloDTO.getNome());
            }
            
            Modelo modelo = toEntity(modeloDTO);
            modeloRepository.persist(modelo);
            System.out.println("Modelo criado com ID: " + modelo.getId());
            
            return toDTO(modelo);
        } catch (Exception e) {
            System.err.println("Erro ao criar modelo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Transactional
    public ModeloDTO update(Long id, ModeloDTO modeloDTO) {
        try {
            System.out.println("Atualizando modelo ID: " + id);
            
            Modelo modelo = modeloRepository.findById(id);
            if (modelo != null) {
                // Validações
                if (modeloDTO.getNome() == null || modeloDTO.getNome().trim().isEmpty()) {
                    throw new RuntimeException("Nome do modelo é obrigatório");
                }
                
                // Verificar se outro modelo já tem esse nome (excluindo o atual)
                List<Modelo> modelosComMesmoNome = modeloRepository.findByNome(modeloDTO.getNome());
                boolean nomeEmUso = modelosComMesmoNome.stream()
                        .anyMatch(m -> !m.getId().equals(id));
                
                if (nomeEmUso) {
                    throw new RuntimeException("Já existe outro modelo com o nome: " + modeloDTO.getNome());
                }
                
                modelo.setNome(modeloDTO.getNome());
                
                // Se tiver marca no DTO, atualizar a relação
                if (modeloDTO.getMarcaId() != null) {
                    Marca marca = marcaRepository.findById(modeloDTO.getMarcaId());
                    if (marca == null) {
                        throw new RuntimeException("Marca não encontrada com ID: " + modeloDTO.getMarcaId());
                    }
                    modelo.setMarca(marca);
                } else {
                    modelo.setMarca(null);
                }
                
                modeloRepository.persist(modelo);
                System.out.println("Modelo atualizado com sucesso: " + modelo.getNome());
                return toDTO(modelo);
            }
            
            System.out.println("Modelo não encontrado para atualização, ID: " + id);
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar modelo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Transactional
    public boolean delete(Long id) {
        try {
            System.out.println("Deletando modelo ID: " + id);
            boolean deleted = modeloRepository.deleteById(id);
            if (deleted) {
                System.out.println("Modelo deletado com sucesso, ID: " + id);
            } else {
                System.out.println("Modelo não encontrado para deleção, ID: " + id);
            }
            return deleted;
        } catch (Exception e) {
            System.err.println("Erro ao deletar modelo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public List<ModeloDTO> findByMarca(Long marcaId) {
        try {
            System.out.println("Buscando modelos por marca ID: " + marcaId);
            List<ModeloDTO> modelos = modeloRepository.findByMarca(marcaId).stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            System.out.println("Modelos encontrados para marca " + marcaId + ": " + modelos.size());
            return modelos;
        } catch (Exception e) {
            System.err.println("Erro ao buscar modelos por marca: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public List<ModeloDTO> findByNome(String nome) {
        try {
            System.out.println("Buscando modelos por nome: " + nome);
            List<ModeloDTO> modelos = modeloRepository.findByNome(nome).stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            System.out.println("Modelos encontrados com nome '" + nome + "': " + modelos.size());
            return modelos;
        } catch (Exception e) {
            System.err.println("Erro ao buscar modelos por nome: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // MÉTODO ADICIONADO PARA O CADASTRO DE TÊNIS
    public Modelo buscarModeloEntity(Long id) {
        try {
            System.out.println("Buscando entidade Modelo por ID: " + id);
            Modelo modelo = modeloRepository.findById(id);
            if (modelo == null) {
                throw new RuntimeException("Modelo não encontrado com ID: " + id);
            }
            // Verificar se a marca está ativa
            if (modelo.getMarca() != null && Boolean.FALSE.equals(modelo.getMarca().getAtiva())) {
                throw new RuntimeException("Modelo com ID " + id + " pertence a uma marca inativa");
            }
            return modelo;
        } catch (Exception e) {
            System.err.println("Erro ao buscar entidade modelo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // MÉTODO ADICIONADO PARA CRIAR MODELO COM MARCA
    @Transactional
    public ModeloDTO criarModeloComMarca(String nomeModelo, Long marcaId) {
        try {
            System.out.println("Criando modelo '" + nomeModelo + "' para marca ID: " + marcaId);
            
            if (nomeModelo == null || nomeModelo.trim().isEmpty()) {
                throw new RuntimeException("Nome do modelo é obrigatório");
            }
            
            // Buscar marca
            Marca marca = marcaService.buscarMarcaEntity(marcaId);
            
            // Verificar se já existe modelo com mesmo nome
            if (modeloRepository.findByNome(nomeModelo).size() > 0) {
                throw new RuntimeException("Já existe um modelo com o nome: " + nomeModelo);
            }
            
            Modelo modelo = new Modelo();
            modelo.setNome(nomeModelo);
            modelo.setMarca(marca);
            
            modeloRepository.persist(modelo);
            System.out.println("Modelo criado com ID: " + modelo.getId());
            
            return toDTO(modelo);
        } catch (Exception e) {
            System.err.println("Erro ao criar modelo com marca: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // MÉTODO ADICIONADO PARA VERIFICAR EXISTÊNCIA
    public boolean existsById(Long id) {
        return modeloRepository.findById(id) != null;
    }
    
    // MÉTODO ADICIONADO PARA BUSCAR OU CRIAR MODELO
    @Transactional
    public Modelo buscarOuCriarModelo(String nomeModelo, Long marcaId) {
        try {
            System.out.println("Buscando ou criando modelo: " + nomeModelo + " para marca: " + marcaId);
            
            // Primeiro tenta buscar por nome
            List<Modelo> modelosExistentes = modeloRepository.findByNome(nomeModelo);
            if (!modelosExistentes.isEmpty()) {
                Modelo modeloExistente = modelosExistentes.get(0);
                System.out.println("Modelo já existe: " + modeloExistente.getId());
                return modeloExistente;
            }
            
            // Se não existe, cria novo
            Marca marca = marcaService.buscarMarcaEntity(marcaId);
            
            Modelo novoModelo = new Modelo();
            novoModelo.setNome(nomeModelo);
            novoModelo.setMarca(marca);
            
            modeloRepository.persist(novoModelo);
            System.out.println("Novo modelo criado: " + novoModelo.getId());
            
            return novoModelo;
        } catch (Exception e) {
            System.err.println("Erro ao buscar ou criar modelo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    private ModeloDTO toDTO(Modelo modelo) {
        ModeloDTO dto = new ModeloDTO();
        dto.setId(modelo.getId());
        dto.setNome(modelo.getNome());
        
        if (modelo.getMarca() != null) {
            dto.setMarcaId(modelo.getMarca().getId());
            dto.setMarcaNome(modelo.getMarca().getNome());
        }
        
        return dto;
    }
    
    private Modelo toEntity(ModeloDTO dto) {
        Modelo modelo = new Modelo();
        modelo.setNome(dto.getNome());
        
        if (dto.getMarcaId() != null) {
            Marca marca = marcaRepository.findById(dto.getMarcaId());
            if (marca != null) {
                modelo.setMarca(marca);
            } else {
                System.err.println("Atenção: Marca com ID " + dto.getMarcaId() + " não encontrada");
            }
        }
        
        return modelo;
    }
    
    // Método para criar modelo sem marca (mais simples)
    @Transactional
    public ModeloDTO criarModeloSimples(String nome) {
        try {
            System.out.println("Criando modelo simples: " + nome);
            
            if (nome == null || nome.trim().isEmpty()) {
                throw new RuntimeException("Nome do modelo é obrigatório");
            }
            
            Modelo modelo = new Modelo();
            modelo.setNome(nome);
            
            modeloRepository.persist(modelo);
            System.out.println("Modelo simples criado com ID: " + modelo.getId());
            
            return toDTO(modelo);
        } catch (Exception e) {
            System.err.println("Erro ao criar modelo simples: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // Método para estatísticas
    public EstatisticasModelo getEstatisticas() {
        try {
            long total = modeloRepository.count();
            long comMarca = modeloRepository.find("marca is not null").count();
            long semMarca = total - comMarca;
            
            EstatisticasModelo estatisticas = new EstatisticasModelo(total, comMarca, semMarca);
            System.out.println("Estatísticas de modelos: " + estatisticas);
            
            return estatisticas;
        } catch (Exception e) {
            System.err.println("Erro ao obter estatísticas: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public static class EstatisticasModelo {
        public Long total;
        public Long comMarca;
        public Long semMarca;
        
        public EstatisticasModelo(Long total, Long comMarca, Long semMarca) {
            this.total = total;
            this.comMarca = comMarca;
            this.semMarca = semMarca;
        }
        
        @Override
        public String toString() {
            return String.format("Total: %d, Com Marca: %d, Sem Marca: %d", total, comMarca, semMarca);
        }
    }
}