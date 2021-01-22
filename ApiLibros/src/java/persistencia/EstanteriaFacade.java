package persistencia;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Estanteria;

/**
 *
 * @author manalda
 */
@Stateless
public class EstanteriaFacade extends AbstractFacade<Estanteria> implements EstanteriaFacadeLocal {
    @PersistenceContext(unitName = "ApiLibrosPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstanteriaFacade() {
        super(Estanteria.class);
    }
    
}
