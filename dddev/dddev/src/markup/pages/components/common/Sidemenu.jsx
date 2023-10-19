import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { setGround } from 'redux/actions/ground';

import * as s from 'markup/styles/components/common/Sidemenu';
const Sidemenu = () => {
  const dispatch = useDispatch();
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const [groundListToggle, setGroundListToggle] = useState(false);

  const groundList = ['test ground name 1', 'test ground name 2', 'test ground name 3', 'test ground name 4', 'test ground name 5'];

  const groundButtonHandler = () => {
    setGroundListToggle(!groundListToggle);
  };

  const groundItemhandler = (value) => {
    dispatch(setGround(value));
  };

  useEffect(() => {
    if (menuToggle) setGroundListToggle(false);
  }, [menuToggle]);

  return (
    <s.SidemenuWrapper $menuToggle={menuToggle}>
      <s.PositionWrapper>
        <div onClick={groundButtonHandler}>open</div>

        <s.MenuNav>
          <s.MenuCategory to="/project">프로젝트</s.MenuCategory>
          <s.MenuChild to="/project/chart">차트</s.MenuChild>
          <s.MenuChild to="/project/log">로그</s.MenuChild>
          <s.MenuCategory to="/document">문서</s.MenuCategory>
          <s.MenuChild to="/document/issue">이슈</s.MenuChild>
          <s.MenuChild to="/document/request">요청</s.MenuChild>
          <s.MenuChild to="/document/explore">문서</s.MenuChild>
        </s.MenuNav>

        <s.GroundList $groundListToggle={groundListToggle}>
          {groundList.map((name) => (
            <s.GroundItem
              key={name}
              onClick={() => {
                groundItemhandler(name);
              }}
            >
              {name}
            </s.GroundItem>
          ))}
        </s.GroundList>
      </s.PositionWrapper>
    </s.SidemenuWrapper>
  );
};

export default Sidemenu;
