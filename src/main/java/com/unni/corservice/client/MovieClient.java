package com.unni.corservice.client;

import com.unni.corservice.config.CustomClientConfig;
import com.unni.corservice.config.MovieHeaders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "movie-client", url = "${movie-client.url}",
configuration = {MovieHeaders.class, CustomClientConfig.class})
public interface MovieClient {

    @GetMapping("/FindByImbdId/{imdbId}")
    String getMovieById(@PathVariable(name = "imdbId") String imdbId);
}
