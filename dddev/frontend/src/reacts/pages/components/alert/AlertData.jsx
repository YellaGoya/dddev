// import eetch from 'eetch/eetch';
import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
// import requestPermission from 'fcm/firebase-messaging.js';
import { db } from 'fcm/firebaseConfig';
// import eetch from 'eetch/eetch';

const AlertData = () => {
  const user = useSelector((state) => state.user);
  const groundsMap = useSelector((state) => state.user.groundsMap);
  const [userDocIdList, setUserDocIdList] = useState([]);
  const [userAlertList, setUserAlertList] = useState([{}]);
  const [allAlertList, setAllAlertList] = useState([{}]);

  useEffect(() => {
    console.log(userAlertList);
  }, [userAlertList]);

  useEffect(() => {
    console.log(allAlertList);
  }, [allAlertList]);

  useEffect(() => {
    console.log(user.accessToken);
    const alertUserDataCollection = db.collection('alertUserData').where('githubId', '==', Number(user.githubId));

    // firestore 실시간 동기화, 문서 변경 발생 시 실행
    alertUserDataCollection.onSnapshot(
      (snapshot) => {
        const arr = [];
        console.log(`Received query snapshot of size ${snapshot.size}`);

        // 전체 알림에서 사용자가 받은 알림의 id를 받아옴
        snapshot.forEach((doc) => {
          arr.push(doc.data());
        });

        setUserDocIdList(arr);

        // 전체 알림에서 id로 사용자가 받을 알림을 찾고
        // const AlertList = [];
        // arr.forEach(({ id, isRead, keyword }) => {
        //   db.collection('webhookData')
        //     .doc(id)
        //     .get()
        //     .then((res) => {
        //       if (res.exists) {
        //         const resData = res.data();
        //         // 알림 읽었는지, 걸린 키워드 뭔지 추가해줌
        //         resData.isRead = isRead;
        //         resData.keyowrd = keyword;
        //         AlertList.push(resData);
        //         console.log('list', resData); // 화면에 출력하는 데이터
        //       }
        //     });
        //   setUserAlertList(AlertList);
        // });
        getUserDocs(arr);
      },
      (err) => {
        console.log(`snapshot error: ${err}`);
      },
    );

    // 전체 웹훅 발생 문서 조회 -> 최초 1번만
    // db.collection('webhookData').where()
    getAllDocs(groundsMap);
  }, [user]);

  const getAllDocs = async (grounds) => {
    const promises = grounds.map(({ id }) => {
      console.log('id', id);
      return db
        .collection('webhookData')
        .where('groundId', '==', `${id}`)
        .get()
        .then((res) => {
          if (res.exists) {
            const resData = res.data();
            resData.nickname = resData.author.nickname;
            console.log('resData ::', resData);
            return resData;
          }

          return null;
        });
    });

    const results = await Promise.all(promises);
    const validResults = results.filter((result) => result !== null);
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
    setUserAlertList(validResults);
  };

  // const getUserDocs = (arr) => {
  //   const AlertList = [];
  //   // for(const [id, isRead, keyword] of arr) {}
  //   arr.map(({ id, isRead, keyword }) => {
  //     db.collection('webhookData')
  //       .doc(id)
  //       .get()
  //       .then((res) => {
  //         if (res.exists) {
  //           const resData = res.data();
  //           // 알림 읽었는지, 걸린 키워드 뭔지 추가해줌
  //           resData.isRead = isRead;
  //           resData.keyowrd = keyword;
  //           AlertList.push(resData);
  //           console.log('list', resData); // 화면에 출력하는 데이터
  //         }
  //       })
  //       .catch((err) => console.log(err));
  //     setUserAlertList(AlertList);
  //   });

  //   // await Promise.all(promises);
  // };

  // {allAlertList.map((doc) => {
  //   return (
  //     <div key={doc.id}>
  //       <div>
  //         {doc.type}
  //         {/* {doc.author.nickname} */}
  //         {doc.branch}
  //         {doc.message}
  //         {doc.timestamp}
  //         {doc.url}
  //       </div>
  //     </div>
  //   );
  // })}
  return (
    <>
      <div>사용자 알림 내역</div>
      {userAlertList.map((alert) => {
        // console.log('html map', alert);
        return (
          <div key={alert.id}>
            <div>
              {alert.id}
              {alert.alertType}
              {alert.author.nickname}
              {alert.branch}
              {alert.message}
              {alert.timestamp}
              {alert.url}
              {alert.keyword}
              {alert.isRead}
            </div>
          </div>
        );
      })}
      <div>전체 알림 내역</div>
      {/* <div>{console.log('allAlertList :: ', allAlertList)}</div> */}
    </>
  );
};

export default AlertData;
