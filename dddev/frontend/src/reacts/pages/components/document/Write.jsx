import { useEffect, useState, useRef, useMemo } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import ReactQuill from 'react-quill';
import Quill from 'quill';
import * as Y from 'yjs';
import { QuillBinding } from 'y-quill';
import { WebsocketProvider } from 'y-websocket';
import { useParams } from 'react-router-dom';

import { setDoc } from 'redux/actions/doc';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';
import eetch from 'eetch/eetch';
import SelectTransParent from 'reacts/pages/components/common/SelectTransparent';
import SelectUser from 'reacts/pages/components/common/SelectUser';
import Input from 'reacts/pages/components/common/Input';

import MarkdownShortcuts from 'quill-markdown-shortcuts';
import hljs from 'highlight.js';

import userStockImage from 'assets/userStockImage.webp';
// import KeyboardDoubleArrowRightRoundedIcon from '@mui/icons-material/KeyboardDoubleArrowRightRounded';
import PublishRoundedIcon from '@mui/icons-material/PublishRounded';
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';
import SaveIcon from '@mui/icons-material/Save';
import SettingsEthernetRoundedIcon from '@mui/icons-material/SettingsEthernetRounded';
import AddIcon from '@mui/icons-material/Add';
import 'quill/dist/quill.snow.css';
import 'highlight.js/styles/github-dark.css';
import * as s from 'reacts/styles/components/document/Write';

const statusList = [
  { id: 0, name: '해야 할 일' },
  { id: 1, name: '진행 중' },
  { id: 2, name: '완료' },
];

const todoList = [
  { id: 0, name: '해야 할 일' },
  { id: 1, name: '진행 중' },
];

const onGoingList = [
  { id: 1, name: '진행 중' },
  { id: 2, name: '완료' },
];

const Write = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const [originalTitle, setOriginalTitle] = useState('');
  const [settingToggle, setSettingToggle] = useState(false);
  const [title, setTitle] = useState('');
  const [step, setStep] = useState(1);
  const [status, setStatus] = useState(null);
  const [receiver, setReceiver] = useState(null);
  const [sender, setSender] = useState(null);
  const [parents, setParents] = useState(null);
  const [parentId, setParentId] = useState(null);
  const [parentName, setParentName] = useState('');
  const [focusTime, setFocusTime] = useState(0);
  const [activeTime, setActiveTime] = useState(0);
  const [users, setUsers] = useState(null);
  const [comments, setComments] = useState([]);
  const titleRef = useRef(null);
  const settingTitleRef = useRef(null);
  const statusRef = useRef(null);
  const quillRef = useRef(null);
  const commentRef = useRef(null);
  const innerHtmlRef = useRef('');
  const lastEditorRef = useRef(null);
  const notInitiatedRef = useRef(true);
  const notDeleteRef = useRef(true);
  const params = useParams();

  const titleResizeHandler = () => {
    titleRef.current.style.height = 'auto';
    titleRef.current.style.height = Math.round(titleRef.current.scrollHeight / 43) * 43 + 10 + 'px';
    settingTitleRef.current.style.height = 'auto';
    settingTitleRef.current.style.height = titleRef.current.style.height;
  };

  const SubmitComment = () => {
    eetch
      .commentDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        type: params.type,
        id: params.docId,
        comment: commentRef.current.getEditor().root.innerHTML,
      })
      .then(() => {
        initRoom(false);
        commentRef.current.getEditor().root.innerHTML = '';
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

  const activeValid = (data, setMessage) => {
    if (focusTime > 0 && data > 0) {
      setMessage({ fail: true, text: '* 집중 시간과 동시에 입력할 수 없습니다.' });
      setActiveTime(0);
    } else {
      setMessage({ fail: false, text: '' });
      timeDocument();
    }
  };

  const focusValid = (data, setMessage) => {
    if (activeTime > 0 && data > 0) {
      setMessage({ fail: true, text: '* 연구 시간과 동시에 입력할 수 없습니다.' });
      setFocusTime(0);
    } else {
      setMessage({ fail: false, text: '' });
      timeDocument();
    }
  };

  const receiverChange = (item) => {
    eetch
      .receiverDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        id: params.docId,
        receiver: item.githubId,
      })
      .then(() => {
        setReceiver(item);
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

  const senderChange = (item) => {
    eetch
      .senderDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        id: params.docId,
        sender: item.githubId,
      })
      .then(() => {
        setSender(item);
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

  useEffect(() => {
    titleResizeHandler();
  }, [title]);

  useEffect(() => {
    if (parentId)
      eetch
        .detailDocument({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId: params.groundId,
          type: params.type === 'issue' ? 'check' : params.type === 'check' ? 'target' : params.type,
          id: parentId,
        })
        .then((res) => {
          setParentName(res.data.title === '' ? '새 문서' : res.data.title);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
  }, [parentId]);

  useEffect(() => {
    eetch
      .groundUsers({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
      .then((res) => {
        setUsers(res.data);
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  }, [params]);

  useEffect(() => {
    if (params && step > 1) {
      eetch
        .parentsList({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId: params.groundId,
          type: params.type === 'issue' ? 'check' : params.type === 'check' ? 'target' : params.type,
        })
        .then((res) => {
          const filtered = res.data.map((parent) => {
            return { id: parent.id, name: parent.title === '' ? '새 문서' : parent.title };
          });
          setParents(filtered);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
    }
  }, [step]);

  useEffect(() => {
    statusRef.current = status;
    if (quillRef.current) {
      if (status === 0) {
        quillRef.current.getEditor().enable();
      } else quillRef.current.getEditor().enable();
    }
  }, [status]);

  const titleDocument = () => {
    if (title === '') setTitle(originalTitle === '' ? '새 문서' : originalTitle);
    else
      eetch
        .titleDocument({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId: params.groundId,
          type: params.type,
          id: params.docId,
          title,
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

  const linkParent = (parentId) => {
    eetch
      .linkDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        type: params.type,
        id: params.docId,
        parentId,
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

  const deleteDocument = () => {
    notDeleteRef.current = false;
    eetch
      .deleteDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        type: params.type,
        id: params.docId,
      })
      .then(() => {
        navigate(-1);
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

  const initRoom = (needData) => {
    eetch
      .detailDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        type: params.type,
        id: params.docId,
      })
      .then((res) => {
        setComments(res.data.comments);
        setStatus(res.data.status);
        setStep(res.data.step);
        setParentId(res.data.parentId);
        setActiveTime(res.data.activeTime);
        setFocusTime(res.data.focusTime);
        setReceiver(res.data.receiveUser);
        setSender(res.data.sendUser);
        dispatch(setDoc({ docTitle: res.data.title }));
        setTitle(res.data.title === '' ? '새 문서' : res.data.title);
        setOriginalTitle(res.data.title === '' ? '새 문서' : res.data.title);
        if (needData) quillRef.current.getEditor().root.innerHTML = res.data.content;
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

  const editDocument = () => {
    if (notDeleteRef.current)
      eetch
        .editDocument({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId: params.groundId,
          type: params.type,
          id: params.docId,
          content: innerHtmlRef.current,
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

  const statusDocument = (status) => {
    eetch
      .statusDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        type: params.type,
        id: params.docId,
        status,
      })
      .then(() => {
        setStatus(status);
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

  const timeDocument = () => {
    eetch
      .timeDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        type: params.type,
        id: params.docId,
        focusTime,
        activeTime,
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

  const getRandomPastelColor = () => {
    const r = Math.floor(Math.random() * 127 + 128);
    const g = Math.floor(Math.random() * 127 + 128);
    const b = Math.floor(Math.random() * 127 + 128);
    return `rgb(${r}, ${g}, ${b})`;
  };

  const modules = useMemo(() => {
    return {
      toolbar: [
        [{ header: [1, 2, 3, 4, false] }],
        ['bold', 'italic', 'underline'],
        [{ list: 'ordered' }, { list: 'bullet' }],
        ['blockquote', 'code-block'],
      ],
      markdownShortcuts: {},
      syntax: {
        highlight: (text) => hljs.highlightAuto(text).value,
      },
      keyboard: true,
    };
  }, []);

  if (!Quill.imports['modules/markdownShortcuts']) {
    Quill.register('modules/markdownShortcuts', MarkdownShortcuts);
  }

  const insertBottom = () => {
    const editor = quillRef.current.getEditor();
    const length = editor.getLength();

    editor.insertText(length, '\n', Quill.sources.USER);
  };

  const CommentQuill = ({ data }) => {
    const commentsRef = useRef(null);

    useEffect(() => {
      commentsRef.current.getEditor().root.innerHTML = data.comment;
    }, []);

    return (
      <s.CommentsWrapper>
        <s.CommentAuthor>{data.author}</s.CommentAuthor>
        <ReactQuill ref={commentsRef} modules={modules} placeholder="추가 사항을 입력해주세요." />
      </s.CommentsWrapper>
    );
  };

  useEffect(() => {
    const roomName = `${params.docId || 'test'}`;
    const doc = new Y.Doc();
    const type = doc.getText('quill');

    const wsProvider = new WebsocketProvider('ws://34.64.243.47:6001', roomName, doc);
    const editor = quillRef.current.getEditor();
    editor.format('font', 'Pretendard');
    const { container } = editor;

    const intervalId = setInterval(() => {
      if (lastEditorRef.current === wsProvider.awareness.clientID && statusRef.current === 0) {
        if (document.activeElement !== titleRef.current && document.activeElement !== settingTitleRef.current) initRoom(false);
        editDocument();
      }
    }, 5000);

    const ping = (noise) => {
      wsProvider.awareness.setLocalStateField('pinged', {
        noise,
      });
    };

    const updateCursor = (react) => {
      const range = editor.getSelection();
      if (range) {
        const [blot] = editor.getLeaf(range.index);

        const { parentNode } = blot.domNode.parentNode;
        const tagIndex = Array.from(parentNode.children).indexOf(blot.domNode.parentNode);
        const blotStart = tagIndex * 10000;
        const blotEnd = blotStart + blot.length();

        if (checkRange(blotStart, blotEnd)) return;
        wsProvider.awareness.setLocalStateField('user', {
          name: `User-${Math.floor(Math.random() * 100)}`,
          color: getRandomPastelColor(),
          cursor: range.index,
          range: { start: blotStart, end: blotEnd },
          react,
        });
      } else {
        wsProvider.awareness.setLocalStateField('user', {
          name: `User-${Math.floor(Math.random() * 100)}`,
          color: getRandomPastelColor(),
          cursor: range,
          range: { start: -10000, end: -10000 },
          react,
        });
      }
    };

    const checkRange = (start, end) => {
      const users = wsProvider.awareness.getStates();

      for (const [id, { user }] of users) {
        if (id !== wsProvider.awareness.clientID && user && user.range) {
          if (start < user.range.end && end > user.range.start) {
            editor.setSelection(null);
            return true;
          }
        }
      }

      return false;
    };

    editor.on('selection-change', () => {
      updateCursor();
      lastEditorRef.current = wsProvider.awareness.clientID;
    });

    editor.on('text-change', (delta, olddelta, source) => {
      innerHtmlRef.current = quillRef.current.getEditor().root.innerHTML;
      if (source === 'user') {
        updateCursor();
        lastEditorRef.current = wsProvider.awareness.clientID;
      }
    });

    editor.container.onclick = () => {
      editor.enable();
    };

    wsProvider.on('status', (event) => {
      if (event.status === 'connected' && notInitiatedRef.current) {
        ping(0);
        initRoom(false);
      }
    });

    wsProvider.awareness.on('update', ({ added, updated, removed }) => {
      const users = wsProvider.awareness.getStates();

      added.concat(updated).forEach((id) => {
        const userState = users.get(id);
        if (userState && userState.pinged && notInitiatedRef.current) {
          const { pinged } = userState;
          if (pinged.noise < 3) ping(pinged.noise + 1);
          if (pinged.noise === 3) {
            ping(4);
            initRoom([...users.keys()].length === 1);
            notInitiatedRef.current = false;
          }
        }

        const wsCycle = setInterval(() => {
          const editorClassName = editor.container.firstChild.className;
          if (editorClassName !== 'ql-editor ql-blank') {
            if (userState && userState.user && userState.user.cursor !== null) {
              const { user } = userState;

              if (id === wsProvider.awareness.clientID) return;

              if (!user.react) updateCursor(true);

              let cursorElement = document.getElementById(`cursor-${id}`);
              if (cursorElement === null) {
                cursorElement = document.createElement('div');
                cursorElement.className = 'user-cursor';
                cursorElement.id = `cursor-${id}`;
                cursorElement.style.position = 'absolute';
                cursorElement.style.color = user.color;
                cursorElement.innerHTML = user.name;

                const cursorMark = document.createElement('div');
                cursorMark.className = 'user-cursor-mark';
                cursorElement.appendChild(cursorMark);
                container.appendChild(cursorElement);
              }

              const cursorPosition = editor.getBounds(user.cursor);
              if (!user.react) {
                cursorElement.style.left = cursorPosition.left + 'px';
                lastEditorRef.current = id;
              }

              cursorElement.style.top = cursorPosition.top + 'px';
            }

            clearInterval(wsCycle);
          }
        }, 100);
      });

      removed.forEach((id) => {
        const cursorElement = document.getElementById(`cursor-${id}`);
        if (cursorElement !== null) {
          cursorElement.remove();
        }
      });
    });

    const binding = new QuillBinding(type, editor, wsProvider.awareness);

    return () => {
      editDocument();
      clearInterval(intervalId);
      binding.destroy();
      wsProvider.disconnect();
    };
  }, [params.docId]);

  return (
    <s.EditorWrapper status={status}>
      <textarea
        ref={titleRef}
        value={title}
        rows={1}
        onChange={(event) => {
          setTitle(event.target.value);
        }}
        onBlur={titleDocument}
      />
      {(receiver || sender) && (
        <s.ChargeInfo>
          {sender && (
            <s.ReceiverInfo>
              <s.ProfileImage src={sender.profileDto ? `https://k9d103.p.ssafy.io/img/user/${sender.profileDto.fileName}` : userStockImage} />
              {sender.nickname}
            </s.ReceiverInfo>
          )}
        </s.ChargeInfo>
      )}
      <ReactQuill ref={quillRef} modules={modules} placeholder="내용을 입력해주세요." />
      {step === 1 ? (
        <s.SettingButton className="only-delete-button" onClick={() => deleteDocument()}>
          <RemoveCircleIcon />
        </s.SettingButton>
      ) : (
        <>
          <s.StatusText>{statusList[status] ? statusList[status].name : null}</s.StatusText>
          <s.SettingButton onClick={() => setSettingToggle(true)}>
            <SettingsEthernetRoundedIcon />
          </s.SettingButton>
          <s.SettingButton $available={status === 2} className="delete-button" onClick={() => deleteDocument()}>
            <RemoveCircleIcon />
          </s.SettingButton>
        </>
      )}
      <s.SettingWrapper $toggle={settingToggle}>
        <s.SettingLabel>제목</s.SettingLabel>
        <s.SettingButton
          className="close-button"
          onClick={() => {
            timeDocument();
            setSettingToggle(false);
          }}
        >
          <SaveIcon />
        </s.SettingButton>
        <textarea
          ref={settingTitleRef}
          value={title}
          rows={1}
          onChange={(event) => {
            setTitle(event.target.value);
          }}
          onBlur={titleDocument}
        />
        <s.DivLine />
        <s.ParentWrapper>
          <SelectTransParent label="부모 문서" list={parents} select={(item) => linkParent(item.id)} selected={parentName} />
        </s.ParentWrapper>

        {params.type === 'issue' ? (
          <>
            <s.DivLine />
            <s.AttributeWrapper>
              <SelectTransParent
                label="진행 상태"
                list={statusList}
                select={(item) => statusDocument(item.id)}
                selected={statusList[status] && statusList[status].name}
              />
              <Input type="number" label="집중 시간" data={focusTime} setData={setFocusTime} valid={focusValid} />
              <Input type="number" label="연구 시간" data={activeTime} setData={setActiveTime} valid={activeValid} />
            </s.AttributeWrapper>
          </>
        ) : params.type === 'request' && step === 2 ? (
          <>
            <s.DivLine />
            <s.RequestWrapper>
              {status === 0 && <s.RequestDescription>* 한 번 상태를 진행 중으로 변경하면 되돌릴 수 없습니다.</s.RequestDescription>}
              {status === 1 && <s.RequestDescription>* 완료 상태로 변경되면 아카이브 되어 삭제가 불가능합니다.</s.RequestDescription>}
              {status === 2 && <s.RequestDescription>* 본 요청 문서는 완료 후 그라운드에 아카이브 되었습니다.</s.RequestDescription>}
              <SelectTransParent
                label="진행 상태"
                list={status === 0 ? todoList : onGoingList}
                select={(item) => statusDocument(item.id)}
                selected={statusList[status].name}
                available={status !== 2}
              />
              <SelectUser label="요청 멤버" list={users} select={(item) => senderChange(item)} selected={sender} keyInsert="send_" />
              <SelectUser label="담당 멤버" list={users} select={(item) => receiverChange(item)} selected={receiver} keyInsert="receive_" />
            </s.RequestWrapper>
          </>
        ) : null}
      </s.SettingWrapper>
      {status === 0 && (
        <s.InsertBottom className="insert-button" status={status} onClick={insertBottom}>
          <AddIcon />
        </s.InsertBottom>
      )}

      {comments &&
        comments.length > 0 &&
        comments.map((comment) => (
          <div key={comment.id}>
            <CommentQuill data={comment} />
          </div>
        ))}

      {params.type === 'request' && status !== 0 && (
        <s.CommentWrapper>
          <ReactQuill ref={commentRef} modules={modules} placeholder="추가 사항을 입력해주세요." />
          <s.InsertBottom className="insert-button" status={status} onClick={insertBottom}>
            <AddIcon />
          </s.InsertBottom>
          <PublishRoundedIcon onClick={SubmitComment} />
        </s.CommentWrapper>
      )}
    </s.EditorWrapper>
  );
};

export default Write;
