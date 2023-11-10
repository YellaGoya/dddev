// import eetch from 'eetch/eetch';
import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import requestPermission from 'fcm/firebase-messaging.js';
import { db } from 'fcm/firebaseConfig';
// import eetch from 'eetch/eetch';

const AlertData = () => {
  const user = useSelector((state) => state.user);
  // const ground = useSelector((state) => state.ground);
  const [docs, setDocs] = useState([{}]);
  // const [groundUsers, setGroundUsers] = useState([{}]);

  useEffect(() => {
    if (user.isLoggedIn) requestPermission({ accessToken: user.accessToken, refreshToken: user.refreshToken });

    // firebase 알림 내역 조회
    // where은 조건
    // const collection = db.collection('AlertData').where('receiverId', '==', `${user.githubId}`);
    const collection = db.collection('AlertData');

    // firestore 실시간 동기화, collection에 문서 변경 발생 시 실행
    collection.onSnapshot(
      (snapshot) => {
        const arr = [];
        // console.log(`Received query snapshot of size ${snapshot.size}`);
        snapshot.forEach((doc) => {
          arr.push(doc.data());
        });
        setDocs(arr);
      },
      (err) => {
        console.log(`snapshot error: ${err}`);
      },
    );

    console.log(`github id :: ${user.githubId}`);
  }, []);

  return (
    <>
      {docs.map((doc) => {
        return (
          <div key={doc.id}>
            {/* {doc.alertType}
            {doc.title}
            {doc.content}
            {doc.sendingDate}
            {doc.isSuccess}
            {doc.url} */}
            {doc.name}
          </div>
        );
      })}
    </>
  );
};

export default AlertData;
