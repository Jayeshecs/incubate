/**
 * 
 */
package domainapp.modules.frd.dom.impl;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.eventbus.ObjectCreatedEvent;
import org.apache.isis.applib.services.eventbus.ObjectLoadedEvent;
import org.apache.isis.applib.services.eventbus.ObjectPersistedEvent;
import org.apache.isis.applib.services.eventbus.ObjectPersistingEvent;
import org.apache.isis.applib.services.eventbus.ObjectRemovingEvent;
import org.apache.isis.applib.services.eventbus.ObjectUpdatedEvent;
import org.apache.isis.applib.services.eventbus.ObjectUpdatingEvent;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.modules.cmn.dom.impl.BasePersistableEntity;
import domainapp.modules.cmn.dom.impl.ILookupEntity;
import domainapp.modules.frd.IFRDDomainConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayesh
 *
 */
@javax.jdo.annotations.PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        table = "FRD_RULE_CHAPTER"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.INCREMENT,
        column="id")
@javax.jdo.annotations.Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@javax.jdo.annotations.Inheritance(
		strategy = InheritanceStrategy.NEW_TABLE
		)
@javax.jdo.annotations.Queries({
    @javax.jdo.annotations.Query(
            name = "findByCode",
            value = "SELECT "
                    + "FROM " +IFRDDomainConstants.PKG_FRD_MODULE + ".impl.RuleChapter"
                    + "WHERE code.indexOf(:code) >= 0 ")
})
@javax.jdo.annotations.Unique(name="RuleChapter_code_UNQ", members = {"code"})
@DomainObject(
        objectType = "frd.RuleChapter",
        auditing = Auditing.ENABLED,
        bounded = true,
        updatedLifecycleEvent = RuleChapter.UpdatedEvent.class
)
public class RuleChapter extends BasePersistableEntity implements Comparable<RuleChapter>, ILookupEntity {

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
    private String code;

    @javax.jdo.annotations.Column(allowsNull = "false", length = 100)
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    @Title(prepend = "Rule chapter: ")
    private String name;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 255)
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private String description;
	
	@SuppressWarnings("deprecation")
	@Override
	public int compareTo(RuleChapter ruleChapter) {
        return ObjectContracts.compare(this, ruleChapter, "code", "code");
	}
	
	//region > lifecycle events
	@SuppressWarnings("serial") public static class CreatedEvent extends ObjectCreatedEvent<PositionLimit> {}
	@SuppressWarnings("serial") public static class LoadedEvent extends ObjectLoadedEvent<PositionLimit> {}
	@SuppressWarnings("serial") public static class PersistedEvent extends ObjectPersistedEvent<PositionLimit> {}
	@SuppressWarnings("serial") public static class PersistingEvent extends ObjectPersistingEvent<PositionLimit> {}
	@SuppressWarnings("serial") public static class UpdatedEvent extends ObjectUpdatedEvent<PositionLimit> {}
	@SuppressWarnings("serial") public static class UpdatingEvent extends ObjectUpdatingEvent<PositionLimit> {}
	@SuppressWarnings("serial") public static class RemovingEvent extends ObjectRemovingEvent<PositionLimit> {}
    //endregion

}
