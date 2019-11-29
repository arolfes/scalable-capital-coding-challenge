package com.github.arolfes.scalable.capital.utils;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.arolfes.scalable.capital.exceptions.NoResponseException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientBuilder.class, EntityUtils.class, HttpClientUtil.class})
@PowerMockIgnore({"javax.net.ssl.*"})
public class HttpClientUtilTest {

    @Test
    public void testGetContentOfPage() throws Exception {
        mockStatic(HttpClientBuilder.class);
        mockStatic(EntityUtils.class);
        CloseableHttpClient clientMock = mock(CloseableHttpClient.class);
        HttpClientBuilder hcbMock = spy(HttpClientBuilder.class);
        when(HttpClientBuilder.create()).thenReturn(hcbMock);
        when(hcbMock.build()).thenReturn(clientMock);
        CloseableHttpResponse responseMock = mock(CloseableHttpResponse.class);
        when(clientMock.execute(any(HttpGet.class))).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));
        when(responseMock.getEntity()).thenReturn(mock(HttpEntity.class));
        when(EntityUtils.toString(any(HttpEntity.class), eq("UTF-8"))).thenReturn("someValue");
        assertThat(HttpClientUtil.getContentOfPage("www.spiegel.de").get(), is("someValue"));
        
    }

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void testQueryGoogle() throws Exception {
        PowerMockito.spy(HttpClientUtil.class);
        when(HttpClientUtil.getContentOfPage("http://www.google.de/search?client=ubuntu&channel=fs&ie=utf-8&oe=utf-8&q=Stackoverflow"))
                .thenReturn(readTestFileFromClasspath("www.google.de.html"));

        List<String> urls = HttpClientUtil.queryGoogleAndReturnMainResultUrls("Stackoverflow");
        assertThat(urls, hasSize(7));
        assertThat(urls,
                hasItems("https://stackoverflow.com", "https://de.wikipedia.org/wiki/Stack_Overflow_(Website)",
                        "https://www.stackoverflowbusiness.com/de/talent", "https://stackoverflow.blog", "https://stackoverflow.blog/podcast",
                        "https://twitter.com/stackoverflow", "https://stellenpakete.de/stellenanzeigen-schalten/stackoverflow-com"));
    }

    @Test
    public void testQueryGoogle_noResult() throws Exception {
        PowerMockito.spy(HttpClientUtil.class);
        when(HttpClientUtil.getContentOfPage("http://www.google.de/search?client=ubuntu&channel=fs&ie=utf-8&oe=utf-8&q=empty"))
                .thenReturn(Optional.empty());

        ex.expect(NoResponseException.class);
        ex.expectMessage("Query of http://www.google.de/search?client=ubuntu&channel=fs&ie=utf-8&oe=utf-8&q=empty failed");
        HttpClientUtil.queryGoogleAndReturnMainResultUrls("empty");
    }

    private static Optional<String> readTestFileFromClasspath(String fileName) throws Exception {
        return Optional.of(new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))));
    }

}
