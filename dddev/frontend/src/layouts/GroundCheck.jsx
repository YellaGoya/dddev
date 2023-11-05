import { useEffect } from 'react';
import { useRoutes, useParams } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';
import { updateUser } from 'redux/actions/user';
import { setGround } from 'redux/actions/ground';
import eetch from 'eetch/eetch';

import Home from 'markup/pages/Home';
import Project from 'markup/pages/Project';
import Document from 'markup/pages/Document';
import NotFound from 'markup/pages/NotFound';

const GroundCheck = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const groundsMap = useSelector((state) => state.user.groundsMap);
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

  useEffect(() => {
    const id = parseInt(params.groundId, 10);
    const available = user.groundsList.includes(id);
    if (available && user.lastGround !== id) {
      const ground = groundsMap.find((ground) => ground.id === id);
      dispatch(setGround({ groundId: ground.id, groundName: ground.name }));
      dispatch(updateUser({ lastGround: id }));
      eetch.changeLastGround({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: id }).catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
    }
  }, [dispatch, navigate, groundsMap, params.groundId, user]);

  return user.groundsList.includes(parseInt(params.groundId, 10)) ? routes : <NotFound type="groundId" />;
};

export default GroundCheck;
