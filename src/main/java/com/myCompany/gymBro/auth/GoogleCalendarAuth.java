package com.myCompany.gymBro.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class GoogleCalendarAuth {

    public static final String APPLICATION_NAME = "Gymbro project";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    public static Credential authorize() throws IOException {
        InputStream in = GoogleCalendarAuth.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new IOException("Resource not found: /credentials.json");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static GoogleAuthorizationCodeRequestUrl getAuthorizationUrl() throws IOException {
        InputStream in = GoogleCalendarAuth.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();

        return flow.newAuthorizationUrl().setRedirectUri("http://localhost:8080/oauth2/callback");
    }
}
