import { useRoutes, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';

import Home from 'markup/pages/Home';
import Project from 'markup/pages/Project';
import Document from 'markup/pages/Document';
import NotFound from 'markup/pages/NotFound';

const GroundCheck = () => {
  const user = useSelector((state) => state.user);
  const params = useParams();

  const routes = useRoutes([
    {
      path: '/',
      element: <Home />,
    },
    {
      path: '/project/*',
      element: <Project />,
    },
    {
      path: '/document/*',
      element: <Document />,
    },
  ]);
  return user.groundsList.includes(parseInt(params.groundId, 10)) ? routes : <NotFound type="groundId" />;
};

export default GroundCheck;
