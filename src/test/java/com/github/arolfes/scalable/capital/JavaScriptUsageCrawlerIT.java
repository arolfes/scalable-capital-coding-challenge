package com.github.arolfes.scalable.capital;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class JavaScriptUsageCrawlerIT {


    @Test
    public void testSearchFor_How_can_i_parse_a_html_string_in_java() throws Exception {
        JavaScriptUsageCrawler javaScriptUsageCrawler = new JavaScriptUsageCrawler();
        List<String> listTop5JavaScriptLibs = javaScriptUsageCrawler.listTop5JavaScriptLibs("How can I parse a HTML string in Java?");
        assertThat(listTop5JavaScriptLibs, hasSize(5));
        assertThat(listTop5JavaScriptLibs, hasItems("js name='jquery.js' - occurence='3'", "js name='rochester.js' - occurence='3'"));
    }

    @Test
    public void testSearchFor_java_create_synchronized_map() throws Exception {
        JavaScriptUsageCrawler javaScriptUsageCrawler = new JavaScriptUsageCrawler();
        List<String> listTop5JavaScriptLibs = javaScriptUsageCrawler.listTop5JavaScriptLibs("java create synchronized map");
        assertThat(listTop5JavaScriptLibs, hasSize(5));
        assertThat(listTop5JavaScriptLibs, hasItems("js name='jquery.js' - occurence='6'", "js name='jquery-migrate.min.js' - occurence='4'"));
    }

    @Test
    public void testSearchFor_java_nio_read_file_to_string_from_classpath() throws Exception {
        JavaScriptUsageCrawler javaScriptUsageCrawler = new JavaScriptUsageCrawler();
        List<String> listTop5JavaScriptLibs = javaScriptUsageCrawler.listTop5JavaScriptLibs("java nio read file to string from classpath");
        assertThat(listTop5JavaScriptLibs, hasSize(5));
        assertThat(listTop5JavaScriptLibs, hasItems("js name='jquery.js' - occurence='2'", "js name='plusone.js' - occurence='2'"));

    }

}
