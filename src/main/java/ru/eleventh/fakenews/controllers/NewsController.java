package ru.eleventh.fakenews.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.eleventh.fakenews.models.News;
import ru.eleventh.fakenews.service.NewsService;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

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
            newsService.upload(file, rubric);
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
}
