import EditProfile from 'reacts/pages/components/user/EditProfile';
import UserAlertData from 'reacts/pages/components/alert/UserAlertData';

import ManageAccountsRoundedIcon from '@mui/icons-material/ManageAccountsRounded';
import * as s from 'reacts/styles/components/common/Message';
const Message = ({ messageToggle, editToggle, setEditToggle, userInfo, setUserInfo }) => {
  return (
    <s.Message $toggle={messageToggle}>
      <h1>구독 알림</h1>
      <span onClick={() => setEditToggle(!editToggle)}>
        <ManageAccountsRoundedIcon />
        계정 설정
      </span>
      <UserAlertData />
      {editToggle && <EditProfile toggle={editToggle} setToggle={setEditToggle} userInfo={userInfo} setUserInfo={setUserInfo} />}
    </s.Message>
  );
};

export default Message;
