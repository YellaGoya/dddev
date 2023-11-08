import { useRoutes } from 'react-router-dom';

import Index from 'reacts/pages/components/document/Index';
import Issue from 'reacts/pages/components/document/Issue';
import Request from 'reacts/pages/components/document/Request';
import Explore from 'reacts/pages/components/document/Explore';
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
      path: '/explore',
      element: <Explore />,
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

  return <>이거 도큐먼트 페이지?{routes}</>;
};

export default Document;
