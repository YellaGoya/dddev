import React, { useEffect, useRef } from 'react';
import ReactQuill from 'react-quill';
import * as Y from 'yjs';
import { QuillBinding } from 'y-quill';
import { WebsocketProvider } from 'y-websocket';
import { useParams } from 'react-router-dom';
import 'quill/dist/quill.snow.css';

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
};

const Write = () => {
  const quillRef = useRef(null);
  const params = useParams();

  useEffect(() => {
    const roomName = `${params.docId}`;
    const doc = new Y.Doc();
    const type = doc.getText('quill');

    // create websocket provider and connect it with the shared document.
    const wsProvider = new WebsocketProvider('ws://www.dddev.co.kr:6001', roomName, doc);

    wsProvider.awareness.setLocalStateField('user', {
      name: 'User-' + Math.floor(Math.random() * 100),
      color: 'blue',
    });

    quillRef.current.getEditor().getContents();

    // create quill binding and connect it with the shared type.
    const binding = new QuillBinding(type, quillRef.current.getEditor(), wsProvider.awareness);

    return () => {
      // clean up on unmount.
      binding.destroy();
      wsProvider.disconnect();
    };
  }, [params.docId]);

  return (
    <>
      <ReactQuill ref={quillRef} modules={modules} />
      <div
        onClick={() => {
          console.log(quillRef.current.getEditor().root.innerHTML);
        }}
      >
        button?
      </div>
    </>
  );
};

export default Write;
