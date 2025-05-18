package com.example.lms.repositories;
import com.example.lms.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer>
{

    Profile  getProfileById(int userId);


}