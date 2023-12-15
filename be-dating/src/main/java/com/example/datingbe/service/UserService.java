package com.example.datingbe.service;

import com.example.datingbe.config.SecurityUtils;
import com.example.datingbe.dto.CustomUserDetails;
import com.example.datingbe.dto.UserDto;
import com.example.datingbe.entity.Authority;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.get() == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user.get());
    }

    public User getUserWithAuthority(){
        Long id =Long.valueOf(SecurityUtils.getCurrentUserLogin().get());
        return userRepository.findById(id).get();
    }

    public User save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActived(1);
        user.setActivation_key(user.getActivation_key());
        user.setCreated_date(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
        return user;
    }

    public User getOne(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }


    public void update(User user){
        userRepository.save(user);
    }

    public Optional<User> findByUsernameAndPassword(String username, String password){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            if(passwordEncoder.matches(password, user.get().getPassword())){
                return user;
            }
        }
        return Optional.empty();
    }

    public Optional<User> findByEmailAndUID(String email, String uid){
                   return userRepository.findByEmailAndUID(email,uid);
    }
    public Optional<User> findByEmail(String email, String password){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            if(passwordEncoder.matches(password, user.get().getPassword())){
                return user;
            }
        }
        return user;
    }
    public String randomKey(){
        String str = "qwert1yui2op3as4dfg5hj6klzx7cvb8nmQ9WE0RTYUIOPASDFGHJKLZXCVBNM";
        Integer length = str.length()-1;
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i=0; i<10; i++){
            Integer ran = (int)(Math.random()*length);
            stringBuilder.append(str.charAt(ran));
        }
        return String.valueOf(stringBuilder);
    }

    public List<User> getFemaleUsers(String sex) {
        return userRepository.findBySex(sex);
    }
    public List<User> findUsersByGenderAndAgeRange(String gender, int ageMin, int ageMax){
        return userRepository.findUsersByGenderAndAgeRange( gender,  ageMin,  ageMax);
    }




    public  double getDistanceBetweenPointsNew(double latitude1, double longitude1, double latitude2, double longitude2, String unit) {
        double theta = longitude1 - longitude2;
        double distance = 60 * 1.1515 * (180/Math.PI) * Math.acos(
                Math.sin(latitude1 * (Math.PI/180)) * Math.sin(latitude2 * (Math.PI/180)) +
                        Math.cos(latitude1 * (Math.PI/180)) * Math.cos(latitude2 * (Math.PI/180)) * Math.cos(theta * (Math.PI/180))
        );
        if (unit.equals("miles")) {
            return distance;
        } else if (unit.equals("kilometers")) {
            return distance * 1.609344;
        } else {
            return 0;
        }
    }
    public UserDto addAdmin( Long userId ) {
        User user = getOne(userId);
        if(user == null) {
            return null;
        }
        Set<Authority> authorities = user.getAuthorities();
        authorities.add(new Authority(Contains.ROLE_ADMIN));
        user.setAuthorities(authorities);
        user = userRepository.save(user);
        return new UserDto(user);
    }
    public UserDto deleteAdmin(Long userId) {
        User user = getOne(userId);
        if(user == null) {
            return null;
        }
        Set<Authority> authorities = new HashSet<Authority>();
        authorities.add(new Authority(Contains.ROLE_USER));
        user.setAuthorities(authorities);
        user = userRepository.save(user);
        return new UserDto(user);
    }

}

