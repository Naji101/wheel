package com.snipers.wheel.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirestoreConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            // Get the service account JSON from an environment variable
            String serviceAccountKey = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
            if (serviceAccountKey == null) {
                throw new IllegalStateException("Environment variable GOOGLE_APPLICATION_CREDENTIALS_JSON not set");
            }
            // Convert the JSON string to an InputStream
            ByteArrayInputStream serviceAccountStream =
                    new ByteArrayInputStream(serviceAccountKey.getBytes(StandardCharsets.UTF_8));

            // Build the FirebaseOptions with the credentials
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            // Initialize the FirebaseApp instance
            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    @Bean
    public Firestore firestore() {
        // Return the Firestore client from the initialized Firebase app
        return FirestoreClient.getFirestore(firebaseApp());
    }
}
