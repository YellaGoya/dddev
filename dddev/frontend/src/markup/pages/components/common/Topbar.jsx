import { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setMenu } from 'redux/actions/menu';

import userStockImage from 'assets/userStockImage.webp';

import MenuIcon from '@mui/icons-material/Menu';
import MenuOpenIcon from '@mui/icons-material/MenuOpen';
import * as s from 'markup/styles/components/common/Topbar';
const Topbar = () => {
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state) => state.user.isLoggedIn);
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const selectedGround = useSelector((state) => state.ground.groundName);

  const [messageToggle, setMessageToggle] = useState(false);

  return (
    <s.TopbarWrapper $isLoggedIn={isLoggedIn}>
      <s.PositionWrapper>
        <s.SelectedGround onClick={() => dispatch(setMenu())}>
          {menuToggle ? <MenuOpenIcon /> : <MenuIcon />}
          {!selectedGround || selectedGround.length === 0 ? '없음' : selectedGround}
        </s.SelectedGround>
      </s.PositionWrapper>
      <s.ProfileImage src={userStockImage} onClick={() => setMessageToggle(!messageToggle)} />
      <s.MessageBoxModal $toggle={messageToggle}>
        <ul>
          <li>sda</li>
          <li>sda</li>
          <li>sda</li>
          <li>sda</li>
          <li>sda</li>
          <li>sda</li>
          <li>sda</li>
          <li>sda</li>
          <li>sda</li>
          <li>sda</li>
        </ul>
      </s.MessageBoxModal>
    </s.TopbarWrapper>
  );
};

export default Topbar;
