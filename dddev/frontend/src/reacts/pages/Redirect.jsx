import { useParams } from 'react-router-dom';

const Redirect = () => {
  const params = useParams();
  // firestore로
  // 읽음 처리 하고
  // url 읽어와서 navigation으로 리다이렉트

  return <div>{params.notiId}</div>;
};

export default Redirect;
