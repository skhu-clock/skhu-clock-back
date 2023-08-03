package com.example.skhuclock.domain.Crawling;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "status")
    private String status;

    @Column(name = "title")
    private String title;

    @Column(name = "write_date")
    private String writeDate;

    @Column(name = "author")
    private String author;

    @Column(name = "views")
    private int views;

    @Column(name = "attachment")
    private String attachment;
}
