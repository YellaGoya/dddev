import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import eetch from 'eetch/eetch';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import * as s from 'reacts/styles/components/project/Index';
const Index = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const params = useParams();
  const user = useSelector((state) => state.user);

  useEffect(() => {
    console.log(params.groundId);
  }, [params.groundId]);

  const createSprint = () => {
    console.log('create!');
    eetch
      .createSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
      .then((res) => console.log(res.data))
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const listSprint = () => {
    console.log('list!');
    eetch
      .createSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
      .then((res) => console.log(res.data))
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  return (
    <s.SprintWrapper>
      <div onClick={createSprint}>스프린트 생성</div>
      <div onClick={listSprint}>스프린트 리스트</div>
    </s.SprintWrapper>
  );
};

export default Index;
