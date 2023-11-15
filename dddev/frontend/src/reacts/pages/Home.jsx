import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import PieChart from 'reacts/pages/components/chart/PieChart';
import LineChart from 'reacts/pages/components/chart/LineChart';

const valuePie = [
  {
    id: '-',
    value: 256,
  },
];

// const valueLine = [
//   {
//     id: '-',
//     data: [
//       {
//         x: 'car',
//         y: 101,
//       },
//       {
//         x: 'moto',
//         y: 203,
//       },
//       {
//         x: 'bicycle',
//         y: 21,
//       },
//       {
//         x: 'horse',
//         y: 211,
//       },
//       {
//         x: 'skateboard',
//         y: 218,
//       },
//       {
//         x: 'others',
//         y: 152,
//       },
//     ],
//   },
// ];

import * as s from 'reacts/styles/Home';
const Home = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const params = useParams();
  const user = useSelector((state) => state.user);
  const [sprintList, setSprintList] = useState([]);
  const [selectedSprint, setSelectedSprint] = useState(null);
  const [activeCount, setActiveCount] = useState({ done: 17, undone: 34 });
  const [activeTime, setActiveTime] = useState({ done: 50, undone: 121 });
  const [focusCount, setFocusCount] = useState({ done: 10, undone: 74 });
  const [focusTime, setFocusTime] = useState({ done: 28, undone: 189 });
  const [totalCount, setTotalCount] = useState({ done: 27, undone: 108 });
  const [totalTime, setTotalTime] = useState({ done: 78, undone: 310 });
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
    if (selectedSprint === 123123) {
      setActiveCount({ done: 17, undone: 34 });
      setActiveTime({ done: 50, undone: 121 });
      setFocusCount({ done: 10, undone: 74 });
      setFocusTime({ done: 28, undone: 189 });
      setTotalCount({ done: 27, undone: 108 });
      setTotalTime({ done: 78, undone: 310 });
    }

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
          const { data } = res;
          const keys = Object.keys(data);

          console.log(data);
          let total = keys.reduce((acc, cur) => {
            return acc + data[cur];
          }, 0);

          const chartData = [{ id: '-', data: [{ x: '스프린트', y: total }] }];

          keys.forEach((key) => {
            total -= data[key];
            chartData[0].data.push({ x: key.substring(5, 7) + '월 ' + key.substring(8, 10) + '일', y: total });
          });

          console.log(chartData);

          setBurnDown(chartData);
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
    console.log(activeCount, focusCount, totalCount);
  }, [burnDown]);

  return (
    <s.DashWrapper>
      <s.ChartWrapper>
        {focusTime && activeTime && totalTime && (
          <s.TimeGrid>
            <s.TimeCard>
              <s.TimeCardTitle>집중 시간</s.TimeCardTitle>
              <s.TimeCardPieWrapper>
                <PieChart data={valuePie} endAngle={(focusTime.done / (focusTime.undone + focusTime.done)) * 360} />
                <s.TimeCardPiePercent>{Math.round((focusTime.done / (focusTime.undone + focusTime.done)) * 100) + '%'}</s.TimeCardPiePercent>
              </s.TimeCardPieWrapper>
              <s.TimeCardInfo>총 {focusTime.done + focusTime.undone}시간 중</s.TimeCardInfo>
              <s.TimeCardDesc>{focusTime.done}시간 완료</s.TimeCardDesc>
            </s.TimeCard>
            <s.TimeCard>
              <s.TimeCardTitle>연구 시간</s.TimeCardTitle>
              <s.TimeCardPieWrapper>
                <PieChart data={valuePie} endAngle={(activeTime.done / (activeTime.undone + activeTime.done)) * 360} />
                <s.TimeCardPiePercent>{Math.round((activeTime.done / (activeTime.undone + activeTime.done)) * 100) + '%'}</s.TimeCardPiePercent>
              </s.TimeCardPieWrapper>
              <s.TimeCardInfo>총 {activeTime.done + activeTime.undone}시간 중</s.TimeCardInfo>
              <s.TimeCardDesc>{activeTime.done}시간 완료</s.TimeCardDesc>
            </s.TimeCard>
            <s.TimeCard>
              <s.TimeCardTitle>전체 시간</s.TimeCardTitle>
              <s.TimeCardPieWrapper>
                <PieChart data={valuePie} endAngle={(totalTime.done / (totalTime.undone + totalTime.done)) * 360} />
                <s.TimeCardPiePercent>{Math.round((totalTime.done / (totalTime.undone + totalTime.done)) * 100) + '%'}</s.TimeCardPiePercent>
              </s.TimeCardPieWrapper>
              <s.TimeCardInfo>총 {totalTime.done + totalTime.undone}시간 중</s.TimeCardInfo>
              <s.TimeCardDesc>{totalTime.done}시간 완료</s.TimeCardDesc>
            </s.TimeCard>
          </s.TimeGrid>
        )}
        {burnDown && (
          <s.BurnDownWrapper>
            <s.BurnDownTitle>번다운 차트</s.BurnDownTitle>
            <s.BurnDownSizeWrapper>
              <LineChart data={burnDown} />
            </s.BurnDownSizeWrapper>
          </s.BurnDownWrapper>
        )}
        {focusCount && activeCount && totalCount && (
          <s.IssueCountWrapper>
            <s.IssueCountTitle>이슈 해결</s.IssueCountTitle>
            <s.IssueCountCard>
              <s.IssueCountLabel>전체</s.IssueCountLabel>
              <s.IssueHighLight>
                {totalCount.done}
                <s.IssueText>
                  {'(' + focusCount.done} + {activeCount.done + ')'}
                </s.IssueText>
              </s.IssueHighLight>
              <s.IssueHighLight>/</s.IssueHighLight>
              <s.IssueHighLight>
                {totalCount.done + totalCount.undone}
                <s.IssueText>
                  {'(' + focusCount.done + focusCount.undone} + {activeCount.done + activeCount.undone + ')'}
                </s.IssueText>
              </s.IssueHighLight>
            </s.IssueCountCard>
          </s.IssueCountWrapper>
        )}
      </s.ChartWrapper>
      <s.DocsWrapper />
    </s.DashWrapper>
  );
};

export default Home;
