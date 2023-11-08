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

import MarkdownShortcuts from 'quill-markdown-shortcuts';
import hljs from 'highlight.js';

import AddIcon from '@mui/icons-material/Add';
import 'quill/dist/quill.snow.css';
import 'highlight.js/styles/github-dark.css';
import * as s from 'reacts/styles/components/document/Write';
const Write = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const quillRef = useRef(null);
  const params = useParams();
  const [lastEditor, setLastEditor] = useState(null);
  const [title, setTitle] = useState('');

  const getRandomPastelColor = () => {
    const r = Math.floor(Math.random() * 127 + 128);
    const g = Math.floor(Math.random() * 127 + 128);
    const b = Math.floor(Math.random() * 127 + 128);
    return `rgb(${r}, ${g}, ${b})`;
  };

  const editDocument = () => {
    eetch
      .editDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        type: params.type,
        id: params.docId,
        title,
        content: quillRef.current.getEditor().root.innerHTML,
      })
      .then((res) => {
        console.log(res);
      });
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
    console.log(lastEditor);
  }, [lastEditor]);

  useEffect(() => {
    console.log(quillRef.current);
  }, [quillRef.current]);

  useEffect(() => {
    const roomName = `${params.docId || 'test'}`;
    const doc = new Y.Doc();
    const type = doc.getText('quill');

    const wsProvider = new WebsocketProvider('ws://34.64.243.47:6001', roomName, doc);
    const editor = quillRef.current.getEditor();
    editor.format('font', 'Pretendard');
    const { container } = editor;

    const ping = (noise) => {
      wsProvider.awareness.setLocalStateField('user', {
        ping: true,
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
          setTitle(res.data.title);
          setDoc({ docTitle: res.data.title });
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

    editor.on('selection-change', () => {
      updateCursor();
      setLastEditor(wsProvider.awareness.clientID);
    });
    editor.on('text-change', (delta, olddelta, source) => {
      if (source === 'user') {
        updateCursor();
        setLastEditor(wsProvider.awareness.clientID);
      }
    });

    editor.container.onclick = () => {
      editor.enable();
    };

    wsProvider.on('status', (event) => {
      if (event.status === 'connected') {
        ping(0);
      }
    });

    wsProvider.awareness.on('update', ({ added, updated, removed }) => {
      const users = wsProvider.awareness.getStates();

      const intervalId = setInterval(() => {
        const editorClassName = editor.container.firstChild.className;

        if (editorClassName !== 'ql-editor ql-blank') {
          added.concat(updated).forEach((id) => {
            const userState = users.get(id);

            if (userState && userState.user && userState.user.cursor !== null) {
              const { user } = userState;
              if (user.ping) {
                if (user.noise < 3) ping(user.noise + 1);
                if (user.noise === 3) initRoom([...users.keys()].length === 1);

                return;
              }

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
                setLastEditor(id);
              }

              cursorElement.style.top = cursorPosition.top + 'px';
            }
          });

          removed.forEach((id) => {
            const cursorElement = document.getElementById(`cursor-${id}`);
            if (cursorElement !== null) {
              cursorElement.remove();
            }
          });

          clearInterval(intervalId);
        }
      }, 100);
    });

    const binding = new QuillBinding(type, editor, wsProvider.awareness);

    return () => {
      binding.destroy();
      wsProvider.disconnect();
    };
  }, [params.docId]);

  return (
    <>
      {/* <h1>docType : {docType}</h1>
      <button type="button" onClick={() => setDocType(0)}>
        이슈
      </button>
      <button type="button" onClick={() => setDocType(1)}>
        요청
      </button>
      <button type="button" onClick={() => setDocType(2)}>
        일반
      </button> */}

      <s.EditorWrapper>
        <ReactQuill ref={quillRef} modules={modules} placeholder="내용을 입력해주세요." />
      </s.EditorWrapper>

      <s.InsertBottom onClick={insertBottom}>
        <AddIcon />
      </s.InsertBottom>

      <div
        onClick={() => {
          // quillRef.current.getEditor().root.innerHTML = '<p>초기화된텍스트</p>';
          editDocument();
        }}
      >
        button?
      </div>
    </>
  );
};

export default Write;
