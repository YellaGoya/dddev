import { useEffect, useState, useRef, useMemo } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import ReactQuill from 'react-quill';
import Quill from 'quill';
import * as Y from 'yjs';
import { QuillBinding } from 'y-quill';
import { WebsocketProvider } from 'y-websocket';
import { useParams } from 'react-router-dom';
// import WriteOptions from 'reacts/pages/components/document/WriteOptions';

import { setDoc } from 'redux/actions/doc';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';
import eetch from 'eetch/eetch';

import MarkdownShortcuts from 'quill-markdown-shortcuts';
import hljs from 'highlight.js';

import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';
import DoneOutlineRoundedIcon from '@mui/icons-material/DoneOutlineRounded';
import SettingsEthernetRoundedIcon from '@mui/icons-material/SettingsEthernetRounded';
import AddIcon from '@mui/icons-material/Add';
import 'quill/dist/quill.snow.css';
import 'highlight.js/styles/github-dark.css';
import * as s from 'reacts/styles/components/document/Write';
const Write = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const [originalTitle, setOriginalTitle] = useState('');
  const [settingToggle, setSettingToggle] = useState(false);
  const [title, setTitle] = useState('');
  const [step, setStep] = useState(1);
  const titleRef = useRef(null);
  const settingTitleRef = useRef(null);
  const quillRef = useRef(null);
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

  useEffect(() => {
    titleResizeHandler();
  }, [title]);

  useEffect(() => {
    console.log(step);
  }, [step]);

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
        setStep(res.data.step);
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

  useEffect(() => {
    const roomName = `${params.docId || 'test'}`;
    const doc = new Y.Doc();
    const type = doc.getText('quill');

    const wsProvider = new WebsocketProvider('ws://34.64.243.47:6001', roomName, doc);
    const editor = quillRef.current.getEditor();
    editor.format('font', 'Pretendard');
    const { container } = editor;

    const intervalId = setInterval(() => {
      if (document.activeElement !== titleRef.current && document.activeElement !== settingTitleRef.current) initRoom(false);

      if (lastEditorRef.current === wsProvider.awareness.clientID) {
        editDocument();
      }
    }, 10000);

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
    <>
      <s.EditorWrapper>
        <textarea
          ref={titleRef}
          value={title}
          rows={1}
          onChange={(event) => {
            setTitle(event.target.value);
          }}
          onBlur={titleDocument}
        />
        <ReactQuill ref={quillRef} modules={modules} placeholder="내용을 입력해주세요." />
        {step === 1 ? (
          <s.SettingButton className="only-delete-button" onClick={() => deleteDocument()}>
            <RemoveCircleIcon />
          </s.SettingButton>
        ) : (
          <>
            <s.SettingButton onClick={() => setSettingToggle(true)}>
              <SettingsEthernetRoundedIcon />
            </s.SettingButton>
            <s.SettingButton className="delete-button" onClick={() => deleteDocument()}>
              <RemoveCircleIcon />
            </s.SettingButton>
          </>
        )}
        <s.SettingWrapper $toggle={settingToggle}>
          <s.SettingLabel>제목</s.SettingLabel>
          <s.SettingButton className="close-button" onClick={() => setSettingToggle(false)}>
            <DoneOutlineRoundedIcon />
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
        </s.SettingWrapper>
      </s.EditorWrapper>

      <s.InsertBottom onClick={insertBottom}>
        <AddIcon />
      </s.InsertBottom>
    </>
  );
};

export default Write;
