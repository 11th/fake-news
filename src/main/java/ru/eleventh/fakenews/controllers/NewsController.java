package ru.eleventh.fakenews.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.eleventh.fakenews.io.ZipFile;
import ru.eleventh.fakenews.models.News;
import ru.eleventh.fakenews.models.Rubric;
import ru.eleventh.fakenews.service.NewsService;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }

    @GetMapping()
    public String main(Model model,
                       @RequestParam(required = false, defaultValue = "") String rubric,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC ) Pageable pageable) {
        if (rubric != null && !rubric.isEmpty()){
            model.addAttribute("newsList", newsService.findAllByRubric(rubric, pageable));
        } else {
            model.addAttribute("newsList", newsService.findAll(pageable));
        }
        return "main";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("news", newsService.findById(id));
        return "show";
    }

    @GetMapping("new")
    public String addNews(@ModelAttribute("news") News news) {
        return "new";
    }

    @PostMapping
    public String create(@ModelAttribute("news") @Valid News news, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "new";
        newsService.save(news);
        return "redirect:news";
    }

    @GetMapping("upload")
    public String uploadNews(@ModelAttribute("news") News news) {
        return "upload";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam(value = "file") MultipartFile file,
                         @RequestParam(value = "rubric", required = false) String rubric,
                         Model model) {
        try {
            ZipFile zipFile = ZipFile.getInstance(file);
            if (zipFile.isValid()) {
                News news = new News();
                news.setTitle(zipFile.getFirstRow());
                news.setText(zipFile.getSecondRow());
                news.setRubric(rubric);
                newsService.save(news);
            }
        } catch (IOException | RuntimeException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "upload";
        }
        return "redirect:/news";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("news", newsService.findById(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("news") @Valid News news, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "edit";
        newsService.update(id, news);
        return "redirect:/news";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        newsService.delete(id);
        return "redirect:/news";
    }

    @GetMapping("addTestArticles")
    public String addTestArticles(){
        addTestArticles(Rubric.DEFAULT);
        addTestArticles(Rubric.TRAVELING);
        addTestArticles(Rubric.POLITICS);
        addTestArticles(Rubric.FINANCE);
        addTestArticles(Rubric.SPORT);
        return "redirect:/news";
    }

    private void addTestArticles(Rubric rubric){
        News news = new News();
        news.setRubric(rubric.getDisplayValue());
        switch (rubric) {
            case FINANCE: {
                news.setTitle("What is Bitcoin???");
                news.setText("This is an article about bitcoin...");
                break;
            }
            case POLITICS: {
                news.setTitle("Democracy for everybody!!!");
                news.setText("This is an article about democracy...");
                break;
            }
            case SPORT: {
                news.setTitle("Sport is life");
                news.setText("This is an article about sport...");
                break;
            }
            case TRAVELING: {
                news.setTitle("From Russia with love!!!");
                news.setText("This is an article about Russia...");
                break;
            }
            default: {
                news.setTitle("I don't know");
                news.setText("Nothing to say...");
            }
        }
        newsService.save(news);
    }
}
