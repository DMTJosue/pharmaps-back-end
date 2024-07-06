package org.sale;

import jakarta.persistence.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/phmps")
public class PharmapsResource {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private final EntityManager em = emf.createEntityManager();

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        try {
            em.getTransaction().begin();
            // Insérer la logique spécifique à register_api.php
            em.persist(user);
            em.getTransaction().commit();
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.serverError().entity("Erreur lors de l'inscription de l'utilisateur").build();
        } finally {
            em.close();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);

            User user = query.getSingleResult();

            // Construire la réponse
            return Response.ok()
                    .entity(Map.of(
                            "value", 1,
                            "message", "Connexion reussi",
                            "userId", user.getId(),
                            "fullname", user.getFullname(),
                            "email", user.getEmail()
                    ))
                    .build();
        } catch (NoResultException e) {
            // Utilisateur non trouvé
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of(
                            "value", 0,
                            "message", "Email ou mot de passe incorrect"
                    ))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Erreur lors de la connexion de l'utilisateur").build();
        } finally {
            em.close();
        }
    }
}
