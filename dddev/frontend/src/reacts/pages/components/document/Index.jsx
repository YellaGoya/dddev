import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import eetch from 'eetch/eetch';

import { DocsTree } from 'assets/DummyLoader';

import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import LibraryAddIcon from '@mui/icons-material/LibraryAdd';
import * as s from 'reacts/styles/components/document/Index';
const Index = () => {
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const groundId = useSelector((state) => state.user.lastGround);

  useEffect(() => {
    eetch.listDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type: 'target' }).then((res) => {
      console.log(res);
    });
  }, [groundId, user.accessToken, user.refreshToken]);

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

  const createRootDocmunet = (type) => {
    eetch.createDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type }).then((res) => {
      console.log(res);
    });
  };

  return (
    <>
      <s.ContentGrid>
        <s.ContentCard>
          <s.ContentLabel>이슈</s.ContentLabel>
          <s.Content>
            <s.TreeParent>
              <s.TreeChild>
                <s.TreeName className="add-button" onClick={() => createRootDocmunet('target')}>
                  <LibraryAddIcon /> 새 문서 작성
                </s.TreeName>
              </s.TreeChild>
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
              <s.TreeChild>
                <s.TreeName className="add-button" onClick={() => createRootDocmunet('request')}>
                  <LibraryAddIcon /> 새 문서 작성
                </s.TreeName>
              </s.TreeChild>
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
              <s.TreeChild>
                <s.TreeName className="add-button" onClick={() => createRootDocmunet('issue')}>
                  <LibraryAddIcon /> 새 문서 작성
                </s.TreeName>
              </s.TreeChild>
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
