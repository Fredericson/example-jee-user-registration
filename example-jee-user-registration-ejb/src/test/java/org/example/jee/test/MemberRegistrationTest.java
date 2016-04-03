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
package org.example.jee.test;

import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.example.jee.event.MemberRegisteredEvent;
import org.example.jee.model.Member;
import org.example.jee.service.MemberRegistration;
import org.example.jee.util.Resources;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MemberRegistrationTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(Member.class, MemberRegistration.class, MemberRegisteredEvent.class, Resources.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @Inject
    MemberRegistration memberRegistration;

    @Inject
    Logger log;
    
    /**
     * Is necessary because email address must be unique.
     */
    private static AtomicInteger emailNumberCounter = new AtomicInteger(0);

    @Test
    public void testRegister() throws Exception {
        Member newMember = createNewMember();
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getFirstName() + " " + newMember.getLastName() + " was persisted with id " + newMember.getId());
    }

	private Member createNewMember() {
		int i = emailNumberCounter.incrementAndGet();
		Member newMember = new Member();
        newMember.setFirstName("Jane");
        newMember.setLastName("Doe");
        newMember.setEmail("jane" + i + "@mailinator.com");
        newMember.setPassword("any4#Password");
        newMember.setLandlinePhoneNumber("2125551234");
        newMember.setMobilePhoneNumber("2125557896");
		return newMember;
	}

    /*********************************************************** 
     * ************* Test PhoneNumber Validation ***************
     * *********************************************************/
    
    @Test
    public void whenRegister_givenMobileNumberButNoLandlineNumber_thenSucceeds() throws Exception {
        Member newMember = createNewMember();
        newMember.setLandlinePhoneNumber(null);
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getFirstName() + " " + newMember.getLastName() + " was persisted with id " + newMember.getId());
    }

    @Test
    public void whenRegister_givenEmptyMobileNumber_thenSucceeds() throws Exception {
        Member newMember = createNewMember();
        newMember.setLandlinePhoneNumber("");
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getFirstName() + " " + newMember.getLastName() + " was persisted with id " + newMember.getId());
    }

    @Test(expected=Exception.class)
    public void whenRegister_givenMobileNumberWithCharacters_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setMobilePhoneNumber("212dfg345");
        memberRegistration.register(newMember);
    }

    @Test
    public void whenRegister_givenLandlineNumberButNoMobileNumber_thenSucceeds() throws Exception {
        Member newMember = createNewMember();
        newMember.setMobilePhoneNumber(null);
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getFirstName() + " " + newMember.getLastName() + " was persisted with id " + newMember.getId());
    }

    @Test(expected=Exception.class)
    public void whenRegister_givenLandlineNumberWithCharacters_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setLandlinePhoneNumber("25dfg 56");
        memberRegistration.register(newMember);
    }

    @Test
    public void whenRegister_givenInternationPhoneNumberWithSpace_thenSucceeds() throws Exception {
        Member newMember = createNewMember();
        newMember.setMobilePhoneNumber("+41 79 234 45 56");
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getFirstName() + " " + newMember.getLastName() + " was persisted with id " + newMember.getId());
    }

    @Test
    public void whenRegister_givenInternationPhoneNumberWithBrackets_thenSucceeds() throws Exception {
        Member newMember = createNewMember();
        newMember.setMobilePhoneNumber("+41 (0)79 234 45 56");
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getFirstName() + " " + newMember.getLastName() + " was persisted with id " + newMember.getId());
    }

    @Test
    public void whenRegister_givenUSPhoneNumber_thenSucceeds() throws Exception {
        Member newMember = createNewMember();
        newMember.setMobilePhoneNumber("555.555.5555");
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getFirstName() + " " + newMember.getLastName() + " was persisted with id " + newMember.getId());
    }

    @Test
    public void whenRegister_givenUSPhoneNumberWithBrackets_thenSucceeds() throws Exception {
        Member newMember = createNewMember();
        newMember.setMobilePhoneNumber("(555)-555-5555");
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getFirstName() + " " + newMember.getLastName() + " was persisted with id " + newMember.getId());
    }

    
    @Test(expected=Exception.class)
    @Ignore
    public void whenRegister_givenNoPhoneNumber_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setLandlinePhoneNumber(null);
        newMember.setMobilePhoneNumber(null);
        memberRegistration.register(newMember);
    }

    /************************************************** 
     * ******* Test Password Validation ***************
     **************************************************/

    @Test(expected=Exception.class)
    public void whenRegister_givenPasswordHasNoSpecialCharacter_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setPassword("any4Password");
        memberRegistration.register(newMember);
    }

    @Test(expected=Exception.class)
    public void whenRegister_givenPasswordHasNoNumber_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setPassword("any#Password");
        memberRegistration.register(newMember);
    }

    /************************************************************* 
     *********** Test First Name and Last Name Validation ********
     *************************************************************/

    @Test(expected=Exception.class)
    public void whenRegister_givenNameHasNoLowerCaseCharacter_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setLastName("DOE");
        memberRegistration.register(newMember);
    }

    @Test(expected=Exception.class)
    public void whenRegister_givenNameHasNoUpperCaseCharacters_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setFirstName("jane");
        memberRegistration.register(newMember);
    }

    @Test(expected=Exception.class)
    public void whenRegister_givenNameContainsNumber_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setFirstName("Jane4");
        memberRegistration.register(newMember);
    }

    @Test(expected=Exception.class)
    public void whenRegister_givenNameContainsSpace_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setLastName("Doe ");
        memberRegistration.register(newMember);
    }

    @Test(expected=Exception.class)
    public void whenRegister_givenNameContainsSpecialCharacter_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setLastName("Doe?");
        memberRegistration.register(newMember);
    }

    @Test(expected=Exception.class)
    public void whenRegister_givenNameWithOneCharacter_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setFirstName("J");
        memberRegistration.register(newMember);
    }

    @Test
    public void whenRegister_givenNameWith50Charaters_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setLastName("DoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDo");
        memberRegistration.register(newMember);
        assertNotNull(newMember.getId());
        log.info(newMember.getFirstName() + " " + newMember.getLastName() + " was persisted with id " + newMember.getId());
    }

    @Test(expected=Exception.class)
    public void whenRegister_givenNameTooLong_thenFails() throws Exception {
        Member newMember = createNewMember();
        newMember.setLastName("DoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoeDoe");
        memberRegistration.register(newMember);
    }

}
