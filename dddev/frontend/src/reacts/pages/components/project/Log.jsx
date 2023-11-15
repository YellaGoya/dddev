import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';
import Modal from 'reacts/pages/components/common/Modal';

import ConfirmationNumberRoundedIcon from '@mui/icons-material/ConfirmationNumberRounded';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import * as s from 'reacts/styles/components/project/Log';
const Log = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const params = useParams();
  const user = useSelector((state) => state.user);
  const [instToggle, setInstToggle] = useState(false);
  const [tokenSuccess, setTokenSuccess] = useState(false);
  const [logs, setLogs] = useState(null);
  const [maximumPage, setMaximumPage] = useState(0);
  const [pageNumber, setPageNumber] = useState(1);

  const generateToken = () => {
    eetch
      .generateToken({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
      .then(() => {
        setTokenSuccess(true);
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

  const loadMore = () => {
    getList(pageNumber + 1);
    setPageNumber(pageNumber + 1);
  };

  const getList = (n = 1) => {
    eetch
      .recentLog({ groundId: params.groundId, page: n })
      .then((res) => {
        if (n === 1) {
          setMaximumPage(res.data.pageSize);
          setLogs(res.data.logs);
        } else {
          setLogs([...logs, ...res.data.logs]);
        }
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
    getList();
  }, [params.groundId]);

  return (
    <s.LogWrapper>
      <s.LogInstructionWrapper $toggle={instToggle}>
        <s.LogTitle $toggle={instToggle} onClick={() => setInstToggle(!instToggle)}>
          * 서버 로그 수집 메뉴얼
          <KeyboardArrowDownIcon />
        </s.LogTitle>
        <h2>
          1. 서버 로그 API 토큰 발급.
          <s.GenerateButton onClick={generateToken}>
            <ConfirmationNumberRoundedIcon />
            토큰 발급
          </s.GenerateButton>
        </h2>
        <h2>2. 수집 할 서버에서 다음 명령어로 스크립트 파일을 저장.</h2>
        <pre>
          # 도커 컨테이너 환경
          <br />
          wget -O dddevLogDocker.sh http://k9d103a.p.ssafy.io/dddevLogDocker
          <br />
          <br />
          # 리눅스 환경
          <br />
          wget -O dddevLogSystem.sh http://k9d103a.p.ssafy.io/dddevLogSystem
        </pre>
        <h2>3. 스크립트 파일 관리자 권한으로 실행 및 설치.</h2>
        <pre>
          # 도커 컨테이너 환경, 컨테이너 명, 로그 API 토큰
          <br />
          {`sudo sh dddevLogDocker.sh {containername} {token} &`}
          <br />
          <br />
          # 리눅스 환경, 로그 파일 경로, 로그 API 토큰
          <br />
          {`sudo sh dddevLogSystem.sh {logPath} {token} &`}
        </pre>
        <h2>4. bg 명령어를 입력하여 백그라운드 실행.</h2>
        <pre>bg</pre>
        <h2>* 도커 환경은 재시작시 해쉬 값이 변경 되므로 종료 후 스크립트 재실행 필요.</h2>
        <h2>* 로그 분석 서비스 종료 명령어.</h2>
        <pre>{`sudo pkill -f "dddev|python3"`}</pre>
      </s.LogInstructionWrapper>
      <s.LogListWrapper>
        <s.LogListTitle>서버 로그 분석</s.LogListTitle>
        {logs && (
          <s.LogList>
            {logs.map((log) => (
              <s.LogItem key={log.localDateTime}>{log.log}</s.LogItem>
            ))}
            {maximumPage > pageNumber && (
              <s.LogItem className="load-more">
                <s.LogMoreButton onClick={() => loadMore()}>
                  <p>이전 로그</p>
                  <MoreVertIcon />
                </s.LogMoreButton>
              </s.LogItem>
            )}
          </s.LogList>
        )}
      </s.LogListWrapper>
      <Modal
        isOpen={tokenSuccess}
        type="alarm"
        message="* 토큰 발급 완료."
        onRequestClose={() => {
          setTokenSuccess(false);
        }}
      />
    </s.LogWrapper>
  );
};

export default Log;
