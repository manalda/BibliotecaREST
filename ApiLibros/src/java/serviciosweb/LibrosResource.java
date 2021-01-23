package serviciosweb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.json.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import modelo.Estanteria;
import modelo.Libro;
import persistencia.EstanteriaFacadeLocal;
import persistencia.LibroFacadeLocal;

/**
 * REST Web Service
 *
 * @author manue
 */
@Path("libros")
public class LibrosResource implements ContainerResponseFilter{
    @Context
    private UriInfo context;
    private final LibroFacadeLocal libroFacade = lookupLibroFacadeLocal();
    private final EstanteriaFacadeLocal estanteriaFacade = lookupEstanteriaFacadeLocal();
    
    public LibrosResource() {
    }
    
    
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type");
    }

    @GET
    @Produces("application/json")
    public Response getLibros(@QueryParam("nombre") String nombre, @QueryParam("autor") String autor) {
        ResponseBuilder respuesta = Response.status(Response.Status.OK);
        
        List<Libro> libros;
        if(nombre != null) {
            libros = libroFacade.findByNombre(nombre);
        } else if(autor != null) {
            libros = libroFacade.findByAutor(autor);
        } else {
            libros = libroFacade.findAll();
        }
        
        if(libros == null || libros.isEmpty()) {
            return respuesta.status(Response.Status.NOT_FOUND).build();
        }
        
        respuesta.entity(libros.toArray(new Libro[0]));
        return respuesta.build();
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getLibroById(@PathParam("id") short id) {
        ResponseBuilder respuesta = Response.status(Response.Status.OK);
        
        try {
            Libro l = libroFacade.find(id);
            respuesta.entity(l);
            return respuesta.build();
        } catch(Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    @POST
//    @Consumes("application/json")
//    public Response createLibro(JsonObject libro) {
//        try {
//            Libro l = new Libro();
//            l.setId((short)(libroFacade.count() + 1));
//            l.setNombre();
//        }
//    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response updateLibro(@PathParam("id") short id, JsonObject nuevo) {
        ResponseBuilder respuesta = Response.status(Response.Status.OK);
        try {
            Libro l = libroFacade.find(id);
            short ubicacion = (short)nuevo.getJsonObject("ubicacion").getInt("id");
            Estanteria e = estanteriaFacade.find(ubicacion);
            l.setNombre(nuevo.getString("nombre"));
            l.setAutor(nuevo.getString("autor"));
            l.setUbicacion(e);
            libroFacade.edit(l);
            return respuesta.build();
        } catch(Exception e) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    private LibroFacadeLocal lookupLibroFacadeLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (LibroFacadeLocal) c.lookup("java:global/ApiLibros/LibroFacade!persistencia.LibroFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private EstanteriaFacadeLocal lookupEstanteriaFacadeLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (EstanteriaFacadeLocal) c.lookup("java:global/ApiLibros/EstanteriaFacade!persistencia.EstanteriaFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}