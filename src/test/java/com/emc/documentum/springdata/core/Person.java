package com.emc.documentum.springdata.core;

import com.emc.documentum.springdata.entitymanager.annotations.Relation;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

import com.emc.documentum.springdata.entitymanager.annotations.Content;


import com.emc.documentum.springdata.entitymanager.mapping.DctmAttribute;
import com.emc.documentum.springdata.entitymanager.mapping.DctmEntity;

import java.util.ArrayList;
import java.util.List;

@DctmEntity(repository="persons")
@TypeAlias("person")
public class Person {

    @DctmAttribute("firstname")
    private String name;

    @Id
    public String _id;
    public Integer age;

    @DctmAttribute("sex")
    private String gender;


    private List<String> hobbies = new ArrayList<String>();
<<<<<<< HEAD

    @EntityField("accountnumbers")
=======
    
    @DctmAttribute("accountnumbers")
>>>>>>> Rebased from Gourav's repository code
    private List<Double> accountNumbers = new ArrayList<Double>();

//    @Relation
//    public Address address;
    
//    private Content file;
//    
//	@Content
//	public boolean getFileAt(String path) {
//		return true;
//	}
//
//	@Content
//	public void setFile(String path) {
//		this.file = file;
//	}

    public List<Double> getAccountNumbers() {
		return accountNumbers;
	}

	public void setAccountNumbers(List<Double> accountNumbers) {
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

