/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package society.dom.member;

import java.io.IOException;
import java.util.Collection;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.jdo.metadata.TypeMetadata;
import javax.xml.bind.JAXBException;

import lombok.Getter;
import lombok.Setter;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.value.Clob;
import org.joda.time.LocalDate;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "society",
        table = "Member"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
//        strategy=VersionStrategy.VERSION_NUMBER,
        strategy= VersionStrategy.DATE_TIME,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM society.dom.member.Member "),
        @javax.jdo.annotations.Query(
                name = "findByNameContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM society.dom.member.Member "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM society.dom.member.Member "
                        + "WHERE name == :name ")
})
@javax.jdo.annotations.Unique(name="Member_name_UNQ", members = {"name"})
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
        // ,cssClassFa = "fa-flag" // use the .png instead
)
public class Member implements Comparable<Member> {


    public TranslatableString title() {
        return TranslatableString.tr("Object: {name}", "name", getName());
    }

    @Override
    public int compareTo(final Member other) {
        return ObjectContracts.compare(this, other, "name");
    }


    @javax.jdo.annotations.Column(allowsNull="false", length = 40)
    @Property
    @Getter @Setter
    private String name;

    @javax.jdo.annotations.Column(allowsNull="true")
    @Property
    @Getter @Setter
    private Integer integer;

    @javax.jdo.annotations.Column(allowsNull="true")
    @Property
    @Getter @Setter
    private LocalDate localDate;

    @javax.jdo.annotations.Column(allowsNull="true")
    @Property
    @Getter @Setter
    private Boolean flag;





    public static class UpdateNameDomainEvent extends ActionDomainEvent<Member> { }
    @Action(domainEvent = UpdateNameDomainEvent.class)
    public Member updateName(
            @Parameter(maxLength = 40)
            final String newName) {
        setName(newName);
        return this;
    }
    public String default0UpdateName() {
        return getName();
    }
    public TranslatableString validateUpdateName(final String name) {
        return name.contains("!")? TranslatableString.tr("Exclamation mark is not allowed"): null;
    }


    public static class UpdateIntegerDomainEvent extends ActionDomainEvent<Member> { }
    @Action(domainEvent = UpdateIntegerDomainEvent.class)
    public Member updateInteger(
            final Integer newInteger) {
        setInteger(newInteger);
        return this;
    }
    public Integer default0UpdateInteger() {
        return getInteger();
    }


    public static class UpdateLocalDateDomainEvent extends ActionDomainEvent<Member> { }
    @Action(domainEvent = UpdateLocalDateDomainEvent.class)
    public Member updateLocalDate(
            final LocalDate newLocaldate) {
        setLocalDate(newLocaldate);
        return this;
    }
    public LocalDate default0UpdateLocalDate() {
        return getLocalDate();
    }


    public static class UpdateBooleanDomainEvent extends ActionDomainEvent<Member> { }
    @Action(domainEvent = UpdateBooleanDomainEvent.class)
    public Member updateFlag(
            final Boolean newFlag) {
        setFlag(newFlag);
        return this;
    }
    public Boolean default0UpdateFlag() {
        return getFlag();
    }


    public static class DownloadJdoMetadataActionEvent extends org.apache.isis.applib.IsisApplibModule.ActionDomainEvent<Member> {}

    @Action(
            domainEvent = DownloadJdoMetadataActionEvent.class,
            semantics = SemanticsOf.SAFE,
            restrictTo = RestrictTo.PROTOTYPING
    )
    @ActionLayout(
            cssClassFa = "fa-download",
            position = ActionLayout.Position.PANEL_DROPDOWN
    )
    @MemberOrder(name = "Metadata", sequence = "710.1")
    public Clob $$(
            @ParameterLayout(named = ".jdo file name")
            final String fileName) throws JAXBException, IOException {

    	StringBuilder sb = new StringBuilder();
    	for(Class<?> clazz : getPersistenceManagerFactory().getManagedClasses()) {
    		final String objClassName = clazz.getName();
    		
    		final TypeMetadata metadata = getPersistenceManagerFactory().getMetadata(objClassName);
    		sb.append(metadata.toString());
    		sb.append("\n");
    	}

        return new Clob(fileName + ".jdo", "text/xml", sb.toString());
    }

    public String default0$$() {
        return "AllJdoMetadata.xml";
    }

    PersistenceManagerFactory getPersistenceManagerFactory() {
        return jdoSupport.getJdoPersistenceManager().getPersistenceManagerFactory();
    }

    @javax.inject.Inject
    IsisJdoSupport jdoSupport;
}
