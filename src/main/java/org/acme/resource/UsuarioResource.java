package org.acme.resource;

import org.acme.dto.CriarUsuarioDTO;
import org.acme.dto.UsuarioDTO;
import org.acme.service.UsuarioService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    @GET
    public List<UsuarioDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            UsuarioDTO usuario = usuarioService.buscarPorId(id);
            return Response.ok(usuario).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/email/{email}")
    public Response buscarPorEmail(@PathParam("email") String email) {
        Optional<UsuarioDTO> usuario = usuarioService.buscarPorEmail(email);
        if (usuario.isPresent()) {
            return Response.ok(usuario.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Usuário não encontrado")
                .build();
    }

    @POST
    public Response criarUsuario(CriarUsuarioDTO dto) {
        try {
            UsuarioDTO usuario = usuarioService.criarUsuario(dto);
            return Response.status(Response.Status.CREATED)
                    .entity(usuario)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarUsuario(@PathParam("id") Long id, CriarUsuarioDTO dto) {
        try {
            UsuarioDTO usuario = usuarioService.atualizarUsuario(id, dto);
            return Response.ok(usuario).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarUsuario(@PathParam("id") Long id) {
        try {
            usuarioService.deletarUsuario(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(@QueryParam("email") String email, @QueryParam("senha") String senha) {
        Optional<UsuarioDTO> usuario = usuarioService.login(email, senha);
        if (usuario.isPresent()) {
            return Response.ok(usuario.get()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Credenciais inválidas")
                .build();
    }
}