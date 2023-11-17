package com.dddev.log.repository;

import com.dddev.log.entity.UserGptAccess;
import com.dddev.log.entity.UserLogAccess;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGptAccessRepository extends CrudRepository<UserGptAccess, String> {
}
