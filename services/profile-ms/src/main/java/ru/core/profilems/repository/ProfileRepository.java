package ru.core.profilems.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.core.profilems.model.Profile;

import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    @Query("SELECT p FROM Profile p WHERE p.name LIKE %:keyword% OR p.surname LIKE %:keyword%")
    Page<Profile> searchAnywhereInNameOrSurname(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT p FROM Profile p WHERE
            LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(p.surname) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Profile> searchAnywhereInNameOrSurnameIgnoreCase(@Param("keyword") String keyword, Pageable pageable);
}
