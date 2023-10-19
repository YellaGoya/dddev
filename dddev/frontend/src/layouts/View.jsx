import { useSelector } from 'react-redux';

import { ViewWrapper, Main } from 'markup/styles/Global';

import Topbar from 'markup/pages/components/common/Topbar';
import Sidemenu from 'markup/pages/components/common/Sidemenu';

const View = ({ children }) => {
  const menuToggle = useSelector((state) => state.menu.menuToggle);
  const isLoggedIn = useSelector((state) => state.user.isLoggedIn);

  return (
    <>
      <Topbar />
      <ViewWrapper $isLoggedIn={isLoggedIn}>
        <Sidemenu />
        <Main $isLoggedIn={isLoggedIn} $menuToggle={menuToggle}>
          {children}
        </Main>
      </ViewWrapper>
    </>
  );
};

export default View;
