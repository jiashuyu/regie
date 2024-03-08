DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS course_loads;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS course_prerequisites;
DROP TABLE IF EXISTS labs;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS students;

CREATE TABLE students
(
    id         SERIAL PRIMARY KEY   NOT NULL,
    email      TEXT UNIQUE          NOT NULL,
    enabled    BOOLEAN DEFAULT TRUE NOT NULL,
    password   TEXT                 NOT NULL,
    first_name TEXT,
    last_name  TEXT
);

CREATE TABLE authorities
(
    id         SERIAL PRIMARY KEY NOT NULL,
    email      TEXT               NOT NULL,
    authority  TEXT               NOT NULL,
    FOREIGN KEY (email) REFERENCES students (email) ON DELETE CASCADE
);

CREATE TABLE departments (
     id          SERIAL PRIMARY KEY,
     name        TEXT NOT NULL,
     chair_email TEXT
);

CREATE TABLE courses (
    id              SERIAL PRIMARY KEY,
    department_id   INT NOT NULL,
    course_number   TEXT NOT NULL,
    name            TEXT NOT NULL,
    max_enrollment  INT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments (id)
);

CREATE TABLE labs (
    id SERIAL PRIMARY KEY,
    course_id INT NOT NULL,
    lab_number TEXT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses (id)
);

CREATE TABLE course_prerequisites (
    id              SERIAL PRIMARY KEY,
    course_id       INT NOT NULL,
    prerequisite_id INT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (prerequisite_id) REFERENCES courses(id),
    UNIQUE(course_id, prerequisite_id)
);

CREATE TABLE enrollments (
    id          SERIAL PRIMARY KEY,
    student_id  INT NOT NULL,
    course_id   INT NOT NULL,
    status      TEXT NOT NULL, -- ('registered', 'dropped', 'pending_approval')
    quarter     TEXT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    UNIQUE(student_id, course_id)
);

CREATE TABLE course_loads (
    student_id   INT PRIMARY KEY,
    current_load INT NOT NULL,
    max_load     INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE TABLE notifications (
    id          SERIAL PRIMARY KEY,
    student_id  INT NOT NULL,
    type        TEXT NOT NULL, -- ('course_added', 'course_dropped', 'approval_request', 'approval_decision')
    message     TEXT NOT NULL,
    sent        BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

INSERT INTO departments (name, chair_email) VALUES ('Computer Science', 'mark@uchicago.edu');

INSERT INTO courses (department_id, course_number, name, max_enrollment)
VALUES (1, '51036', 'Java Programming', 40),
       (1, '51410', 'Object Oriented Programming', 20);

INSERT INTO course_prerequisites (course_id, prerequisite_id) VALUES (2, 1);

INSERT INTO students (email, enabled, password, first_name, last_name)
VALUES ('jiashuyu@uchicago.edu', true, '123456', 'Shuyu', 'Jia'),
       ('brandon@uchicago.edu', true, '123456', 'Brandon', 'Lee');

INSERT INTO enrollments (student_id, course_id, status, quarter)
VALUES (1, 1, 'registered', 'Spring 2024'),
       (2, 1, 'pending_approval', 'Winter 2024');

INSERT INTO notifications (student_id, type, message, sent)
VALUES (1, 'course_added', 'You have been enrolled in a course.', false);
