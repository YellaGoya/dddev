import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';

import eetch from 'eetch/eetch';

import Message from 'markup/pages/components/common/Message';
import userStockImage from 'assets/userStockImage.webp';
import MenuIcon from '@mui/icons-material/Menu';

import ReviewsRoundedIcon from '@mui/icons-material/ReviewsRounded';
import MenuOpenIcon from '@mui/icons-material/MenuOpen';
import * as s from 'markup/styles/components/common/Topbar';
const Topbar = () => {
  const dispatch = useDispatch();
  const user = useSelector((state) => state.user);
  const isLoggedIn = useSelector((state) => state.user.isLoggedIn);
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const messageToggle = useSelector((state) => state.menu.messageToggle);
  const selectedGround = useSelector((state) => state.ground.groundName);
  const [editToggle, setEditToggle] = useState(false);
  const [userInfo, setUserInfo] = useState({});

  useEffect(() => {
    if (user.isLoggedIn)
      eetch
        .userInfo({ Authorization: user.accessToken })
        .then((res) => {
          setUserInfo(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
  }, [editToggle, user.accessToken]);

  return (
    <s.TopbarWrapper $isLoggedIn={isLoggedIn}>
      <s.PositionWrapper>
        <s.SelectedGround onClick={() => dispatch(setMenu())}>
          {menuToggle ? <MenuOpenIcon /> : <MenuIcon />}
          {!selectedGround || selectedGround.length === 0 ? '없음' : selectedGround}
        </s.SelectedGround>
      </s.PositionWrapper>
      <s.PorfileButtonWrapper onClick={() => dispatch(setMessage(!messageToggle))}>
        <s.ProfileImage src={userInfo.profileDto ? `https://k9d103.p.ssafy.io/img/user/${userInfo.profileDto.fileName}` : userStockImage} />
        <ReviewsRoundedIcon />
      </s.PorfileButtonWrapper>
      <Message messageToggle={messageToggle} editToggle={editToggle} setEditToggle={setEditToggle} userInfo={userInfo} setUserInfo={setUserInfo} />
    </s.TopbarWrapper>
  );
};

export default Topbar;
