package com.star.douban.rest;

import com.star.douban.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: zzStar
 * @Date: 05-15-2021 13:19
 */
@RestController
public class MovieController {

    @Resource
    private MovieService movieService;

    @GetMapping("/search/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) throws IOException {
        return movieService.parseMovie(keyword);
    }

    @GetMapping("/search/{keyword}/{page}/{size}")
    public List<Map<String, Object>> search(@PathVariable("keyword") String keyword,
                                            @PathVariable("page") int page,
                                            @PathVariable("size") int size) throws IOException {
        movieService.parseMovie(keyword);
        return movieService.searchResults(keyword, page, size);
    }
}
