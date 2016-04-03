package org.example.jee.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.example.jee.model.Member;

@Stateless
public class MemberUpdater {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;
    
    @Inject
    private Event<Member> memberEventSrc;

    public void update(Member member) {
        log.info("Update Member(id=" + member.getId() + ")" + member.getFirstName() + " " + member.getLastName() );
        em.merge(member);
        memberEventSrc.fire(member);
    }
}
