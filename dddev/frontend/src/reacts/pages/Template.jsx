import { useRoutes } from 'react-router-dom';
import AlertData from './components/alert/AlertData';
// import CreateAlert from './components/alert/CreateAlert';
import Redirect from './components/alert/Redirect';

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
      {routes}
    </>
  );
};

export default Template;
