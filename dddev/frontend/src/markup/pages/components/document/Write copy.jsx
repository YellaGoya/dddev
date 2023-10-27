import React, { useEffect, useRef } from 'react';
import * as Y from 'yjs';
import { WebrtcProvider } from 'y-webrtc';
import { QuillBinding } from 'y-quill';
import Quill from 'quill';
// import { useParams } from 'react-router-dom';
import 'quill/dist/quill.snow.css';

const Editor = () => {
  const editorRef = useRef(null);
  // const params = useParams();

  useEffect(() => {
    let provider;

    if (editorRef.current) {
      const ydoc = new Y.Doc();
      // const roomName = `${params.docId}`;
      provider = new WebrtcProvider('test123331231231234677899', ydoc, {
        rtcConfiguration: {
          // RTC Configuration
          iceServers: [
            {
              urls: 'stun:www.dddev.co.kr:6001',
            },
            {
              urls: 'turn:www.dddev.co.kr:6001',
              username: 'ssafydddev',
              credential: 'd103d103',
            },
          ],
        },
      });

      console.log(provider);

      provider.on('status', (event) => {
        console.log(event.status);
      });

      // Awareness instance
      const { awareness } = provider;

      // Listen for changes
      awareness.on('change', ({ added, updated, removed }) => {
        console.log('Awareness change detected.');

        // Log added clients
        for (const clientId of added) {
          console.log(`Client ${clientId} was added.`);
        }

        // Log updated clients
        for (const clientId of updated) {
          console.log(`Client ${clientId} was updated.`);
        }

        // Log removed clients
        for (const clientId of removed) {
          console.log(`Client ${clientId} was removed.`);
        }
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
  }, []);

  return <div ref={editorRef} />;
};

export default Editor;
