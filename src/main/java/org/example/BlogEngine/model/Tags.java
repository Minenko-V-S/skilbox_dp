package org.example.BlogEngine.model;


import javax.persistence.*;

@Entity
@Table(name = "tags", schema="test")
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;
    // искусственный коммит 9.5.22
    public Tags() {
    }

    public Tags(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.matches("#\\S+")) {
            this.name = name;
        } else {
            this.name = "";
        }
    }
}
