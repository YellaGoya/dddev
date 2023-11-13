import { useRoutes } from 'react-router-dom';

import Index from 'reacts/pages/components/project/Index';
import Chart from 'reacts/pages/components/project/Chart';
import Log from 'reacts/pages/components/project/Log';

const Project = () => {
  const routes = useRoutes([
    {
      path: '/',
      element: <Index />,
    },
    {
      path: '/chart',
      element: <Chart />,
    },
    {
      path: '/log',
      element: <Log />,
    },
  ]);

  return routes;
};

export default Project;
