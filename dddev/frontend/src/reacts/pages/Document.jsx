import { useRoutes, useParams } from 'react-router-dom';

import Index from 'reacts/pages/components/document/Index';
import Request from 'reacts/pages/components/document/Request';
import Write from 'reacts/pages/components/document/Write';

const Document = () => {
  const params = useParams();
  const routes = useRoutes([
    {
      path: '/',
      element: <Index />,
    },
    {
      path: '/docs/:type/:docId',
      element: <Write />,
    },
  ]);

  return params.type === 'request' ? <Request /> : routes;
};

export default Document;
