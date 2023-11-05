import EditProfile from 'markup/pages/components/user/EditProfile';

import * as s from 'markup/styles/components/common/MessageBoxModal';
const MessageBoxModal = ({ messageToggle, editToggle, setEditToggle, userInfo, setUserInfo }) => {
  return (
    <s.MessageBoxModal $toggle={messageToggle}>
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
      {editToggle && <EditProfile toggle={editToggle} setToggle={setEditToggle} userInfo={userInfo} setUserInfo={setUserInfo} />}
    </s.MessageBoxModal>
  );
};

export default MessageBoxModal;
