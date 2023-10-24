import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { DocsTree } from 'assets/DummyLoader';

import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import * as s from 'markup/styles/components/document/Index';
const Index = () => {
  const navigate = useNavigate();
  const groundId = useSelector((state) => state.ground.groundId);

  const RenderDocsTree = ({ doc }) => {
    const [toggle, setToggle] = useState(doc.depth === 2);

    return (
      <s.TreeChild>
        <s.TreeName
          $toggle={toggle}
          onClick={() => {
            setToggle(!toggle);
          }}
        >
          {doc.childs && <KeyboardArrowDownIcon />}
          <p
            onClick={(event) => {
              event.stopPropagation();
              console.log(3);
            }}
          >
            {doc.name}
          </p>
        </s.TreeName>
        <s.TreeParent $toggle={toggle}>{doc.childs && doc.childs.map((child) => <RenderDocsTree key={child.id} doc={child} />)}</s.TreeParent>
      </s.TreeChild>
    );
  };

  return (
    <>
      <s.ContentGrid>
        <s.ContentCard>
          <s.ContentLabel>이슈</s.ContentLabel>
          <s.Content>
            <s.TreeParent>
              {DocsTree.map((doc) => (
                <RenderDocsTree key={doc.id} doc={doc} />
              ))}
            </s.TreeParent>
          </s.Content>
        </s.ContentCard>
        <s.ContentCard>
          <s.ContentLabel>요청</s.ContentLabel>
          <s.Content>
            <s.TreeParent>
              {DocsTree.map((doc) => (
                <RenderDocsTree key={doc.id} doc={doc} />
              ))}
            </s.TreeParent>
          </s.Content>
        </s.ContentCard>
        <s.ContentCard>
          <s.ContentLabel>일반</s.ContentLabel>
          <s.Content>
            <s.TreeParent>
              {DocsTree.map((doc) => (
                <RenderDocsTree key={doc.id} doc={doc} />
              ))}
            </s.TreeParent>
          </s.Content>
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
