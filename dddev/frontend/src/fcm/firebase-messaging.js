import { getToken } from 'firebase/messaging';
import eetch from 'eetch/eetch';
import { messaging } from './firebaseConfig';

async function requestPermission({ accessToken, refreshToken }) {
  const permission = await Notification.requestPermission();
  if (permission === 'denied') {
    localStorage.setItem('fcmPermissionRequested', false);
    return;
  }

  const token = await getToken(messaging, {
    vapidKey: 'BPx7bK6vhpUDuWrJbMr0ZDuGcKtB-1JvroQQFKJH2XE9TRRPOfx0i60AuKlei0r_rgqDReweOxVxu_MlGAJKSNg',
  }).catch((err) => {
    const error = "AbortError: Failed to execute 'subscribe' on 'PushManager': Subscription failed - no active Service Worker";
    if (err.toString() === error) {
      return getToken(messaging, {
        vapidKey: 'BPx7bK6vhpUDuWrJbMr0ZDuGcKtB-1JvroQQFKJH2XE9TRRPOfx0i60AuKlei0r_rgqDReweOxVxu_MlGAJKSNg',
      }).catch((err) => console.log('another err :: ', err));
    }
  });

  if (token === null) {
    await getToken(messaging, {
      vapidKey: 'BPx7bK6vhpUDuWrJbMr0ZDuGcKtB-1JvroQQFKJH2XE9TRRPOfx0i60AuKlei0r_rgqDReweOxVxu_MlGAJKSNg',
    });
  }

  if (token) {
    eetch.addDeviceToken({ accessToken, refreshToken, deviceToken: token }).catch((err) => {
      if (err.message === 'gayeonNotGood') console.log(err);
    });
  }
}

export default requestPermission;
