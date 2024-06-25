package com.flashcardsoez.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private String thumbnailPath;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User author;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Card> cardList;
    @OneToMany(mappedBy = "testData", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Test> cardTestList;
    @Column
    private LocalDateTime updated_at;

   public  Deck() {
        this.thumbnailPath = "/images/defaultThumbnail.png";
          this.updated_at = LocalDateTime.now();
    }
}
