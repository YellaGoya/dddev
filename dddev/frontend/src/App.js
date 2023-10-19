import { BrowserRouter, useRoutes } from 'react-router-dom';
import { Provider } from 'react-redux';
import store from 'redux/store';

import { Global } from 'markup/styles/Global';

import View from 'layouts/View';

import Home from 'markup/pages/Home';
import Login from 'markup/pages/Login';
import Project from 'markup/pages/Project';
import Document from 'markup/pages/Document';

const Routing = () => {
  const isLoggedIn = false;

  const routes = useRoutes([
    {
      path: '/',
      element: isLoggedIn ? <Home /> : <Login />,
    },
    {
      path: '/signin',
      element: isLoggedIn ? <Home /> : <Login />,
    },
    {
      path: '/project/*',
      element: isLoggedIn ? <Project /> : <Login />,
    },
    {
      path: '/document/*',
      element: isLoggedIn ? <Document /> : <Login />,
    },
  ]);

  return routes;
};

const App = () => {
  return (
    <Provider store={store}>
      <Global />
      <BrowserRouter forceRefresh>
        <View>
          <Routing />
        </View>
      </BrowserRouter>
    </Provider>
  );
};

export default App;
