package ru.eleventh.fakenews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.eleventh.fakenews.models.News;
import ru.eleventh.fakenews.repository.NewsRepository;

@Service
public class NewsService {
    private NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Page<News> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    public News findById(long id) {
        return newsRepository.findById(id).stream().findAny().orElse(null);
    }

    public Page<News> findAllByRubric (String rubric, Pageable pageable){
        return newsRepository.findAllByRubric(rubric, pageable);
    }

    public void save(News news) {
        newsRepository.save(news);
    }

    public void update(int id, News news) {
        newsRepository.save(news);
    }

    public void delete(long id) {
        newsRepository.deleteById(id);
    }
}
