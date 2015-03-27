package com.emc.documentum.springdata.core;

import com.emc.documentum.springdata.entitymanager.annotations.Relation;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

import com.emc.documentum.springdata.entitymanager.annotations.DCTMObject;
import com.emc.documentum.springdata.entitymanager.annotations.EntityField;

import java.util.ArrayList;
import java.util.List;

@DCTMObject(repository="persons")
@TypeAlias("person")
public class Person {

    @EntityField("firstname")
    private String name;

    @Id
    public String _id;
    public Integer age;

    @EntityField("sex")
    private String gender;


    private List<String> hobbies = new ArrayList<String>();
    
<<<<<<< HEAD
<<<<<<< HEAD
    @EntityField("accountnumbers")
    private List<Double> accountNumbers = new ArrayList<Double>();

=======
    private List<Long> accountNumbers = new ArrayList<Long>();
>>>>>>> Repeating Attributes Working
=======
    @EntityField("accountnumbers")
    private List<Double> accountNumbers = new ArrayList<Double>();
>>>>>>> Added test for repeating attributes
//    @Relation
//    public Address address;



<<<<<<< HEAD
<<<<<<< HEAD
    public List<Double> getAccountNumbers() {
		return accountNumbers;
	}

	public void setAccountNumbers(List<Double> accountNumbers) {
=======
    public List<Long> getAccountNumbers() {
		return accountNumbers;
	}

	public void setAccountNumbers(List<Long> accountNumbers) {
>>>>>>> Repeating Attributes Working
=======
    public List<Double> getAccountNumbers() {
		return accountNumbers;
	}

	public void setAccountNumbers(List<Double> accountNumbers) {
>>>>>>> Added test for repeating attributes
		this.accountNumbers = accountNumbers;
	}

	public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

//    public Address getAddress() {
//        return address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }




	public void set_id(String _id) {
		this._id = _id;
	}


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

	public Person(String name, Integer age, String gender)
	{
		this.name = name;
		this.age = age;
		this.gender = gender;
	}
	
	public Person() {}
	
 }

