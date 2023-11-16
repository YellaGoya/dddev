import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import { db } from 'fcm/firebaseConfig';

import * as s from 'reacts/styles/components/alert/AlertData';
const AlertData = () => {
  const user = useSelector((state) => state.user);
  const groundsMap = useSelector((state) => state.user.groundsMap);

  const [allAlertList, setAllAlertList] = useState([]);

  useEffect(() => {
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

  const githubLinkClick = async (url) => {
    window.open(url, '_blank');
  };

  return (
    <s.AlarmList>
      {allAlertList.map((alert) => {
        return (
          <s.AlarmItem key={alert.id}>
            <span onClick={() => githubLinkClick(alert.url)}>
              <s.AlertTag>{alert.type}</s.AlertTag>
              <s.AlertTag>{alert.branch}</s.AlertTag>
              <s.Username className="user">{alert.nickname}</s.Username> <s.UserMessage className="user">: {alert.message}</s.UserMessage>
            </span>
          </s.AlarmItem>
        );
      })}
    </s.AlarmList>
  );
};

export default AlertData;
