package org.acme.repository;

import org.acme.model.Marca;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MarcaRepository implements PanacheRepository<Marca> {
    
    public Optional<Marca> findByNome(String nome) {
        return find("nome", nome).firstResultOptional();
    }
    
    public List<Marca> findByPais(String pais) {
        return find("paisOrigem", pais).list();
    }
    
    public List<Marca> findAtivas() {
        return find("ativa", true).list();
    }
    
    public List<Marca> findByNomeContaining(String nome) {
        return find("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%").list();
    }
    
    public boolean existsByNome(String nome) {
        return count("nome", nome) > 0;
    }
}