package com.snipers.wheel.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirestoreConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            // Load the service account key JSON file
            FileInputStream serviceAccount = new FileInputStream("C:\\Users\\Nader Qanadilo\\Desktop\\Naji Projects\\wheel\\wheel\\target\\classes\\wheel-eb920-firebase-adminsdk-zd34d-902d603493.json");

            // Build the FirebaseOptions with the credentials
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Initialize the FirebaseApp instance
            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase test", e);
        }
    }

    @Bean
    public Firestore firestore() {
        // Return the Firestore client from the initialized Firebase app
        return FirestoreClient.getFirestore(firebaseApp());
    }
}
