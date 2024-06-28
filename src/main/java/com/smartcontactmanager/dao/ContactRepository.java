package com.smartcontactmanager.dao;

import com.smartcontactmanager.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Query("select c from Contact as c where c.user.id =:id order by cid desc")
    public Page<Contact> findAllById(@Param("id") int id, Pageable pageable); //Pageable contain Current page and how many records need to show on page

    @Modifying
    @Transactional
    @Query("delete from Contact where user.id =:id")
    public void deleteContactAgainstUserId(@Param("id") int id);

}
