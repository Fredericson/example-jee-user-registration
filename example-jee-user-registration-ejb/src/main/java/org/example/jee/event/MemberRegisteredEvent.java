package org.example.jee.event;

import org.example.jee.model.Member;

public class MemberRegisteredEvent {

	private final Member member;
	
	public MemberRegisteredEvent(Member member){
		this.member = member;
	}

	public Member getMember() {
		return member;
	}
}
