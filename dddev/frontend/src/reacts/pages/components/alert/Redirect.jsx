import { useEffect } from 'react';
import { db } from 'fcm/firebaseConfig';
import { useLocation } from 'react-router-dom';

const Redirect = () => {
  const location = useLocation();
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const githubUrl = queryParams.get('githubUrl');
    const docId = queryParams.get('docId');
    const collection = db.collection('AlertData');

    collection.doc(docId).update({ isRead: true });

    window.location.href = githubUrl;
  }, []);

  return <div />;
};

export default Redirect;
