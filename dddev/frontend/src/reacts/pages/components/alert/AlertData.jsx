import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import { db } from 'fcm/firebaseConfig';

const AlertData = () => {
  const user = useSelector((state) => state.user);
  const groundsMap = useSelector((state) => state.user.groundsMap);

  const [allAlertList, setAllAlertList] = useState([]);

  useEffect(() => {
    // 전체 웹훅 내역 조회
    getAllDocs(groundsMap);
  }, [user]);

  const getAllDocs = async (grounds) => {
    const promises = grounds.map(({ id }) => {
      return db
        .collection('webhookData')
        .where('groundId', '==', id)
        .orderBy('timestamp', 'desc')
        .limit(20)
        .get()
        .then(async (resList) => {
          const resListMap = resList.docs.map((res) => {
            if (res.exists) {
              const resData = res.data();
              resData.nickname = resData.author.nickname;
              return resData;
            }

            return null;
          });

          const eachResults = await Promise.all(resListMap);
          const validEachResults = eachResults.filter((result) => result !== null);
          return validEachResults;
        })
        .catch((err) => console.log(err));
    });

    const results = await Promise.all(promises);
    let validResults = results.flat().filter((result) => result !== null);
    validResults.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
    validResults = validResults.slice(0, 10);
    setAllAlertList(validResults);
  };

  // 깃허브 링크 연결
  const githubLinkClick = async (url) => {
    window.open(url, '_blank');
  };

  return (
    <>
      <div>**********AlertData 컴포넌트 시작**********</div>

      <div>전체 알림 내역</div>
      {allAlertList.map((alert) => {
        return (
          <div key={alert.id}>
            <div onClick={() => githubLinkClick(alert.url)}>
              {alert.type}
              {alert.nickname}
              {alert.branch}
              {alert.message}
              {/* {alert.timestamp.toDate().toString()}
            {alert.url} */}
            </div>
          </div>
        );
      })}
      <div>**********AlertData 컴포넌트 끝**********</div>
    </>
  );
};

export default AlertData;
