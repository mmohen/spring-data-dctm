package com.emc.documentum.springdata.repository.relation.mn;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

import com.emc.documentum.springdata.entitymanager.annotations.Relation;
import com.emc.documentum.springdata.entitymanager.annotations.RelationshipType;
import com.emc.documentum.springdata.entitymanager.mapping.DctmAttribute;
import com.emc.documentum.springdata.entitymanager.mapping.DctmEntity;

@DctmEntity(repository = "persons")
@TypeAlias("person")
public class Person {
  public Person() {
  }

  @Id
  public String _id;

  public Integer age;

  @DctmAttribute("firstname")
  private String name;

  @DctmAttribute("sex")
  private String gender;

  @Relation(value=RelationshipType.ONE_TO_MANY, name="address")
  List<Address> address;

  public List<Address> getAddress() {
    return address;
  }

  public void setAddress(List<Address> address) {
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String get_id() {
    return _id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Person(String name, Integer age, String gender) {
    this.name = name;
    this.age = age;
    this.gender = gender;
  }

  @Override
  public String toString() {
    return "Person{" +
        "_id='" + _id + '\'' +
        ", age=" + age +
        ", name='" + name + '\'' +
        ", gender='" + gender + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    Person person = (Person)o;

    if (_id != null ? !_id.equals(person._id) : person._id != null) { return false; }
    if (age != null ? !age.equals(person.age) : person.age != null) { return false; }
    if (name != null ? !name.equals(person.name) : person.name != null) { return false; }
    if (gender != null ? !gender.equals(person.gender) : person.gender != null) { return false; }
    return !(address != null ? !address.equals(person.address) : person.address != null);

  }

  @Override
  public int hashCode() {
    int result = _id != null ? _id.hashCode() : 0;
    result = 31 * result + (age != null ? age.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (gender != null ? gender.hashCode() : 0);
    result = 31 * result + (address != null ? address.hashCode() : 0);
    return result;
  }
}

