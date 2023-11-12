import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import { updateUser } from 'redux/actions/user';

import EditSettings from 'reacts/pages/components/user/EditSettings';

import PlaylistAddCheckIcon from '@mui/icons-material/PlaylistAddCheck';
import AutoAwesomeRoundedIcon from '@mui/icons-material/AutoAwesomeRounded';
import BarChartRoundedIcon from '@mui/icons-material/BarChartRounded';
import FreeBreakfastRoundedIcon from '@mui/icons-material/FreeBreakfastRounded';
import FolderCopyRoundedIcon from '@mui/icons-material/FolderCopyRounded';
import FlagRoundedIcon from '@mui/icons-material/FlagRounded';
import ForumRoundedIcon from '@mui/icons-material/ForumRounded';
import AssignmentRoundedIcon from '@mui/icons-material/AssignmentRounded';

import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';

import * as s from 'reacts/styles/components/common/Sidemenu';
const Sidemenu = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const groundId = useSelector((state) => state.user.lastGround);
  const groundsMap = useSelector((state) => state.user.groundsMap);
  const [groundListToggle, setGroundListToggle] = useState(false);
  const [settingToggle, setSettingToggle] = useState(false);

  const groundButtonHandler = () => {
    setGroundListToggle(!groundListToggle);
  };

  const groundItemhandler = (value) => {
    dispatch(updateUser(value));
    navigate(`/${value}`);
  };

  useEffect(() => {
    if (!menuToggle) setGroundListToggle(false);
  }, [menuToggle]);

  return (
    <s.SidemenuWrapper $menuToggle={menuToggle}>
      <s.PositionWrapper>
        <s.BottomWrapper>
          <s.MenuCategory onClick={groundButtonHandler}>
            <PlaylistAddCheckIcon />
            그라운드
          </s.MenuCategory>
          <s.MenuButton type="button" onClick={() => setSettingToggle(true)}>
            <SettingsRoundedIcon />
            설정
          </s.MenuButton>
        </s.BottomWrapper>
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
          <s.GroundItem className="addGround" onClick={() => navigate('/newground')}>
            + 새로운 그라운드
          </s.GroundItem>
          <div />
        </s.GroundList>
        <EditSettings toggle={settingToggle} setToggle={setSettingToggle} />
      </s.PositionWrapper>
    </s.SidemenuWrapper>
  );
};

export default Sidemenu;
