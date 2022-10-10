package com.chord.nft.metadata.service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

//@Service
public class GraphqlHttpService {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final OkHttpClient client = new OkHttpClient();

   // @Value("${core.api}")
    private String coreAPIURL;

    public String post(String query, String variables) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(coreAPIURL);

        URI uri = new URIBuilder(request.getURI())
                .addParameter("query", query)
                .addParameter("variables", variables)
                .build();

        request.setURI(uri);
        HttpResponse response = client.execute(request);
        return EntityUtils.toString(response.getEntity());
    }
}
