package org.acme.resource;

import org.acme.dto.CriarUsuarioAdminDTO;
import org.acme.dto.LoginAdminDTO;
import org.acme.dto.UsuarioAdminDTO;
import org.acme.service.UsuarioAdminService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/admin/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioAdminResource {

    @Inject
    UsuarioAdminService usuarioAdminService;

    @GET
    public List<UsuarioAdminDTO> listarTodos() {
        return usuarioAdminService.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            UsuarioAdminDTO admin = usuarioAdminService.buscarPorId(id);
            return Response.ok(admin).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/username/{username}")
    public Response buscarPorUsername(@PathParam("username") String username) {
        Optional<UsuarioAdminDTO> admin = usuarioAdminService.buscarPorUsername(username);
        if (admin.isPresent()) {
            return Response.ok(admin.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Administrador não encontrado")
                .build();
    }

    @GET
    @Path("/nivel/{nivelAcesso}")
    public List<UsuarioAdminDTO> buscarPorNivelAcesso(@PathParam("nivelAcesso") String nivelAcesso) {
        return usuarioAdminService.buscarPorNivelAcesso(nivelAcesso);
    }

    @GET
    @Path("/ativos")
    public List<UsuarioAdminDTO> buscarAtivos() {
        return usuarioAdminService.buscarAtivos();
    }

    @POST
    public Response criarAdmin(CriarUsuarioAdminDTO dto) {
        try {
            UsuarioAdminDTO admin = usuarioAdminService.criarAdmin(dto);
            return Response.status(Response.Status.CREATED)
                    .entity(admin)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarAdmin(@PathParam("id") Long id, CriarUsuarioAdminDTO dto) {
        try {
            UsuarioAdminDTO admin = usuarioAdminService.atualizarAdmin(id, dto);
            return Response.ok(admin).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/{id}/status")
    public Response ativarDesativarAdmin(@PathParam("id") Long id, Boolean ativo) {
        try {
            UsuarioAdminDTO admin = usuarioAdminService.ativarDesativarAdmin(id, ativo);
            return Response.ok(admin).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginAdminDTO dto) {
        Optional<UsuarioAdminDTO> admin = usuarioAdminService.login(dto);
        if (admin.isPresent()) {
            return Response.ok(admin.get()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Credenciais inválidas ou usuário inativo")
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarAdmin(@PathParam("id") Long id) {
        try {
            usuarioAdminService.deletarAdmin(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}