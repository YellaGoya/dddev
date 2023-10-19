import { useRoutes } from 'react-router-dom';

import Index from 'markup/pages/components/project/Index';
import Chart from 'markup/pages/components/project/Chart';
import Log from 'markup/pages/components/project/Log';

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

  return <>이거 프로젝트 페이지야!!!{routes}</>;
};

export default Project;
