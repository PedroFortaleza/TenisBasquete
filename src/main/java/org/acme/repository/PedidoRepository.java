package org.acme.repository;

import org.acme.model.Pedido;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {

    public List<Pedido> findByUsuarioId(Long usuarioId) {
        return find("usuario.id", usuarioId).list();
    }

    public List<Pedido> findByStatus(String status) {
        return find("status", status).list();
    }
}