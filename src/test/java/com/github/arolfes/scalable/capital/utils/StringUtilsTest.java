package com.github.arolfes.scalable.capital.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testRemoveAllAfterFirstAmpersand() throws Exception {
        assertThat(StringUtils.removeAllAfterFirstAmpersand("lang=de&modules=startup"), is("lang=de"));
        assertThat(StringUtils.removeAllAfterFirstAmpersand("lang=de_modules=startup"), is("lang=de_modules=startup"));
    }

    @Test
    public void testRemoveAllAfterFirstQuestionMark() throws Exception {
        assertThat(StringUtils.removeAllAfterFirstQuestionMark("value?lang=de&modules=startup"), is("value"));
        assertThat(StringUtils.removeAllAfterFirstQuestionMark("lang=de_modules=startup"), is("lang=de_modules=startup"));
    }

    @Test
    public void testRemoveAllAfterFirstEncodedQuestionMark() throws Exception {
        assertThat(StringUtils.removeAllAfterFirstEncodedQuestionMark("value%3Flang=de&modules=startup"), is("value"));
        assertThat(StringUtils.removeAllAfterFirstEncodedQuestionMark("lang=de_modules=startup"), is("lang=de_modules=startup"));
    }

    @Test
    public void testRemoveLastSlash() throws Exception {
        assertThat(StringUtils.removeLastSlash("/value/"), is("/value"));
        assertThat(StringUtils.removeLastSlash("/value\\"), is("/value\\"));
    }

    @Test
    public void testRemoveAllBeforeLastSlash() throws Exception {
        assertThat(StringUtils.removeAllBeforeLastSlash("someBefore/value"), is("value"));
        assertThat(StringUtils.removeAllBeforeLastSlash("someBefore\\value\\"), is("someBefore\\value\\"));
    }

    @Test
    public void testRemoveUselessUrlChars() throws Exception {
        assertThat(StringUtils.removeUselessUrlChars(
                "https://stackoverflow.com/&amp;sa=U&amp;ved=2ahUKEwjKnfGW8ojmAhXhN-wKHVxiAVgQFjAAegQIBxAB&amp;usg=AOvVaw3y-mvPV7GPH7mWWlPo8qjH"),
                is("https://stackoverflow.com"));
        assertThat(StringUtils.removeUselessUrlChars(
                "https://stackoverflow.com/jobs&amp;sa=U&amp;ved=2ahUKEwjKnfGW8ojmAhXhN-wKHVxiAVgQjBAwAXoECAcQAw&amp;usg=AOvVaw0eRL5-sfzSAUHd5FbrspD0"),
                is("https://stackoverflow.com/jobs"));
    }

    @Test
    public void testSpecialApiJsHandling() throws Exception {
        assertThat(StringUtils.specialGoogleApiHandling("//google/maps/api/js?someKey=someValue"), is("maps/api/js?someKey=someValue"));
    }

    @Test
    public void testRemoveUselessJsChars() throws Exception {
        assertThat(StringUtils.removeUselessJsChars(
                "//maps.googleapis.com/maps/api/js?callback=initMap&amp;key=AIzaSyB2SwEnLolzqpyBcZobqtXhYG-yi_dLZjk"), is("maps/api/js"));
        assertThat(StringUtils.removeUselessJsChars("https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"), is("jquery.min.js"));
        assertThat(StringUtils.removeUselessJsChars("//use.typekit.net/tbm7ngk.js"), is("tbm7ngk.js"));
        assertThat(StringUtils.removeUselessJsChars("//use.typekit.net/tbm7ngk.js&amp;someKey%3Fothers?otherothers"), is("tbm7ngk.js"));
    }

    @Test
    public void testExtractFileName() throws Exception {
        assertThat(StringUtils.extractFileName("https://stackoverflow.blog/podcast&amp;someKey"), is("stackoverflow.blog_podcast.html"));
        assertThat(StringUtils.extractFileName("https://de.wikipedia.org/wiki/Stack_Overflow_(Website)"),
                is("de.wikipedia.org_wiki_Stack_Overflow_(Website).html"));

    }



}
