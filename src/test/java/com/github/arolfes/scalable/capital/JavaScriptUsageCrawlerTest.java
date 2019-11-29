package com.github.arolfes.scalable.capital;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.arolfes.scalable.capital.utils.HttpClientUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientUtil.class})
public class JavaScriptUsageCrawlerTest {

    @Test
    public void testListTop5JavaScriptLibs() throws Exception {
        mockStatic(HttpClientUtil.class);
        when(HttpClientUtil.queryGoogleAndReturnMainResultUrls("stackoverflow"))
                .thenReturn(Arrays.asList(new String[] {"https://stackoverflow.blog", "https://stackoverflow.blog/podcast/"}));
        when(HttpClientUtil.getContentOfPage("https://stackoverflow.blog")).thenReturn(readTestFileFromClasspath("stackoverflow.blog.html"));
        when(HttpClientUtil.getContentOfPage("https://stackoverflow.blog/podcast/"))
                .thenReturn(readTestFileFromClasspath("stackoverflow.blog_podcast.html"));

        JavaScriptUsageCrawler javaScriptUsageCrawler = new JavaScriptUsageCrawler();
        List<String> listTop5JavaScriptLibs = javaScriptUsageCrawler.listTop5JavaScriptLibs("stackoverflow");
        assertThat(listTop5JavaScriptLibs, hasSize(5));
        assertThat(listTop5JavaScriptLibs, hasItems("js name='wp-hide-post-public.js' - occurence='2'", "js name='e-201948.js' - occurence='2'"));
    }


    private static Optional<String> readTestFileFromClasspath(String fileName) throws Exception {
        return Optional.of(new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))));
    }

}
