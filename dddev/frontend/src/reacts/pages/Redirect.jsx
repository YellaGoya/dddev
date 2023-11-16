import { useParams } from 'react-router-dom';

const Redirect = () => {
  const params = useParams();
  return <div>{params.notiId}</div>;
};

export default Redirect;
