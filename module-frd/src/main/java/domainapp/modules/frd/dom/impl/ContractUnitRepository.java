/**
 * 
 */
package domainapp.modules.frd.dom.impl;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import domainapp.modules.cmn.dom.impl.AbstractLookupByCodeRepository;

/**
 * @author Jayesh
 *
 */
@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = ContractUnit.class
)
public class ContractUnitRepository extends AbstractLookupByCodeRepository<ContractUnit> {

	public ContractUnitRepository() {
		super(ContractUnit.class);
	}

	@Override
	protected ContractUnit create() {
		return new ContractUnit();
	}

}
