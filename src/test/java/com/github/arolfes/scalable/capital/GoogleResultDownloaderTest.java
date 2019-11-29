package com.github.arolfes.scalable.capital;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.arolfes.scalable.capital.utils.HttpClientUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientUtil.class})
public class GoogleResultDownloaderTest {

    @Test
    public void testDownloadUrlAndCountJsFrameworks() throws Exception {
        mockStatic(HttpClientUtil.class);
        when(HttpClientUtil.getContentOfPage("https://stackoverflow.blog")).thenReturn(readTestFileFromClasspath("stackoverflow.blog.html"));
        ConcurrentHashMap<String, Integer> jsCountMap = new ConcurrentHashMap<>();
        GoogleResultDownloader googleResultDownloader = new GoogleResultDownloader(jsCountMap);
        googleResultDownloader.downloadUrlAndCountJsFrameworks("https://stackoverflow.blog");
        assertThat(jsCountMap, aMapWithSize(10));
        assertThat(jsCountMap.get("jquery.js"), is(1));
        assertThat(jsCountMap.get("stackoverflow.min.js"), is(1));
    }

    private static Optional<String> readTestFileFromClasspath(String fileName) throws Exception {
        return Optional.of(new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))));
    }

}
