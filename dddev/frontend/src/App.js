import { useEffect } from 'react';
import { BrowserRouter, useRoutes, Navigate } from 'react-router-dom';
// import { Provider } from 'react-redux';
import { Provider, useSelector } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { store, persistor } from 'redux/store';
import requestPermission from './firebase/firebase-messaging';

import View from 'layouts/View';

import Home from 'markup/pages/Home';
import Login from 'markup/pages/Login';
import Project from 'markup/pages/Project';
import Document from 'markup/pages/Document';

import { Global } from 'markup/styles/Global';

const Routing = () => {
  useEffect(() => {
    navigator.serviceWorker.register('/firebase-messaging-sw.js').then((registration) => {
      console.log('Service Worker registered with scope:', registration.scope);

      // Wait for the service worker to become ready
      return navigator.serviceWorker.ready;
    });

    requestPermission();
  }, []);

  // navigator.serviceWorker.register('/firebase-messaging-sw.js').then((registration) => {
  //   console.log('Service Worker registered with scope:', registration.scope);

  //   // Wait for the service worker to become ready
  //   return navigator.serviceWorker.ready;
  // });

  const user = useSelector((state) => state.user);

  return useRoutes([
    {
      path: '/',
      element: user.isLoggedIn ? (
        user.lastGround === null || user.lastGround === undefined ? (
          <Navigate to="/login/groundinit" />
        ) : (
          <Navigate to={`/${user.lastGround}`} />
        )
      ) : (
        <Navigate to="/login" />
      ),
      // element: <Navigate to="/101" />,
    },
    {
      path: '/:groundId',
      element: <Home />,
      // element: user.isLoggedIn ? <Home /> : <Navigate to="/login" />,
    },
    {
      path: '/login/*',
      element: <Login />,
    },
    {
      path: '/:groundId/project/*',
      element: <Project />,
      // element: user.isLoggedIn ? <Project /> : <Navigate to="/login" />,
    },
    {
      path: '/:groundId/document/*',
      element: <Document />,
      // element: user.isLoggedIn ? <Document /> : <Navigate to="/login" />,
    },
  ]);
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
