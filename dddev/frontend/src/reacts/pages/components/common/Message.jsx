import EditProfile from 'reacts/pages/components/user/EditProfile';

import * as s from 'reacts/styles/components/common/Message';
const Message = ({ messageToggle, editToggle, setEditToggle, userInfo, setUserInfo }) => {
  return (
    <s.Message $toggle={messageToggle}>
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
    </s.Message>
  );
};

export default Message;
