CREATE DATABASE IF NOT EXISTS school;
USE school;

CREATE TABLE class (
    class_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    course_number INTEGER NOT NULL,
    term VARCHAR(50) NOT NULL,
    section_number INTEGER NOT NULL,
    description VARCHAR(500) NOT NULL
);

CREATE TABLE category (
    name VARCHAR(50) PRIMARY KEY NOT NULL
);

CREATE TABLE assignment(
    assignment_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    point_value INTEGER NOT NULL,
    description VARCHAR(500) NOT NULL
);

CREATE TABLE student(
    student_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE class_category(
    class_id INTEGER NOT NULL,
    category_name VARCHAR(50) NOT NULL,
    weight INTEGER NOT NULL,

    FOREIGN KEY (class_id) REFERENCES class (class_id),
    FOREIGN KEY (category_name) REFERENCES category (name)
);

CREATE TABLE class_student(
    class_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    grade INTEGER NOT NULL,

    FOREIGN KEY (class_id) REFERENCES class (class_id),
    FOREIGN KEY (student_id) REFERENCES student (student_id)
);

CREATE TABLE assignment_student(
    assignment_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    grade INTEGER NOT NULL,

    FOREIGN KEY (assignment_id) REFERENCES assignment (assignment_id),
    FOREIGN KEY (student_id) REFERENCES student (student_id)
);