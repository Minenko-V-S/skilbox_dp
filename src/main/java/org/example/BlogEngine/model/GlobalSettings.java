package org.example.BlogEngine.model;


import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "global_settings", schema="test")
@NoArgsConstructor
public class GlobalSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    // Системное имя настройки
    @Column(nullable = false)
    private String code;

    // Название настройки
    @Column(nullable = false)
    private String name;

    // Значение настройки
    @Column(nullable = false)
    private String value;

     /*          Значения глобальных настроек:
              code                        name                        value
         MULTIUSER_MODE          Многопользовательский режим         YES / NO
         POST_PREMODERATION      Премодерация постов                 YES / NO
         STATISTICS_IS_PUBLIC    Показывать всем статистику блога    YES / NO
     */


    public GlobalSettings(long id, String code, String name, String value) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
