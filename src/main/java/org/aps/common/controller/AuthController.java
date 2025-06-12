package org.aps.common.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aps.common.entity.User;
import org.aps.common.repository.UserRepository;
import org.aps.common.request.ChangePasswordRequest;
import org.aps.common.request.FindPasswordRequest;
import org.aps.common.request.LoginRequest;
import org.aps.common.response.LoginResult;
import org.aps.common.service.MailService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final MailService mailService;

    @Value("${jwt.secret}")
    private String secret;

    @PostMapping("/signup")
    public ResponseEntity<?> signupHandle(@RequestBody @Valid User user, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.status(400).body("입력값 오류");
        }
        User found = userRepository.findByEmail(user.getEmail());

        if (found != null){
            return ResponseEntity.status(400).body("이미 사용중인 이메일");
        }

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        User users = User.builder()
                .email(user.getEmail())
                .password(hashedPassword)
                .role(user.getRole())
                .name(user.getName())
                .build();

        userRepository.save(users);

        return ResponseEntity.status(200).body(users);
    }



    @PostMapping("/login")
    public ResponseEntity<?> loginHandle(@RequestBody @Valid LoginRequest login, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.status(400).body("입력값 오류");
        }

        User user = userRepository.findByEmail(login.getEmail());

        if (user == null || !BCrypt.checkpw(login.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 비밀번호");
        }

        String token = JWT.create()
                .withIssuer("aps")
                .withSubject(user.getEmail())
                .sign(Algorithm.HMAC256(secret));

        LoginResult loginResult = LoginResult.builder()
                .token(token)
                .user(user)
                .build();

        return ResponseEntity.status(200).body(loginResult);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutHandle(HttpSession session){
        session.removeAttribute("user");
        return ResponseEntity.status(200).body(null);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePasswordHandle(HttpServletRequest request, @RequestBody @Valid ChangePasswordRequest changePassword, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(400).body(null);
        }

        User user = (User) request.getAttribute("user");
        if (!changePassword.getEmail().equals(user.getEmail())) {
            return ResponseEntity.status(403).body(null);
        }

        User found = userRepository.findByEmail(changePassword.getEmail());

        if (found == null || !BCrypt.checkpw(changePassword.getOldPassword(), found.getPassword())) {
            return ResponseEntity.status(401).body(null);
        }

        String hashedNewPassword = BCrypt.hashpw(changePassword.getNewPassword(), BCrypt.gensalt());
        found.setPassword(hashedNewPassword);
        userRepository.save(found);

        return ResponseEntity.status(200).body("비밀번호 변경 완료");
    }


    @PostMapping("/find-password")
    public ResponseEntity<?> findPasswordHandle(@RequestBody @Valid FindPasswordRequest passwordRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(400).body("이메일 형식 오류");
        }

        User found = userRepository.findByEmail(passwordRequest.getEmail());
        if (found == null) {
            return ResponseEntity.status(400).body("등록되지않은 이메일");
        }

        String temporalPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedTempPassword = BCrypt.hashpw(temporalPassword, BCrypt.gensalt());

        found.setPassword(hashedTempPassword);
        userRepository.save(found);

        mailService.sendTemporalPasswordMessage(found.getEmail(), temporalPassword);

        return ResponseEntity.ok("임시 비밀번호가 발송되었습니다.");
    }

}
