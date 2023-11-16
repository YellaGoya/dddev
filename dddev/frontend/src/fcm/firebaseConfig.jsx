import firebase from 'firebase/compat/app';
import 'firebase/compat/auth';
import 'firebase/compat/firestore';
import 'firebase/compat/messaging';

const firebaseConfig = {
  apiKey: 'AIzaSyD1HPZuKiyZOvTJMyqUhf1dv6wkJaRRaKM',
  authDomain: 'dddev-1e0d5.firebaseapp.com',
  projectId: 'dddev-1e0d5',
  storageBucket: 'dddev-1e0d5.appspot.com',
  messagingSenderId: '1067642901305',
  appId: '1:1067642901305:web:6510a6e3861c9414c2cee8',
  measurementId: 'G-8KXEK3KVET',
};

const firebaseApp = firebase.initializeApp(firebaseConfig);

const db = firebaseApp.firestore();
const auth = firebase.auth();
const messaging = firebase.messaging();

export { auth, db, messaging };
