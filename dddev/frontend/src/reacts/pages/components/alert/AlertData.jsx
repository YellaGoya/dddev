// import eetch from 'eetch/eetch';
import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
// import requestPermission from 'fcm/firebase-messaging.js';
import { db } from 'fcm/firebaseConfig';
// import eetch from 'eetch/eetch';

const AlertData = () => {
  const user = useSelector((state) => state.user);
  // const ground = useSelector((state) => state.ground);
  const groundsMap = useSelector((state) => state.groundsMap);
  const [docs, setDocs] = useState([{}]);
  // const [groundUsers, setGroundUsers] = useState([{}]);
  // const [dataId, setDataId] = useState([]);
  const [allDocs, setAllDocs] = useState([{}]);

  useEffect(() => {
    // firebase 알림 내역 조회

    // const collection = db.collection('AlertData').where('receiverId', '==', `${user.githubId}`);
    // const collection = db.collection('AlertData');
    const alertUserDataCollection = db.collection('alertUserData').where('githubId', '==', `${user.githubId}`);
    const webhookDataCollection = db.collection('webhookData');

    // firestore 실시간 동기화, collection에 문서 변경 발생 시 실행
    alertUserDataCollection.onSnapshot(
      (snapshot) => {
        const arr = [];
        // console.log(`Received query snapshot of size ${snapshot.size}`);

        // githubId로 보고 있는 db에 새 문서가 추가된 경우 알림 내역 다시 조회
        snapshot.forEach((userDoc) => {
          // const { keyword } = userDoc.data().keyword;
          // const { isRead } = userDoc.data().isRead;

          // id로 문서 검색
          webhookDataCollection
            .where('id', '==', `${userDoc.data().id}`)
            .get()
            .then((docs) => {
              console.log(docs);
              // docs.forEach((doc) => {
              //   // userDoc에 있는 키워드 추가
              //   doc.data.keyword = keyword;
              //   // 읽은 여부 추가
              //   doc.data.isRead = isRead;
              //   arr.push(doc.data());
              // });
            });
        });
        setDocs(arr);
      },
      (err) => {
        console.log(`snapshot error: ${err}`);
      }
    );

    groundsMap.forEach(({ id }) => {
      webhookDataCollection
        .where('groundId', '==', `${id}`)
        .get()
        .then((docs) => {
          const arr = [];
          docs.forEach((doc) => {
            arr.push(doc.data());
          });
          setAllDocs(arr);
        });
    });

    // console.log(`github id :: ${user.githubId}`);
  }, [user.accessToken, user.refreshToken]);

  return (
    <>
      {docs.map((doc) => {
        return (
          <div key={doc.id}>
            <div>
              {doc.alertType}
              {doc.author.nickname}
              {doc.branch}
              {doc.message}
              {doc.timestamp}
              {doc.url}
              {doc.keyword}
            </div>
            <div>{doc.isRead}</div>
          </div>
        );
      })}
      <div>
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
      </div>
    </>
  );
};

export default AlertData;
