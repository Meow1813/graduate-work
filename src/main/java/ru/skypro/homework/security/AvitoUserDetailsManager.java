package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.WrongPasswordException;
import ru.skypro.homework.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvitoUserDetailsManager implements UserDetailsManager {

    @Autowired
    private  UserRepository repository;
    @Autowired
    private  ModelMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    public void createUser(UserDetails user) {
        var userEntity = convertToUserEntity(user);
        userEntity.setPassword(encoder.encode(user.getPassword()));
        repository.save(userEntity);
    }

    @Override
    public void updateUser(UserDetails user) {
        UserEntity newUser = convertToUserEntity(user);
        UserEntity oldUser = repository.findByUsername(user.getUsername());
        newUser.setId(oldUser.getId());
        repository.save(newUser);
    }

    @Override
    public void deleteUser(String username) {
        repository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = repository.findByUsername(username);
//        String password = encoder.encode(oldPassword);
//        log.info("{}", oldPassword);
//        log.info("oldPassword: {}", encoder.encode(oldPassword));
//        log.info("dbPassword: {}", user.getPassword());
        if (encoder.matches(oldPassword, user.getPassword())) {
            log.info("new password: {}", newPassword);
            user.setPassword(encoder.encode(newPassword));
            repository.save(user);
        } else {
            throw new WrongPasswordException("The old password is incorrect");
        }
    }

    @Override
    public boolean userExists(String username) {
        return repository.findByUsername(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repository.findByUsername(username);
        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException("User '" + username + "' not found");
    }

    private UserDetails convertToUserDetails(UserEntity userEntity) {
        return mapper.map(userEntity, UserDetails.class);
    }

    private UserEntity convertToUserEntity(UserDetails userDetails) {
        return mapper.map(userDetails, UserEntity.class);
    }
}
