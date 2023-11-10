import { useRoutes } from 'react-router-dom';

import Index from 'reacts/pages/components/document/Index';
import Issue from 'reacts/pages/components/document/Issue';
import Request from 'reacts/pages/components/document/Request';
import General from 'reacts/pages/components/document/General';
import Write from 'reacts/pages/components/document/Write';

const Document = () => {
  const routes = useRoutes([
    {
      path: '/',
      element: <Index />,
    },
    {
      path: '/issue',
      element: <Issue />,
    },
    {
      path: '/request',
      element: <Request />,
    },
    {
      path: '/general',
      element: <General />,
    },
    {
      path: '/docs',
      element: <Write />,
    },
    {
      path: '/docs/:type/:docId',
      element: <Write />,
    },
  ]);

  return routes;
};

export default Document;
