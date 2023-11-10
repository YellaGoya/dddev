// importScripts('https://www.gstatic.com/firebasejs/10.5.0/firebase-app-compat.js');
// importScripts('https://www.gstatic.com/firebasejs/10.5.0/firebase-firestore-compat.js');

// const firebaseConfig = {
//   apiKey: 'AIzaSyD1HPZuKiyZOvTJMyqUhf1dv6wkJaRRaKM',
//   authDomain: 'dddev-1e0d5.firebaseapp.com',
//   projectId: 'dddev-1e0d5',
//   storageBucket: 'dddev-1e0d5.appspot.com',
//   messagingSenderId: '1067642901305',
//   appId: '1:1067642901305:web:6510a6e3861c9414c2cee8',
//   measurementId: 'G-8KXEK3KVET',
// };

// const firebaseApp = firebase.initializeApp(firebaseConfig);

// const db = firebaseApp.firestore();

// console.log('firebase-messaging-sw.js 실행중');

self.addEventListener('install', function (e) {
  // console.log('fcm sw install..');
  self.skipWaiting();
});

self.addEventListener('activate', function (e) {
  // console.log('fcm sw activate..');
});

self.addEventListener('push', function (e) {
  // console.log('push: ', e.data.json());
  if (!e.data.json()) return;

  const resultData = e.data.json().notification;
  const notificationTitle = resultData.title;
  const notificationOptions = {
    body: resultData.body,
    // icon: resultData.image,
    tag: 'dddev',
    data: {
      githubUrl: resultData.click_action,
      docId: resultData.icon,
    },
    // click_action: resultData.click_action,
    // ...resultData,
  };
  // console.log('push: ', { resultData, notificationTitle, notificationOptions });

  e.waitUntil(self.registration.showNotification(notificationTitle, notificationOptions));

  // self.registration.showNotification(notificationTitle, notificationOptions);
});

// const messaging = firebaseApp.messaging();

// self.addEventListener('push', function (event) {
//   messaging.onBackgroundMessage(function (payload) {
//     const notificationTitle = payload.data.title;
//     const notificationOptions = { body: payload.data.body };
//     return event.waitUntil(self.registration.showNotification(notificationTitle, notificationOptions));
//   });
// });

self.addEventListener('notificationclick', function (event) {
  // console.log('notification click');
  // console.log(event.notification);
  // const collection = db.collection('AlertData');
  const { docId } = event.notification.data;
  // console.log(`docId : ${docId}`);
  // collection.doc(docId).update({ isRead: true });
  const { githubUrl } = event.notification.data;
  const redirectUrl = `http://localhost:3000/temp/redirect?githubUrl=${githubUrl}&docId=${docId}`;
  event.notification.close();
  event.waitUntil(clients.openWindow(redirectUrl));
});
