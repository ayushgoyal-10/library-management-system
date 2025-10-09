package com.library.dao;

import com.library.model.Member;

import java.util.List;

public interface MemberDAO {
    void addMember(Member member);
    Member getMemberById(int memberId);
    List<Member> getAllMembers();
    void deleteMember(int memberId);
    void updateMember(Member member);
    Member getMemberByEmail(String email);
    List<Member> searchMembers(String keyword);
}
