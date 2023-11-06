import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { toggleMenu } from 'redux/actions/menu';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { updateUser } from 'redux/actions/user';
import { logoutUser } from 'redux/actions/user';

import eetch from 'eetch/eetch';

import Message from 'markup/pages/components/common/Message';
import userStockImage from 'assets/userStockImage.webp';
import MenuIcon from '@mui/icons-material/Menu';

import ReviewsRoundedIcon from '@mui/icons-material/ReviewsRounded';
import MenuOpenIcon from '@mui/icons-material/MenuOpen';
import * as s from 'markup/styles/components/common/Topbar';
const Topbar = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const isLoggedIn = useSelector((state) => state.user.isLoggedIn);
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const messageToggle = useSelector((state) => state.menu.messageToggle);
  const selectedGround = useSelector((state) => state.ground.groundName);
  const [editToggle, setEditToggle] = useState(false);
  const [userInfo, setUserInfo] = useState({});

  useEffect(() => {
    if (user.isLoggedIn) {
      eetch
        .userInfo({ accessToken: user.accessToken, refreshToken: user.refreshToken })
        .then((res) => {
          setUserInfo(res.data);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });

      eetch
        .userGrounds({ accessToken: user.accessToken, refreshToken: user.refreshToken })
        .then((grounds) => {
          const groundsList = grounds.data.map((ground) => ground.groundDto.id);
          const groundsMap = grounds.data.map((ground) => ground.groundDto);
          dispatch(
            updateUser({
              groundsList,
              groundsMap,
            }),
          );
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
    }
  }, [dispatch, navigate, user.accessToken, user.isLoggedIn, user.refreshToken]);

  return (
    <s.TopbarWrapper $isLoggedIn={isLoggedIn}>
      <s.PositionWrapper>
        <s.SelectedGround onClick={() => dispatch(toggleMenu())}>
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
