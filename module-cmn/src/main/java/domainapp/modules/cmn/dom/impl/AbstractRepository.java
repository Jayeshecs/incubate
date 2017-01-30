/**
 * 
 */
package domainapp.modules.cmn.dom.impl;

import java.util.List;

import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

import lombok.Getter;

/**
 * @author Jayesh
 *
 */
public abstract class AbstractRepository<T> {

	@Getter
	private Class<T> clazz;
	
	/**
	 * @param clazz Class of generic type T
	 */
	public AbstractRepository(Class<T> clazz) {
		this.clazz = clazz;
	}

    public List<T> listAll() {
        return repositoryService.allInstances(clazz);
    }

    /**
	 * Purpose of this method is to necessary service into given bean and
	 * persist as well in store.
	 * 
	 * @param bean
	 * @return
	 */
    public T create(T bean) {
        serviceRegistry.injectServicesInto(bean);
        repositoryService.persist(bean);
        return bean;
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
    @javax.inject.Inject
    ServiceRegistry2 serviceRegistry;
}
