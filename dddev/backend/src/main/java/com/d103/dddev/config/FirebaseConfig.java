package com.d103.dddev.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.d103.dddev.api.alert.repository.AlertDataRepo;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FirebaseConfig {
	// @Value("${firebase-sdk-path}")
	// private String firebaseSdkPath = "src/main/resources/firebase-adminsdk.json";
	// private String firebaseSdkPath = "firebase-adminsdk.json";

	private final AlertDataRepo alertDataRepo;

	@PostConstruct
	public void initialize() {
		try {
			// ClassPathResource resource = new ClassPathResource(firebaseSdkPath);
			// InputStream serviceAccount = resource.getInputStream();
			FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-adminsdk.json");
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();
			if(FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
			alertDataRepo.getFirestore();
			log.info("Firebase :: getFirestore() success!");

		} catch (FileNotFoundException e) {
			log.error("Firebase ServiceAccountKey FileNotFoundException" + e.getMessage());
		} catch (IOException e) {
			log.error("FirebaseOptions IOException" + e.getMessage());
		} catch (Exception e) {
			log.error("Firebase PostConstruct Exception {}", e.getMessage());
		}
	}
}
