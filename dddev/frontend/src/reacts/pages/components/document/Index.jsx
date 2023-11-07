import { useState, useEffect, useReducer } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import eetch from 'eetch/eetch';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import AddRoundedIcon from '@mui/icons-material/AddRounded';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import LibraryAddIcon from '@mui/icons-material/LibraryAdd';
import LinearScaleIcon from '@mui/icons-material/LinearScale';
import DriveFileRenameOutlineIcon from '@mui/icons-material/DriveFileRenameOutline';
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';
import * as s from 'reacts/styles/components/document/Index';

const toggleReducer = (state, action) => {
  switch (action.type) {
    case 'TOGGLE':
      return { ...state, [action.id]: !state[action.id] };
    case 'SET':
      return { ...state, [action.id]: action.value };
    default:
      return state;
  }
};

const Index = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);
  const groundId = useSelector((state) => state.user.lastGround);
  const [toggleStates, dispatchToggle] = useReducer(toggleReducer, {});
  const [issueTree, setIssueTree] = useState([]);
  const [newDocId, setNewDocId] = useState(null);
  const [moreLine, setMoreLine] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      const tree = await getIssueTree();
      setInitialToggleStates(tree);
    };

    fetchData();
  }, []);

  const setInitialToggleStates = (tree) => {
    if (tree)
      tree.forEach((node) => {
        dispatchToggle({ type: 'SET', id: node.id, value: node.step === 2 });
        if (node.children) {
          setInitialToggleStates(node.children);
        }
      });
  };

  const getIssueTree = () => {
    return eetch
      .treeDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type: 'target' })
      .then((res) => {
        if (res.data[0].title === '미분류') res.data.shift();
        setIssueTree(res.data);
        return res.data;
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
    const toggle = toggleStates[doc.id] ?? true;

    return (
      <s.TreeChild>
        <s.TreeName
          $toggle={toggle}
          $isEmpty={doc.title === ''}
          $isNew={doc.id === newDocId}
          $isMore={doc.id === moreLine}
          onClick={() => {
            dispatchToggle({ type: 'TOGGLE', id: doc.id });
          }}
        >
          {doc.children && doc.children.length > 0 && <KeyboardArrowDownIcon className="foldSign" />}
          <s.DocTitle
            onClick={(event) => {
              event.stopPropagation();
              navigate(
                `/${groundId}/document/docs/${type === 'issue' ? (doc.step === 1 ? 'target' : doc.step === 2 ? 'check' : 'issue') : type}/${doc.id}`,
              );
            }}
          >
            {doc.title === '' ? '새 문서' : doc.name}
            {doc.step < (type === 'issue' ? 3 : 2) && (
              <LibraryAddIcon
                className="addChild"
                onClick={(event) => {
                  event.stopPropagation();
                  dispatchToggle({ type: 'SET', id: doc.id, value: false });
                  createChildDocmunet(type, doc.id, doc.step);
                }}
              />
            )}
          </s.DocTitle>
          <LinearScaleIcon
            className="moreButton"
            onClick={(event) => {
              event.stopPropagation();
              setMoreLine(doc.id);
            }}
          />
          <DriveFileRenameOutlineIcon
            className="editName"
            onClick={(event) => {
              event.stopPropagation();
              setMoreLine(doc.id);
            }}
          />
          <RemoveCircleIcon
            className="deleteDoc"
            onClick={(event) => {
              event.stopPropagation();
              deleteDocument(type, doc.id, doc.step);
            }}
          />
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
      .then((res) => {
        getIssueTree();
        setNewDocId(res.data.id);
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
        type = 'check';
      } else if (step === 2) {
        type = 'issue';
      }
    }

    eetch
      .createDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type, parentId })
      .then((res) => {
        getIssueTree();
        setNewDocId(res.data.id);
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

  const deleteDocument = (type, id, step) => {
    if (type === 'issue') {
      if (step === 1) {
        type = 'target';
      } else if (step === 2) {
        type = 'check';
      }
    }

    eetch
      .deleteDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type, id })
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
    <s.ContentGrid>
      <s.ContentCard>
        <s.ContentLabel>이슈</s.ContentLabel>
        <s.Content>
          <s.TreeParent>
            {issueTree && issueTree.length > 0 && issueTree.map((doc) => <RenderDocsTree key={doc.id} doc={doc} type="issue" />)}
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
  );
};

export default Index;
