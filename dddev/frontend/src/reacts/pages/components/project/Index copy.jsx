import { useEffect, useReducer, useRef, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

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

import * as s from 'reacts/styles/components/project/Index';
const Index = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const params = useParams();
  const user = useSelector((state) => state.user);
  const groundId = useSelector((state) => state.user.lastGround);
  const [lastSprint, setLastSprint] = useState(null);
  const [toggleDocs, dispatchToggle] = useReducer(toggleReducer, {});
  const [newDocId, setNewDocId] = useState(null);
  const [moreLine, setMoreLine] = useState(false);

  const [sprintTree, setSprintTree] = useState([]);
  const [noSprintTree, setNoSprintTree] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const issues = await getTree();
      setInitialToggleStates(issues);
    };

    fetchData();

    listSprint();
  }, [params.groundId]);

  useEffect(() => {
    console.log(lastSprint);
  }, [lastSprint]);

  const setInitialToggleStates = (tree) => {
    if (tree)
      tree.forEach((node) => {
        dispatchToggle({ type: 'SET', id: node.id, value: node.step === 2 });
        if (node.children) {
          setInitialToggleStates(node.children);
        }
      });
  };

  const getTree = () => {
    return eetch
      .treeDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type: 'target' })
      .then((res) => {
        setSprintTree(res.data);
        setNoSprintTree(res.data);
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

  const createSprint = () => {
    console.log('create!');
    eetch
      .createSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
      .then((res) => console.log(res.data))
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const listSprint = () => {
    eetch
      .listSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
      .then((res) => {
        setLastSprint(res.data[res.data.length - 1].status === 0 && res.data[res.data.length - 1]);
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
        getTree();
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

  const createRootDocmunet = (sprintId) => {
    eetch
      .createDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type: 'target', sprintId })
      .then((res) => {
        getTree();
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

  const RenderDocsTree = ({ doc, type }) => {
    const toggle = toggleDocs[doc.id] ?? true;
    const [title, setTitle] = useState(doc.title === '' ? '새 문서' : doc.title || '');
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

  return (
    <s.SprintWrapper>
      <div onClick={createSprint}>스프린트 생성</div>
      <div onClick={listSprint}>스프린트 리스트</div>
      <div>
        <s.TreeParent>
          {sprintTree && sprintTree.length > 0 && sprintTree.map((doc) => <RenderDocsTree key={doc.id} doc={doc} type="target" />)}
          <s.TreeChild>
            <s.TreeName className="add-button" onClick={() => createRootDocmunet(lastSprint.id)}>
              <AddRoundedIcon /> 새 문서 작성
            </s.TreeName>
          </s.TreeChild>
        </s.TreeParent>
      </div>
      <div>
        <s.TreeParent>
          {noSprintTree && noSprintTree.length > 0 && noSprintTree.map((doc) => <RenderDocsTree key={doc.id} doc={doc} type="target" />)}
          <s.TreeChild>
            <s.TreeName className="add-button" onClick={() => createRootDocmunet()}>
              <AddRoundedIcon /> 새 문서 작성
            </s.TreeName>
          </s.TreeChild>
        </s.TreeParent>
      </div>
    </s.SprintWrapper>
  );
};

export default Index;
