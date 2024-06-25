package com.flashcardsoez.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User teacher;
    @OneToMany(mappedBy = "course")
    private List<Test> testsInCourse;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "CourseMember",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<User> studentsInCourse = new HashSet<>();



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<Test> getTestsInCourse() {
        return testsInCourse;
    }

    public void setTestsInCourse(List<Test> testsInCourse) {
        this.testsInCourse = testsInCourse;
    }

    public Set<User> getStudentsInCourse() {
        return studentsInCourse;
    }

    public void setStudentsInCourse(Set<User> studentsInCourse) {
        this.studentsInCourse = studentsInCourse;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
