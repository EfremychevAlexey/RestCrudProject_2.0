--Создаем схему, удаляя прежнюю, если такая существует
DROP SCHEMA IF EXISTS school CASCADE;
CREATE SCHEMA IF NOT EXISTS school AUTHORIZATION admin;


--Создаем таблицу курсов
CREATE TABLE IF NOT EXISTS school.courses
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    course_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT courses_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS school.courses
    OWNER to admin;


--Создаем таблицу со учителями
CREATE TABLE IF NOT EXISTS school.teachers
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    teacher_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT teachers_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS school.teachers
    OWNER to admin;


--Создаем таблицу со студентами
CREATE TABLE IF NOT EXISTS school.students
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    student_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    course_id bigint,
    CONSTRAINT students_pkey PRIMARY KEY (id),
    CONSTRAINT courses_fkey FOREIGN KEY (course_id)
        REFERENCES school.courses (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS school.students
    OWNER to admin;


--Создаем связывающую таблицу для курсов и учителей(связь многие ко многим)
CREATE TABLE IF NOT EXISTS school.courses_teachers
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    course_id bigint NOT NULL,
    teacher_id bigint NOT NULL,
    CONSTRAINT courses_teachers_pkey PRIMARY KEY (id),
    CONSTRAINT courses_fkey FOREIGN KEY (course_id)
        REFERENCES school.courses (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT teachers_fkey FOREIGN KEY (teacher_id)
        REFERENCES school.teachers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS school.courses_teachers
    OWNER to admin;


--Заполняем таблицы начальными данными.
INSERT INTO school.courses (course_name) VALUES ('Java');
INSERT INTO school.courses (course_name) VALUES ('Python');
INSERT INTO school.courses (course_name) VALUES ('JavaScript');
INSERT INTO school.courses (course_name) VALUES ('PHP');
INSERT INTO school.courses (course_name) VALUES ('SQL');

INSERT INTO school.teachers (teacher_name) VALUES ('Демидов Дмитрий');
INSERT INTO school.teachers (teacher_name) VALUES ('Чайкина Ольга');
INSERT INTO school.teachers (teacher_name) VALUES ('Дудин Виктор');

INSERT INTO school.courses_teachers (course_id, teacher_id) VALUES ('2', '1');
INSERT INTO school.courses_teachers (course_id, teacher_id) VALUES ('4', '1');
INSERT INTO school.courses_teachers (course_id, teacher_id) VALUES ('3', '2');
INSERT INTO school.courses_teachers (course_id, teacher_id) VALUES ('5', '2');
INSERT INTO school.courses_teachers (course_id, teacher_id) VALUES ('1', '3');
INSERT INTO school.courses_teachers (course_id, teacher_id) VALUES ('2', '3');
INSERT INTO school.courses_teachers (course_id, teacher_id) VALUES ('3', '3');
INSERT INTO school.courses_teachers (course_id, teacher_id) VALUES ('4', '3');
INSERT INTO school.courses_teachers (course_id, teacher_id) VALUES ('5', '3');

INSERT INTO school.students (student_name, course_id) VALUES ('Alexey Efremychev', '1');
INSERT INTO school.students (student_name, course_id) VALUES ('Alexey Shakshin', '1');
INSERT INTO school.students (student_name, course_id) VALUES ('Alexey Ilin', '1');
INSERT INTO school.students (student_name, course_id) VALUES ('Maxim Efremychev', '2');