package ru.eleventh.fakenews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.eleventh.fakenews.io.ZipFile;
import ru.eleventh.fakenews.models.News;
import ru.eleventh.fakenews.repository.NewsRepository;

import java.io.IOException;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Page<News> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    public News findById(long id) {
        return newsRepository.findById(id).stream().findAny().orElse(null);
    }

    public Page<News> findAllByRubric(String rubric, Pageable pageable) {
        return newsRepository.findAllByRubric(rubric, pageable);
    }

    public void save(News news) {
        newsRepository.save(news);
    }

    public void update(int id, News news) {
        news.setId(id);
        newsRepository.save(news);
    }

    public void delete(long id) {
        newsRepository.deleteById(id);
    }

    public void upload(MultipartFile file, String rubric) throws IOException {
        ZipFile zipFile = ZipFile.getInstance(file);
        zipFile.checkFileValid();
        News news = new News();
        news.setTitle(zipFile.getFirstRow());
        news.setText(zipFile.getSecondRow());
        news.setRubric(rubric);
        newsRepository.save(news);
    }
}
