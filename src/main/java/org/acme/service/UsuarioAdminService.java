package org.acme.service;

import org.acme.dto.CriarUsuarioAdminDTO;
import org.acme.dto.LoginAdminDTO;
import org.acme.dto.UsuarioAdminDTO;
import org.acme.model.UsuarioAdmin;
import org.acme.repository.UsuarioAdminRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioAdminService {

    @Inject
    UsuarioAdminRepository usuarioAdminRepository;

    public List<UsuarioAdminDTO> listarTodos() {
        return usuarioAdminRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioAdminDTO buscarPorId(Long id) {
        UsuarioAdmin admin = usuarioAdminRepository.findById(id);
        if (admin == null) {
            throw new RuntimeException("Administrador não encontrado");
        }
        return toDTO(admin);
    }

    public Optional<UsuarioAdminDTO> buscarPorUsername(String username) {
        return usuarioAdminRepository.findByUsername(username)
                .map(this::toDTO);
    }

    public Optional<UsuarioAdminDTO> buscarPorEmail(String email) {
        return usuarioAdminRepository.findByEmail(email)
                .map(this::toDTO);
    }

    public List<UsuarioAdminDTO> buscarPorNivelAcesso(String nivelAcesso) {
        return usuarioAdminRepository.findByNivelAcesso(nivelAcesso).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioAdminDTO> buscarAtivos() {
        return usuarioAdminRepository.findAtivos().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioAdminDTO criarAdmin(CriarUsuarioAdminDTO dto) {
        // Verificar se username já existe
        if (usuarioAdminRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username já está em uso");
        }

        // Verificar se email já existe
        if (usuarioAdminRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já está em uso");
        }

        // Validar nível de acesso
        if (!isNivelAcessoValido(dto.getNivelAcesso())) {
            throw new RuntimeException("Nível de acesso inválido. Use: ADMIN, SUPER_ADMIN, MODERADOR");
        }

        UsuarioAdmin admin = new UsuarioAdmin();
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setSenha(dto.getSenha()); // Em produção, usar hash para senha
        admin.setNomeCompleto(dto.getNomeCompleto());
        admin.setNivelAcesso(dto.getNivelAcesso().toUpperCase());
        admin.setAtivo(true);
        admin.setDataCriacao(LocalDateTime.now());

        usuarioAdminRepository.persist(admin);
        return toDTO(admin);
    }

    @Transactional
    public UsuarioAdminDTO atualizarAdmin(Long id, CriarUsuarioAdminDTO dto) {
        UsuarioAdmin admin = usuarioAdminRepository.findById(id);
        if (admin == null) {
            throw new RuntimeException("Administrador não encontrado");
        }

        // Verificar se novo username já existe (se for diferente do atual)
        if (!admin.getUsername().equals(dto.getUsername()) && 
            usuarioAdminRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username já está em uso");
        }

        // Verificar se novo email já existe (se for diferente do atual)
        if (!admin.getEmail().equals(dto.getEmail()) && 
            usuarioAdminRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já está em uso");
        }

        // Validar nível de acesso
        if (dto.getNivelAcesso() != null && !isNivelAcessoValido(dto.getNivelAcesso())) {
            throw new RuntimeException("Nível de acesso inválido. Use: ADMIN, SUPER_ADMIN, MODERADOR");
        }

        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            admin.setSenha(dto.getSenha());
        }
        admin.setNomeCompleto(dto.getNomeCompleto());
        if (dto.getNivelAcesso() != null) {
            admin.setNivelAcesso(dto.getNivelAcesso().toUpperCase());
        }

        usuarioAdminRepository.persist(admin);
        return toDTO(admin);
    }

    @Transactional
    public UsuarioAdminDTO ativarDesativarAdmin(Long id, Boolean ativo) {
        UsuarioAdmin admin = usuarioAdminRepository.findById(id);
        if (admin == null) {
            throw new RuntimeException("Administrador não encontrado");
        }

        admin.setAtivo(ativo);
        usuarioAdminRepository.persist(admin);
        return toDTO(admin);
    }

    @Transactional
    public UsuarioAdminDTO registrarLogin(Long id) {
        UsuarioAdmin admin = usuarioAdminRepository.findById(id);
        if (admin == null) {
            throw new RuntimeException("Administrador não encontrado");
        }

        admin.setDataUltimoLogin(LocalDateTime.now());
        usuarioAdminRepository.persist(admin);
        return toDTO(admin);
    }

    public Optional<UsuarioAdminDTO> login(LoginAdminDTO dto) {
        Optional<UsuarioAdmin> admin = usuarioAdminRepository.findByUsernameAndSenha(dto.getUsername(), dto.getSenha());
        
        if (admin.isPresent()) {
            // Registrar o login
            registrarLogin(admin.get().getId());
            return admin.map(this::toDTO);
        }
        
        return Optional.empty();
    }

    @Transactional
    public void deletarAdmin(Long id) {
        UsuarioAdmin admin = usuarioAdminRepository.findById(id);
        if (admin == null) {
            throw new RuntimeException("Administrador não encontrado");
        }
        usuarioAdminRepository.delete(admin);
    }

    private boolean isNivelAcessoValido(String nivelAcesso) {
        return nivelAcesso != null && 
               (nivelAcesso.equalsIgnoreCase("ADMIN") ||
                nivelAcesso.equalsIgnoreCase("SUPER_ADMIN") ||
                nivelAcesso.equalsIgnoreCase("MODERADOR"));
    }

    private UsuarioAdminDTO toDTO(UsuarioAdmin admin) {
        UsuarioAdminDTO dto = new UsuarioAdminDTO();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setEmail(admin.getEmail());
        dto.setNomeCompleto(admin.getNomeCompleto());
        dto.setNivelAcesso(admin.getNivelAcesso());
        dto.setAtivo(admin.getAtivo());
        dto.setDataCriacao(admin.getDataCriacao());
        dto.setDataUltimoLogin(admin.getDataUltimoLogin());
        return dto;
    }
}