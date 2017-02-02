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
        repositoryFor = SubAssetType.class
)
public class SubAssetTypeRepository extends AbstractLookupByCodeRepository<SubAssetType> {

	public SubAssetTypeRepository() {
		super(SubAssetType.class);
	}

	@Override
	protected SubAssetType create() {
		return new SubAssetType();
	}

}
