self.addEventListener('install', function (e) {
  self.skipWaiting();
});

self.addEventListener('activate', function (e) {
});

self.addEventListener('push', function (e) {
  if (!e.data.json()) return;

  const resultData = e.data.json().notification;
  const notificationTitle = resultData.title;
  const notificationOptions = {
    body: resultData.body,
    tag: 'dddev',
    data: {
      githubUrl: resultData.click_action,
      docId: resultData.icon,
    },
  };

  e.waitUntil(self.registration.showNotification(notificationTitle, notificationOptions));

});


self.addEventListener('notificationclick', function (event) {
  const { docId } = event.notification.data;
  const { githubUrl } = event.notification.data;
  const redirectUrl = `https://k9d103.p.ssafy.io/temp/redirect?githubUrl=${githubUrl}&docId=${docId}`;
  event.notification.close();
  event.waitUntil(clients.openWindow(redirectUrl));
});
