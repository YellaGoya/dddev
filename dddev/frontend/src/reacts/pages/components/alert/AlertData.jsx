// import eetch from 'eetch/eetch';
import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import requestPermission from 'fcm/firebase-messaging.js';
import { db } from 'fcm/firebaseConfig';

const AlertData = () => {
  const user = useSelector((state) => state.user);
  const [docs, setDocs] = useState([{}]);

  // const messages = [
  //   { id: 3, content: 'test', title: 'test', show: true },
  //   { id: 4, content: 'test', title: 'test', show: false },
  //   { id: 5, content: 'test', title: 'test', show: true },
  //   { id: 6, content: 'test', title: 'test', show: false },
  // ];

  useEffect(() => {
    if (user.isLoggedIn) requestPermission({ accessToken: user.accessToken, refreshToken: user.refreshToken });

    // collection.get().then((res) => {
    //   // console.log('firestore res :: ', res);
    //   const arr = [];
    //   res.forEach((doc) => {
    //     arr.push(doc.data());
    //   });
    //   setDocs(arr);
    // });

    const collection = db.collection('AlertData').where('alertType', '==', 'pull_request');

    collection.onSnapshot(
      (snapshot) => {
        const arr = [];
        // console.log(`new alert data!! ${snapshot.docs}`);
        console.log(`Received query snapshot of size ${snapshot.size}`);
        snapshot.forEach((doc) => {
          // console.log(doc.data());
          arr.push(doc.data());
        });
        setDocs(arr);
        // console.log(docs);
      },
      (err) => {
        console.log(`snapshot error: ${err}`);
      },
    );
  }, []);

  // useEffect(() => {
  //   eetch
  //     .gayeonTest({ accessToken: user.accessToken, refreshToken: user.refreshToken, value: messages })
  //     .then((res) => {
  //       console.log(res.data);
  //     })
  //     .catch((err) => {
  //       if (err.message === 'gayeonNotGood') console.log(err);
  //     });
  // }, []);

  return (
    <>
      {/* <div>{messages[0].id}</div> */}
      {/* {messages.map((message) => {
        return message.show ? <div key={message.id}>{message.id}</div> : null;
      })} */}
      {docs.map((doc) => {
        return (
          <div key={doc.id}>
            {doc.alertType}
            {doc.title}
            {doc.content}
            {doc.creatorId}
            {doc.receiverId}
            {doc.sendingDate}
            {doc.isSuccess}
          </div>
        );
      })}
    </>
  );
};

export default AlertData;
