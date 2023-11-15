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
  // const [alertState, setAlertState] = useState({});
  const groundsMap = useSelector((state) => state.user.groundsMap);
  const [groundName, setGroundName] = useState('');

  useEffect(() => {
    console.log('keywords: ', keywords);
  }, [keywords]);

  const updateAlert = (keywordList) => {
    setKeywords(keywordList);

    // 키워드 수정되면 알림 수정
    eetch
      .updateAlert({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        lastGround,
        keywords,
      })
      .then((res) => {
        console.log('res:', res.data);
        setPushToggle(res.data.pushId !== null);
        setPullRequestToggle(res.data.pullRequestId !== null);
        setKeywords(res.data.keyword ? res.data.keyword : []);
        setPushId(res.data.pushId);
        setPullRequestId(res.data.pullRequestId);
      });
  };

  return (
    <div>
      <label>
        그라운드 이름 : {groundName}
        Push 알림 토글:
        <input
          type="checkbox"
          checked={pushToggle}
          onChange={() => clickToggle('push', pushId)} // 'pushId' 대신 실제 id를 사용해야 합니다.
        />
      </label>
      <label>
        Pull Request 알림 토글:
        <input
          type="checkbox"
          checked={pullRequestToggle}
          onChange={() => clickToggle('pull_request', pullRequestId)} // 'pullRequestId' 대신 실제 id를 사용해야 합니다.
        />
      </label>
      <label>
        {' '}
        {(pushToggle || pullRequestToggle) && (
          <label>
            <Input label="키워드" array={keywords} enter={setKeywords} />
            <div type="button" onClick={updateAlert}>
              키워드수정
            </div>
          </label>
        )}
      </label>
    </div>
  );
};

export default GetAlert;
