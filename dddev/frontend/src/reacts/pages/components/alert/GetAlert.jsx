import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import Input from 'reacts/pages/components/common/Input';
import requestPermission from 'fcm/firebase-messaging.js';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import * as s from 'reacts/styles/components/alert/GetAlert';
const GetAlert = ({ groundId }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const [pushToggle, setPushToggle] = useState(false);
  const [pullRequestToggle, setPullRequestToggle] = useState(false);
  const [keywords, setKeywords] = useState(null);
  const [pushId, setPushId] = useState();
  const [pullRequestId, setPullRequestId] = useState();

  useEffect(() => {
    if (pushToggle || pullRequestToggle) updateAlert();
  }, [keywords]);

  useEffect(() => {
    if (user.isLoggedIn) requestPermission({ accessToken: user.accessToken, refreshToken: user.refreshToken });
  }, [user.accessToken, user.refreshToken]);

  useEffect(() => {
    eetch
      .getAlert({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId })
      .then((res) => {
        setPushToggle(res.data.pushId !== null);
        setPullRequestToggle(res.data.pullRequestId !== null);
        setKeywords(res.data.keyword ? res.data.keyword : []);
        setPushId(res.data.pushId);
        setPullRequestId(res.data.pullRequestId);
      })
      .catch((err) => console.error(err));
  }, [groundId]);

  const updateAlert = () => {
    eetch
      .updateAlert({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId,
        keyword: keywords,
      })
      .then((res) => {
        setPushToggle(res.data.pushId !== null);
        setPullRequestToggle(res.data.pullRequestId !== null);
        setPushId(res.data.pushId);
        setPullRequestId(res.data.pullRequestId);
      });
  };

  const removeToggle = () => {
    if (pushToggle) {
      eetch
        .deleteAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId,
          alertId: pushId,
        })
        .then((res) => {
          setPushToggle(res.data.pushId !== null);
          setPushId(res.data.pushId);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
    }

    if (pullRequestToggle) {
      eetch
        .deleteAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId,
          alertId: pullRequestId,
        })
        .then((res) => {
          setPullRequestToggle(res.data.pullRequestId !== null);
          setPullRequestId(res.data.pullRequestId);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
    }
  };

  const clickToggle = (type, id) => {
    const toggle = type === 'push' ? pushToggle : pullRequestToggle;
    const setToggle = type === 'push' ? setPushToggle : setPullRequestToggle;

    if (!toggle) {
      eetch
        .createAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId,
          keyword: keywords,
          type,
        })
        .then((res) => {
          setPushToggle(res.data.pushId !== null);
          setPullRequestToggle(res.data.pullRequestId !== null);
          setKeywords(res.data.keyword ? res.data.keyword : []);
          setPushId(res.data.pushId);
          setPullRequestId(res.data.pullRequestId);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
    } else {
      eetch
        .deleteAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId,
          alertId: id,
        })
        .then((res) => {
          setToggle((toggle) => !toggle);
          setPushToggle(res.data.pushId !== null);
          setPullRequestToggle(res.data.pullRequestId !== null);
          setKeywords(res.data.keyword ? res.data.keyword : []);
          setPushId(res.data.pushId);
          setPullRequestId(res.data.pullRequestId);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
    }
  };

  return (
    <s.OutWrapper>
      <s.AlarmTitle>알림 설정</s.AlarmTitle>
      <s.ButtonWrapper>
        <s.Button $toggle={pushToggle} checked={pushToggle} onClick={() => clickToggle('push', pushId)}>
          푸쉬
        </s.Button>
        <s.Button $toggle={pullRequestToggle} checked={pullRequestToggle} onClick={() => clickToggle('pull_request', pullRequestId)}>
          풀 / 리퀘스트
        </s.Button>
        <s.Button onClick={removeToggle}>삭제</s.Button>
      </s.ButtonWrapper>

      {(pushToggle || pullRequestToggle) && <Input label="키워드" array={keywords} enter={setKeywords} />}
    </s.OutWrapper>
  );
};

export default GetAlert;
