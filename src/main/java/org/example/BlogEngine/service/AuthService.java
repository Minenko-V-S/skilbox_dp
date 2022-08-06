package org.example.BlogEngine.service;


import com.github.cage.Cage;
import com.github.cage.GCage;
import org.apache.commons.io.FileUtils;
import org.example.BlogEngine.model.CaptchaCodes;
import org.example.BlogEngine.model.GlobalSettings;
import org.example.BlogEngine.model.Session;
import org.example.BlogEngine.model.User;
import org.example.BlogEngine.repository.CaptchaRepository;
import org.example.BlogEngine.repository.GlobalSettingsRepository;
import org.example.BlogEngine.repository.SessionRepository;
import org.example.BlogEngine.repository.UserRepository;
import org.example.BlogEngine.requests.LoginRequest;
import org.example.BlogEngine.response.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final HttpSession httpSession;
    private final CaptchaRepository captchaRepository;
    private final GlobalSettingsRepository globalSettingsRepository;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, HttpSession httpSession, CaptchaRepository captchaRepository, GlobalSettingsRepository globalSettingsRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.httpSession = httpSession;
        this.captchaRepository = captchaRepository;
        this.globalSettingsRepository = globalSettingsRepository;
    }

    private boolean result = false;

    public ResponseEntity<?> getAuthCheck() {
        AuthResponse authResponse = new AuthResponse();
        Optional<Integer> userIdOptinal;
        User u;
        String sessionName = httpSession.getId();
        boolean isSession = sessionRepository.findAll().stream()
                .anyMatch(s -> s.getSessionName().equals(sessionName));
        if (isSession) {
            userIdOptinal = sessionRepository.findAll().stream()
                    .filter(s -> s.getSessionName().equals(sessionName))
                    .map(Session::getUserId).findAny();
            if (userIdOptinal.isPresent()) {
                int userId = userIdOptinal.get();
                u = userRepository.getOne(userId);
                LinkedHashMap<String, Object> user = new LinkedHashMap<>();
                user.put("id", userIdOptinal.get());
                user.put("name", u.getName());
                user.put("photo", u.getPhoto());
                user.put("email", u.getEmail());
                user.put("moderation", u.getIsModerator());
                user.put("moderationCount", getModerationCount(userId));
                user.put("settings", u.getIsModerator());
                authResponse.setResult(true);
                authResponse.setUser(user);
                return new ResponseEntity<>(authResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User is not authorized.", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Session is not open.", HttpStatus.OK);
        }
    }

    private int getModerationCount(Integer userId) {
        return userRepository.getModerationCount(userId);
    }

    public ResponseEntity<?> getCaptcha () {
        Cage cage = new GCage();
        String secretCode = cage.getTokenGenerator().next();
        System.out.println("secretCode: " + secretCode);
        String code = cage.getTokenGenerator().next();
        String code64 = "";
        CaptchaCodes captcha = new CaptchaCodes();
        Map<String, String> map = new LinkedHashMap<>();
        try (OutputStream os = new FileOutputStream("image.png", false)) {
            cage.draw(code, os);
            //resize image
//            BufferedImage bi = cage.drawImage("image.png");
//            resizeImageWithHint(bi, 100, 35, BufferedImage.TYPE_INT_RGB);
//            cage.draw("image.png", new FileOutputStream(String.valueOf(bi)));
            /////
            byte[] fileContent = FileUtils.readFileToByteArray(new File("image.png"));
            code64 = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        captcha.setCode(code);
        captcha.setSecretCode(secretCode);
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        captcha.setTimestamp(timestamp);
        captchaRepository.save(captcha);
        map.put("secret", secretCode);
        map.put("image", "data:image/png;base64, " + code64);
        clearOldCaptchas();
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private void clearOldCaptchas() {
        List<CaptchaCodes> oldCaptchas = captchaRepository.findAll().stream()
                .filter(c -> c.getTimestamp().getTime() < (Timestamp.valueOf(LocalDateTime.now()).getTime() - 3_600_000))
                .collect(Collectors.toList());
        captchaRepository.deleteInBatch(oldCaptchas);
    }

    public ResponseEntity<?> postAuthRegister(LoginRequest loginRequest){
        Map<String, Object>  output = new LinkedHashMap<>();
        ResponseEntity<?> responseEntity;
        if (globalSettingsRepository.findAll().stream().
                findAny().orElse(new GlobalSettings()).
                isMultiuserMode()) { // if MULTIUSER_MODE = true
            User user = new User();
            Map<String, String> errors = new LinkedHashMap<>();
            Optional<User> userOptional = userRepository.findOneByEmail(loginRequest.getEmail());

            result = true;
            if (userOptional.isPresent()) {
                errors.put("email", "Этот e-mail уже зарегистрирован!");
                result = false;
            }
            if (loginRequest.getName().length() > 30) {
                errors.put("name", "Ошибка: длина имени превышает 30 знаков!");
                result = false;
            }
            if (loginRequest.getPassword().length() < 6 || loginRequest.getPassword().length() > 12) {
                errors.put("password", "Пароль имеет недопустимую длину!");
                result = false;
            }

            Optional<CaptchaCodes> codeOptional = captchaRepository.findBySecretCode(loginRequest.getCaptchaSecret());
            if(codeOptional.isPresent()) {
                if(!loginRequest.getCaptcha().equals(codeOptional.get().getCode())) {
                    errors.put("captcha", "Код с картинки введён неверно!");
                    result = false;
                }
            }
            if (result) {
                String code = generateCode(16);
                user.setEmail(loginRequest.getEmail());
                user.setName(loginRequest.getName());
                user.setPassword(loginRequest.getPassword());
                user.setRegTime(Timestamp.valueOf(LocalDateTime.now()));
                user.setCode(code);
                user.setIsModerator(false);
                userRepository.save(user);
                output.put("result", true);
                responseEntity = new ResponseEntity<>(output, HttpStatus.OK);
            } else {
                output.put("result", false);
                output.put("errors", errors);
                responseEntity = new ResponseEntity<>(output, HttpStatus.BAD_REQUEST);
            }
        } else { // if MULTIUSER_MODE = false
            responseEntity = new ResponseEntity<>("New users forbidden", HttpStatus.NOT_ACCEPTABLE);
        }
        return responseEntity;
    }

    private String generateCode(int length) {
        final String pattern = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(pattern.charAt(rnd.nextInt(pattern.length())));
        }
        return stringBuilder.toString();
    }


}
