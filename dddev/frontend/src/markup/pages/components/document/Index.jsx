import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

import * as s from 'markup/styles/components/document/Index';
const Index = () => {
  const navigate = useNavigate();
  const groundId = useSelector((state) => state.ground.groundId);

  return (
    <>
      <s.ContentGrid>
        <s.ContentCard>
          <s.Content>3242423</s.Content>
        </s.ContentCard>
        <s.ContentCard>
          <s.Content>3242423</s.Content>
        </s.ContentCard>
        <s.ContentCard>
          <s.Content>3242423</s.Content>
        </s.ContentCard>
      </s.ContentGrid>
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
