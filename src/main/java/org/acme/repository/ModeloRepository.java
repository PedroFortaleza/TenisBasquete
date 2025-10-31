package org.acme.repository;

import org.acme.model.Modelo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ModeloRepository implements PanacheRepository<Modelo> {
    
    public List<Modelo> findByMarca(Long marcaId) {
        return find("marca.id", marcaId).list();
    }
    
    public List<Modelo> findByNome(String nome) {
        return find("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%").list();
    }
}