package com.flashcardsoez.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String username;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String avtPath;
    @Column
    private String email;
    @Column
    private String password;
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Otp otp;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Deck> deckList;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<TestScore> testDoneList;
    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Course> createdCourses;
    @ManyToMany(mappedBy = "studentsInCourse", fetch = FetchType.EAGER)
    private Set<Course> courseList = new HashSet<>();

 
    @Column
    private LocalDateTime created_at;

    public User() {

        this.created_at = LocalDateTime.now();
        this.avtPath = "/images/defaultAvt.png";
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
