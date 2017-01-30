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

import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;

import lombok.Getter;
import lombok.Setter;

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
}
