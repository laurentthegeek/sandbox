package jpa.common;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Access(AccessType.FIELD)
@NamedQueries({
    @NamedQuery(name=Country.FIND_ALL,
        query="select e from Country e"),
    @NamedQuery(name=Country.FIND_BY_COUNTRY_CODE,
        query="select e from Country e where e.countryCode = :"+ Country.COUNTRY_CODE)})
public class Country {

    // Query and parameters names
    public static final String FIND_ALL = "jpa.common.Country.findAll";
    public static final String FIND_BY_COUNTRY_CODE = "jpa.common.Country.findByCountryCode";
    public static final String COUNTRY_CODE = "countryCode";

    @Id
    @Enumerated(EnumType.STRING)
    private ISOCountryCode countryCode;
    private String name;

    @Override
    public String toString() {
        return String.format("Country[%s]: %s", countryCode, name);
    }

    public ISOCountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(ISOCountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
