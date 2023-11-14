import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import PieChart from 'reacts/pages/components/chart/PieChart';

const data = [
  {
    id: '-',
    value: 256,
  },
];

import * as s from 'reacts/styles/Home';
const Home = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const params = useParams();
  const user = useSelector((state) => state.user);
  const [sprintList, setSprintList] = useState([]);
  const [selectedSprint, setSelectedSprint] = useState(null);
  const [activeCount, setActiveCount] = useState(null);
  const [activeTime, setActiveTime] = useState(null);
  const [focusCount, setFocusCount] = useState(null);
  const [focusTime, setFocusTime] = useState(null);
  const [totalCount, setTotalCount] = useState(null);
  const [totalTime, setTotalTime] = useState(null);
  const [burnDown, setBurnDown] = useState(null);

  useEffect(() => {
    if (params.groundId) getSprintList();
  }, [params.groundId]);

  const getSprintList = () => {
    eetch
      .listSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
      .then((res) => {
        setSprintList(res.data);
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  useEffect(() => {
    setSelectedSprint(sprintList[sprintList.length - 1]);
  }, [sprintList]);

  useEffect(() => {
    if (selectedSprint) {
      eetch
        .activeCount({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, sprintId: selectedSprint.id })
        .then((res) => {
          setActiveCount(res.data);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
      eetch
        .activeTime({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, sprintId: selectedSprint.id })
        .then((res) => {
          setActiveTime(res.data);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
      eetch
        .focusCount({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, sprintId: selectedSprint.id })
        .then((res) => {
          setFocusCount(res.data);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
      eetch
        .focusTime({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, sprintId: selectedSprint.id })
        .then((res) => {
          setFocusTime(res.data);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
      eetch
        .totalCount({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, sprintId: selectedSprint.id })
        .then((res) => {
          setTotalCount(res.data);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
      eetch
        .totalTime({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, sprintId: selectedSprint.id })
        .then((res) => {
          setTotalTime(res.data);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
      eetch
        .burnDown({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, sprintId: selectedSprint.id })
        .then((res) => {
          setBurnDown(res.data);
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
  }, [selectedSprint]);

  useEffect(() => {
    console.log(activeCount, activeTime, focusCount, focusTime, totalCount, totalTime, burnDown);
  }, [burnDown]);

  return (
    <s.DashWrapper>
      <s.ChartWrapper>
        <PieChart data={data} endAngle={180} />
      </s.ChartWrapper>
      <s.DocsWrapper />
    </s.DashWrapper>
  );
};

export default Home;
