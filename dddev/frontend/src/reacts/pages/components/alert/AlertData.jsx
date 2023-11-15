import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import { db } from 'fcm/firebaseConfig';

const AlertData = () => {
  const user = useSelector((state) => state.user);
  const groundsMap = useSelector((state) => state.user.groundsMap);

  const [userAlertList, setUserAlertList] = useState([]);
  const [allAlertList, setAllAlertList] = useState([]);

  useEffect(() => {
    const alertUserDataCollection = db.collection('alertUserData').where('githubId', '==', Number(user.githubId));

    // firestore 실시간 동기화, 문서 변경 발생 시 실행
    alertUserDataCollection.onSnapshot(
      (snapshot) => {
        const arr = [];
        // console.log(`Received query snapshot of size ${snapshot.size}`);

        // 사용자가 받은 알림의 id를 받아옴
        snapshot.forEach((doc) => {
          arr.push(doc.data());
        });

        getUserDocs(arr);
      },
      (err) => {
        console.log(`snapshot error: ${err}`);
      },
    );

    // 전체 웹훅 내역 조회
    getAllDocs(groundsMap);
  }, [user]);

  const getAllDocs = async (grounds) => {
    // console.log('grounds', grounds);
    const promises = grounds.map(({ id }) => {
      return db
        .collection('webhookData')
        .where('groundId', '==', id)
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
    const validResults = results.flat().filter((result) => result !== null);
    validResults.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
    setAllAlertList(validResults);
  };

  const getUserDocs = async (arr) => {
    const promises = arr.map(({ id, isRead, keyword }) => {
      return db
        .collection('webhookData')
        .doc(id)
        .get()
        .then((res) => {
          if (res.exists) {
            const resData = res.data();
            resData.isRead = isRead;
            resData.keyword = keyword;
            resData.nickname = resData.author.nickname;
            return resData;
          }

          return null;
        });
    });

    const results = await Promise.all(promises);
    const validResults = results.filter((result) => result !== null);
    validResults.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
    setUserAlertList(validResults);
  };

  return (
    <>
      <div>=========AlertData 컴포넌트 시작===========</div>
      <div>사용자 알림 내역</div>
      {userAlertList.map((alert) => {
        return (
          <div key={alert.id}>
            <div>
              {alert.alertType}
              {alert.nickname}
              {alert.branch}
              {alert.message}
              {alert.timestamp}
              {alert.url}
              {alert.keyword}
              {String(alert.isRead)}
            </div>
          </div>
        );
      })}
      <div>전체 알림 내역</div>
      {allAlertList.map((alert) => {
        return (
          <div key={alert.id}>
            {alert.alertType}
            {alert.nickname}
            {alert.branch}
            {alert.message}
            {alert.timestamp}
            {alert.url}
          </div>
        );
      })}
      <div>=========AlertData 컴포넌트 끝==========</div>
    </>
  );
};

export default AlertData;
