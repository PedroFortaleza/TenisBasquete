package org.acme.service;


import org.acme.dto.CriarUsuarioDTO;
import org.acme.dto.UsuarioDTO;
import org.acme.model.Usuario;
import org.acme.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.listAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return toDTO(usuario);
    }

    public Usuario buscarUsuarioEntity(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return usuario;
    }

    public Optional<UsuarioDTO> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(this::toDTO);
    }

    @Transactional
    public UsuarioDTO criarUsuario(CriarUsuarioDTO dto) {
        // Verificar se email já existe
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setNome(dto.getNome());
        usuario.setSenha(dto.getSenha()); // Em produção, usar hash para senha
        usuario.setDataCriacao(LocalDateTime.now());

        usuarioRepository.persist(usuario);
        return toDTO(usuario);
    }

    @Transactional
    public UsuarioDTO atualizarUsuario(Long id, CriarUsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuario.setSenha(dto.getSenha());
        }
        usuario.setDataAtualizacao(LocalDateTime.now());

        usuarioRepository.persist(usuario);
        return toDTO(usuario);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.delete(usuario);
    }

    public Optional<UsuarioDTO> login(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha)
                .map(this::toDTO);
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getNome());
        dto.setDataCriacao(usuario.getDataCriacao());
        dto.setDataAtualizacao(usuario.getDataAtualizacao());
        return dto;
    }
}