import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import eetch from 'eetch/eetch';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';
import { updateUser } from 'redux/actions/user';

import EditSettings from 'reacts/pages/components/user/EditSettings';

import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import PlaylistAddCheckIcon from '@mui/icons-material/PlaylistAddCheck';
import AutoAwesomeRoundedIcon from '@mui/icons-material/AutoAwesomeRounded';
import BarChartRoundedIcon from '@mui/icons-material/BarChartRounded';
import FreeBreakfastRoundedIcon from '@mui/icons-material/FreeBreakfastRounded';
import FolderCopyRoundedIcon from '@mui/icons-material/FolderCopyRounded';
import ForumRoundedIcon from '@mui/icons-material/ForumRounded';

import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';

import * as s from 'reacts/styles/components/common/Sidemenu';
const Sidemenu = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const groundId = useSelector((state) => state.user.lastGround);
  const groundsMap = useSelector((state) => state.user.groundsMap);
  const [groundListToggle, setGroundListToggle] = useState(false);
  const [settingToggle, setSettingToggle] = useState(false);
  const [generals, setGenerals] = useState([]);

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

  useEffect(() => {
    eetch
      .treeDocument({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId, type: 'general' })
      .then((res) => {
        setGenerals(res.data);
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  }, [groundId]);

  const GeneralItems = ({ doc }) => {
    const [isToggle, setIsToggle] = useState(false);
    return (
      <s.GeneralItem>
        <s.GeneralName onClick={() => setIsToggle(!isToggle)}>
          {doc.children && doc.children.length > 0 && <KeyboardArrowDownIcon className="foldSign" />}
          <p
            onClick={(event) => {
              event.stopPropagation();
              navigate(`/${groundId}/document/find/docs/general/${doc.id}`);
            }}
          >
            {doc.title === '' ? '새 문서' : doc.title}
          </p>
        </s.GeneralName>

        {doc.children && doc.children.length > 0 && (
          <s.childWrapper $isToggle={isToggle}>
            <s.GeneralList>
              {doc.children.map((child) => {
                return <GeneralItems key={child.id} doc={child} />;
              })}
            </s.GeneralList>
          </s.childWrapper>
        )}
      </s.GeneralItem>
    );
  };

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
          <s.MenuCategory to={`/${groundId}/document/find`}>
            <FolderCopyRoundedIcon />
            문서
          </s.MenuCategory>
          <s.MenuChild to={`/${groundId}/document/request`}>
            <ForumRoundedIcon />
            요청
          </s.MenuChild>
          {/* <s.MenuChild to={`/${groundId}/document/issue`}>
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
          </s.MenuChild> */}
        </s.MenuNav>

        <s.GroundList $groundListToggle={groundListToggle}>
          {groundsMap &&
            groundsMap.map(({ id, name }) => (
              <s.GroundItem
                key={id}
                onClick={() => {
                  groundButtonHandler();
                  groundItemhandler(id);
                }}
              >
                {name}
              </s.GroundItem>
            ))}
          <s.GroundItem
            className="addGround"
            onClick={() => {
              groundButtonHandler();
              navigate('/newground');
            }}
          >
            + 새로운 그라운드
          </s.GroundItem>
          <div />
        </s.GroundList>
        <s.DivLine />
        <s.TreeWrapper>
          <s.GeneralList>
            {generals.map((doc) => {
              return <GeneralItems key={doc.id} doc={doc} />;
            })}
          </s.GeneralList>
        </s.TreeWrapper>
        <EditSettings toggle={settingToggle} setToggle={setSettingToggle} />
      </s.PositionWrapper>
    </s.SidemenuWrapper>
  );
};

export default Sidemenu;
