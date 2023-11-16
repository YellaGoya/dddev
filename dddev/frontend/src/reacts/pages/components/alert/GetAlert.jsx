import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import Input from 'reacts/pages/components/common/Input';
import eetch from 'eetch/eetch';
import requestPermission from 'fcm/firebase-messaging.js';

const GetAlert = () => {
  const user = useSelector((state) => state.user);
  const lastGround = useSelector((state) => state.user.lastGround);
  const [pushToggle, setPushToggle] = useState(false);
  const [pullRequestToggle, setPullRequestToggle] = useState(false);
  const [keywords, setKeywords] = useState(['']);
  const [pushId, setPushId] = useState();
  const [pullRequestId, setPullRequestId] = useState();
  const groundsMap = useSelector((state) => state.user.groundsMap);
  const [groundName, setGroundName] = useState('');

  useEffect(() => {
    // 버튼 없앨거면 여기에 updateAlert();
  }, [keywords]);

  useEffect(() => {
    if (user.isLoggedIn) requestPermission({ accessToken: user.accessToken, refreshToken: user.refreshToken });
  }, [user.accessToken, user.refreshToken]);

  useEffect(() => {
    // 지금 그라운드 이름 불러오기
    const ground = groundsMap.find((ground) => ground.id === lastGround);
    setGroundName(ground.name);
    // 초기 상태를 설정하는 API 호출
    eetch
      .getAlert({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: lastGround })
      .then((res) => {
        setPushToggle(res.data.pushId !== null);
        setPullRequestToggle(res.data.pullRequestId !== null);
        setKeywords(res.data.keyword ? res.data.keyword : []);
        setPushId(res.data.pushId);
        setPullRequestId(res.data.pullRequestId);
      })
      .catch((err) => console.error(err));
  }, [user.accessToken, user.refreshToken]);

  const updateAlert = () => {
    // 키워드 수정되면 알림 수정
    eetch
      .updateAlert({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: lastGround,
        keyword: keywords,
      })
      .then((res) => {
        setPushToggle(res.data.pushId !== null);
        setPullRequestToggle(res.data.pullRequestId !== null);
        setKeywords(res.data.keyword ? res.data.keyword : []);
        setPushId(res.data.pushId);
        setPullRequestId(res.data.pullRequestId);
      });
  };

  const clickToggle = (type, id) => {
    const toggle = type === 'push' ? pushToggle : pullRequestToggle;
    const setToggle = type === 'push' ? setPushToggle : setPullRequestToggle;

    if (!toggle) {
      eetch
        .createAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId: Number(lastGround),
          keyword: keywords,
          type,
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
          // 알림 허용을 안해서 422 에러가 뜨면 알림 허용을 해야 한다고 안내하기
          console.log('createAlert err: ', err);
        });
    } else {
      eetch
        .deleteAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId: Number(lastGround),
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
          console.log(err);
        });
    }
  };

  return (
    <div>
      <div>=============getAlert 컴포넌트 시작============</div>
      <label>
        그라운드 이름 : {groundName}
        Push 알림 토글:
        <input type="checkbox" checked={pushToggle} onChange={() => clickToggle('push', pushId)} />
      </label>
      <label>
        Pull Request 알림 토글:
        <input type="checkbox" checked={pullRequestToggle} onChange={() => clickToggle('pull_request', pullRequestId)} />
      </label>
      <label>
        {(pushToggle || pullRequestToggle) && (
          <label>
            <Input label="키워드" array={keywords} enter={setKeywords} />
            <div type="button" onClick={updateAlert}>
              키워드수정
            </div>
          </label>
        )}
      </label>
      <div>=============getAlert 컴포넌트 끝============</div>
    </div>
  );
};

export default GetAlert;