package ru.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    @Modifying
    @Query(value = "" +
            "UPDATE User u " +
            "SET u.rate = :rate " +
            "WHERE u.id = :id")
    void setUserRate(@Param("id") Long userId, @Param("rate") Double userRate);
}
