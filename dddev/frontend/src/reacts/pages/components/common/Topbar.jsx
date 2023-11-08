import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { toggleMenu } from 'redux/actions/menu';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { updateUser } from 'redux/actions/user';
import { logoutUser } from 'redux/actions/user';

import eetch from 'eetch/eetch';

import Message from 'reacts/pages/components/common/Message';
import userStockImage from 'assets/userStockImage.webp';
import MenuIcon from '@mui/icons-material/Menu';

import ReviewsRoundedIcon from '@mui/icons-material/ReviewsRounded';
import MenuOpenIcon from '@mui/icons-material/MenuOpen';
import * as s from 'reacts/styles/components/common/Topbar';
const Topbar = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const isLoggedIn = useSelector((state) => state.user.isLoggedIn);
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const messageToggle = useSelector((state) => state.menu.messageToggle);
  const selectedGround = useSelector((state) => state.ground.groundName);
  const docTitle = useSelector((state) => state.doc.docTitle);
  const [editToggle, setEditToggle] = useState(false);
  const [userInfo, setUserInfo] = useState({});
  const [pathState, setPathState] = useState([]);
  const paths = window.location.pathname.split('/');

  useEffect(() => {
    const pathNames = [];

    if (paths[2])
      switch (paths[2]) {
        case 'project':
          pathNames.push(`/ 프로젝트`);
          break;
        case 'document':
          pathNames.push(`/ 문서`);
          break;
        case 'groundinit':
          pathNames.push('그라운드 생성');
          break;
        default:
          break;
      }

    if (paths[3])
      switch (paths[3]) {
        case 'chart':
          pathNames.push(`/ 차트`);
          break;
        case 'log':
          pathNames.push(`/ 로그`);
          break;
        case 'issue':
          pathNames.push(`/ 이슈`);
          break;
        case 'request':
          pathNames.push(`/ 요청`);
          break;
        case 'general':
          pathNames.push(`/ 일반`);
          break;
        default:
          break;
      }

    setPathState(pathNames);
  }, [paths]);

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
      <s.PositionWrapper $isGround={Boolean(selectedGround)}>
        <s.SelectedGround onClick={() => dispatch(toggleMenu())}>
          {menuToggle ? <MenuOpenIcon /> : <MenuIcon />}
          {!selectedGround || selectedGround.length === 0 ? '' : selectedGround}
        </s.SelectedGround>
        {paths[2] ? (
          <s.PathsText>
            <span>{pathState[0]}</span>
          </s.PathsText>
        ) : null}
        {paths[3] ? (
          <s.PathsText>
            <span>{pathState[1] || docTitle}</span>
          </s.PathsText>
        ) : null}
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
