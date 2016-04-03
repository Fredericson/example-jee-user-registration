/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.jee.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.example.jee.validation.OneFieldMustContainValue;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@XmlRootElement
@Table(name = "Registrant", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@OneFieldMustContainValue.List({
	@OneFieldMustContainValue(fieldNames = {"landlinePhoneNumber", "mobilePhoneNumber"}, 
			message = "provide either a landlinePhoneNumber or MobilePhoneNumber")
})
public class Member implements Serializable {
	
    private static final String PHONE_NUMBER_VALIDATION_MSG = "empty or US or European Number";
	private static final String REGEX_PHONE_NUMBER = "(^$)|(^(?:\\+\\d{1,3}|0\\d{1,3}|00\\d{1,2})?(?:\\s?\\(\\d+\\))?(?:[-\\/\\s\\.]|\\d)+$)";

	/** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;
    
    private static final String REGEX_AT_LEAST_ONE_LOWERCASE_AND_ONE_UPPERCASE_CHAR = "^(?=.*[a-z])(?=.*[A-Z])[a-zA-Z]+$";
    private static final String NAME_VALIDATION_MSG = "Only alphabetical Characters allowed, must contain one lowerCase and one Capital Charater";
    
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "first_name")
    @Size(min = 2, max = 50)
    @Pattern(regexp = REGEX_AT_LEAST_ONE_LOWERCASE_AND_ONE_UPPERCASE_CHAR, message = NAME_VALIDATION_MSG)
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    @Size(min = 2, max = 50)
    @Pattern(regexp = REGEX_AT_LEAST_ONE_LOWERCASE_AND_ONE_UPPERCASE_CHAR, message = NAME_VALIDATION_MSG)
    private String lastName;

    @NotNull
    @Size(min = 6, max = 20)
    @NotEmpty
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&]).{6,20})", message = "must contain one digit from 0-9,"
    		+ " one lowercase character, one lowercase character, one special symbol @#$%!&, min 6 chars, max 20 chars")
    private String password;

	@NotNull
    @NotEmpty
    @Email
    private String email;
	
    @Column(name = "email_validated")
	private boolean emailValidated;

	@Column(name = "landline_phone_number")
    @Pattern(regexp = REGEX_PHONE_NUMBER, message = PHONE_NUMBER_VALIDATION_MSG)
    private String landlinePhoneNumber;

    @Column(name = "mobile_phone_number")
    @Pattern(regexp = REGEX_PHONE_NUMBER, message = PHONE_NUMBER_VALIDATION_MSG)
    private String mobilePhoneNumber;

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailValidated() {
		return emailValidated;
	}

	public void setEmailValidated() {
		this.emailValidated = true;
	}

    public String getLandlinePhoneNumber() {
		return landlinePhoneNumber;
	}

	public void setLandlinePhoneNumber(String landlinePhoneNumber) {
		this.landlinePhoneNumber = landlinePhoneNumber;
	}

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}    
}
