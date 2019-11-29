package com.github.arolfes.scalable.capital;

import static com.github.arolfes.scalable.capital.utils.StringUtils.extractFileName;
import static com.github.arolfes.scalable.capital.utils.StringUtils.removeUselessJsChars;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.arolfes.scalable.capital.utils.HttpClientUtil;

public class GoogleResultDownloader {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleResultDownloader.class);

    private final ConcurrentHashMap<String, Integer> jsCountMap;

    public GoogleResultDownloader(ConcurrentHashMap<String, Integer> jsCountMap) {
        this.jsCountMap = jsCountMap;
    }

    public void downloadUrlAndCountJsFrameworks(String url) {
        try {
            Optional<String> response = HttpClientUtil.getContentOfPage(url);
            LOG.debug("google result ispresent {}", response.isPresent());
            if (!response.isPresent()) {
                LOG.warn("no content loaded for {}", url);
                return;
            }

            String fileName = extractFileName(url);
            LOG.info("result will be saved in " + fileName);

            Files.write(Paths.get("./target/" + fileName), response.get().getBytes());

            Document doc = Jsoup.parse(response.get());
            Elements scripts = doc.select("script[type='text/javascript']");
            scripts.stream().map(script -> script.attr("src")).filter(s -> !s.isEmpty() && !s.equals(" ")).map(s -> {
                String jsUrl = removeUselessJsChars(s);
                jsCountMap.put(jsUrl, jsCountMap.getOrDefault(jsUrl, 0) + 1);
                return jsUrl;
            }).forEach(LOG::debug);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }



}
