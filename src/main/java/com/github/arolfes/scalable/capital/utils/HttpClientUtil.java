package com.github.arolfes.scalable.capital.utils;

import static com.github.arolfes.scalable.capital.utils.StringUtils.removeUselessUrlChars;

import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.arolfes.scalable.capital.exceptions.NoResponseException;

public final class HttpClientUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

    private HttpClientUtil() {
        // private empty default constructor
    }

    public static List<String> queryGoogleAndReturnMainResultUrls(String searchString) throws Exception {
        String address = "http://www.google.de/search?client=ubuntu&channel=fs&ie=utf-8&oe=utf-8&q=" + URLEncoder.encode(searchString, "UTF-8");

        Optional<String> response = getContentOfPage(address);
        LOG.debug("google result {}", response);
        Document doc = Jsoup.parse(response.orElseThrow(() -> new NoResponseException("Query of " + address + " failed")));
        Elements links = doc.select("div.kCrYT > a[href]");
        return links.stream().map(link -> link.attr("href").replace("/url?q=", "")).map(s -> {
            String urlToDownload = removeUselessUrlChars(s);
            LOG.trace("url is {}", urlToDownload);
            return urlToDownload;
        }).collect(Collectors.toList());
    }


    public static Optional<String> getContentOfPage(String urlForGetRequest) throws Exception {

        CloseableHttpClient client = HttpClientBuilder.create().setSSLSocketFactory(createSSLConnectionSocketFactory()).disableCookieManagement()
                .setProxy(createProxy()).build();
        final HttpGet get = new HttpGet(urlForGetRequest);

        HttpResponse response = client.execute(get);
        int statusCode = response.getStatusLine().getStatusCode();
        LOG.debug("statusCode=" + statusCode);
        if (statusCode == HttpStatus.SC_OK) {
            return Optional.of(EntityUtils.toString(response.getEntity(), "UTF-8"));
        }
        LOG.error("StatusCode is not 200, was " + statusCode + ", url = " + urlForGetRequest);
        LOG.error("responseBody " + EntityUtils.toString(response.getEntity(), "UTF-8"));
        return Optional.empty();
    }

    protected static HttpHost createProxy() {

        Properties properties = System.getProperties();
        if (properties.containsKey("http.proxyHost") && properties.containsKey("http.proxyPort")) {
            return new HttpHost(properties.getProperty("http.proxyHost"), Integer.valueOf(properties.getProperty("http.proxyPort")), "http");
        } else if (properties.containsKey("https.proxyHost") && properties.containsKey("https.proxyPort")) {
            return new HttpHost(properties.getProperty("https.proxyHost"), Integer.valueOf(properties.getProperty("https.proxyPort")), "https");
        }
        return null;
    }

    protected static SSLConnectionSocketFactory createSSLConnectionSocketFactory() throws GeneralSecurityException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustAllStrategy());
        return new SSLConnectionSocketFactory(builder.build());
    }
}
