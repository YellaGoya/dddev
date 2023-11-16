import { useRoutes } from 'react-router-dom';
import AlertData from './components/alert/AlertData';
import Redirect from './components/alert/Redirect';
import GetAlert from './components/alert/GetAlert';
import UserAlertData from './components/alert/UserAlertData';

const Alert = () => {
  const routes = useRoutes([
    {
      path: '/redirect/*',
      element: <Redirect />,
    },
  ]);
  return (
    <>
      <AlertData />
      <GetAlert />
      <UserAlertData />
      {routes}
    </>
  );
};

export default Alert;
