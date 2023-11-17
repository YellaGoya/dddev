import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import EditProfile from 'reacts/pages/components/user/EditProfile';
import UserAlertData from 'reacts/pages/components/alert/UserAlertData';

import ManageAccountsRoundedIcon from '@mui/icons-material/ManageAccountsRounded';
import FlareRoundedIcon from '@mui/icons-material/FlareRounded';
import NightsStayRoundedIcon from '@mui/icons-material/NightsStayRounded';

import { updateUser } from 'redux/actions/user';
import * as s from 'reacts/styles/components/common/Message';
const Message = ({ messageToggle, editToggle, setEditToggle, userInfo, setUserInfo }) => {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);

  useEffect(() => {
    const themeLink = document.getElementById('theme-dddev');
    if (user.mode) themeLink.href = '/variables-lite.css';
    else themeLink.href = '/variables.css';
  }, [user.mode]);

  return (
    <s.Message $toggle={messageToggle}>
      <h1>구독 알림</h1>
      <s.LiteMode
        $toggle={user.mode}
        onClick={() => {
          dispatch(updateUser({ mode: true }));
        }}
      >
        <FlareRoundedIcon />
      </s.LiteMode>
      <s.DarkMode
        $toggle={!user.mode}
        onClick={() => {
          dispatch(updateUser({ mode: false }));
        }}
      >
        <NightsStayRoundedIcon />
      </s.DarkMode>
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
