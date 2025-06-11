package org.aps.common.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aps.common.entity.User;
import org.aps.common.repository.UserRepository;
import org.aps.common.request.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;

    @GetMapping("/signup")
    public ResponseEntity<?> signupHandle(@RequestBody @Valid User user, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.status(400).body("입력값 오류");
        }
        User found = userRepository.findByEmail(user.getEmail());

        if (found != null){
            return ResponseEntity.status(400).body("이미 사용중인 이메일");
        }

       User users = User.builder().
               email(user.getEmail()).
               password(user.getPassword()).
               role(user.getRole()).
               name(user.getName()).
               build();

        userRepository.save(users);

        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginHandle(@RequestBody @Valid LoginRequest login, BindingResult result, HttpSession session){
        if (result.hasErrors()){
            return ResponseEntity.status(400).body("입력값 오류");
        }

        User user = userRepository.findByEmail(login.getEmail());

        if (user == null || !user.getPassword().equals(login.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        session.setAttribute("user", user);
        return ResponseEntity.status(200).body(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutHandle(HttpSession session){
        session.removeAttribute("user");
        return ResponseEntity.status(200).body(null);
    }
}
