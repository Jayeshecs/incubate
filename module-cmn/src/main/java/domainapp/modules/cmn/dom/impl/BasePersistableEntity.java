/**
 * 
 */
package domainapp.modules.cmn.dom.impl;

import java.util.Date;

import javax.jdo.InstanceCallbacks;
import javax.jdo.JDOHelper;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class representing base persistable entity containing common
 * properties presenting snapshot of activity detail like creation and
 * modification.
 * 
 * @author Jayesh
 *
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
@Slf4j
public abstract class BasePersistableEntity implements InstanceCallbacks {
	
    @javax.jdo.annotations.Column(allowsNull = "false")
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
	private Date dateCreation;
	
    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
	private Date dateModification;
	
    @javax.jdo.annotations.Column(allowsNull = "false", length = 100)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
	private String userCreation;
	
    @javax.jdo.annotations.Column(allowsNull = "true", length = 100)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
	private String userModification;
	
    @javax.jdo.annotations.Column(allowsNull = "false", length = 100)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
	private String programCreation;
	
    @javax.jdo.annotations.Column(allowsNull = "true", length = 100)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
	private String programModification;

	public static void initializeForCreate(BasePersistableEntity bean) {
		bean.setDateCreation(new Date());
		bean.setUserCreation("system"); // TODO
		bean.setProgramCreation("system"); // TODO
	}

	public static void initializeForModification(BasePersistableEntity bean) {
		bean.setDateModification(new Date());
		bean.setUserModification("system"); // TODO
		bean.setProgramModification("system"); // TODO
	}

	@Override
	public void jdoPreClear() {
		// DO NOTHING
	}

	@Override
	public void jdoPreDelete() {
		// DO NOTHING
	}

	@Override
	public void jdoPostLoad() {
		// DO NOTHING
	}

	@Override
	public void jdoPreStore() {
		if(JDOHelper.isNew(this)) {
    		BasePersistableEntity.initializeForCreate(this);
		} else {
			BasePersistableEntity.initializeForModification(this);
		}
	}
    
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
    	if(log.isDebugEnabled()) {
    		log.debug("lifecycle callback: created: " + this.toString());
    	}
    }
    
    public void loaded() {
    	if(log.isDebugEnabled()) {
    		log.debug("lifecycle callback: loaded: " + this.toString());
    	}
    }
    
    public void persisting() {
    	if(log.isDebugEnabled()) {
    		log.debug("lifecycle callback: persisting: " + this.toString());
    	}
    }
    
    public void persisted() {
    	if(log.isDebugEnabled()) {
    		log.debug("lifecycle callback: persisted: " + this.toString());
    	}
    }
    
    public void updating() {
    	if(log.isDebugEnabled()) {
    		log.debug("lifecycle callback: updating: " + this.toString());
    	}
    }
    
    public void updated() {
    	if(log.isDebugEnabled()) {
    		log.debug("lifecycle callback: updated: " + this.toString());
    	}
    }
    
    public void removing() {
    	if(log.isDebugEnabled()) {
    		log.debug("lifecycle callback: removing: " + this.toString());
    	}
    }
    
    public void removed() {
    	if(log.isDebugEnabled()) {
    		log.debug("lifecycle callback: removed: " + this.toString());
    	}
    }
    //endregion
}
