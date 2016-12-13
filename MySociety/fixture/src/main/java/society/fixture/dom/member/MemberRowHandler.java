package society.fixture.dom.member;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import lombok.Getter;
import lombok.Setter;
import society.dom.member.Member;
import society.dom.member.MemberRepository;

public class MemberRowHandler implements ExcelFixtureRowHandler {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Integer integer;

    @Getter @Setter
    private LocalDate localDate;

    @Getter @Setter
    private Boolean flag;

    @Override
    public List<Object> handleRow(
            final FixtureScript.ExecutionContext executionContext,
            final ExcelFixture excelFixture,
            final Object previousRow) {

        final Member member = repository.findOrCreate(name);

        member.setFlag(getFlag());
        member.setInteger(getInteger());
        member.setLocalDate(getLocalDate());

        executionContext.addResult(excelFixture, member);
        return Collections.singletonList(member);
    }
    private static double random(final double from, final double to) {
        return Math.random() * (to-from) + from;
    }


    @Inject
    MemberRepository repository;

}
