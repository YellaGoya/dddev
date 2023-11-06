import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';

const firebaseConfig = {
  apiKey: 'AIzaSyD1HPZuKiyZOvTJMyqUhf1dv6wkJaRRaKM',
  authDomain: 'dddev-1e0d5.firebaseapp.com',
  projectId: 'dddev-1e0d5',
  storageBucket: 'dddev-1e0d5.appspot.com',
  messagingSenderId: '1067642901305',
  appId: '1:1067642901305:web:6510a6e3861c9414c2cee8',
  measurementId: 'G-8KXEK3KVET',
};

const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);

// const url = `https://k9d103.p.ssafy.io/user/device-token`;
//   const options = {
//     method: 'POST',
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization,
//     },
//     body: JSON.stringify({ deviceToken }),
//   };

async function requestPermission() {
  // console.log('권한 요청 중...');

  const permission = await Notification.requestPermission();
  if (permission === 'denied') {
    // console.log('알림 권한 허용 안됨');
    localStorage.setItem('fcmPermissionRequested', false);
    return;
  }

  // console.log('알림 권한이 허용됨');

  const token = await getToken(messaging, {
    // 변경 예정
    vapidKey: 'BPx7bK6vhpUDuWrJbMr0ZDuGcKtB-1JvroQQFKJH2XE9TRRPOfx0i60AuKlei0r_rgqDReweOxVxu_MlGAJKSNg',
  }).catch((err) => {
    // console.log('error ::');
    // console.log(err);
    // console.log(err.toString());
    const error = "AbortError: Failed to execute 'subscribe' on 'PushManager': Subscription failed - no active Service Worker";
    if (err.toString() === error) {
      return getToken(messaging, {
        vapidKey: 'BPx7bK6vhpUDuWrJbMr0ZDuGcKtB-1JvroQQFKJH2XE9TRRPOfx0i60AuKlei0r_rgqDReweOxVxu_MlGAJKSNg',
      }).catch((err) => 
      // console.log('another err :: ', err)
      );
    }

    // console.log(err);
  });

  if (token === null) {
    // console.log('get token retry');
    await getToken(messaging, {
      // 변경 예정
      vapidKey: 'BPx7bK6vhpUDuWrJbMr0ZDuGcKtB-1JvroQQFKJH2XE9TRRPOfx0i60AuKlei0r_rgqDReweOxVxu_MlGAJKSNg',
    });
  }

  // console.log('token: ' + token);

  // if (token && localStorage.getItem('fcmPermissionRequested') === null) {
  //   console.log('token: ', token);
  //   console.log('backend api ::::::::::::::::::::::::::::::::::::::::::::::::::::');
  //   localStorage.setItem('fcmPermissionRequested', true);
  // } else console.log('Can not get Token');

  onMessage(messaging, (payload) => {
    // console.log('메시지가 도착했습니다.', payload);
    // ...
  });
}

export default requestPermission;
