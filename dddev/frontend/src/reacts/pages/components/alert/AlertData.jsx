/* eslint-disable prettier/prettier */
// import eetch from 'eetch/eetch';
import { useState, useEffect } from 'react';
// import { useSelector } from 'react-redux';
// import requestPermission from 'fcm/firebase-messaging.js';
import { db } from 'fcm/firebaseConfig';
// import eetch from 'eetch/eetch';

const AlertData = () => {
  // const user = useSelector((state) => state.user);
  const [userAlertList, setUserAlertList] = useState([{}]); // 구독중인 알림 내역

  useEffect(() => {
    // user.githubId 로 받아와야 합니다
    const alertUserDataCollection = db.collection('alertUserData').where('githubId', '==', 111165249);

    // firestore 실시간 동기화, 문서 변경 발생 시 실행
    alertUserDataCollection.onSnapshot(
      (snapshot) => {
        const arr = [];
        console.log(`Received query snapshot of size ${snapshot.size}`);

        // 전체 알림에서 사용자가 받은 알림의 id를 받아옴
        snapshot.forEach((doc) => {
          arr.push(doc.data());
        });

        // 전체 알림에서 id로 사용자가 받을 알림을 찾고
        const AlertList = [];
        arr.forEach(({ id, isRead, keyword }) => {
          db.collection('webhookData')
            .doc(id)
            .get()
            .then((res) => {
              if (res.exists) {
                const resData = res.data();
                // 알림 읽었는지, 걸린 키워드 뭔지 추가해줌
                resData.isRead = isRead;
                resData.keyowrd = keyword;
                AlertList.push(resData);
                console.log('list', resData); // 화면에 출력하는 데이터
              }
            });
          setUserAlertList(AlertList);
        });
      },
      (err) => {
        console.log(`snapshot error: ${err}`);
      }
    );
  }, []);

  return (
    <>
      {userAlertList.map((alert) => {
        return (
          // alert.id 지금 중복이 있어서 수정할 계획 -> 원래 고유한 값 맞음
          // 콘솔에는 나오는데 화면에는 뜨지 않는다... 왜지??ㅠㅠㅠㅠㅠㅠㅠ
          // 저장할 때 잠깐 떴다가 사라지는거같은데?
          <div key={alert}>
            <div>
              {alert.alertType}
              {/* {alert.author.nickname} */}
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
      {/* <div>
        {allDocs.map((doc) => {
          return (
            <div key={doc.id}>
              <div>
                {doc.type}
                {doc.author.nickname}
                {doc.branch}
                {doc.message}
                {doc.timestamp}
                {doc.url}
              </div>
            </div>
          );
        })}
      </div> */}
    </>
  );
};

export default AlertData;
