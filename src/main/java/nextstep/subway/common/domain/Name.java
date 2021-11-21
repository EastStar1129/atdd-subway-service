package nextstep.subway.common.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(unique = true, name = "name")
    private String value;

    protected Name() {
    }

    private Name(String value) {
        Assert.hasText(value, "name value must not be empty");
        this.value = value;
    }

    public static Name from(String value) {
        return new Name(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public String toString() {
        return "Name{" +
            "value='" + value + '\'' +
            '}';
    }
}
