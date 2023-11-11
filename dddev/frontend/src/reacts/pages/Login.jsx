import { useRoutes, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

import GitLogin from 'reacts/pages/components/intro/GitLogin';
import GitResult from 'reacts/pages/components/intro/GitResult';
import GroundInit from 'reacts/pages/components/intro/GroundInit';

import { ContentWrapper } from 'reacts/styles/Global';
const Login = () => {
  const user = useSelector((state) => state.user);

  const routes = useRoutes([
    {
      path: '/',
      element: user.isLoggedIn ? <Navigate to="/" /> : <GitLogin />,
    },
    {
      path: '/github',
      element: <GitResult />,
    },
    {
      path: '/groundinit',
      element: <GroundInit />,
    },
  ]);
  return <ContentWrapper>{routes}</ContentWrapper>;
};

export default Login;
