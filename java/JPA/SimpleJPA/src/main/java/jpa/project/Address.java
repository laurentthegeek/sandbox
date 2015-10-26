package jpa.project;

import jpa.common.Country;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
@Access(AccessType.FIELD) // TODO check best practices
public class Address {

    private String street;
    private String city;
    @ManyToOne @JoinColumn
    private Country country;

    @Override
    public String toString() {
        return String.format("Address: %s, %s, %s", getStreet(), getCity(), getCountry());
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountryCode(Country code) {
        this.country = code;
    }
}
