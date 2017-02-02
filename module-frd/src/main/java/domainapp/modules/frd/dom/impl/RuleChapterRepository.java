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
        repositoryFor = RuleChapter.class
)
public class RuleChapterRepository extends AbstractLookupByCodeRepository<RuleChapter> {

	public RuleChapterRepository() {
		super(RuleChapter.class);
	}

	@Override
	protected RuleChapter create() {
		return new RuleChapter();
	}

}
