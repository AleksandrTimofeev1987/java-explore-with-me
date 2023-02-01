package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.user.entity.User;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "" +
            "SELECT u " +
            "FROM User u " +
            "WHERE u.id IN :ids ")
    List<User> findUsersById(@Param("ids") Long[] ids, Pageable page);
}
