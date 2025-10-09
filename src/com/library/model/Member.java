package com.library.model;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Member{
    private int member_id;
    private String name;
    private String email;
    private String phone;
    private String status;
    private LocalDate membershipDate;
    private LocalDateTime createdDate;

    public Member() {
    }

    public Member(String name, String email, String phone, LocalDate membershipDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipDate = membershipDate;
        this.status= "ACTIVE";
    }


    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Member{" +
                "member_id=" + member_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", membershipDate=" + membershipDate +
                ", createdDate=" + createdDate +
                '}';
    }
}