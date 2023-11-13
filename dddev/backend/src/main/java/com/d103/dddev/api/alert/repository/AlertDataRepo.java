package com.d103.dddev.api.alert.repository;

import org.springframework.stereotype.Repository;

import com.d103.dddev.api.alert.entity.AlertHistoryDocument;
import com.d103.dddev.api.alert.entity.AlertUserHistoryDocument;
import com.d103.dddev.api.alert.entity.WebhookDataDocument;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AlertDataRepo {
	public static final String ALERT_NAME = "AlertData";
	public static final String WEBHOOK_NAME = "webhookData";
	public static final String ALERT_USER_NAME = "alertUserData";
	// private Firestore FIRE_STORE = FirestoreClient.getFirestore();
	private Firestore FIRE_STORE = null;

	// public List<AlertDataEntity> getAllAlertData() throws Exception {
	// 	List<AlertDataEntity> list = new ArrayList<>();
		// ApiFuture<QuerySnapshot> future = FIRE_STORE.collection(COLLECTION_NAME).get();
		// List<QueryDocumentSnapshot> documents = future.get().getDocuments();
		// for(QueryDocumentSnapshot document : documents) {
		// 	list.add(document.toObject(AlertDataEntity.class));
		// }
		// return list;
	// }

	public void getFirestore() {
		FIRE_STORE = FirestoreClient.getFirestore();
	}

	public String addAlertData(AlertHistoryDocument alertHistoryDocument) throws Exception{
		// Firestore FIRE_STORE = FirestoreClient.getFirestore();
		DocumentReference document = FIRE_STORE.collection(ALERT_NAME).document();
		document.set(alertHistoryDocument);
		// log.info("새로운 문서 - 알림 데이터 - 가 추가되었습니다. document id: {}", document.getId()	);
		return document.getId();
	}

	public String addWebhookData(WebhookDataDocument webhookDataDocument) throws Exception{
		// Firestore FIRE_STORE = FirestoreClient.getFirestore();
		DocumentReference document = FIRE_STORE.collection(WEBHOOK_NAME).document(webhookDataDocument.getId());
		document.set(webhookDataDocument);
		// log.info("새로운 문서 - 웹훅 데이터 - 가 추가되었습니다. document id: {}", document.getId()	);
		return document.getId();
	}

	public String addAlertUserData(AlertUserHistoryDocument alertUserHistoryDocument) throws Exception{
		// Firestore FIRE_STORE = FirestoreClient.getFirestore();
		DocumentReference document = FIRE_STORE.collection(ALERT_USER_NAME).document();
		document.set(alertUserHistoryDocument);
		// log.info("새로운 문서 - 웹훅 데이터 - 가 추가되었습니다. document id: {}", document.getId()	);
		return document.getId();
	}
}
