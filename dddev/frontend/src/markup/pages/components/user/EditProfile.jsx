import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import eetch from 'eetch/eetch';
import Input from 'markup/pages/components/common/Input';

import * as s from 'markup/styles/components/user/EditProfile';
const EditProfile = ({ toggle, setToggle }) => {
  const user = useSelector((state) => state.user);
  const [userInfo, setUserInfo] = useState(null);
  const [pat, setPat] = useState('');
  const [patMessage, setPatMessage] = useState(null);

  const submitPat = () => {
    eetch
      .githubTokenUpdate({ Authorization: user.accessToken, personalAccessToken: pat })
      .then(() => {
        setPatMessage('통과');
      })
      .catch(() => {
        setPatMessage('틀려먹음');
      });
  };

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
          유저 프로필 수정이 들어갈 곳
          <Input label="Pat" data={pat} setData={setPat} enter={submitPat} click={submitPat} message={patMessage} />
          <p onClick={() => setToggle(false)}>close</p>
        </s.EditModalWrapper>
      </s.EditWrapper>
    </>
  );
};

export default EditProfile;
