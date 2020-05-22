package com.spooky.patito.model.sql;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "test_case")
public class TestCase {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    @Column(name = "input_path")
    String inputPath;

    @Column(name = "output_path")
    String outputPath;

    @Column(name = "ext_id")
    String extId;

    @Column(name = "problem_id")
    Long problemId;

}
