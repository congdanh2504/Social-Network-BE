package com.example.social_network_fpt_be.config;

import com.example.social_network_fpt_be.SocialNetworkFptBeApplication;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
@Configuration
public class ConnectFirebase {
    public ConnectFirebase() throws IOException {
        ClassLoader classLoader = SocialNetworkFptBeApplication.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("ServiceAccountKey.json")).getFile());
        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
