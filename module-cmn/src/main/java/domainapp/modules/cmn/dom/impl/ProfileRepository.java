/**
 * 
 */
package domainapp.modules.cmn.dom.impl;

import java.util.List;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

/**
 * @author Jayesh
 *
 */
@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Profile.class
)
public class ProfileRepository extends AbstractRepository<Profile>{

    public ProfileRepository() {
		super(Profile.class);
	}

    public List<Profile> findByCode(final String code) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                		getClazz(),
                        "findByCode",
                        "code", code));
    }

    public Profile create(final String code, String name) {
        final Profile bean = new Profile();
        bean.setCode(code);
        bean.setName(name);
        return create(bean);
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
    @javax.inject.Inject
    ServiceRegistry2 serviceRegistry;

}
