/**
 * 
 */
package domainapp.modules.cmn.dom.impl;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.Identifier;
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

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jayesh
 *
 */
@javax.jdo.annotations.PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        table = "CMN_PROFILE_DEF"
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
                        + "FROM domainapp.modules.cmn.dom.impl.Profile "
                        + "WHERE code.indexOf(:code) >= 0 ")
})
@javax.jdo.annotations.Unique(name="Profile_code_UNQ", members = {"code"})
@DomainObject(
        objectType = "cmn.Profile",
        auditing = Auditing.ENABLED,
        bounded = true,
        updatedLifecycleEvent = Profile.UpdatedEvent.class
)
@Slf4j
public class Profile extends BasePersistableEntity implements Comparable<Profile>{

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
    private String code;

    @javax.jdo.annotations.Column(allowsNull = "false", length = 100)
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    @Title(prepend = "Profile: ")
    private String name;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 255)
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private String description;

	@SuppressWarnings("deprecation")
	@Override
	public int compareTo(Profile profile) {
        return ObjectContracts.compare(this, profile, "code");
	}
	
	//region > lifecycle events
	@SuppressWarnings("serial") public static class CreatedEvent extends ObjectCreatedEvent<Profile> {}
	@SuppressWarnings("serial") public static class LoadedEvent extends ObjectLoadedEvent<Profile> {}
	@SuppressWarnings("serial") public static class PersistedEvent extends ObjectPersistedEvent<Profile> {}
	@SuppressWarnings("serial") public static class PersistingEvent extends ObjectPersistingEvent<Profile> {}
	@SuppressWarnings("serial") public static class UpdatedEvent extends ObjectUpdatedEvent<Profile> {}
	@SuppressWarnings("serial") public static class UpdatingEvent extends ObjectUpdatingEvent<Profile> {}
	@SuppressWarnings("serial") public static class RemovingEvent extends ObjectRemovingEvent<Profile> {}
    //endregion
    
	//region > object-level validation
    /**
     * Prevent user from viewing another user's data.
     */
    public boolean hidden() {
        // previously we manually checked that the user couldn't modify an object owned by some other user.
        // however, with application tenancy support this is automatically taken care of by Isis.
        return false;
    }

    /**
     * Prevent user from modifying any other user's data.
     */
    public String disabled(final Identifier.Type identifierType){
        // previously we manually checked that the user couldn't modify an object owned by some other user.
        // however, with application tenancy support this is automatically taken care of by Isis.
        return null;
    }

    /**
     * In a real app, if this were actually a rule, then we'd expect that
     * invoking the {@link #completed() done} action would clear the {@link #getDueBy() dueBy}
     * property (rather than require the user to have to clear manually).
     */
    public String validate() {
//        if(isComplete() && getDueBy() != null) {
//            return "Due by date must be set to null if item has been completed";
//        }
        return null;
    }
    //endregion
    
    //region > lifecycle callbacks
    public void created() {
        log.debug("lifecycle callback: created: " + this.toString());
    }
    public void loaded() {
    	log.debug("lifecycle callback: loaded: " + this.toString());
    }
    public void persisting() {
    	log.debug("lifecycle callback: persisting: " + this.toString());
    }
    public void persisted() {
    	log.debug("lifecycle callback: persisted: " + this.toString());
    }
    public void updating() {
    	log.debug("lifecycle callback: updating: " + this.toString());
    }
    public void updated() {
    	log.debug("lifecycle callback: updated: " + this.toString());
    }
    public void removing() {
    	log.debug("lifecycle callback: removing: " + this.toString());
    }
    public void removed() {
    	log.debug("lifecycle callback: removed: " + this.toString());
    }
    //endregion
}
