import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import PieChart from 'reacts/pages/components/chart/PieChart';
import LineChart from 'reacts/pages/components/chart/LineChart';
import AlertData from 'reacts/pages/components/alert/AlertData';

import AddCommentRoundedIcon from '@mui/icons-material/AddCommentRounded';

import DirectionsRunRoundedIcon from '@mui/icons-material/DirectionsRunRounded';
import GradingRoundedIcon from '@mui/icons-material/GradingRounded';

const valuePie = [
  {
    id: '-',
    value: 256,
  },
];

const valueLine = [
  {
    id: '-',
    data: [
      {
        x: 'car',
        y: 100,
      },
      {
        x: 'moto',
        y: 80,
      },
      {
        x: 'bicycle',
        y: 45,
      },
      {
        x: 'horse',
        y: 40,
      },
      {
        x: 'skateboard',
        y: 23,
      },
      {
        x: 'others',
        y: 21,
      },
    ],
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
  const [activeCount, setActiveCount] = useState({ done: 0, undone: 0 });
  const [activeTime, setActiveTime] = useState({ done: 0, undone: 0 });
  const [focusCount, setFocusCount] = useState({ done: 0, undone: 0 });
  const [focusTime, setFocusTime] = useState({ done: 0, undone: 0 });
  const [totalCount, setTotalCount] = useState({ done: 0, undone: 0 });
  const [totalTime, setTotalTime] = useState({ done: 0, undone: 0 });
  const [burnDown, setBurnDown] = useState(null);
  const [lineArea, setLineArea] = useState(null);

  const [requests, setRequests] = useState([]);

  useEffect(() => {
    if (params.groundId) {
      getSprintList();
      getRequests();
    }
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

  const getRequests = () => {
    eetch
      .listRequest({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, filter: 'step2' })
      .then((res) => {
        setRequests(res.data.reverse().slice(0, 4));
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
          const { data } = res;
          const keys = Object.keys(data);

          let total = keys.reduce((acc, cur) => {
            return acc + data[cur];
          }, 0);

          const chartData = [{ id: '-', data: [{ x: '스프린트', y: total }] }];

          keys.forEach((key) => {
            total -= data[key];
            chartData[0].data.push({ x: key.substring(5, 7) + '월 ' + key.substring(8, 10) + '일', y: total });
          });

          setLineArea(total);
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

  return (
    <s.DashWrapper>
      <s.ChartWrapper>
        <s.ChartTitle>대쉬보드 차트</s.ChartTitle>
        {focusTime && activeTime && totalTime && (
          <s.TimeGrid>
            <s.TimeCard>
              <s.TimeCardPieWrapper>
                <PieChart data={valuePie} endAngle={(Number(focusTime.done) / (Number(focusTime.undone) + Number(focusTime.done))) * 360} />
                <s.TimeCardPiePercent>
                  {Math.round((Number(focusTime.done) / (Number(focusTime.undone) + Number(focusTime.done))) * 100) + '%'}
                </s.TimeCardPiePercent>
              </s.TimeCardPieWrapper>
              <s.TimeCardInfo>총 {Number(focusTime.done) + Number(focusTime.undone)}시간 중</s.TimeCardInfo>
              <s.TimeCardDesc>{focusTime.done}시간 완료</s.TimeCardDesc>
              {Number(focusTime.undone) + Number(focusTime.done) === 0 && (
                <s.NoContentBlur>
                  <s.NoContentInfo>* 데이터 부족</s.NoContentInfo>
                </s.NoContentBlur>
              )}
              <s.TimeCardTitle>집중 시간</s.TimeCardTitle>
            </s.TimeCard>
            <s.TimeCard>
              <s.TimeCardPieWrapper>
                <PieChart data={valuePie} endAngle={(Number(activeTime.done) / (Number(activeTime.undone) + Number(activeTime.done))) * 360} />
                <s.TimeCardPiePercent>
                  {Math.round((Number(activeTime.done) / (Number(activeTime.undone) + Number(activeTime.done))) * 100) + '%'}
                </s.TimeCardPiePercent>
              </s.TimeCardPieWrapper>
              <s.TimeCardInfo>총 {activeTime.done + activeTime.undone}시간 중</s.TimeCardInfo>
              <s.TimeCardDesc>{activeTime.done}시간 완료</s.TimeCardDesc>
              {activeTime.undone + activeTime.done === 0 && (
                <s.NoContentBlur>
                  <s.NoContentInfo>* 데이터 부족</s.NoContentInfo>
                </s.NoContentBlur>
              )}
              <s.TimeCardTitle>연구 시간</s.TimeCardTitle>
            </s.TimeCard>
            <s.TimeCard>
              <s.TimeCardPieWrapper>
                <PieChart data={valuePie} endAngle={(Number(totalTime.done) / (Number(totalTime.undone) + Number(totalTime.done))) * 360} />
                <s.TimeCardPiePercent>
                  {Math.round((Number(totalTime.done) / (Number(totalTime.undone) + Number(totalTime.done))) * 100) + '%'}
                </s.TimeCardPiePercent>
              </s.TimeCardPieWrapper>
              <s.TimeCardInfo>총 {Number(totalTime.done) + Number(totalTime.undone)}시간 중</s.TimeCardInfo>
              <s.TimeCardDesc>{totalTime.done}시간 완료</s.TimeCardDesc>
              {Number(totalTime.undone) + Number(totalTime.done) === 0 && (
                <s.NoContentBlur>
                  <s.NoContentInfo>* 데이터 부족</s.NoContentInfo>
                </s.NoContentBlur>
              )}
              <s.TimeCardTitle>전체 시간</s.TimeCardTitle>
            </s.TimeCard>
          </s.TimeGrid>
        )}
        {burnDown && (
          <s.BurnDownWrapper>
            <s.BurnDownSizeWrapper>
              <LineChart data={burnDown[0].data.length === 0 ? valueLine : burnDown} line={lineArea} />
            </s.BurnDownSizeWrapper>
            {burnDown[0].data.length <= 1 && (
              <s.NoContentBlur>
                <s.NoContentInfo>* 데이터 부족</s.NoContentInfo>
              </s.NoContentBlur>
            )}
            <s.BurnDownTitle>번다운 차트</s.BurnDownTitle>
          </s.BurnDownWrapper>
        )}
        {focusCount && activeCount && totalCount && (
          <s.IssueCountWrapper>
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
                  {'(' + (Number(focusCount.done) + Number(activeCount.done))} + {Number(focusCount.undone) + Number(activeCount.undone) + ')'}
                </s.IssueText>
              </s.IssueHighLight>
            </s.IssueCountCard>
            {Number(focusCount.done) + Number(focusCount.undone) + Number(activeCount.done) + Number(activeCount.undone) === 0 && (
              <s.NoContentBlur>
                <s.NoContentInfo>* 데이터 부족</s.NoContentInfo>
              </s.NoContentBlur>
            )}
            <s.IssueCountTitle>이슈 해결</s.IssueCountTitle>
          </s.IssueCountWrapper>
        )}

        <s.LinkWrapper>
          <s.LinkSprint onClick={() => navigate(`/${params.groundId}/project`)}>
            스프린트
            <DirectionsRunRoundedIcon />
          </s.LinkSprint>
          <s.LinkIssue onClick={() => navigate(`/${params.groundId}/document/find`)}>
            이슈 문서
            <GradingRoundedIcon />
          </s.LinkIssue>
        </s.LinkWrapper>
      </s.ChartWrapper>
      <s.DivLine />
      <s.DocsWrapper>
        <s.RequestWrapper>
          <s.RequestTitle>최근 요청 문서</s.RequestTitle>
          <s.RequestGrid>
            {requests.length > 0 ? (
              requests.map((request) => (
                <s.RequestCard key={request.id} onClick={() => navigate(`/${params.groundId}/document/find/docs/request/${request.id}`)}>
                  <s.RequestStatus status={request.status}>
                    {request.status === 0 ? '해야 할 일' : request.status === 1 ? '진행 중' : '완료'}
                  </s.RequestStatus>
                  <s.RequestName>{request.title === '' ? '새 문서' : request.title}</s.RequestName>
                  <s.RequestDate>{request.updatedAt.substring(0, 10)}</s.RequestDate>
                  <s.RequestUserName>{request.modifier.nickname}</s.RequestUserName>
                  <s.RequestUserEmail>{request.modifier.email}</s.RequestUserEmail>
                </s.RequestCard>
              ))
            ) : (
              <s.RequestCard className="no-request" onClick={() => navigate(`/${params.groundId}/document/find`)}>
                <AddCommentRoundedIcon />
              </s.RequestCard>
            )}
          </s.RequestGrid>
        </s.RequestWrapper>
        <s.AlarmWrapper>
          <s.AlarmTitle>GitHub 알림 리스트</s.AlarmTitle>
          <AlertData />
        </s.AlarmWrapper>
      </s.DocsWrapper>
    </s.DashWrapper>
  );
};

export default Home;
