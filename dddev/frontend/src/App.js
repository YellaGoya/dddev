import { useEffect } from 'react';
import { BrowserRouter, useRoutes, useLocation, Navigate } from 'react-router-dom';
// import { Provider } from 'react-redux';
import { Provider, useSelector } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { store, persistor } from 'redux/store';
import requestPermission from './firebase/firebase-messaging';

import View from 'layouts/View';

import Login from 'markup/pages/Login';
import GroundCheck from 'layouts/GroundCheck';

import { Global } from 'markup/styles/Global';

const Routing = () => {
  useEffect(() => {
    // navigator.serviceWorker.register('/firebase-messaging-sw.js').then((registration) => {
    //   console.log('Service Worker registered with scope:', registration.scope);

    //   // Wait for the service worker to become ready
    //   return navigator.serviceWorker.ready;
    // });

    requestPermission();
  }, []);

  const location = useLocation();
  const user = useSelector((state) => state.user);
  const routes = useRoutes([
    {
      path: '/login/*',
      element: <Login />,
    },
    {
      path: '/',
      element: user.lastGround ? <Navigate to={`/${user.lastGround}`} /> : <Navigate to="/login/groundinit" />,
    },
    {
      path: '/:groundId/*',
      element: <GroundCheck />,
    },
  ]);

  return user.isLoggedIn ? (
    location.pathname.startsWith('/login') ? (
      <Navigate to={`/${user.lastGround}`} />
    ) : (
      routes
    )
  ) : location.pathname.startsWith('/login') ? (
    routes
  ) : (
    <Navigate to="/login" />
  );
};

const App = () => {
  return (
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <Global />
        <BrowserRouter forceRefresh>
          <View>
            <Routing />
          </View>
        </BrowserRouter>
      </PersistGate>
    </Provider>
  );
};

export default App;
