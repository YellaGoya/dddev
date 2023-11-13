import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import eetch from 'eetch/eetch';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

const Index = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const params = useParams();
  const user = useSelector((state) => state.user);

  useEffect(() => {
    console.log(params.groundId);
    if (params.groundId)
      eetch
        .listSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
        .then((res) => {
          console.log(res.data);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
  }, [params.groundId]);

  return <div>Index</div>;
};

export default Index;
