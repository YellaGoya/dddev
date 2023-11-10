import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import eetch from 'eetch/eetch';

import { updateUser } from 'redux/actions/user';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import EditSettings from 'reacts/pages/components/user/EditSettings';

import AutoAwesomeRoundedIcon from '@mui/icons-material/AutoAwesomeRounded';
import BarChartRoundedIcon from '@mui/icons-material/BarChartRounded';
import FreeBreakfastRoundedIcon from '@mui/icons-material/FreeBreakfastRounded';
import FolderCopyRoundedIcon from '@mui/icons-material/FolderCopyRounded';
import FlagRoundedIcon from '@mui/icons-material/FlagRounded';
import ForumRoundedIcon from '@mui/icons-material/ForumRounded';
import AssignmentRoundedIcon from '@mui/icons-material/AssignmentRounded';

import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import MeetingRoomRoundedIcon from '@mui/icons-material/MeetingRoomRounded';

import * as s from 'reacts/styles/components/common/Sidemenu';
const Sidemenu = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const groundId = useSelector((state) => state.user.lastGround);
  const groundsMap = useSelector((state) => state.user.groundsMap);
  const [groundInfo, setGroundInfo] = useState({});
  const [groundListToggle, setGroundListToggle] = useState(false);
  const [settingToggle, setSettingToggle] = useState(false);

  const groundButtonHandler = () => {
    setGroundListToggle(!groundListToggle);
  };

  const groundItemhandler = (value) => {
    dispatch(updateUser(value));
    navigate(`/${value}`);
  };

  const logoutHandler = () => {
    dispatch(logoutUser());
    dispatch(setMenu(false));
    dispatch(setMessage(false));
    navigate(`/login`);
  };

  useEffect(() => {
    if (!menuToggle) setGroundListToggle(false);
  }, [menuToggle]);

  useEffect(() => {
    if (user.isLoggedIn && user.lastGround)
      eetch
        .getGround({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: user.lastGround })
        .then((res) => {
          setGroundInfo({ name: res.data.name, focusTime: res.data.focusTime, activeTime: res.data.activeTime });
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
  }, [user.accessToken, groundListToggle]);

  return (
    <s.SidemenuWrapper $menuToggle={menuToggle}>
      <s.PositionWrapper>
        <s.MenuNav>
          <s.MenuCategory to={`/${groundId}/project`}>
            <AutoAwesomeRoundedIcon />
            프로젝트
          </s.MenuCategory>
          <s.MenuChild to={`/${groundId}/project/chart`}>
            <BarChartRoundedIcon />
            차트
          </s.MenuChild>
          <s.MenuChild to={`/${groundId}/project/log`}>
            <FreeBreakfastRoundedIcon />
            로그
          </s.MenuChild>
          <s.MenuCategory to={`/${groundId}/document`}>
            <FolderCopyRoundedIcon />
            문서
          </s.MenuCategory>
          <s.MenuChild to={`/${groundId}/document/issue`}>
            <FlagRoundedIcon />
            이슈
          </s.MenuChild>
          <s.MenuChild to={`/${groundId}/document/request`}>
            <ForumRoundedIcon />
            요청
          </s.MenuChild>
          <s.MenuChild to={`/${groundId}/document/general`}>
            <AssignmentRoundedIcon />
            일반
          </s.MenuChild>
        </s.MenuNav>

        <s.GroundList $groundListToggle={groundListToggle}>
          {groundsMap &&
            groundsMap.map(({ id, name }) => (
              <s.GroundItem
                key={id}
                onClick={() => {
                  groundItemhandler(id);
                }}
              >
                {name}
              </s.GroundItem>
            ))}
          <s.GroundItem onClick={() => navigate('/newground')}>+ 새로운 그라운드</s.GroundItem>
          <div onClick={groundButtonHandler}>open</div>
        </s.GroundList>
        <s.BottomWrapper>
          <s.MenuButton type="button" onClick={() => setSettingToggle(true)}>
            <SettingsRoundedIcon />
            설정
          </s.MenuButton>
          <s.MenuButton type="button" onClick={logoutHandler}>
            <MeetingRoomRoundedIcon />
            로그아웃
          </s.MenuButton>
        </s.BottomWrapper>
        <EditSettings toggle={settingToggle} setToggle={setSettingToggle} groundInfo={groundInfo} setGroundInfo={setGroundInfo} />
      </s.PositionWrapper>
    </s.SidemenuWrapper>
  );
};

export default Sidemenu;
