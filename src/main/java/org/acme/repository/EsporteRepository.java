package org.acme.repository;

import org.acme.model.Esporte;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EsporteRepository implements PanacheRepository<Esporte> {
    
    public Optional<Esporte> findByNome(String nome) {
        return find("nome", nome).firstResultOptional();
    }
    
    public List<Esporte> findAtivos() {
        return find("ativo", true).list();
    }
    
    public List<Esporte> findByNomeContaining(String nome) {
        return find("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%").list();
    }
    
    public boolean existsByNome(String nome) {
        return count("nome", nome) > 0;
    }
}