import { useState, useEffect, useReducer, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import eetch from 'eetch/eetch';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import AddRoundedIcon from '@mui/icons-material/AddRounded';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import LibraryAddIcon from '@mui/icons-material/LibraryAdd';
import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
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
  const [toggleDocs, dispatchToggle] = useReducer(toggleReducer, {});
  const [issueTree, setIssueTree] = useState([]);
  const [requestTree, setRequestTree] = useState([]);
  const [generalTree, setGeneralTree] = useState([]);
  const [newDocId, setNewDocId] = useState(null);
  const [moreLine, setMoreLine] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      const issues = await getTree('target');
      setInitialToggleStates(issues);
      const requests = await getTree('request');
      setInitialToggleStates(requests);
      const generals = await getTree('general');
      setInitialToggleStates(generals);
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

  const getTree = (type) => {
    return eetch
      .treeDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type })
      .then((res) => {
        if (res.data[0].title === '미분류') res.data.shift();
        switch (type) {
          case 'target':
            setIssueTree(res.data);
            break;
          case 'request':
            setRequestTree(res.data);
            break;
          case 'general':
            setGeneralTree(res.data);
            break;
          default:
            break;
        }

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
    const toggle = toggleDocs[doc.id] ?? true;
    const [title, setTitle] = useState(doc.title === '' ? '새 문서' : doc.title);
    const [onEdit, setOnEdit] = useState(false);
    const editRef = useRef(null);

    return (
      <s.TreeChild>
        <s.TreeName
          $toggle={toggle}
          $onEdit={onEdit}
          $isEmpty={title === '새 문서'}
          $isNew={doc.id === newDocId}
          $isMore={doc.id === moreLine}
          onClick={() => {
            dispatchToggle({ type: 'TOGGLE', id: doc.id });
          }}
        >
          {doc.children && doc.children.length > 0 && <KeyboardArrowDownIcon className="foldSign" />}
          <s.TitleWrapper>
            <s.DocTitle
              $onEdit={onEdit}
              onClick={(event) => {
                event.stopPropagation();
                navigate(
                  `/${groundId}/document/docs/${type === 'issue' ? (doc.step === 1 ? 'target' : doc.step === 2 ? 'check' : 'issue') : type}/${
                    doc.id
                  }`,
                );
              }}
            >
              {title}
            </s.DocTitle>
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
          </s.TitleWrapper>

          <MoreHorizIcon
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
              setOnEdit(true);
              editRef.current.style.display = 'block';
              editRef.current.focus();
            }}
          />
          <RemoveCircleIcon
            className="deleteDoc"
            onClick={(event) => {
              event.stopPropagation();
              deleteDocument(type, doc.id, doc.step);
            }}
          />
          <s.DocEdit
            ref={editRef}
            $onEdit={onEdit}
            value={title}
            onClick={(event) => event.stopPropagation()}
            onChange={(event) => {
              setTitle(event.target.value);
            }}
            onBlur={(event) => {
              if (event.target.value === '') setTitle(doc.title === '' ? '새 문서' : doc.title);
              else titleDocument(type, doc.id, doc.step, title);
              editRef.current.style.display = 'none';
              setOnEdit(false);
            }}
            onKeyPress={(event) => {
              if (event.key === 'Enter') {
                if (event.target.value === '') setTitle(doc.title === '' ? '새 문서' : doc.title);
                else titleDocument(type, doc.id, doc.step, title);
                event.target.blur();
              }
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
        getTree(type);
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
        getTree(type === 'request' || type === 'general' ? type : 'target');
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

  const titleDocument = (type, id, step, title) => {
    if (type === 'issue') {
      if (step === 1) {
        type = 'target';
      } else if (step === 2) {
        type = 'check';
      }
    }

    eetch
      .titleDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type, id, title })
      .then(() => {
        getTree(type === 'request' || type === 'general' ? type : 'target');
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
        getTree(type === 'request' || type === 'general' ? type : 'target');
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
            {requestTree && requestTree.length > 0 && requestTree.map((doc) => <RenderDocsTree key={doc.id} doc={doc} type="request" />)}
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
            {generalTree && generalTree.length > 0 && generalTree.map((doc) => <RenderDocsTree key={doc.id} doc={doc} type="general" />)}
            <s.TreeChild>
              <s.TreeName className="add-button" onClick={() => createRootDocmunet('general')}>
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
