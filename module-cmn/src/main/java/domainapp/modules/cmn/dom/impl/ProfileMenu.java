/**
 * 
 */
package domainapp.modules.cmn.dom.impl;

import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

/**
 * @author Jayesh
 *
 */

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        repositoryFor = Profile.class
)
@DomainServiceLayout(
        named = "Profiles",
        menuOrder = "1"
)
public class ProfileMenu {


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Profile> listAll() {
        return profileRepository.listAll();
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<Profile> findByCode(
            @ParameterLayout(named="Code")
            final String code
    ) {
        return profileRepository.findByCode(code);
    }


    public static class CreateDomainEvent extends ActionDomainEvent<ProfileMenu> {}
    
    @Action(domainEvent = CreateDomainEvent.class)
    @MemberOrder(sequence = "3")
    public Profile create(
            @ParameterLayout(named="Code")
            final String code,
            @ParameterLayout(named="Name")
            final String name
            ) {
        return profileRepository.create(code, name);
    }

    @javax.inject.Inject
    ProfileRepository profileRepository;

}
