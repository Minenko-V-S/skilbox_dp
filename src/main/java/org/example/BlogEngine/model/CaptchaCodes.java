package org.example.BlogEngine.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "captcha_codes", schema="test")
public class CaptchaCodes {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Дата и время генерации кода капчи
    @NotNull
    @Column(nullable = false)
    private Instant time;

    // Код, отображаемый на картинкке капчи
    @Column(nullable = false)
    private String code;

    //Код, передаваемый в параметре
    @Column(name = "secret_code", nullable = false)
    private String secretCode;

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }
}
