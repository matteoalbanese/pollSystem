package com.matalban.pollsystem.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "option")
    private List<Vote> vote;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    private String optionName;

    //capire come saranno rappresentati i timestamp
    private String createdAt;

    //default false, cambia a true quando viene calcolato il vincitore
    private Boolean winner;
    private Double percentage;

}
