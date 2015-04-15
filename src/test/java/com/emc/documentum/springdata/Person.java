package com.emc.documentum.springdata;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

import com.emc.documentum.springdata.entitymanager.mapping.DctmEntity;
import com.emc.documentum.springdata.entitymanager.mapping.DctmAttribute;

@DctmEntity(repository = "persons")
@TypeAlias("person")
public class Person {

  @Id
  public String _id;

  public void set_id(String _id) {
    this._id = _id;
  }

  @DctmAttribute("firstname")
  private String name;

  public Integer age;

  @DctmAttribute("sex")
  private String gender;

  public String getName() {
    return name;
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

  public Person() {}

  @Override
  public String toString() {
    return "Person{" +
        "_id='" + _id + '\'' +
        ", name='" + name + '\'' +
        ", age=" + age +
        ", gender='" + gender + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    Person person = (Person)o;

    if (name != null ? !name.equals(person.name) : person.name != null) { return false; }
    if (age != null ? !age.equals(person.age) : person.age != null) { return false; }
    return !(gender != null ? !gender.equals(person.gender) : person.gender != null);

  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (age != null ? age.hashCode() : 0);
    result = 31 * result + (gender != null ? gender.hashCode() : 0);
    return result;
  }
}

