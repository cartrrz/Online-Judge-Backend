CREATE TABLE IF NOT EXISTS test_case(
    id INT UNSIGNED AUTO_INCREMENT,
    problem_id INT(30) NOT NULL,
    input_path VARCHAR(500) NOT NULL,
    output_path VARCHAR(500) NOT NULL,
    ext_id INT NOT NULL UNIQUE,
    PRIMARY KEY (id)
)
