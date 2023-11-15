import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import { db } from 'fcm/firebaseConfig';

const AlertData = () => {
  const user = useSelector((state) => state.user);
  const githubId = useSelector((state) => state.user.githubId);
  const groundsMap = useSelector((state) => state.user.groundsMap);

  const [userAlertList, setUserAlertList] = useState([]);
  const [allAlertList, setAllAlertList] = useState([]);

  useEffect(() => {
    const alertUserDataCollection = db.collection('alertUserData').where('githubId', '==', Number(githubId)).orderBy('timestamp', 'desc').limit(10);

    // firestore 실시간 동기화, 문서 변경 발생 시 실행
    alertUserDataCollection.onSnapshot(
      (snapshot) => {
        const arr = [];

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

  const getUserDocs = async (arr) => {
    const promises = arr.map(({ id, isRead, keyword }) => {
      return db
        .collection('webhookData')
        .orderBy('timestamp', 'desc')
        .limit(10)
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
    // validResults.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
    setUserAlertList(validResults);
  };

  // 깃허브 링크 연결 및 읽음 처리(사용자 구독 알림만)
  const githubLinkClick = async (url, docId) => {
    const docRef = db.collection('alertUserData').where('githubId', '==', Number(githubId)).where('id', '==', docId);

    const snapshot = await docRef.get();

    if (!snapshot.empty) {
      snapshot.docs[0].ref.update({
        isRead: true,
      });
    }

    window.open(url, '_blank');
  };

  return (
    <>
      <div>**********AlertData 컴포넌트 시작**********</div>
      <div>사용자 알림 내역</div>
      {userAlertList.map((alert) => {
        return (
          <div key={alert.id}>
            <div onClick={githubLinkClick(alert.url, alert.id)}>
              {alert.type}
              {alert.nickname}
              {alert.branch}
              {alert.message}
              {alert.timestamp.toDate().toString()}
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
            {alert.type}
            {alert.nickname}
            {alert.branch}
            {alert.message}
            {alert.timestamp.toDate().toString()}
            {alert.url}
          </div>
        );
      })}
      <div>**********AlertData 컴포넌트 끝**********</div>
    </>
  );
};

export default AlertData;
