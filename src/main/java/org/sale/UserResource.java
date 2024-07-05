package org.sale;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
@Path("/users")
public class UserResource {
    private  final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private final EntityManager em = emf.createEntityManager();
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<User> users = em.createQuery("select u from User u", User.class).getResultList();
        em.close();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") int id) {
        User user = em.find(User.class, id);
        em.close();
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, User updatedUser) {
        User existingUser = em.find(User.class, id);
        if (existingUser == null) {
            em.close();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        em.getTransaction().begin();
        existingUser.setFullname(updatedUser.getFullname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        em.getTransaction().commit();
        em.close();

        return Response.ok(existingUser).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") int id) {
        User user = em.find(User.class, id);
        if (user == null) {
            em.close();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        em.getTransaction().begin();
        em.remove(user);
        em.getTransaction().commit();
        em.close();

        return Response.noContent().build();
    }
}
