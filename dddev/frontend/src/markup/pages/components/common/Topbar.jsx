import { useDispatch, useSelector } from 'react-redux';
import { setMenu } from 'redux/actions/menu';

import * as s from 'markup/styles/components/common/Topbar';

const Topbar = () => {
  const dispatch = useDispatch();
  const isLoggedIn = useSelector((state) => state.user.isLoggedIn);
  const test = useSelector((state) => state.user.accessToken);
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const selectedGround = useSelector((state) => state.ground.groundName);

  console.log(isLoggedIn, test);

  return (
    <s.TopbarWrapper $isLoggedIn={isLoggedIn}>
      <s.PositionWrapper>
        <s.SelectedGround onClick={() => dispatch(setMenu())}>
          {menuToggle ? '닫기' : '열기'}
          {!selectedGround || selectedGround.length === 0 ? '없음' : selectedGround}
        </s.SelectedGround>
      </s.PositionWrapper>
    </s.TopbarWrapper>
  );
};

export default Topbar;
