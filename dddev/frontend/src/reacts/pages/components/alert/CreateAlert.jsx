// import eetch from 'eetch/eetch';
import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
// import Select from 'reacts/pages/components/common/SelectUser';
import Input from 'reacts/pages/components/common/Input';
import eetch from 'eetch/eetch';

// 프로필 속에 들어갈 알림 컴포넌트
// 알림 조회, 생성, 수정, 삭제가 다 들어감

const CreateAlert = () => {
  const user = useSelector((state) => state.user);
  const groundsMap = useSelector((state) => state.user.groundsMap);
  // const [ground, setGround] = useState(null);
  const [keywords, setKeywords] = useState([]);
  // const [types, setTypes] = useState([{}]);
  const [pushToggle, setPushToggle] = useState(false);
  const [pullRequestToggle, setPullRequestToggle] = useState(false);
  const [alertList, setAlertList] = useState([{}]);

  // const alertListRes = {
  //   keyword: '',
  //   groupName: '',
  //   pushId: null,
  //   pullRequestId: null,
  //   userId: null,
  // };

  useEffect(() => {
    // 알림 리스트 조회
    eetch
      .alertList({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
      })
      .then((res) => {
        console.log(res);
        // const arr = [];
        // res.data.foreach(({ alertListRes }) => {
        //   arr.push(alertListRes);
        // });
        setAlertList(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [user.accessToken, user.refreshToken]);

  const updateAlert = (groundName, keyword) => {
    const groundId = groundsMap.find((ground) => ground.name === groundName);

    // 키워드 수정되면 알림 수정
    eetch.updateAlert({
      accessToken: user.accessToken,
      refreshToken: user.refreshToken,
      groundId,
      keyword,
    });
  };

  // 알림 생성 api 호출
  // const submitAlert = () => {
  //   console.log(ground);
  //   eetch
  //     .createAlert({
  //       accessToken: user.accessToken,
  //       refreshToken: user.refreshToken,
  //       repoId: 0,
  //       keyword: keywords,
  //       type,
  //     })
  //     .catch((err) => {
  //       console.log(err);
  //     });
  // };

  const clickPushToggle = (pushId) => {
    setPushToggle((pushToggle) => !pushToggle);

    // 토글이 on이 되었으면 알림 생성, off가 되었으면 알림 삭제
    if (pushToggle) {
      eetch
        .createAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          repoId: 0,
          keyword: keywords,
          type: 'push',
        })
        .then((res) => {
          setAlertList(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      eetch
        .deleteAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          alertId: pushId,
        })
        .then((res) => {
          setAlertList(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  const clickPullRequestToggle = (pullRequestId) => {
    setPullRequestToggle((pullRequestToggle) => !pullRequestToggle);

    // 토글이 on이 되었으면 알림 생성, off가 되었으면 알림 삭제
    if (pushToggle) {
      eetch
        .createAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          repoId: 0,
          keyword: keywords,
          type: 'pull_request',
        })
        .then((res) => {
          setAlertList(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      eetch
        .deleteAlert({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          alertId: pullRequestId,
        })
        .then((res) => {
          setAlertList(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    }
  };

  return (
    <div>
      {alertList.map(({ keyword, groundName, pushId, pullRequestId }) => (
        <div key={groundName}>
          <div>{groundName}</div>
          {pushToggle ? (
            <div type="button" onClick={clickPushToggle(pushId)}>
              푸시 토글 {pushToggle} 상태
            </div>
          ) : (
            <div type="button" onClick={clickPushToggle(pushId)}>
              푸시 토글 {pushToggle} 상태
            </div>
          )}
          {pullRequestToggle ? (
            <div type="button" onClick={clickPullRequestToggle(pullRequestId)}>
              풀 리퀘스트 토글 {pullRequestToggle} 상태
            </div>
          ) : (
            <div type="button" onClick={clickPullRequestToggle(pullRequestId)}>
              플 리퀘스르 토글 {pullRequestToggle} 상태
            </div>
          )}
          {(pushToggle || pullRequestToggle) && { keyword } ? (
            <Input label="키워드" array={keyword} enter={updateAlert(groundName, keyword)} />
          ) : null}
        </div>
      ))}
    </div>
  );
};

export default CreateAlert;
