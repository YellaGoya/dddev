import React, { useEffect, useRef } from 'react';
import ReactQuill from 'react-quill';
import Quill from 'quill';
import * as Y from 'yjs';
import { QuillBinding } from 'y-quill';
import MarkdownShortcuts from 'quill-markdown-shortcuts';
import { WebsocketProvider } from 'y-websocket';
import { useParams } from 'react-router-dom';
import 'quill/dist/quill.snow.css';

import * as s from 'markup/styles/components/document/Write';
const Write = () => {
  const quillRef = useRef(null);
  const params = useParams();

  const [docType, setDocType] = React.useState(2);

  const getRandomPastelColor = () => {
    const r = Math.floor(Math.random() * 127 + 128);
    const g = Math.floor(Math.random() * 127 + 128);
    const b = Math.floor(Math.random() * 127 + 128);
    return `rgb(${r}, ${g}, ${b})`;
  };

  const modules = {
    toolbar: [
      [{ header: [1, 2, 3, 4, false] }],
      ['bold', 'italic', 'underline'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      ['blockquote', 'code-block'],
    ],
    markdownShortcuts: {},
  };
  Quill.register('modules/markdownShortcuts', MarkdownShortcuts);

  useEffect(() => {
    const roomName = `${params.docId}`;
    const doc = new Y.Doc();
    const type = doc.getText('quill');

    const wsProvider = new WebsocketProvider('ws://34.22.71.151:6001', roomName, doc);

    const editor = quillRef.current.getEditor();
    editor.format('font', 'Pretendard');
    const { container } = editor;

    const updateCursor = (react = false) => {
      const range = editor.getSelection();
      if (range) {
        const [blot] = editor.getLeaf(range.index);

        const { parentNode } = blot.domNode.parentNode;
        const tagIndex = Array.from(parentNode.children).indexOf(blot.domNode.parentNode);
        const blotStart = tagIndex * 10000;
        const blotEnd = blotStart + blot.length();

        if (checkRange(blotStart, blotEnd)) return;

        console.log('pass');

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
      console.log('hello');

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

    editor.on('selection-change', updateCursor);
    editor.on('text-change', (delta, oldDelta, source) => {
      if (source === 'user') {
        updateCursor();

        // delta.ops.forEach((op) => {
        //   if (op.insert && typeof op.insert === 'string' && op.insert === '\n') {
        //     console.log('새로운 태그가 생성되었습니다:', op.insert);
        //   }
        // });
      }
    });

    editor.container.onclick = () => {
      editor.enable();
    };

    const lastInputTimes = {};

    wsProvider.awareness.on('update', ({ added, updated, removed }) => {
      const users = wsProvider.awareness.getStates();

      added.concat(updated).forEach((id) => {
        const { user } = users.get(id);
        if (user && user.cursor !== null) {
          lastInputTimes[id] = Date.now();
          let cursorElement = document.getElementById(`cursor-${id}`);

          // if (id === wsProvider.awareness.clientID) {
          //   if (user.range) {
          //     const ownUser = wsProvider.awareness.getLocalState().user;
          //     if (ownUser && ownUser.range) {
          //       checkRange();
          //     }
          //   }

          //   return;
          // }

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
          cursorElement.style.top = cursorPosition.top + 'px';
          if (user.react) return;
          cursorElement.style.left = cursorPosition.left + 'px';
          updateCursor(true);
        }
      });

      removed.forEach((id) => {
        delete lastInputTimes[id];
        const cursorElement = document.getElementById(`cursor-${id}`);
        if (cursorElement !== null) {
          cursorElement.remove();
        }
      });
    });

    setInterval(() => {
      const now = Date.now();

      Object.keys(lastInputTimes).forEach((id) => {
        if (now - lastInputTimes[id] > 7000) {
          const cursorElement = document.getElementById(`cursor-${id}`);
          if (cursorElement !== null) {
            cursorElement.remove();
          }
        }
      });
    }, 1000);

    const binding = new QuillBinding(type, editor, wsProvider.awareness);

    return () => {
      binding.destroy();
      wsProvider.disconnect();

      console.log('언마운트. 여기서 db에 저장');
    };
  }, [params.docId]);

  return (
    <>
      <h1>docType : {docType}</h1>
      <button type="button" onClick={() => setDocType(0)}>
        이슈
      </button>
      <button type="button" onClick={() => setDocType(1)}>
        요청
      </button>
      <button type="button" onClick={() => setDocType(2)}>
        일반
      </button>

      <s.EditorWrapper>
        <ReactQuill ref={quillRef} modules={modules} placeholder="내용을 입력해주세요." />
      </s.EditorWrapper>

      <div
        onClick={() => {
          quillRef.current.getEditor().root.innerHTML = '<p>초기화된텍스트</p>';
        }}
      >
        button?
      </div>
    </>
  );
};

export default Write;
