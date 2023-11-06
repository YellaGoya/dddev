package com.d103.dddev.api.alert.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.d103.dddev.api.alert.entity.AlertDataEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AlertDataRepo {
	public static final String COLLECTION_NAME = "AlertData";
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

	public void addAlertData(AlertDataEntity alertDataEntity) throws Exception{
		// Firestore FIRE_STORE = FirestoreClient.getFirestore();
		DocumentReference document = FIRE_STORE.collection(COLLECTION_NAME).document();
		document.set(alertDataEntity);
		log.info("새로운 문서 - 알림 데이터 - 가 추가되었습니다. document id: {}", document.getId()	);
	}
}
