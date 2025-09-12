// src/main/java/com/yourproject/resource/PedidoResource.java
package org.acme.resource;

import org.acme.dto.CriarPedidoDTO;
import org.acme.dto.PedidoDTO;
import org.acme.enums.StatusPedido;
import org.acme.service.PedidoService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Inject
    PedidoService pedidoService;

    @GET
    public List<PedidoDTO> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            PedidoDTO pedido = pedidoService.buscarPorId(id);
            return Response.ok(pedido).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/usuario/{usuarioId}")
    public List<PedidoDTO> buscarPorUsuario(@PathParam("usuarioId") Long usuarioId) {
        return pedidoService.buscarPorUsuario(usuarioId);
    }

    @POST
    public Response criarPedido(CriarPedidoDTO dto) {
        try {
            PedidoDTO pedido = pedidoService.criarPedido(dto);
            return Response.status(Response.Status.CREATED)
                    .entity(pedido)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}/status")
    public Response atualizarStatus(@PathParam("id") Long id, StatusPedido status) {
        try {
            PedidoDTO pedido = pedidoService.atualizarStatus(id, status);
            return Response.ok(pedido).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarPedido(@PathParam("id") Long id) {
        try {
            pedidoService.deletarPedido(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}