import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import { setGround } from 'redux/actions/ground';
import { setMenu } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import * as s from 'markup/styles/components/common/Sidemenu';
const Sidemenu = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const groundId = useSelector((state) => state.ground.groundId);
  const [groundListToggle, setGroundListToggle] = useState(false);

  const groundList = [
    { groundId: 'test1', groundName: 'this is test1' },
    { groundId: 'test2', groundName: 'this is test2' },
    { groundId: 'test3', groundName: 'this is test3' },
    { groundId: 'test4', groundName: 'this is test4' },
  ];

  const groundButtonHandler = () => {
    setGroundListToggle(!groundListToggle);
  };

  const groundItemhandler = (value) => {
    dispatch(setGround(value));
    navigate(`/${value.groundId}`);
  };

  const logoutHandler = () => {
    dispatch(logoutUser());
    dispatch(setMenu());
    navigate(`/login`);
  };

  useEffect(() => {
    if (!menuToggle) setGroundListToggle(false);
  }, [menuToggle]);

  return (
    <s.SidemenuWrapper $menuToggle={menuToggle}>
      <s.PositionWrapper>
        <s.MenuNav>
          <s.MenuCategory to={`/${groundId}/project`}>프로젝트</s.MenuCategory>
          <s.MenuChild to={`/${groundId}/project/chart`}>차트</s.MenuChild>
          <s.MenuChild to={`/${groundId}/project/log`}>로그</s.MenuChild>
          <s.MenuCategory to={`/${groundId}/document`}>문서</s.MenuCategory>
          <s.MenuChild to={`/${groundId}/document/issue`}>이슈</s.MenuChild>
          <s.MenuChild to={`/${groundId}/document/request`}>요청</s.MenuChild>
          <s.MenuChild to={`/${groundId}/document/explore`}>문서</s.MenuChild>
        </s.MenuNav>

        <s.GroundList $groundListToggle={groundListToggle}>
          {groundList.map(({ groundId, groundName }) => (
            <s.GroundItem
              key={groundId}
              onClick={() => {
                groundItemhandler({ groundId, groundName });
              }}
            >
              {groundName}
            </s.GroundItem>
          ))}
          <div onClick={groundButtonHandler}>open</div>
        </s.GroundList>
        <button type="button" onClick={logoutHandler}>
          logout
        </button>
      </s.PositionWrapper>
    </s.SidemenuWrapper>
  );
};

export default Sidemenu;
