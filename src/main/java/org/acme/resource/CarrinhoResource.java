package org.acme.resource;

import org.acme.dto.CarrinhoDTO;
import org.acme.dto.AdicionarItemCarrinhoDTO;
import org.acme.service.CarrinhoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/carrinho")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarrinhoResource {

    @Inject
    CarrinhoService carrinhoService;

    @GET
    @Path("/usuario/{usuarioId}")
    public Response buscarCarrinho(@PathParam("usuarioId") Long usuarioId) {
        try {
            CarrinhoDTO carrinho = carrinhoService.buscarCarrinhoPorUsuario(usuarioId);
            return Response.ok(carrinho).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/usuario/{usuarioId}/contagem")
    public Response contarItens(@PathParam("usuarioId") Long usuarioId) {
        try {
            Integer quantidade = carrinhoService.contarItensNoCarrinho(usuarioId);
            return Response.ok(quantidade).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/usuario/{usuarioId}/total")
    public Response calcularTotal(@PathParam("usuarioId") Long usuarioId) {
        try {
            Double total = carrinhoService.calcularTotalCarrinho(usuarioId);
            return Response.ok(total).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/usuario/{usuarioId}/tenis/{tenisId}")
    public Response verificarTenisNoCarrinho(@PathParam("usuarioId") Long usuarioId, 
                                           @PathParam("tenisId") Long tenisId) {
        try {
            boolean estaNoCarrinho = carrinhoService.isTenisNoCarrinho(usuarioId, tenisId);
            return Response.ok(estaNoCarrinho).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/usuario/{usuarioId}/tenis/{tenisId}/quantidade")
    public Response obterQuantidadeTenis(@PathParam("usuarioId") Long usuarioId, 
                                       @PathParam("tenisId") Long tenisId) {
        try {
            Integer quantidade = carrinhoService.getQuantidadeTenisNoCarrinho(usuarioId, tenisId);
            return Response.ok(quantidade).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/usuario/{usuarioId}/validar-pedido")
    public Response validarParaPedido(@PathParam("usuarioId") Long usuarioId) {
        try {
            boolean valido = carrinhoService.validarCarrinhoParaPedido(usuarioId);
            return Response.ok(valido).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/usuario/{usuarioId}/itens")
    public Response adicionarItem(@PathParam("usuarioId") Long usuarioId, AdicionarItemCarrinhoDTO dto) {
        try {
            CarrinhoDTO carrinho = carrinhoService.adicionarItem(usuarioId, dto);
            return Response.ok(carrinho).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/usuario/{usuarioId}/itens/{tenisId}")
    public Response atualizarQuantidade(@PathParam("usuarioId") Long usuarioId, 
                                       @PathParam("tenisId") Long tenisId, 
                                       Integer quantidade) {
        try {
            CarrinhoDTO carrinho = carrinhoService.atualizarQuantidadeItem(usuarioId, tenisId, quantidade);
            return Response.ok(carrinho).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/usuario/{usuarioId}/itens/{tenisId}")
    public Response removerItem(@PathParam("usuarioId") Long usuarioId, 
                               @PathParam("tenisId") Long tenisId) {
        try {
            CarrinhoDTO carrinho = carrinhoService.removerItem(usuarioId, tenisId);
            return Response.ok(carrinho).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/usuario/{usuarioId}/limpar")
    public Response limparCarrinho(@PathParam("usuarioId") Long usuarioId) {
        try {
            CarrinhoDTO carrinho = carrinhoService.limparCarrinho(usuarioId);
            return Response.ok(carrinho).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/usuario/{usuarioId}/finalizar")
    public Response finalizarCarrinho(@PathParam("usuarioId") Long usuarioId) {
        try {
            CarrinhoDTO carrinho = carrinhoService.transferirCarrinhoParaPedido(usuarioId);
            return Response.ok(carrinho).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}