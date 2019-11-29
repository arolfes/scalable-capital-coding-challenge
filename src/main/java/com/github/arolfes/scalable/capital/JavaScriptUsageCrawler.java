package com.github.arolfes.scalable.capital;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.arolfes.scalable.capital.utils.HttpClientUtil;

public class JavaScriptUsageCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(JavaScriptUsageCrawler.class);

    public static void main(String[] args) throws Exception {
        JavaScriptUsageCrawler js = new JavaScriptUsageCrawler();
        js.listTop5JavaScriptLibs(args[0]).stream().forEach(System.out::println);
    }

    protected List<String> listTop5JavaScriptLibs(String searchString) throws Exception {
        List<String> queryGoogle = HttpClientUtil.queryGoogleAndReturnMainResultUrls(searchString);
        queryGoogle.parallelStream().forEach(LOG::info);

        ConcurrentHashMap<String, Integer> jsCountMap = new ConcurrentHashMap<>();

        queryGoogle.parallelStream().forEach(url -> {
            LOG.debug("going to download url {} ", url);
            GoogleResultDownloader googleResultDownloader = new GoogleResultDownloader(jsCountMap);
            googleResultDownloader.downloadUrlAndCountJsFrameworks(url);
        });

        LinkedHashMap<String, Integer> jsCountSorted =
                jsCountMap.entrySet().stream().sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return jsCountSorted.entrySet().stream().limit(5).map(e -> new String("js name='" + e.getKey() + "' - occurence='" + e.getValue() + "'"))
                .collect(Collectors.toList());
    }


}
