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
        repositoryFor = AssetGroup.class
)
public class AssetGroupRepository extends AbstractLookupByCodeRepository<AssetGroup> {

	public AssetGroupRepository() {
		super(AssetGroup.class);
	}

	@Override
	protected AssetGroup create() {
		return new AssetGroup();
	}

}
