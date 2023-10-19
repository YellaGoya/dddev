import { useDispatch, useSelector } from 'react-redux';
import { setMenu } from 'redux/actions/menu';

import * as s from 'markup/styles/components/common/Topbar';

const Topbar = () => {
  const dispatch = useDispatch();
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const selectedGround = useSelector((state) => state.ground.ground);

  const menuButtonhandler = () => {
    dispatch(setMenu());
  };

  return (
    <s.TopbarWrapper>
      <s.SelectedGround onClick={() => menuButtonhandler()}>
        {menuToggle ? '열기' : '닫기'}
        {selectedGround.length === 0 ? '없음' : selectedGround}
      </s.SelectedGround>
    </s.TopbarWrapper>
  );
};

export default Topbar;
