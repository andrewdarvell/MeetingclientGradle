package ru.darvell.android.meetingclient.api;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.io.IOException;

public class MeetingHttpRequest extends GoogleHttpClientSpiceRequest<String> {

    private String baseUrl;

    public MeetingHttpRequest(String url) {
        super(String.class);
        this.baseUrl = url;
    }

    @Override
    public String loadDataFromNetwork() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        HttpRequestFactory factory = httpTransport.createRequestFactory(new MeetingHttpRequestInitializer());
//        HttpRequest request = factory.buildGetRequest(new GenericUrl(baseUrl));
        HttpRequest request = factory.buildPostRequest(baseUrl,);

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
