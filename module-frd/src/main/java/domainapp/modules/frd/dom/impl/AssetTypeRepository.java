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
        repositoryFor = AssetType.class
)
public class AssetTypeRepository extends AbstractLookupByCodeRepository<AssetType> {

	public AssetTypeRepository() {
		super(AssetType.class);
	}

	@Override
	protected AssetType create() {
		return new AssetType();
	}

}
