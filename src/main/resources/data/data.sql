DROP TABLE IF EXISTS news;

CREATE TABLE news
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    title  VARCHAR NOT NULL,
    text   VARCHAR NOT NULL,
    rubric VARCHAR
);

INSERT INTO news (id, title, text, rubric)
VALUES (1, 'What is Bitcoin???', 'This is an article about bitcoin...', 'Finance'),
       (2, 'Democracy for everybody!!!', 'This is an article about democracy...', 'Politics'),
       (3, 'Sport is life', 'This is an article about sport...', 'Sport'),
       (4, 'From Russia with love!!!', 'This is an article about Russia...', 'Traveling'),
       (5, 'I do not know', 'Nothing to say...', '');