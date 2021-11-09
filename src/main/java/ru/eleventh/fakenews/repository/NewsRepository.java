package ru.eleventh.fakenews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.eleventh.fakenews.models.News;

public interface NewsRepository extends CrudRepository<News, Long> {
    Page<News> findAll (Pageable pageable);

    Page<News> findAllByRubric (String rubric, Pageable pageable);
}
