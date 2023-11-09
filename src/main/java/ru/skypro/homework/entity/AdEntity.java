package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "ads")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
    private String image;
    private int price;
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private Collection<CommentEntity> comments;
}
