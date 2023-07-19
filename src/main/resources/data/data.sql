DROP TABLE IF EXISTS news;

CREATE TABLE news
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    title  VARCHAR NOT NULL,
    text   VARCHAR NOT NULL
);

-- INSERT INTO news (id, title, text)
-- VALUES (1, 'News 1', 'Text 1'),
--        (2, 'News 2', 'Text 2'),
--        (3, 'News 3', 'Text 3'),
--        (4, 'News 4', 'Text 4'),
--        (5, 'News 5', 'Text 5');