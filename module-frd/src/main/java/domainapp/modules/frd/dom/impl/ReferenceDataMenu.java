/**
 * 
 */
package domainapp.modules.frd.dom.impl;

import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
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
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Reference Data",
        menuOrder = "2",
        menuBar = MenuBar.PRIMARY
)
public class ReferenceDataMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<AssetType> listAllAssetType() {
        return assetTypeRepository.listAll();
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<AssetType> findByCodeAssetType(
            @ParameterLayout(named="Code")
            final String code
    ) {
        return assetTypeRepository.findByCode(code);
    }


    @SuppressWarnings("serial")
	public static class CreateAssetTypeDomainEvent extends ActionDomainEvent<AssetType> {}
    
    @Action(domainEvent = CreateAssetTypeDomainEvent.class)
    @MemberOrder(sequence = "3")
    public AssetType createAssetType(
            @ParameterLayout(named="Code")
            final String code,
            @ParameterLayout(named="Name")
            final String name
            ) {
        return assetTypeRepository.create(code, name);
    }

    @javax.inject.Inject
    AssetTypeRepository assetTypeRepository;

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "4")
    public List<AssetGroup> listAllAssetGroup() {
        return assetGroupRepository.listAll();
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "5")
    public List<AssetGroup> findByCodeAssetGroup(
            @ParameterLayout(named="Code")
            final String code
    ) {
        return assetGroupRepository.findByCode(code);
    }


    @SuppressWarnings("serial")
	public static class CreateAssetGroupDomainEvent extends ActionDomainEvent<AssetGroup> {}
    
    @Action(domainEvent = CreateAssetGroupDomainEvent.class)
    @MemberOrder(sequence = "6")
    public AssetGroup createAssetGroup(
            @ParameterLayout(named="Code")
            final String code,
            @ParameterLayout(named="Name")
            final String name
            ) {
        return assetGroupRepository.create(code, name);
    }

    @javax.inject.Inject
    AssetGroupRepository assetGroupRepository;

}
