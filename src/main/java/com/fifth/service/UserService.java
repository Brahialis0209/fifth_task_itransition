package com.fifth.service;

import com.fifth.entity.Role;
import com.fifth.entity.User;
import com.fifth.repository.RoleRepository;
import com.fifth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (user.getBlock().equals("block")){
            throw new UsernameNotFoundException("User blocked");
        }
        Date date = new Date();
        String curr_date = date.toString();
        user.setLogdate(curr_date);
        user.setStatus("online");
        userRepository.save(user);

        return (UserDetails) user;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }





    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Date date = new Date();
        String curr_date = date.toString();
        user.setRegdate(curr_date);
        user.setBlock("free");
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public void blockUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (user.getUsername().equals(auth.getName())){
            user.setStatus("block");
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        }
        user.setBlock("block");
        user.setStatus("block");
        userRepository.save(user);
    }

    public void unblockUser(User user) {
        user.setBlock("free");
        user.setStatus("offline");
        userRepository.save(user);
    }



    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }
}