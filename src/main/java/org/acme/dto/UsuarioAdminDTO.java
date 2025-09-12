package org.acme.dto;

import java.time.LocalDateTime;

public class UsuarioAdminDTO {
    private Long id;
    private String username;
    private String email;
    private String nomeCompleto;
    private String nivelAcesso;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimoLogin;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(String nivelAcesso) { this.nivelAcesso = nivelAcesso; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataUltimoLogin() { return dataUltimoLogin; }
    public void setDataUltimoLogin(LocalDateTime dataUltimoLogin) { this.dataUltimoLogin = dataUltimoLogin; }
}