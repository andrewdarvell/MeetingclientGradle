package ru.darvell.android.meetingclient.api;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.io.IOException;
import java.util.Map;

public class MeetingHttpRequest extends GoogleHttpClientSpiceRequest<String> {

    private String baseUrl;
    private Map<String, String> parameters;

    public MeetingHttpRequest(String url, Map<String, String> parameters) {
        super(String.class);
        this.baseUrl = url;
        this.parameters = parameters;
    }

    @Override
    public String loadDataFromNetwork() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        HttpRequestFactory factory = httpTransport.createRequestFactory(new MeetingHttpRequestInitializer());

        HttpContent content = new UrlEncodedContent(parameters);
        HttpRequest request = factory.buildPostRequest(new GenericUrl(baseUrl), content);
        return request.execute().parseAsString();
    }

    private class MeetingHttpRequestInitializer implements HttpRequestInitializer{

        MeetingHttpRequestInitializer() {}

        @Override
        public void initialize(HttpRequest request) throws IOException {
            // тут можно добавить каких-то параметров к request, например, парсер json
            // request.setParser(new JacksonFactory().createJsonObjectParser()));
        }
    }
}
