package com.library.dao;

import com.library.model.Member;
import com.library.util.DatabaseConnection;
import com.mysql.cj.jdbc.ClientPreparedStatement;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {

    @Override
    public void addMember(Member member) {
        String sql= "INSERT INTO members(name, email, phone, membership_date, status) values(?,?,?,?,?)";
        try(Connection connection= DatabaseConnection.getConnection();
            PreparedStatement statement= connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPhone());
            statement.setDate(4, Date.valueOf(member.getMembershipDate()));
            statement.setString(5, member.getStatus());

            int affectedRows= statement.executeUpdate();
            if(affectedRows>0){
                try(ResultSet generatedKeys= statement.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        member.setMember_id(generatedKeys.getInt(1));
                        System.out.println("Member added successfully with ID : " + member.getMember_id());
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("Error adding member : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Member getMemberById(int memberId) {
        String sql= "SELECT * FROM members WHERE member_id= ?";
        Member member= null;
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1, memberId);
            try(ResultSet resultSet= statement.executeQuery()){
                if(resultSet.next()){
                    member= new Member();
                    member.setMember_id(resultSet.getInt("member_id"));
                    member.setName(resultSet.getString("name"));
                    member.setEmail(resultSet.getString("email"));
                    member.setPhone(resultSet.getString("phone"));
                    member.setMembershipDate(resultSet.getDate("membership_date").toLocalDate());
                    member.setStatus(resultSet.getString("status"));
                    System.out.println("Found member : " + member.getName());
                }else{
                    System.out.println("Member not found with ID : " + memberId);
                }
            }
        }catch(SQLException e){
            System.out.println("Error getting member by ID : " + e.getMessage());
            e.printStackTrace();
        }
        return member;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> members= new ArrayList<>();
        String sql= "SELECT * FROM members ORDER BY member_id";

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Member member= new Member();
                member.setMember_id(resultSet.getInt("member_id"));
                member.setName(resultSet.getString("name"));
                member.setEmail(resultSet.getString("email"));
                member.setPhone(resultSet.getString("phone"));
                member.setMembershipDate(resultSet.getDate("membership_date").toLocalDate());
                member.setStatus(resultSet.getString("status"));
                System.out.println("Found member : " + member.getName());

                members.add(member);
            }
            System.out.println("Retrieved " + members.size() + " members from database");
        }catch(SQLException e){
            System.out.println("Error retrieving members : " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public void deleteMember(int memberId) {
        String sql= "DELETE FROM members WHERE member_id= ?";
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1, memberId);
            int affectedRows = statement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Member deleted successfully with ID : " + memberId);
            }else {
                System.out.println("Member not found with ID : " + memberId);
            }
        }catch(SQLException e){
            System.out.println("Error deleting member : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateMember(Member member) {
        String sql= "UPDATE members SET name= ?, email= ?, phone= ?, status= ? WHERE member_id= ?";
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            statement.setString(3, member.getPhone());
            statement.setString(4, member.getStatus());
            statement.setInt(5, member.getMember_id());

            int affectedRows = statement.executeUpdate();
            if(affectedRows> 0){
                System.out.println("Member updated successfully with ID : " + member.getMember_id());
            }else{
                System.out.println("Member not found with ID : " + member.getMember_id());
            }
        } catch (SQLException e) {
            System.out.println("Error updating member : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Member getMemberByEmail(String email) {
        String sql= "SELECT * FROM members WHERE email= ?";
        Member member= null;
        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, email);
            try(ResultSet resultSet= statement.executeQuery()){
                if(resultSet.next()){
                    member= new Member();
                    member.setMember_id(resultSet.getInt("member_id"));
                    member.setName(resultSet.getString("name"));
                    member.setEmail(resultSet.getString("email"));
                    member.setPhone(resultSet.getString("phone"));
                    member.setMembershipDate(resultSet.getDate("membership_date").toLocalDate());
                    member.setStatus(resultSet.getString("status"));
                    System.out.println("Found member by email : " + member.getName());
                }else{
                    System.out.println("No member found with email : " + email);
                }
            }
        }catch (SQLException e){
            System.out.println("Error getting member with email : " + e.getMessage());
            e.printStackTrace();
        }
        return member;
    }

    @Override
    public List<Member> searchMembers(String keyword) {
        List<Member> members= new ArrayList<>();
        String sql= "SELECT * FROM members WHERE name LIKE ? OR email LIKE ? OR phone LIKE ?";

        try(Connection connection= DatabaseConnection.getConnection();
        PreparedStatement statement= connection.prepareStatement(sql)){
            String word= "%" + keyword + "%";
            statement.setString(1, word);
            statement.setString(2, word);
            statement.setString(3, word);
            try(ResultSet resultSet= statement.executeQuery()){
                while(resultSet.next()){
                    Member member= new Member();
                    member.setMember_id(resultSet.getInt("member_id"));
                    member.setName(resultSet.getString("name"));
                    member.setEmail(resultSet.getString("email"));
                    member.setPhone(resultSet.getString("phone"));
                    member.setMembershipDate(resultSet.getDate("membership_date").toLocalDate());
                    member.setStatus(resultSet.getString("status"));

                    members.add(member);
                }
            }
        }catch (SQLException e){
            System.out.println("Error searching members : " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }
}
