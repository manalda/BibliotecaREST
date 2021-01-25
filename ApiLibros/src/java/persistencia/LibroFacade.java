package persistencia;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelo.Estanteria;
import modelo.Libro;

/**
 *
 * @author manalda
 */
@Stateless
public class LibroFacade extends AbstractFacade<Libro> implements LibroFacadeLocal {
    @PersistenceContext(unitName = "ApiLibrosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LibroFacade() {
        super(Libro.class);
    }

    @Override
    public List<Libro> findByNombre(String nombre) {
        Query consulta = em.createNamedQuery("Libro.findByNombre");
        consulta.setParameter("nombre", "%" + nombre + "%");
        return consulta.getResultList();
    }

    @Override
    public List<Libro> findByAutor(String autor) {
        Query consulta = em.createNamedQuery("Libro.findByAutor");
        consulta.setParameter("autor", "%" + autor + "%");
        return consulta.getResultList();
    }

    @Override
    public List<Libro> findByUbicacion(Estanteria ubicacion) {
        Query consulta = em.createNamedQuery("Libro.findByUbicacion");
        consulta.setParameter("ubicacion", ubicacion);
        return consulta.getResultList();
    }
    
}
