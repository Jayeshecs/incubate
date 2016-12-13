/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package society.dom.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MemberRepositoryTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    MemberRepository memberRepository;

    @Before
    public void setUp() throws Exception {
        memberRepository = new MemberRepository();
        memberRepository.container = mockContainer;
    }

    public static class Create extends MemberRepositoryTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final Member Member = new Member();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(Member.class);
                    inSequence(seq);
                    will(returnValue(Member));

                    oneOf(mockContainer).persistIfNotAlready(Member);
                    inSequence(seq);
                }
            });

            // when
            final Member obj = memberRepository.create("Foobar");

            // then
            assertThat(obj).isEqualTo(Member);
            assertThat(obj.getName()).isEqualTo("Foobar");
        }

    }

    public static class ListAll extends MemberRepositoryTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<Member> all = Lists.newArrayList();

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).allInstances(Member.class);
                    will(returnValue(all));
                }
            });

            // when
            final List<Member> list = memberRepository.listAll();

            // then
            assertThat(list).isEqualTo(all);
        }
    }
}
