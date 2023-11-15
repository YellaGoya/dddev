import { useRoutes } from 'react-router-dom';
import AlertData from './components/alert/AlertData';
// import CreateAlert from './components/alert/CreateAlert';
// import AlertList from './components/alert/AlertList';
import Redirect from './components/alert/Redirect';
import GetAlert from './components/alert/GetAlert';

const Template = () => {
  const routes = useRoutes([
    {
      path: '/redirect/*',
      element: <Redirect />,
    },
  ]);
  return (
    <>
      <AlertData />
      {/* <CreateAlert /> */}
      {/* <AlertList /> */}
      <GetAlert />
      {routes}
    </>
  );
};

export default Template;
