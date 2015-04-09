package com.emc.documentum.springdata.repository.relation;

import org.springframework.data.annotation.Id;

import com.emc.documentum.springdata.entitymanager.mapping.DctmEntity;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@DctmEntity(repository = "address")
public class Address {

  public Address() {
  }

  public Address(String street, String city, String country) {
    this.street = street;
    this.city = city;
    this.country = country;
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

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    Address address = (Address)o;

    if (street != null ? !street.equals(address.street) : address.street != null) { return false; }
    if (city != null ? !city.equals(address.city) : address.city != null) { return false; }
    if (country != null ? !country.equals(address.country) : address.country != null) { return false; }
    return !(id != null ? !id.equals(address.id) : address.id != null);

  }

  @Override
  public int hashCode() {
    int result = street != null ? street.hashCode() : 0;
    result = 31 * result + (city != null ? city.hashCode() : 0);
    result = 31 * result + (country != null ? country.hashCode() : 0);
    result = 31 * result + (id != null ? id.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Address{" +
        "street='" + street + '\'' +
        ", city='" + city + '\'' +
        ", country='" + country + '\'' +
        ", id='" + id + '\'' +
        '}';
  }

  private String street;
  private String city;
  private String country;
  @Id
  private String id;
}
