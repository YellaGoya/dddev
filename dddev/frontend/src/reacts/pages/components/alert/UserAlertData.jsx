import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { updateUser } from 'redux/actions/user';
import { db } from 'fcm/firebaseConfig';

import * as s from 'reacts/styles/components/alert/UserAlertData';
const UserAlertData = () => {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);
  const githubId = useSelector((state) => state.user.githubId);

  const [userAlertList, setUserAlertList] = useState([]);

  useEffect(() => {
    const alertUserDataCollection = db.collection('alertUserData').where('githubId', '==', Number(githubId)).orderBy('timestamp', 'desc').limit(10);

    alertUserDataCollection.onSnapshot(
      (snapshot) => {
        const arr = [];

        snapshot.forEach((doc) => {
          arr.push(doc.data());
        });

        getUserDocs(arr);
      },
      (err) => {
        console.log(`snapshot error: ${err}`);
      },
    );
  }, [user.githubId]);

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
    const unread = validResults.filter((res) => {
      return res.isRead === false;
    }).length;

    dispatch(
      updateUser({
        unread: unread ? unread : 0,
      }),
    );
    setUserAlertList(validResults);
  };

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
    <s.UserAlertList>
      {userAlertList.map((alert) => {
        return (
          <s.UserAlertItem key={alert.id}>
            <s.AlertTag>{alert.type}</s.AlertTag>
            {'>'}
            <s.AlertTag>{alert.branch}</s.AlertTag>
            {alert.keyword && alert.keyword.length > 0 && (
              <>
                {'>'}
                <s.AlertTag>{alert.keyword}</s.AlertTag>
              </>
            )}

            <s.AlertContent $read={alert.isRead} onClick={() => githubLinkClick(alert.url, alert.id)}>
              {alert.nickname} : {alert.message}
            </s.AlertContent>
          </s.UserAlertItem>
        );
      })}
    </s.UserAlertList>
  );
};

export default UserAlertData;
