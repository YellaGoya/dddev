import React, { useEffect, useRef } from 'react';
import * as Y from 'yjs';
import { WebrtcProvider } from 'y-webrtc';
import { QuillBinding } from 'y-quill';
import Quill from 'quill';
import { useParams } from 'react-router-dom';
import 'quill/dist/quill.snow.css';

const Editor = () => {
  const editorRef = useRef(null);
  const params = useParams();

  useEffect(() => {
    let provider;

    if (editorRef.current) {
      const ydoc = new Y.Doc();
      const roomName = `${params.docId}`;
      provider = new WebrtcProvider(roomName, ydoc);

      provider.on('status', (event) => {
        console.log(event.status);
      });

      const { awareness } = provider;

      awareness.on('change', () => {
        console.log(awareness.getStates());
      });

      const quill = new Quill(editorRef.current, {
        theme: 'snow',
        modules: {
          toolbar: [[{ header: [1, 2, false] }], ['bold', 'italic', 'underline'], ['image', 'code-block']],
        },
        placeholder: 'Write something...',
        readOnly: false,
      });

      const type = ydoc.getText('quills');

      const binding = new QuillBinding(type, quill);
      console.log(binding);
    }

    return () => {
      if (provider) {
        provider.destroy();
      }
    };
  }, [params]);

  return <div ref={editorRef} />;
};

export default Editor;
