package org.acme.resource;

import org.acme.dto.CriarEstoqueDTO;
import org.acme.dto.EstoqueDTO;
import org.acme.service.EstoqueService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/estoque")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstoqueResource {

    @Inject
    EstoqueService estoqueService;

    @GET
    public List<EstoqueDTO> listarTodos() {
        return estoqueService.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            EstoqueDTO estoque = estoqueService.buscarPorId(id);
            return Response.ok(estoque).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/modelo/{modeloId}")
    public List<EstoqueDTO> buscarPorModelo(@PathParam("modeloId") Long modeloId) {
        return estoqueService.buscarPorModelo(modeloId);
    }

    @GET
    @Path("/disponiveis")
    public List<EstoqueDTO> buscarDisponiveis() {
        return estoqueService.buscarDisponiveis();
    }

    @POST
    public Response criarEstoque(CriarEstoqueDTO dto) {
        try {
            EstoqueDTO estoque = estoqueService.criarEstoque(dto);
            return Response.status(Response.Status.CREATED)
                    .entity(estoque)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarEstoque(@PathParam("id") Long id, CriarEstoqueDTO dto) {
        try {
            EstoqueDTO estoque = estoqueService.atualizarEstoque(id, dto);
            return Response.ok(estoque).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/{id}/quantidade")
    public Response atualizarQuantidade(@PathParam("id") Long id, Integer quantidade) {
        try {
            EstoqueDTO estoque = estoqueService.atualizarQuantidade(id, quantidade);
            return Response.ok(estoque).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarEstoque(@PathParam("id") Long id) {
        try {
            estoqueService.deletarEstoque(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}