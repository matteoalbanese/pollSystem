package com.matalban.pollsystem.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "poll_option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> vote;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    private String optionName;

    //capire come saranno rappresentati i timestamp
    private Date createdAt;

    //default false, cambia a true quando viene calcolato il vincitore
    private Boolean winner = false ;
    private Double percentage;

}
