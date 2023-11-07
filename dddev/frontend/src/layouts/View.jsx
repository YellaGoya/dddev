import { useSelector, useDispatch } from 'react-redux';

import { ViewWrapper, Main } from 'reacts/styles/Global';
import Topbar from 'reacts/pages/components/common/Topbar';
import Sidemenu from 'reacts/pages/components/common/Sidemenu';

import { setMessage } from 'redux/actions/menu';

const View = ({ children }) => {
  const dispatch = useDispatch();

  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const messageToggle = useSelector((state) => state.menu.messageToggle);
  const isLoggedIn = useSelector((state) => state.user.isLoggedIn);

  return (
    <>
      <Topbar />
      <ViewWrapper
        $isLoggedIn={isLoggedIn}
        onClick={() => {
          if (messageToggle) dispatch(setMessage(false));
        }}
      >
        <Sidemenu />
        <Main $isLoggedIn={isLoggedIn} $menuToggle={menuToggle}>
          {children}
        </Main>
      </ViewWrapper>
    </>
  );
};

export default View;
