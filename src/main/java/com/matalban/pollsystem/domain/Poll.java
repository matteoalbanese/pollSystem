package com.matalban.pollsystem.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Poll {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private UserAccount owner;

    @OneToMany(mappedBy = "poll")
    private List<Option> options;

    private String question;
    private Status status;
    private Date expirationDate;


}
