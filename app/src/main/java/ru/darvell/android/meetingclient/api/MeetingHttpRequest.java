package ru.darvell.android.meetingclient.api;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.io.IOException;
import java.util.Map;

public class MeetingHttpRequest extends GoogleHttpClientSpiceRequest<Object> {

    private String baseUrl;
    private Map<String, String> parameters;
    private int requestType;

    public MeetingHttpRequest(String url, Map<String, String> parameters, int requestType) {
        super(Object.class);
        this.baseUrl = url;
        this.parameters = parameters;
        this.requestType = requestType;
    }


    @Override
    public Object loadDataFromNetwork() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        HttpRequestFactory factory = httpTransport.createRequestFactory();

        HttpContent content = new UrlEncodedContent(parameters);
        HttpRequest request = factory.buildPostRequest(new GenericUrl(baseUrl), content);

        return null;
    }

    private class MeetingHttpRequestInitializer implements HttpRequestInitializer{

        MeetingHttpRequestInitializer() {}

        @Override
        public void initialize(HttpRequest request) throws IOException {
            // тут можно добавить каких-то параметров к request, например, парсер json
//            request.setParser(new JacksonFactory().createJsonObjectParser()));
//            request.setParser(new JsonObjectParser(new JacksonObjectPersisterFactory() ));
//            request.setParser(new JacksonFactory().createJsonObjectParser());
        }
    }
}
