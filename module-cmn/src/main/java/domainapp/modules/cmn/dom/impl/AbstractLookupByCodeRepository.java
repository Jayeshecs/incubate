/**
 * 
 */
package domainapp.modules.cmn.dom.impl;

import java.util.List;

import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

/**
 * @author Jayesh
 *
 */
public abstract class AbstractLookupByCodeRepository<T extends ILookupEntity> extends AbstractRepository<T> {

    public AbstractLookupByCodeRepository(Class<T> clazz) {
		super(clazz);
	}

    public List<T> findByCode(final String code) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                		getClazz(),
                        "findByCode",
                        "code", code));
    }

    public T create(final String code, String name) {
        final T bean = create();
        bean.setCode(code);
        bean.setName(name);
        return create(bean);
    }
    
    protected abstract T create();

    @javax.inject.Inject
    RepositoryService repositoryService;
    @javax.inject.Inject
    ServiceRegistry2 serviceRegistry;

}
