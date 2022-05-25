package org.example.BlogEngine.model;



import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "captcha_codes", schema="test")
@NoArgsConstructor
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

}
