import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import eetch from 'eetch/eetch';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import AddRoundedIcon from '@mui/icons-material/AddRounded';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import LibraryAddIcon from '@mui/icons-material/LibraryAdd';
import * as s from 'reacts/styles/components/document/Index';
const Index = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);
  const groundId = useSelector((state) => state.user.lastGround);
  const [issueTree, setIssueTree] = useState([]);

  useEffect(() => {
    getIssueTree();
  }, []);

  useEffect(() => {
    console.log(issueTree);
  }, [issueTree]);

  const getIssueTree = () => {
    eetch
      .treeDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type: 'target' })
      .then((res) => {
        setIssueTree(res.tree);
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const RenderDocsTree = ({ doc, type }) => {
    const [toggle, setToggle] = useState(doc.step === 2);

    return (
      <s.TreeChild>
        <s.TreeName
          $toggle={toggle}
          $isEmpty={doc.title === ''}
          onClick={() => {
            setToggle(!toggle);
          }}
        >
          {doc.children && doc.children.length > 0 && <KeyboardArrowDownIcon />}
          <p
            onClick={(event) => {
              event.stopPropagation();
              console.log(3);
            }}
          >
            {doc.title === '' ? '새 문서' : doc.name}
            {doc.step < (type === 'issue' ? 3 : 2) && (
              <LibraryAddIcon
                className="addChild"
                onClick={(event) => {
                  event.stopPropagation();
                  createChildDocmunet(type, doc.id, doc.step);
                }}
              />
            )}
          </p>
        </s.TreeName>
        <s.TreeParent $toggle={toggle}>
          {doc.children && doc.children.map((child) => <RenderDocsTree key={child.id} doc={child} type={type} />)}
        </s.TreeParent>
      </s.TreeChild>
    );
  };

  const createRootDocmunet = (type) => {
    eetch
      .createDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type })
      .then(() => {
        getIssueTree();
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const createChildDocmunet = (type, parentId, step) => {
    if (type === 'issue') {
      if (step === 1) {
        step = 'check';
      } else if (step === 2) {
        step = 'issue';
      }
    }

    eetch
      .createDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type: step, parentId })
      .then(() => {
        getIssueTree();
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  return (
    <>
      <s.ContentGrid>
        <s.ContentCard>
          <s.ContentLabel>이슈</s.ContentLabel>
          <s.Content>
            <s.TreeParent>
              {issueTree.length > 0 && issueTree.map((doc) => <RenderDocsTree key={doc.id} doc={doc} type="issue" />)}
              <s.TreeChild>
                <s.TreeName className="add-button" onClick={() => createRootDocmunet('target')}>
                  <AddRoundedIcon /> 새 문서 작성
                </s.TreeName>
              </s.TreeChild>
            </s.TreeParent>
          </s.Content>
        </s.ContentCard>
        <s.ContentCard>
          <s.ContentLabel>요청</s.ContentLabel>
          <s.Content>
            <s.TreeParent>
              <s.TreeChild>
                <s.TreeName className="add-button" onClick={() => createRootDocmunet('request')}>
                  <AddRoundedIcon /> 새 문서 작성
                </s.TreeName>
              </s.TreeChild>
            </s.TreeParent>
          </s.Content>
        </s.ContentCard>
        <s.ContentCard>
          <s.ContentLabel>일반</s.ContentLabel>
          <s.Content>
            <s.TreeParent>
              <s.TreeChild>
                <s.TreeName className="add-button" onClick={() => createRootDocmunet('issue')}>
                  <AddRoundedIcon /> 새 문서 작성
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
