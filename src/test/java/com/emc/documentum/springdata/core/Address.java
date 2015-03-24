package com.emc.documentum.springdata.core;

import com.emc.documentum.springdata.entitymanager.annotations.DCTMObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

/**
 * Created with IntelliJ IDEA.
 * User: ramanwalia
 * Date: 20/03/15
 * Time: 9:25 PM
 * To change this template use File | Settings | File Templates.
 */
@DCTMObject(repository="addresses")
@TypeAlias("address")
public class Address {


    @Id
    String id;
    int doorNo;
    String street;
    String city;
    String state;
    String country;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(int doorNo) {
        this.doorNo = doorNo;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


}
