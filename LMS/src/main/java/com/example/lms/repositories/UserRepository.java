    package com.example.lms.repositories;
    
    import com.example.lms.models.User;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    
    import java.util.List;
    import java.util.Optional;
    @Repository
    public interface UserRepository  extends JpaRepository<User, Integer>
    {
    
        List<User> findAllByEmail(String email);
        List<User> findAllByNameAndEmail(String name, String email);
        List<User> findAllByName(String name);
        Optional<User> findByNameAndPasswordAndEmail(String name, String password,String email );
        Optional<User> findByNameAndEmail(String name, String mail );
        Optional<User> findByEmail(String email);
        Optional<User> findByName(String name);
    
    
        boolean existsByEmail(String email);
    }