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
package society.integtests.tests.modules.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.wrapper.DisabledException;
import org.apache.isis.applib.services.wrapper.InvalidException;
import org.apache.isis.core.metamodel.services.jdosupport.Persistable_datanucleusIdLong;
import org.apache.isis.core.metamodel.services.jdosupport.Persistable_datanucleusVersionTimestamp;
import org.junit.Before;
import org.junit.Test;

import society.dom.member.Member;
import society.fixture.scenarios.demo.DemoFixture;
import society.integtests.tests.DomainAppIntegTest;

public class MemberIntegTest extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    DemoFixture fs;
    Member memberPojo;
    Member memberWrapped;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        memberPojo = fs.getMembers().get(0);

        assertThat(memberPojo).isNotNull();
        memberWrapped = wrap(memberPojo);
    }

    public static class Name extends MemberIntegTest {

        @Test
        public void accessible() throws Exception {
            // when
            final String name = memberWrapped.getName();
            // then
            assertThat(name).isNotNull();
        }

        @Test
        public void cannotBeUpdatedDirectly() throws Exception {

            // expect
            expectedExceptions.expect(DisabledException.class);

            // when
            memberWrapped.setName("new name");
        }
    }

    public static class UpdateName extends MemberIntegTest {

        @Test
        public void happyCase() throws Exception {

            // when
            memberWrapped.updateName("new name");

            // then
            assertThat(memberWrapped.getName()).isEqualTo("new name");
        }

        @Test
        public void failsValidation() throws Exception {

            // expect
            expectedExceptions.expect(InvalidException.class);
            expectedExceptions.expectMessage("Exclamation mark is not allowed");

            // when
            memberWrapped.updateName("new name!");
        }
    }


    public static class Title extends MemberIntegTest {

        @Inject
        DomainObjectContainer container;

        @Test
        public void interpolatesName() throws Exception {

            // given
            final String name = memberWrapped.getName();

            // when
            final String title = container.titleOf(memberWrapped);

            // then
            assertThat(title).isEqualTo("Object: " + name);
        }
    }

    public static class DataNucleusId extends MemberIntegTest {

        @Test
        public void shouldBePopulated() throws Exception {
            // when
            final Long id = mixin(Persistable_datanucleusIdLong.class, memberPojo).$$();
            // then
            assertThat(id).isGreaterThanOrEqualTo(0);
        }
    }

    public static class DataNucleusVersionTimestamp extends MemberIntegTest {

        @Test
        public void shouldBePopulated() throws Exception {
            // when
            final Timestamp timestamp = mixin(Persistable_datanucleusVersionTimestamp.class, memberPojo).$$();
            // then
            assertThat(timestamp).isNotNull();
        }
    }


}