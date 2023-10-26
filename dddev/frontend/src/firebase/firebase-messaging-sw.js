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

async function requestPermission() {
  console.log('권한 요청 중...');

  const permission = await Notification.requestPermission();
  if (permission === 'denied') {
    console.log('알림 권한 허용 안됨');
    return;
  }

  console.log('알림 권한이 허용됨');

  const token = await getToken(messaging, {
    vapidKey: 'BPx7bK6vhpUDuWrJbMr0ZDuGcKtB-1JvroQQFKJH2XE9TRRPOfx0i60AuKlei0r_rgqDReweOxVxu_MlGAJKSNg',
  });

  if (token) console.log('token: ', token);
  else console.log('Can not get Token');

  onMessage(messaging, (payload) => {
    console.log('메시지가 도착했습니다.', payload);
    // ...
  });
}

requestPermission();
