import eetch from 'eetch/eetch';
import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import * as s from 'markup/styles/components/user/EditProfile';
const EditProfile = ({ toggle, setToggle }) => {
  const user = useSelector((state) => state.user);
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    eetch
      .userInfo({ Authorization: user.accessToken })
      .then((res) => {
        setUserInfo(res.data);
        console.log(userInfo);
        console.log(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <>
      <s.OutWrapper $toggle={toggle} onClick={() => setToggle(false)} />
      <s.EditWrapper $toggle={toggle}>
        <s.EditModalWrapper>
          유저 프로필 수정이 들어갈 곳<p onClick={() => setToggle(false)}>close</p>
        </s.EditModalWrapper>
      </s.EditWrapper>
    </>
  );
};

export default EditProfile;
