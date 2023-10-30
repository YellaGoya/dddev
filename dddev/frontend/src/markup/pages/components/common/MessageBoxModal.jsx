import { useState } from 'react';

import EditProfile from 'markup/pages/components/user/EditProfile';

import * as s from 'markup/styles/components/common/MessageBoxModal';
const MessageBoxModal = ({ toggle }) => {
  const [editToggle, setEditToggle] = useState(false);

  return (
    <s.MessageBoxModal $toggle={toggle}>
      <p onClick={() => setEditToggle(!editToggle)}>openEdit</p>
      <ul>
        <li>sda</li>
        <li>sda</li>
        <li>sda</li>
        <li>sda</li>
        <li>sda</li>
        <li>sda</li>
        <li>sda</li>
        <li>sda</li>
        <li>sda</li>
        <li>sda</li>
      </ul>
      <EditProfile toggle={editToggle} setToggle={setEditToggle} />
    </s.MessageBoxModal>
  );
};

export default MessageBoxModal;
