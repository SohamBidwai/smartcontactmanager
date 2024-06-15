package com.smartcontactmanager.dao;

import com.smartcontactmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    //in below line email value come as dynamic value
    @Query("select u from User u where u.email = :email")
    public User getUserByName(@Param("email") String name);

}
