package com.zhp.bos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhp.bos.entity.user.User;

public interface BaseDao extends JpaRepository<User, Integer> {

}
