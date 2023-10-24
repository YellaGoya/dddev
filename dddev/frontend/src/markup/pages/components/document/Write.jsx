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
const modules = {
  toolbar: [
    [{ header: [1, 2, 3, 4, false] }],
    [{ color: [] }, { background: [] }],
    ['bold', 'italic', 'underline', 'strike'],
    [{ list: 'ordered' }, { list: 'bullet' }],
    ['blockquote', 'code-block'],
    [{ script: 'sub' }, { script: 'super' }],
    [{ align: [] }],
  ],
  markdownShortcuts: {},
};
Quill.register('modules/markdownShortcuts', MarkdownShortcuts);

const Write = () => {
  const quillRef = useRef(null);
  const params = useParams();

  const [docType, setDocType] = React.useState(2);

  useEffect(() => {
    const roomName = `${params.docId}`;
    const doc = new Y.Doc();
    const type = doc.getText('quill');

    const wsProvider = new WebsocketProvider('ws://www.dddev.co.kr:6001', roomName, doc);

    wsProvider.awareness.setLocalStateField('user', {
      name: 'User-' + Math.floor(Math.random() * 100),
      color: 'blue',
    });

    const binding = new QuillBinding(type, quillRef.current.getEditor(), wsProvider.awareness);

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
        <ReactQuill ref={quillRef} modules={modules} />
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
