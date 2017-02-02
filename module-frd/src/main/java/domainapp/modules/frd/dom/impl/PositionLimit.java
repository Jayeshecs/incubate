/**
 * 
 */
package domainapp.modules.frd.dom.impl;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.services.eventbus.ObjectCreatedEvent;
import org.apache.isis.applib.services.eventbus.ObjectLoadedEvent;
import org.apache.isis.applib.services.eventbus.ObjectPersistedEvent;
import org.apache.isis.applib.services.eventbus.ObjectPersistingEvent;
import org.apache.isis.applib.services.eventbus.ObjectRemovingEvent;
import org.apache.isis.applib.services.eventbus.ObjectUpdatedEvent;
import org.apache.isis.applib.services.eventbus.ObjectUpdatingEvent;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.modules.cmn.dom.impl.BasePersistableEntity;
import domainapp.modules.cmn.dom.impl.Profile;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jayesh
 *
 */
@javax.jdo.annotations.PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        table = "FRD_POSITION_LIMIT"
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
@DomainObject(
        objectType = "frd.PositionLimit",
        auditing = Auditing.ENABLED,
        bounded = false,
        updatedLifecycleEvent = PositionLimit.UpdatedEvent.class
)
@Slf4j
public class PositionLimit extends BasePersistableEntity implements Comparable<PositionLimit> {

    @javax.jdo.annotations.Column(allowsNull = "false", name = "PROFILE_ID")
    @Property(editing = Editing.DISABLED)
    @Getter @Setter
    private Profile profile;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 255)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Contract name to which position limit is applicable")
    @Getter @Setter
    private String contractName;

    @javax.jdo.annotations.Column(allowsNull = "true", name = "RULE_CHAPTER_ID")
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Rule chapter e.g. 101, 102, 204D, etc.")
    @Getter @Setter
    private RuleChapter ruleChapter;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Code identifying the associated commodity e.g. 48, 62, OPF, etc.")
    @Getter @Setter
    private String commodityCode;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Contract size e.g. 40,000 Pounds, 25 Metric Tons, etc.")
    @Getter @Setter
    private Integer contractSize;

    @javax.jdo.annotations.Column(allowsNull = "true", name = "CONTRACT_UNIT_ID")
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Contract units e.g. Pounds, Metric Tons, Board Feet, etc.")
    @Getter @Setter
    private ContractUnit contractUnit;

    @javax.jdo.annotations.Column(allowsNull = "false", name = "ASSET_TYPE_ID")
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Asset type e.g. Futures, Eu. Option, Swap, etc.")
    @Getter @Setter
    private AssetType assetType;

    @javax.jdo.annotations.Column(allowsNull = "true", name = "SUB_ASSET_TYPE_ID")
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Sub asset type or Settlement e.g. Physically Delivered Futures, Financially Settled Futures, Financially Settled Swap, etc.")
    @Getter @Setter
    private SubAssetType subAssetType;

    @javax.jdo.annotations.Column(allowsNull = "true", name = "ASSET_GROUP_ID")
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Asset group e.g. Agriculture, Weather, Equity, FX, etc.")
    @Getter @Setter
    private AssetGroup assetGroup;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Yes for Diminishing Balance Contract")
    @Getter @Setter
    private Boolean diminishing;

    @javax.jdo.annotations.Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Reporting level for this position limit")
    @Getter @Setter
    private Integer reportingLevel;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Spot Month Position Comprised of Futures and Deliveries")
    @Getter @Setter
    private String spotMonthPosition;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Spot Month Aggregate Into Futures Equivalent Leg (1)")
    @Getter @Setter
    private String futureLeg1;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Spot Month Aggregate Into Futures Equivalent Leg (2)")
    @Getter @Setter
    private String futureLeg2;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Spot-Month Aggregate Into Ratio Leg (1)")
    @Getter @Setter
    private String ratioLeg1;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Spot-Month Aggregate Into Ratio Leg (2)")
    @Getter @Setter
    private String ratioLeg2;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Spot-Month Accountability Level")
    @Getter @Setter
    private String accountabilityLevel;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Daily Accountability Level (For Daily Contract)")
    @Getter @Setter
    private String accountabilityLevelDaily;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Initial Spot-Month Limit (In Net Futures Equivalents) Leg (1)/ Leg (2)")
    @Getter @Setter
    private String initialLimitNetFutures;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 128)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Initial Spot-Month Limit Effective Date e.g. Close of trading on the first business day following the first Friday of the contract month")
    @Getter @Setter
    private String initialLimitEffectiveDate;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 128)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Spot-Month Limit (In Contract Units) Leg (1) / Leg (2)")
    @Getter @Setter
    private String limitInContractUnit;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Second Spot-Month Limit (In Net Futures Equivalents) Leg (1)/ Leg (2)")
    @Getter @Setter
    private String secondLimitNetFutures;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 128)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Second Spot-Month Limit Effective Date")
    @Getter @Setter
    private String secondLimitEffectiveDate;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Single Month Aggregate Into Futures Equivalent Leg (1)")
    @Getter @Setter
    private String singleFutureLeg1;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Single Month Aggregate Into Futures Equivalent Leg (2)")
    @Getter @Setter
    private String singleFutureLeg2;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Single Month Aggregate Into Ratio Leg (1)")
    @Getter @Setter
    private String singleRatioLeg1;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Single Month Aggregate Into Ratio Leg (2)")
    @Getter @Setter
    private String singleRatioLeg2;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Single Month Accountability Level")
    @Getter @Setter
    private String singleAccountabilityLevel;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "Single Month Limit (In Net Futures Equivalents) Leg (1)/ Leg (2)")
    @Getter @Setter
    private String singleLimitNetFutures;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "AllMonth Aggregate Into Futures Equivalent Leg (1)")
    @Getter @Setter
    private String allFutureLeg1;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "All Month Aggregate Into Futures Equivalent Leg (2)")
    @Getter @Setter
    private String allFutureLeg2;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "All Month Aggregate Into Ratio Leg (1)")
    @Getter @Setter
    private String allRatioLeg1;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "All Month Aggregate Into Ratio Leg (2)")
    @Getter @Setter
    private String allRatioLeg2;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "All Month Accountability Level Leg (1) / Leg (2)")
    @Getter @Setter
    private String allAccountabilityLevel;

    @javax.jdo.annotations.Column(allowsNull = "true", length = 16)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(describedAs = "All Month Limit (In Net Futures Equivalents) Leg (1)/ Leg (2)")
    @Getter @Setter
    private String allLimitNetFutures;

	@SuppressWarnings("deprecation")
	@Override
	public int compareTo(PositionLimit profile) {
        return ObjectContracts.compare(this, profile, "profile", "code");
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
