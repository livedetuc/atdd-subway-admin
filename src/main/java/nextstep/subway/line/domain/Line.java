package nextstep.subway.line.domain;

import java.util.List;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.Sections;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {
    private static String ALREADY_CONTAIN_SECTION_MESSAGE = "이미 포함된 Section 입니다. sectionId=%s";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private LineName name;

    @Embedded
    private LineColor color;

    @Embedded
    private Sections sections = Sections.createEmpty();

    protected Line() {}

    private Line(String name, String color) {
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Section section) {
        validateAddableSection(section);

        sections.add(section);
        section.registerLine(this);
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return name;
    }

    public LineColor getColor() {
        return color;
    }

    public List<Station> getStations() {
        return sections.getSortedStations();
    }

    private void validateAddableSection(Section section) {
        if (sections.contains(section)) {
            throw new IllegalStateException(String.format(ALREADY_CONTAIN_SECTION_MESSAGE, section.getId()));
        }
    }
}
