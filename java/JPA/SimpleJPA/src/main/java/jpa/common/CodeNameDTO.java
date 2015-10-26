package jpa.common;

// Public top-level class required by Hibernate (EclipseLink works fine with private nested class)
public class CodeNameDTO {

    public final ISOCountryCode code;
    public final String name;

    public CodeNameDTO(ISOCountryCode code, String name) {
        this.code = code;
        this.name = name;
    }
}
