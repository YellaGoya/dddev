import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

const Index = () => {
  const navigate = useNavigate();
  const groundId = useSelector((state) => state.ground.groundId);

  return (
    <>
      Index
      <button
        type="button"
        onClick={() => {
          navigate(`/${groundId}/document/docs`);
        }}
      >
        새 문서 추가
      </button>
    </>
  );
};

export default Index;
